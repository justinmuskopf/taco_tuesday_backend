package com.muskopf.tacotuesday.api;

import java.util.Arrays;

public class NoSuchResourceException extends RuntimeException {
    public NoSuchResourceException(Class<?> resourceClass, String[] invalidProperties) {
        super("No such " + resourceClass.getSimpleName() + " exists with properties: " + Arrays.toString(invalidProperties));
    }

    public NoSuchResourceException(Class<?> resourceClass, String invalidProperty) {}
}
