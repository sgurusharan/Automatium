package com.automatium.test;

import com.automatium.config.TestConfiguration;
import com.automatium.data.TestData;
import com.automatium.exception.InvalidFirstPageException;
import com.automatium.exception.NotAtHomePageException;
import com.automatium.exception.UnknownPageException;
import com.automatium.logging.TestLogger;
import com.automatium.page.BaseHomePage;
import com.automatium.page.BasePage;
import com.automatium.page.TestEntryPage;
import com.automatium.runner.AutomatiumTestRunner;
import com.automatium.utils.SeleniumUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;

/**
 * Created by Gurusharan on 19-11-2016.
 *
 * Abstract test class that all JUnit tests that use Automatium
 * should extend directly or indirectly.
 *
 * This class allows the tester to configure the expected TestEntryPage,
 * and tries to guarantee that the Application Under Test (AUT) is at
 * the configured TestEntryPage before any test method (or custom setup
 * method) is invoked.
 */
@Ignore
@RunWith(AutomatiumTestRunner.class)
public abstract class AutomatiumTest {

    protected static final String TESTSETUPTAG = "TESTSETUP";

    protected final TestLogger logger = TestLogger.getSingletonInstance();
    protected final TestConfiguration testConfiguration = TestConfiguration.getSingletonInstance();
    protected final TestData testData = TestData.getSingletonInstance();

    protected BasePage currentPage;

    /**
     * This method should return the expected entry point for all the test methods.
     *
     * @param <T>
     * @return A class that extends from TestEntryPage
     */
    protected abstract <T extends TestEntryPage> Class<T> getExpectedFirstPage();

    @Before
    public void goToExpectedFirstPage() {

        Class expectedFirstPage = getExpectedFirstPage();

        if (expectedFirstPage == null) {
            currentPage = BasePage.getCurrentPage(GlobalTestStorage.getGlobalDriver());
            logger.warn(TESTSETUPTAG, "No expected first page given... Continuing with next test from page '" + currentPage.getClass().getSimpleName() + "'...");
            return;
        }

        try {
            currentPage = BasePage.getCurrentPage(GlobalTestStorage.getGlobalDriver());
        } catch (UnknownPageException e) {
            logger.warn(TESTSETUPTAG, "Unable to determine current page... Restarting from URL '" + testConfiguration.getStartUrl()+ "'...");
            SeleniumUtils.gotoURL(GlobalTestStorage.getGlobalDriver(), testConfiguration.getStartUrl());
            currentPage = BasePage.getCurrentPage(GlobalTestStorage.getGlobalDriver());
        }

        if (expectedFirstPage.isInstance(currentPage)) {
            logger.info(TESTSETUPTAG, "We are already at expected first page '" + expectedFirstPage.getSimpleName() + "'");
        }
        else {
            logger.debug(TESTSETUPTAG, "We are not at expected first page '" + expectedFirstPage.getSimpleName() + "'");
            logger.debug(TESTSETUPTAG, "Attempting to go to home page...");
            currentPage.goBackToHomePage();
            currentPage = BasePage.getCurrentPage(GlobalTestStorage.getGlobalDriver());
            if (currentPage instanceof BaseHomePage) {
                logger.debug(TESTSETUPTAG, "Verified that we are at home page.");
            }
            else {
                throw new NotAtHomePageException(currentPage);
            }

            logger.debug(TESTSETUPTAG, "Attempting to navigate to expected first page '" + expectedFirstPage.getSimpleName() + "'");
            currentPage = currentPage.tryToNavigateToPageFromHomePage(expectedFirstPage);
        }

        logger.info(TESTSETUPTAG, "We are now at expected first page '" + expectedFirstPage.getSimpleName() + "'");
    }

    public static void closeSession() {
        if (GlobalTestStorage.getGlobalDriver() != null) {
            SeleniumUtils.quit(GlobalTestStorage.getGlobalDriver());
        }
    }
}

