package com.github.xtermi2.java24.jep499structuredconcurrency;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.StructuredTaskScope;
import java.util.function.Supplier;

public class StructuredConcurrency {

    public record Response(String user, String order) {
    }

    Response executeStructuredConcurrently() throws ExecutionException, InterruptedException {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Supplier<String> user = scope.fork(this::findUser);
            Supplier<String> order = scope.fork(this::fetchOrder);

            scope.join()               // Join both forks
                    .throwIfFailed();  // ... and propagate errors

            // Here, both forks have succeeded, so compose their results
            return new Response(user.get(), order.get());
        }
    }

    Response executeClassicConcurrently() throws ExecutionException, InterruptedException {
        try (var exec = Executors.newCachedThreadPool()) {
            Future<String> user = exec.submit(this::findUser);
            Future<String> order = exec.submit(this::fetchOrder);
            String theUser = user.get();   // Join findUser
            String theOrder = order.get();  // Join fetchOrder
            return new Response(theUser, theOrder);
        }
    }

    private String findUser() throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(1));
        return "User 1";
    }

    private String fetchOrder() throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(2));
        return "Order 1";
    }
}
