package com.github.xtermi2.java24.jep488primitivetypesinpatterns;

import java.util.Map;

public class PrimitiveTypesInPatterns {

    public static String switchPatternMatchingPrimitive(int i) {
        return switch (i) {
            case 1 -> "one";
            case 2 -> "two";
            case 3 -> "three";
            case int medium when medium > 3 && medium < 100 -> "medium";
            case int big when big >= 100 -> "big";
            default -> "unsupported";
        };
    }

    public static String instanceOfMatchingPrimitive(Object o) {
        if (o instanceof byte b) {
            return "byte=" + b;
        }
        return "unsupported";
    }

    public static JsonValue recordPatternPrimitive() {
        // However, we do not need to pass a double when creating a JsonNumber record;
        // we can pass an int such as 30, and the Java compiler automatically widens the int to double:
        var json = new JsonValue.JsonObject(Map.of("name", new JsonValue.JsonString("John"),
                "age", new JsonValue.JsonNumber(30)));
        return json;
    }
}

sealed interface JsonValue {
    record JsonString(String s) implements JsonValue {
    }

    record JsonNumber(double d) implements JsonValue {
    }

    record JsonObject(Map<String, JsonValue> map) implements JsonValue {
    }
}