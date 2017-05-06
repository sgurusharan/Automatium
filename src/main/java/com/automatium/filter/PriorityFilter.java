package com.automatium.filter;

import com.automatium.annotation.TestPropertiesAccessor;
import org.junit.runners.model.FrameworkMethod;

/**
 * Created by Gurusharan on 23-11-2016.
 */
public class PriorityFilter extends TestFilter {

    private final String priority;

    public PriorityFilter(String priority) {
        this.priority = priority;
    }

    @Override
    public boolean shouldExecuteTest(FrameworkMethod testMethod) {
        String priority = TestPropertiesAccessor.getPriority(testMethod);

        if (priority != null) {
            return ((priority.equalsIgnoreCase(this.priority) || satisfiesOrFilters(testMethod)) && satisfiesAndFilters(testMethod));
        }

        return false;
    }
}
