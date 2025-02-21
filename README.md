[![Java CI](https://github.com/xtermi2/java24/actions/workflows/maven.yml/badge.svg)](https://github.com/xtermi2/java24/actions/workflows/maven.yml)

# Schedule

- 2025/02/06 Initial Release Candidate
- 2025/02/20 Final Release Candidate
- 2025/03/18 General Availability

# JDK 24 Features

- [JEP 404: Generational Shenandoah (Experimental)](https://openjdk.org/jeps/404)
    - Enhance the [Shenandoah garbage collector](https://openjdk.org/projects/shenandoah/) with experimental generational collection capabilities to improve sustainable throughput, load-spike resilience, and memory utilization.
    - Continue to support compressed object pointers.
    - It is not a goal to improve CPU and power usage compared to traditional stop-the-world GCs. If longer pauses can be tolerated, other collectors such as G1 may still provide more energy-efficient behavior.
    - activate via `-XX:+UnlockExperimentalVMOptions -XX:ShenandoahGCMode=generational`
- [JEP 450: Compact Object Headers (Experimental)](https://openjdk.org/jeps/450)
    - Reduce the size of object headers in the HotSpot JVM from between 96 and 128 bits down to 64 bits on 64-bit architectures. This will reduce heap size, improve deployment density, and increase data locality.
    - Part of [project Lilliput](https://openjdk.org/projects/lilliput/)
    - Early adopters of Project Lilliput who have tried it with real-world applications confirm that live data is typically reduced by 10%–20%.
    - activate via `-XX:+UnlockExperimentalVMOptions -XX:+UseCompactObjectHeaders` 
- [JEP 472: Prepare to Restrict the Use of JNI](https://openjdk.org/jeps/472)
    - Issue warnings about uses of the Java Native Interface (JNI) and adjust the Foreign Function & Memory (FFM) API to issue warnings in a consistent manner. All such warnings aim to prepare developers for a future release that ensures integrity by default by uniformly restricting JNI and the FFM API.
- [JEP 475: Late Barrier Expansion for G1](https://openjdk.org/jeps/475)
    - Simplify the implementation of the G1 garbage collector's barriers, which record information about application memory accesses, by shifting their expansion from early in the C2 JIT's compilation pipeline to later.
- [JEP 478: Key Derivation Function API (Preview)](https://openjdk.org/jeps/478)
    - Introduce an API for Key Derivation Functions (KDFs), which are cryptographic algorithms for deriving additional keys from a secret key and other data.
    - see `KeyDerivationFunctionAPI.java`
- [JEP 479: Remove the Windows 32-bit x86 Port](https://openjdk.org/jeps/479)
    - Remove the source code and build support for the Windows 32-bit x86 port. This port was deprecated for removal in JDK 21 with the express intent to remove it in a future release.
- [JEP 483: Ahead-of-Time Class Loading & Linking](https://openjdk.org/jeps/483)
    - Improve startup time by making the classes of an application instantly available, in a loaded and linked state, when the HotSpot Java Virtual Machine starts. Achieve this by monitoring the application during one run and storing the loaded and linked forms of all classes in a cache for use in subsequent runs.
    - Part of [Project Leyden](https://openjdk.org/projects/leyden/)
    - 1st recording run `java -XX:AOTMode=record -XX:AOTConfiguration=app.aotconf -cp app.jar com.example.App`
    - 2nd create the cache from the configuration `java -XX:AOTMode=create -XX:AOTConfiguration=app.aotconf -XX:AOTCache=app.aot -cp app.jar`
    - 3rd run the application with the cache `java -XX:AOTCache=app.aot -cp app.jar com.example.App`
    - same JDK release and same hardware architecture is required!
    - Class paths must contain only JAR files
    - APIs used by JVMTI agents are limited
  ```bash
  ./mvnw clean package
  
  java -XX:AOTMode=record -XX:AOTConfiguration=target/app.aotconf --enable-preview --enable-native-access=ALL-UNNAMED -Dforeign.restricted=permit -cp target/java24-1.0-SNAPSHOT.jar com.github.xtermi2.java24.jep494moduleimport.ModuleImportDeclaration
  
  java -XX:AOTMode=create -XX:AOTConfiguration=target/app.aotconf -XX:AOTCache=target/app.aot --enable-preview --enable-native-access=ALL-UNNAMED -Dforeign.restricted=permit -cp target/java24-1.0-SNAPSHOT.jar
  
  time java -XX:AOTCache=target/app.aot --enable-preview --enable-native-access=ALL-UNNAMED -Dforeign.restricted=permit -cp target/java24-1.0-SNAPSHOT.jar com.github.xtermi2.java24.jep494moduleimport.ModuleImportDeclaration
  
  time java --enable-preview --enable-native-access=ALL-UNNAMED -Dforeign.restricted=permit -cp target/java24-1.0-SNAPSHOT.jar com.github.xtermi2.java24.jep494moduleimport.ModuleImportDeclaration
  ```
- [JEP 484: Class-File API](https://openjdk.org/jeps/484)
    - Provide a standard API for parsing, generating, and transforming Java class files.
    - Minor changes since second Preview contains refinements based upon experience and feedback.
    - see example `ClassFileAPI.java`
- [JEP 485: Stream Gatherers](https://openjdk.org/jeps/485)
    - Enhance the Stream API to support custom intermediate operations. This will allow stream pipelines to transform
      data in ways that are not easily achievable with the existing built-in intermediate operations.
    - No changes since second preview in JDK 23.
    - See example `StreamGatherers.java`
- [JEP 486: Permanently Disable the Security Manager](https://openjdk.org/jeps/486)
    - The Security Manager has not been the primary means of securing client-side Java code for many years, it has rarely been used to secure server-side code, and it is costly to maintain. We therefore deprecated it for removal in Java 17 via JEP 411 (2021). As the next step toward removing the Security Manager, we will revise the Java Platform specification so that developers cannot enable it and other Platform classes do not refer to it.
- [JEP 487: Scoped Values (Fourth Preview)](https://openjdk.org/jeps/487)
    - Enable the sharing of immutable data within and across threads. They are preferred to thread-local variables,
      especially when using large numbers of virtual threads.
    - Unlike a thread-local variable, a scoped value is written once and is then immutable, and is available only for a
      bounded period during execution of the thread.
    - In effect, a scoped value is an implicit method parameter. It is "as if" every method in a sequence of calls has
      an additional, invisible, parameter. None of the methods declare this parameter and only the methods that have
      access to the scoped value object can access its value (the data). Scoped values make it possible to pass data
      securely from a caller to a faraway callee through a sequence of intermediate methods that do not declare a
      parameter for the data and have no access to the data.
    - changes since second preview in JDK 23:
        - The `callWhere` and `runWhere` methods from the `ScopedValue` class, leaving the API completely fluent.
    - see example `ScopedValueServer.java`
- [JEP 488: Primitive Types in Patterns, instanceof, and switch (Second Preview)](https://openjdk.org/jeps/488)
    - Enhance pattern matching by allowing primitive type patterns in all pattern contexts, and extend instanceof and
      switch to work with all primitive types.
    - No changes since first preview in JDK 23.
    - see example `PrimitiveTypesInPatterns.java`
- [JEP 489: Vector API (Ninth Incubator)](https://openjdk.org/jeps/489)
    - Introduce an API to express vector computations that reliably compile at runtime to optimal vector instructions on
      supported CPU architectures, thus achieving performance superior to equivalent scalar computations.
    - The Vector API will incubate until necessary features
      of [Project Valhalla](https://openjdk.org/projects/valhalla/) become available as preview features. At
      that time, the Vector API will be adapted and its implementation to use them, and will be promoted from incubation
      to preview.
- [JEP 490: ZGC: Remove the Non-Generational Mode](https://openjdk.org/jeps/490)
    - Remove the non-generational mode of the Z Garbage Collector (ZGC), keeping the generational mode as the default for ZGC.
    - Reduce the maintenance cost of supporting two different modes.
- [JEP 491: Synchronize Virtual Threads without Pinning](https://openjdk.org/jeps/491)
    - Improve the scalability of Java code that uses synchronized methods and statements by arranging for virtual threads that block in such constructs to release their underlying platform threads for use by other virtual threads. This will eliminate nearly all cases of virtual threads being pinned to platform threads, which severely restricts the number of virtual threads available to handle an application's workload.
    - see `SynchronizeVirtualThreadsWithoutPinning.java`
- [JEP 492: Flexible Constructor Bodies (Third Preview)](https://openjdk.org/jeps/492)
    - In constructors in the Java programming language, allow statements that do not reference the instance being
      created to appear before an explicit constructor invocation.
    - No significant changes second preview in JDK 23.
    - see example `FlexibleConstructorBodies.java`
- [JEP 493: Linking Run-Time Images without JMODs](https://openjdk.org/jeps/493)
    - Reduce the size of the JDK by approximately 25% by enabling the `jlink` tool to create custom run-time images without using the JDK's JMOD files. This feature must be enabled when the JDK is built; it will not be enabled by default, and some JDK vendors may choose not to enable it.
    - Allow users to link a run-time image from modules regardless of whether those modules are standalone JMOD files, modular JAR files, or are part of a run-time image linked previously.
- [JEP 494: Module Import Declarations (Second Preview)](https://openjdk.org/jeps/494)
    - Enhance the Java programming language with the ability to succinctly import all of the packages exported by a
      module. This simplifies the reuse of modular libraries, but does not require the importing code to be in a module
      itself.
    - Changes since fist preview in JDK 23:
        - Lift the restriction that no module is able to declare a transitive dependence on the java.base module, and
          revise the declaration of the java.se module to transitively require the java.base module. With these changes,
          importing the java.se module will import the entire Java SE API on demand.
        - Allow type-import-on-demand declarations to shadow module import declarations.
    - See example `ModuleImportDeclaration.java`
- [JEP 495: Simple Source Files and Instance Main Methods (Fourth Preview)](https://openjdk.org/jeps/495)
    - Evolve the Java language so that students can write their first programs without needing to understand language
      features designed for large programs. Far from using a separate dialect of Java, students can write streamlined
      declarations for single-class programs and then seamlessly expand their programs to use more advanced features as
      their skills grow.
    - changes since 3rd preview in JDK 23:
        - New terminology and a revised title but otherwise unchanged.
    - see example `UnnamedClasses.java`
- [JEP 496: Quantum-Resistant Module-Lattice-Based Key Encapsulation Mechanism](https://openjdk.org/jeps/496)
    - Enhance the security of Java applications by providing an implementation of the quantum-resistant Module-Lattice-Based Key-Encapsulation Mechanism (ML-KEM). Key encapsulation mechanisms (KEMs) are used to secure symmetric keys over insecure communication channels using public key cryptography. ML-KEM is designed to be secure against future quantum computing attacks. It has been standardized by the United States National Institute of Standards and Technology (NIST) in [FIPS 203](https://csrc.nist.gov/pubs/fips/203/final).
    - Provide ML-KEM implementations of the KeyPairGenerator, KEM, and KeyFactory APIs, with support for the parameter sets ML-KEM-512, ML-KEM-768, and ML-KEM-1024 standardized in FIPS 203.
    - The `keytool` will support generating ML-KEM key pairs and certificates, e.g.:
    ```bash
    # For example, to generate an ML-KEM key pair and sign the certificate with an EC key:
    keytool -keystore ks -storepass changeit -genkeypair -alias ec -keyalg ec -dname CN=ec -ext bc
    keytool -keystore ks -storepass changeit -genkeypair -alias mlkem -keyalg ML-KEM -groupname ML-KEM-768 -dname CN=ML-KEM -signer ec
    ```
    - see `QuantumResistantModuleLatticeBasedKeyEncapsulationMechanism.java`
- [JEP 497: Quantum-Resistant Module-Lattice-Based Digital Signature Algorithm](https://openjdk.org/jeps/497)
    - Enhance the security of Java applications by providing an implementation of the quantum-resistant Module-Lattice-Based Digital Signature Algorithm (ML-DSA).
    - Provide ML-DSA implementations of the KeyPairGenerator, Signature, and KeyFactory APIs, with support for the parameter sets ML-DSA-44, ML-DSA-65, and ML-DSA-87 standardized in FIPS 204
    - The `keytool` will support generating ML-DSA key pairs and certificates, e.g.:
    ```bash
    # For example, to generate ML-DSA key pair and certificate
    keytool -keystore ks -storepass changeit -genkeypair -alias mldsa -keyalg ML-DSA -groupname ML-DSA-65 -dname CN=ML-DSA
    ```
    - see `QuantumResistantModuleLatticeBasedDigitalSignatureAlgorithm.java`
- [JEP 498: Warn upon Use of Memory-Access Methods in sun.misc.Unsafe](https://openjdk.org/jeps/498)
    - Issue a warning at run time on the first occasion that any memory-access method in sun.misc.Unsafe is invoked. All of these unsupported methods were terminally deprecated in JDK 23. They have been superseded by standard APIs, namely the VarHandle API (JEP 193, JDK 9) and the Foreign Function & Memory API (JEP 454, JDK 22).
    - JDK 26 or later will throw an exception whenever a memory-access method is used, whether directly or via reflection. This will further alert application developers and users to the imminent removal of the methods. 
    - In releases after JDK 26, we will remove memory-access methods which have had standard replacements since JDK 9 (2017). 
    - In releases after JDK 26, we will remove memory-access methods which have only had standard replacements since JDK 22 (2023).
- [JEP 499: Structured Concurrency (Fourth Preview)](https://openjdk.org/jeps/499)
    - Simplify concurrent programming by introducing an API for structured concurrency. Structured concurrency treats
      groups of related tasks running in different threads as a single unit of work, thereby streamlining error handling
      and cancellation, improving reliability, and enhancing observability.
    - no changes since last preview in JDK 23.
    - see example `StructuredConcurrency.java`
- [JEP 501: Deprecate the 32-bit x86 Port for Removal
  ](https://openjdk.org/jeps/501)
    - Deprecate the 32-bit x86 port, with the intent to remove it in a future release. This will thereby deprecate the Linux 32-bit x86 port, which is the only 32-bit x86 port remaining in the JDK. It will also, effectively, deprecate any remaining downstream 32-bit x86 ports. After the 32-bit x86 port is removed, the architecture-agnostic Zero port will be the only way to run Java programs on 32-bit x86 processors.

----------------------

##### Other References

- https://openjdk.org/projects/jdk/24/
