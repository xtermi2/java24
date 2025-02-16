String greeting = "Hello, World!";

public String greeting() {
    return greeting;
}

void main() {
    // use automatically imported static methods from java.io.IO
    println(greeting());

    // use classes from java.base module, they are automatically import
    List<Serializable> list = new ArrayList<>();
    list.add(new HashMap<>());
    list.forEach(IO::println);
    File f = new File("test.txt");
    final Path path = f.toPath();
    System.out.println(path);
    Stream.of("a", "b", "c")
            .forEach(IO::println);
}
