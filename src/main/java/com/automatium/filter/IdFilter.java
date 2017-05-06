package com.automatium.filter;

import com.automatium.annotation.TestPropertiesAccessor;
import org.junit.runners.model.FrameworkMethod;

/**
 * Created by Gurusharan on 20-11-2016.
 */
public class IdFilter extends TestFilter {
    private final String id;

    public IdFilter(String id) {
        this.id = id;
    }


    @Override
    public boolean shouldExecuteTest(FrameworkMethod testMethod) {
        String id = TestPropertiesAccessor.getId(testMethod);

        if (id != null) {
            return ((id.equalsIgnoreCase(this.id) || satisfiesOrFilters(testMethod)) && satisfiesAndFilters(testMethod));
        }

        return false;
    }
}
