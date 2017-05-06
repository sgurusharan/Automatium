package com.automatium.filter;

import com.automatium.annotation.TestPropertiesAccessor;
import org.junit.runners.model.FrameworkMethod;

/**
 * Created by Gurusharan on 20-11-2016.
 */
public class ModuleFilter extends TestFilter {

    private final String module;

    public ModuleFilter(String module) {
        this.module = module;
    }

    @Override
    public boolean shouldExecuteTest(FrameworkMethod testMethod) {
        String module = TestPropertiesAccessor.getModule(testMethod);

        if (module != null) {
            return ((module.equalsIgnoreCase(this.module) || satisfiesOrFilters(testMethod)) && satisfiesAndFilters(testMethod));
        }

        return false;
    }
}
