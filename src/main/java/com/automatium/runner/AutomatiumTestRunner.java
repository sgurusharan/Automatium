package com.automatium.runner;

import com.automatium.config.TestConfiguration;
import com.automatium.filter.TestFilter;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.util.List;

/**
 * Created by Gurusharan on 20-11-2016.
 */
public class AutomatiumTestRunner extends BlockJUnit4ClassRunner {

    public final static TestConfiguration testConfiguration = TestConfiguration.getSingletonInstance();
    private final static AutomatiumTestListener testListener = new AutomatiumTestListener();
    /**
     * Creates a BlockJUnit4ClassRunner to run {@code klass}
     *
     * @param klass
     * @throws InitializationError if the test class is malformed.
     */
    public AutomatiumTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    public void run(RunNotifier notifier) {
        notifier.addListener(testListener);
        super.run(notifier);
    }

    @Override
    protected List<FrameworkMethod> getChildren() {
        List<FrameworkMethod> allTestMethods = super.getChildren();
        return TestFilter.filterTests(allTestMethods, testConfiguration.getTestFilters());
    }
}
