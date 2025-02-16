package com.github.xtermi2.java24.jep492flexibleconstructorbodies;

import java.util.ArrayList;
import java.util.Collection;

public class FlexibleConstructorBodies {

    public static class CollectionOfTen<T> extends ArrayList<T> {

        private boolean initialized;

        public CollectionOfTen(Collection<T> init) {
            // now we can execute statements before the super constructor call
            if (null == init || init.size() != 10) {
                throw new IllegalArgumentException("Collection must have 10 elements");
            }
            // now it is possible to initialize fields before the super constructor call
            initialized = false;
            super(init);
            initialized = true;
        }
    }
}
