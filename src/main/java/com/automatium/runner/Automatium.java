package com.automatium.runner;

import com.automatium.logging.TestLogger;
import com.automatium.test.BaseTest;
import com.automatium.utils.GlobalUtils;
import org.junit.runner.JUnitCore;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by Gurusharan on 20-11-2016.
 */
public class Automatium {
    private static final TestLogger logger = TestLogger.getSingletonInstance();
    private static final String AUTOMATIUMTAG = "AUTOMATIUM";

    public static void main(String[] args) {
        JUnitCore coreRunner = new JUnitCore();

        Iterator<Class> testClassIterator = getTestClassesToRun().iterator();
        while (testClassIterator.hasNext()) {
            coreRunner.run(testClassIterator.next());
        }

    }

    private static Set<Class> getTestClassesToRun() {
        logger.info(AUTOMATIUMTAG, "Identifying tests to run...");

        Set testClasses = GlobalUtils.reflections.getSubTypesOf(BaseTest.class);
        logger.info(AUTOMATIUMTAG, String.format("Identified %d test classes (unfiltered list)", testClasses.size()));
        return testClasses;
    }
}
