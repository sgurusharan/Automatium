package com.automatium.annotation;

import org.junit.runners.model.FrameworkMethod;

/**
 * Created by Gurusharan on 23-11-2016.
 */
public class TestPropertiesAccessor {
    public static String getId(FrameworkMethod testMethod) {
        TestProperties testProperties = testMethod.getAnnotation(TestProperties.class);

        if (testProperties == null) {
            return null;
        }

        return testProperties.id();
    }

    public static String getModule(FrameworkMethod testMethod) {
        TestProperties testProperties = testMethod.getAnnotation(TestProperties.class);

        // If no test properties defined or no module name given for test method
        if (testProperties == null || testProperties.module() == null) {
            testProperties = testMethod.getDeclaringClass().getAnnotation(TestProperties.class);
        }

        if (testProperties == null) {
            return null;
        }

        return testProperties.module();
    }

    public static String getName(FrameworkMethod testMethod) {
        TestProperties testProperties = testMethod.getAnnotation(TestProperties.class);

        if (testProperties == null) {
            return null;
        }

        return testProperties.name();
    }

    public static String getPriority(FrameworkMethod testMethod) {
        TestProperties testProperties = testMethod.getAnnotation(TestProperties.class);

        // If no test properties defined or no priority given for test method
        if (testProperties == null || testProperties.priority() == null) {
            testProperties = testMethod.getDeclaringClass().getAnnotation(TestProperties.class);
        }

        if (testProperties == null) {
            return null;
        }

        return testProperties.priority();
    }

    public static String getProduct(FrameworkMethod testMethod) {
        TestProperties testProperties = testMethod.getAnnotation(TestProperties.class);

        // If no test properties defined or no product name given for test method
        if (testProperties == null || testProperties.product() == null) {
            testProperties = testMethod.getDeclaringClass().getAnnotation(TestProperties.class);
        }

        if (testProperties == null) {
            return null;
        }

        return testProperties.product();
    }

    public static String getGroup(FrameworkMethod testMethod) {
        TestProperties testProperties = testMethod.getAnnotation(TestProperties.class);

        if (testProperties == null) {
            return null;
        }

        return testProperties.group();
    }
}
