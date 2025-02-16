package com.github.xtermi2.java24.jep487scopedvalues;

public class RequestHandler {

    static void handle(String request, String response) {
        var securityContext = ScopedValueServer.SECURITY_CONTEXT.get();
        if ("admin".equals(securityContext.username())) {
            System.out.println("run as super user");
        } else {
            System.out.println("run as " + securityContext.username());
        }
        System.out.println(request + " - " + response);
    }
}
