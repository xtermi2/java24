package com.github.xtermi2.java24.jep494moduleimport;

// this will import e.g. all the classes from package java.util, java.util.stream, java.io, java.lang, java.nio.file
import module java.base;

public class ModuleImportDeclaration {
    public static void main(String[] args) {
        List<Serializable> list = new ArrayList<>();
        list.add(new HashMap<>());
        list.forEach(System.out::println);
        File f = new File("test.txt");
        final Path path = f.toPath();
        System.out.println(path);
        Stream.of("a", "b", "c")
                .forEach(System.out::println);
    }
}
