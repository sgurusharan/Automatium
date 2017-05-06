package com.automatium.filter;

import org.junit.runners.model.FrameworkMethod;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Gurusharan on 20-11-2016.
 */
public abstract class TestFilter {

    public abstract boolean shouldExecuteTest(FrameworkMethod testMethod);
    protected Collection<TestFilter> orFilters = new LinkedList<>();
    protected Collection<TestFilter> andFilters = new LinkedList<>();

    public void or(TestFilter filter) {
        orFilters.add(filter);
    }

    public void and(TestFilter filter) {
        andFilters.add(filter);
    }

    protected boolean satisfiesOrFilters(FrameworkMethod testMethod) {
        if (orFilters.isEmpty()) {
            return false;
        }
        return testSatisfiesAtLeastOneFilter(testMethod, orFilters);
    }

    protected boolean satisfiesAndFilters(FrameworkMethod testMethod) {
        return testSatisfiesAllFilters(testMethod, andFilters);
    }

    public static List<FrameworkMethod> filterTests(List<FrameworkMethod> testMethods, Collection<TestFilter> filters) {
        return testMethods.stream().filter(testMethod -> testSatisfiesAllFilters(testMethod, filters)).collect(Collectors.toList());
    }

    private static boolean testSatisfiesAtLeastOneFilter(FrameworkMethod testMethod, Collection<TestFilter> filters) {
        return (filters.size() == 0) || filters.stream().anyMatch(filter -> filter.shouldExecuteTest(testMethod));
    }

    public static boolean testSatisfiesAllFilters(FrameworkMethod testMethod, Collection<TestFilter> filters) {
        return (filters.size() == 0) || filters.stream().allMatch(filter -> filter.shouldExecuteTest(testMethod));
    }
}
