package com.example.demo.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ObjectsUtils {

    /**
     * Returns casted object
     *
     * @param object object to cast
     * @param <T>    type to cast
     * @return caste object of type <T>
     */
    @SuppressWarnings("unchecked") public static <T> T casted(final Object object) {
        return (T) object;
    }

    /**
     * Cast to specified class if object is of specified class, if not - return null
     *
     * @param object object to consume
     * @param clazz  class to cast to
     * @param <T>    type of object
     */
    public static <T> T castIfInstance(final Object object, final Class<T> clazz) {
        if (clazz.isInstance(object)) {
            return casted(object);
        }
        return null;
    }

    public static List<String> convertStringToList(String inputStr) {
        inputStr = inputStr.substring(1, inputStr.length() - 1);
        List<String> strList = Stream.of(inputStr.split(",", -1)).collect(Collectors.toList());
        return strList;
    }

}
