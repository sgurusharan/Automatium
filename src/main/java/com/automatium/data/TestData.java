package com.automatium.data;

import com.automatium.config.OverridableProperties;
import com.automatium.logging.TestLogger;

import java.io.IOException;

/**
 * Created by Gurusharan on 20-11-2016.
 *
 * A class representing a set of overridable test data.
 *
 * The testdata are all loaded from the test data file whose path is passed as
 * either an environment variable or a system property with the name 'testData'.
 *
 * The testdata can be overridden by specifying an environment variable or a
 * system property of the same name. A system property takes higher priority.
 *
 */
public class TestData extends OverridableProperties {

    private static TestData singletonInstance;

    private static final String TEST_DATA_FILE_ENV_VARIABLE = "testData";

    private TestData(String testDataFileName) throws IOException {
        super(testDataFileName);
    }

    public static TestData getSingletonInstance() {
        if (singletonInstance == null) {
            try {
                String testDataFile = System.getProperty(TEST_DATA_FILE_ENV_VARIABLE, System.getenv(TEST_DATA_FILE_ENV_VARIABLE));
                if (testDataFile == null) {
                    TestLogger.getSingletonInstance().warn("CONFIG", String.format("No system property or environment variable '%s' found. Unable to load test data.", TEST_DATA_FILE_ENV_VARIABLE));
                }

                singletonInstance = new TestData(testDataFile);
            }
            catch (IOException e) {
                e.printStackTrace();
                TestLogger.printErrorAndExit(String.format("Define system property or environment variable '%s'.", TEST_DATA_FILE_ENV_VARIABLE));
            }
        }
        return singletonInstance;
    }
}
