package com.automatium.filter;

import com.automatium.annotation.TestPropertiesAccessor;
import org.junit.runners.model.FrameworkMethod;

/**
 * Created by Gurusharan on 20-11-2016.
 */
public class GroupFilter extends TestFilter {
    private final String group;

    public GroupFilter(String group) {
        this.group = group;
    }


    @Override
    public boolean shouldExecuteTest(FrameworkMethod testMethod) {
        String group = TestPropertiesAccessor.getGroup(testMethod);

        if (group != null) {
            return ((group.equalsIgnoreCase(this.group) || satisfiesOrFilters(testMethod)) && satisfiesAndFilters(testMethod));
        }

        return false;
    }
}
