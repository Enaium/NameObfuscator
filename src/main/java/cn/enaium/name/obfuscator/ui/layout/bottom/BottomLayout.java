package cn.enaium.name.obfuscator.ui.layout.bottom;

import cn.enaium.name.obfuscator.Config;
import cn.enaium.name.obfuscator.StringPool;
import cn.enaium.name.obfuscator.ui.layout.center.panel.SelectFilePanel;
import cn.enaium.name.obfuscator.ui.layout.center.panel.control.StringPoolPanel;
import cn.enaium.name.obfuscator.util.MessageUtil;
import cn.enaium.name.obfuscator.util.StringUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.SimpleRemapper;
import org.objectweb.asm.tree.*;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import static org.objectweb.asm.Opcodes.*;

/**
 * @author Enaium
 */
public class BottomLayout extends JPanel {

    private static final HashMap<String, List<String>> superHashMap = new HashMap<>();

    public BottomLayout() {
        setLayout(new BorderLayout());
        final var obfuscate = new JButton("Obfuscate");
        add(obfuscate, BorderLayout.CENTER);
        add(new StatusPanel(), BorderLayout.SOUTH);
        obfuscate.addActionListener(e -> {
            new Thread(() -> {
                try {
                    start(new File(SelectFilePanel.INPUT_JAR_TEXT_FIELD.getText()), new File(SelectFilePanel.OUTPUT_JAR_TEXT_FIELD.getText()));
                } catch (IOException exception) {
                    MessageUtil.error(exception);
                }
            }).start();
        });
    }


    private final HashMap<String, ClassReader> readerHashMap = new HashMap<>();
    private final HashMap<String, String> map = new HashMap<>();
    private final HashMap<String, byte[]> libraryHashMap = new HashMap<>();

