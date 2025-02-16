package com.github.xtermi2.java24.jep484classfileapi;

import java.io.IOException;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.ClassTransform;
import java.lang.classfile.TypeKind;
import java.lang.constant.ClassDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.reflect.AccessFlag;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.StringJoiner;

public class ClassFileAPI {
    private final String name;

    public ClassFileAPI(String name) {
        this.name = name;
    }

    public static void main(String[] args) throws IOException {
        generateGetter();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ClassFileAPI.class.getSimpleName() + "[", "]").add("name='" + name + "'").toString();
    }

    public static void generateGetter() throws IOException {
        ClassFile cf = ClassFile.of();
        final Path classFileAPIPath = Path.of("target/classes/com/github/xtermi2/java24/jep484classfileapi/ClassFileAPI.class");
        ClassModel classFileAPIClassModel = cf.parse(classFileAPIPath);

        // list all fields
        classFileAPIClassModel.fields().forEach(x -> System.out.println("field: " + x));

        final String nameGetter = "getName";
        if (classFileAPIClassModel.methods().stream().noneMatch(x -> nameGetter.equals(x.methodName().stringValue()))) {
            addGetter(nameGetter, classFileAPIClassModel, cf, classFileAPIPath);
        }
    }

    private static void addGetter(String getter,
                                  ClassModel classFileAPIClassModel,
                                  ClassFile cf,
                                  Path classFileAPIPath) throws IOException {
        System.out.println("add " + getter + " to " + classFileAPIClassModel);
        // we add the method at the end of the class
        final ClassTransform classTransform = ClassTransform.endHandler(classBuilder ->
                classBuilder.withMethod(getter, MethodTypeDesc.of(ClassDesc.of(String.class.getName())), AccessFlag.PUBLIC.mask(), methodBuilder ->
                        methodBuilder.withCode(codeBuilder -> {
                            codeBuilder.aload(0);
                            codeBuilder.getfield(ClassDesc.of(ClassFileAPI.class.getName()), "name", ClassDesc.of(String.class.getName()));
                            codeBuilder.return_(TypeKind.REFERENCE);
                        })));

        byte[] newBytes = cf.transformClass(classFileAPIClassModel, classTransform);
        Files.write(classFileAPIPath, newBytes, StandardOpenOption.WRITE);
    }
}
