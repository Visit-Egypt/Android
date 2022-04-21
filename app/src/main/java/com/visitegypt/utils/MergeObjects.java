package com.visitegypt.utils;

import java.lang.reflect.Method;

public class MergeObjects {
    public static class MergeTwoObjects {
        public static void merge(Object obj, Object update) {
            if (!obj.getClass().isAssignableFrom(update.getClass())) {
                return;
            }

            Method[] methods = obj.getClass().getMethods();

            for (Method fromMethod : methods) {
                if (fromMethod.getDeclaringClass().equals(obj.getClass())
                        && fromMethod.getName().startsWith("get")) {

                    String fromName = fromMethod.getName();
                    String toName = fromName.replace("get", "set");

                    try {
                        Method toMethod = obj.getClass().getMethod(toName, fromMethod.getReturnType());
                        Object value = fromMethod.invoke(update, (Object[]) null);
                        if (value != null) {
                            toMethod.invoke(obj, value);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}