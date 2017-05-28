package com.automatium.runner;

import com.automatium.test.BaseTest;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

/**
 * Created by Gurusharan on 20-11-2016.
 */
public class AutomatiumTestListener extends RunListener {
    @Override
    public void testRunFinished(Result result) throws Exception {
        BaseTest.closeSession();
    }
}
