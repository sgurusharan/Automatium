package com.automatium.filter;

import com.automatium.annotation.TestPropertiesAccessor;
import org.junit.runners.model.FrameworkMethod;

/**
 * Created by Gurusharan on 23-11-2016.
 */
public class NameFilter extends TestFilter {
    private final String name;

    public NameFilter(String name) {
        this.name = name;
    }

    @Override
    public boolean shouldExecuteTest(FrameworkMethod testMethod) {
        String name = TestPropertiesAccessor.getName(testMethod);

        if (name != null) {
            return ((name.equalsIgnoreCase(this.name) || satisfiesOrFilters(testMethod)) && satisfiesAndFilters(testMethod));
        }

        return false;
    }
}
