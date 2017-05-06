package com.automatium.filter;

import com.automatium.annotation.TestPropertiesAccessor;
import org.junit.runners.model.FrameworkMethod;

/**
 * Created by Gurusharan on 23-11-2016.
 */
public class ProductFilter extends TestFilter{
    private final String product;

    public ProductFilter(String product) {
        this.product = product;
    }

    @Override
    public boolean shouldExecuteTest(FrameworkMethod testMethod) {
        String product = TestPropertiesAccessor.getProduct(testMethod);

        if (product != null) {
            return ((product.equalsIgnoreCase(this.product) || satisfiesOrFilters(testMethod)) && satisfiesAndFilters(testMethod));
        }

        return false;
    }
}