    private void start(File input, File output) throws IOException {
        readerHashMap.clear();
        map.clear();
        libraryHashMap.clear();
        //Read all libraries
        StatusPanel.STATUS_LABEL.setText("Loading libraries...");
        for (String s : Config.LIBRARIES.getStringList()) {
            final var file = new File(s);
            var files = new ArrayList<File>();
            if (file.isDirectory()) {
                listFiles(file, files);
            } else if (file.isFile()) {
                files.add(file);
            }

            for (File f : files) {
                final var zipFile = new ZipFile(f);
                final var entries = zipFile.entries();
                while (entries.hasMoreElements()) {
                    final ZipEntry zipEntry = entries.nextElement();
                    if (zipEntry.isDirectory()) continue;
                    if (zipEntry.getName().endsWith(".class"))
                        libraryHashMap.put(zipEntry.getName(), zipFile.getInputStream(zipEntry).readAllBytes());
                }
                zipFile.close();
            }
        }

        //Read all classes
        StatusPanel.STATUS_LABEL.setText("Loading class...");
        analyzeJar(input);
        final var outputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(output)));
        ZipFile zipFile = new ZipFile(input);
        final var entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            final ZipEntry zipEntry = entries.nextElement();
            if (zipEntry.isDirectory()) {
                continue;
            } else if (!zipEntry.getName().endsWith(".class")) {
                outputStream.putNextEntry(new JarEntry(zipEntry.getName()));
                outputStream.write(zipFile.getInputStream(zipEntry).readAllBytes());
                continue;
            }

            readerHashMap.put(zipEntry.getName(), new ClassReader(zipFile.getInputStream(zipEntry)));
        }
        zipFile.close();


        var pool = new StringPool(StringPoolPanel.POOL.getText().split("\n"));

        if (StringPoolPanel.POOL.getText().isEmpty()) {
            var size = readerHashMap.size();

            for (Map.Entry<String, ClassReader> stringClassReaderEntry : readerHashMap.entrySet()) {
                ClassNode classNode = new ClassNode();
                stringClassReaderEntry.getValue().accept(classNode, 0);
                size += classNode.fields.size();
                size += classNode.methods.size();
            }

            for (int i = 0; i < size; i++) {
                pool.getStrings().add(RandomStringUtils.random(RandomUtils.nextInt(10, Byte.MAX_VALUE), StringUtil.cha[RandomUtils.nextInt(0, StringUtil.cha.length - 1)]));
            }
        }


        StatusPanel.STATUS_LABEL.setText("Building mapping...");
        //class mapping
        for (Map.Entry<String, ClassReader> stringClassReaderEntry : readerHashMap.entrySet()) {
            final var value = stringClassReaderEntry.getValue();
            ClassNode classNode = new ClassNode();
            value.accept(classNode, 0);

            var classObf = pool.next();
            if (classNode.name.contains("/")) {
                classObf = classNode.name.substring(0, classNode.name.lastIndexOf("/")) + "/" + classObf;
            }

            var filter = false;
            for (String s : Config.FILTER_CLASS_NAME.getStringList()) {
                if (Config.ENABLE_FILTER_CLASS_NAME.getEnable() && (classNode.name.startsWith(s.replace(".", "/")) || classNode.name.endsWith(s.replace(".", "/")))) {
                    filter = true;
                    break;
                }
            }

            if (Config.CLASS_NAME.getEnable()) {
                if (!filter) {
                    map.put(classNode.name, classObf);
                }
            }
        }

        //member mapping
        for (Map.Entry<String, ClassReader> stringClassReaderEntry : readerHashMap.entrySet()) {
            ClassNode classNode = new ClassNode();
            stringClassReaderEntry.getValue().accept(classNode, 0);
            for (FieldNode field : classNode.fields) {
                if (Config.FIELD_NAME.getEnable()) {
                    map.put(classNode.name + "." + field.name, pool.next());
                }
            }

            for (MethodNode method : classNode.methods) {
                if (!(method.name.equals("<init>") ||
                        method.name.equals("<clinit>") ||
                        (method.access == Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC && method.name.equals("main") && method.desc.equals("([Ljava/lang/String;)V")))) {
                    if (Config.METHOD_NAME.getEnable()) {
                        if (!isOverride(classNode.name, method)) {
                            map.put(classNode.name + "." + method.name + method.desc, pool.next());
                        }
                    }
                }
            }
        }

        StatusPanel.STATUS_LABEL.setText("Outputting...");
        //Output
        for (Map.Entry<String, ClassReader> stringClassReaderEntry : readerHashMap.entrySet()) {
            var name = stringClassReaderEntry.getKey();

            var fileName = name.substring(0, name.lastIndexOf("."));
            if (map.containsKey(fileName)) {
                name = map.get(fileName) + ".class";
            }

            outputStream.putNextEntry(new ZipEntry(name));
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            stringClassReaderEntry.getValue().accept(new ClassRemapper(classWriter, new SimpleRemapper(map) {
                @Override
                public String mapFieldName(String owner, String name, String descriptor) {
                    String remappedName = map(owner + '.' + name);
                    if (remappedName == null) {
                        if (superHashMap.containsKey(owner)) {
                            for (String s : superHashMap.get(owner)) {
                                String rn = mapFieldName(s, name, descriptor);
                                if (rn != null) {
                                    return rn;
                                }
                            }
                        }
                    }
                    return remappedName == null ? name : remappedName;
                }

                @Override
                public String mapMethodName(String owner, String name, String descriptor) {
                    String remappedName = map(owner + '.' + name + descriptor);
                    if (remappedName == null) {
                        if (superHashMap.get(owner) != null) {
                            for (String s : superHashMap.get(owner)) {
                                String rn = mapMethodName(s, name, descriptor);
                                if (rn != null) {
                                    return rn;
                                }
                            }
                        }
                    }
                    return remappedName == null ? name : remappedName;
                }
            }), 0);
            final var classReader = new ClassReader(classWriter.toByteArray());
            final var classNode = new ClassNode();
            classReader.accept(classNode, 0);

            classNode.version = V11;

            if (Config.REMOVE_SOURCE.getEnable()) {
                classNode.visitSource(null, null);
            }

            for (MethodNode method : classNode.methods) {
                if (Config.LOCAL_VARIABLE_NAME.getEnable()) {
                    if (method.parameters != null) {
                        for (ParameterNode parameter : method.parameters) {
                            parameter.name = pool.next();
                        }
                    }

                    if (method.localVariables != null) {
                        for (LocalVariableNode localVariable : method.localVariables) {
                            localVariable.name = pool.next();
                        }
                    }
                }

                if (Config.REMOVE_LINE_NUMBER.getEnable()) {
                    for (AbstractInsnNode instruction : method.instructions) {
                        if (instruction instanceof LineNumberNode) {
                            method.instructions.remove(instruction);
                        }
                    }
                }
            }

            final var finalClassWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(finalClassWriter);
            outputStream.write(finalClassWriter.toByteArray());
        }
        outputStream.closeEntry();
        outputStream.close();
        StatusPanel.reset();
    }

    private boolean isOverride(String name, MethodNode methodNode) {
        var list = new ArrayList<String>();
        find(name, list);
        for (String s : list) {
            ClassNode cn = null;
            if (readerHashMap.containsKey(s + ".class")) {
                final var classReader = readerHashMap.get(s + ".class");
                ClassNode classNode = new ClassNode();
                classReader.accept(classNode, 0);
                cn = classNode;
            } else {
                try {
                    var classNode = new ClassNode();
                    ClassReader classReader = new ClassReader(s);
                    classReader.accept(classNode, 0);
                    cn = classNode;
                } catch (IOException e) {
                    if (libraryHashMap.containsKey(s + ".class")) {
                        var classNode = new ClassNode();
                        ClassReader classReader = new ClassReader(libraryHashMap.get(s + ".class"));
                        classReader.accept(classNode, 0);
                        cn = classNode;
                    } else {
//                        MessageUtil.error(new IOException("Class no found " + s));
                        return true;
                    }
                }
            }

            for (MethodNode method : cn.methods) {
                if ((method.name + method.desc).equals(methodNode.name + methodNode.desc)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void find(String name, List<String> classes) {
        if (superHashMap.containsKey(name)) {
            final var list = superHashMap.get(name);
            for (String s : list) {
                classes.add(s);
                find(s, classes);
            }
        }
    }

    private void analyzeJar(File inputFile) throws IOException {
        superHashMap.put("java/lang/Object", Collections.emptyList());
        JarFile jarFile = new JarFile(inputFile);
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            if (jarEntry.isDirectory())
                continue;
            if (!jarEntry.getName().endsWith(".class"))
                continue;

            analyze(jarFile.getInputStream(jarEntry).readAllBytes());

        }
        jarFile.close();
    }

    private void analyze(byte[] bytes) {
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(new ClassVisitor(ASM9) {
            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                ArrayList<String> strings = new ArrayList<>();
                if (superHashMap.containsKey(name)) {
                    if (superName != null) {
                        if (!superHashMap.get(name).contains(superName)) {
                            strings.add(superName);
                        }
                    }

                    if (interfaces != null) {
                        for (String inter : interfaces) {
                            if (!superHashMap.get(name).contains(inter)) {
                                strings.add(inter);
                            }
                        }
                    }
                    superHashMap.get(name).addAll(strings);
                } else {
                    if (superName != null) {
                        strings.add(superName);
                    }

                    if (interfaces != null) {
                        Collections.addAll(strings, interfaces);
                    }
                    superHashMap.put(name, strings);
                }
                super.visit(version, access, name, signature, superName, interfaces);
            }
        }, 0);
    }

    private void listFiles(File file, List<File> list) {
        File[] listFiles = file.listFiles();
        if (listFiles == null) {
            return;
        }

        for (File listFile : listFiles) {
            if (listFile.isFile() && listFile.getName().endsWith(".zip") || listFile.getName().endsWith(".jar")) {
                list.add(listFile);
            } else if (file.isDirectory()) {
                listFiles(listFile.getAbsoluteFile(), list);
            }
        }
    }
}
