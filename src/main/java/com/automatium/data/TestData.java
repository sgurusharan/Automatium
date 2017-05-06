package com.automatium.data;

import com.automatium.config.OverridableProperties;
import com.automatium.logging.TestLogger;

import java.io.IOException;

/**
 * Created by Gurusharan on 20-11-2016.
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
                singletonInstance = new TestData(System.getProperty(TEST_DATA_FILE_ENV_VARIABLE, System.getenv(TEST_DATA_FILE_ENV_VARIABLE)));
            }
            catch (IOException e) {
                e.printStackTrace();
                TestLogger.printErrorAndExit(String.format("Define system property or environment variable '%s'.", TEST_DATA_FILE_ENV_VARIABLE));
            }
        }
        return singletonInstance;
    }
}
