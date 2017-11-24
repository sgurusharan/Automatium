package com.automatium.page;

/**
 * Created by Gurusharan on 08-04-2017.
 *
 * Abstract class representing a page where execution of
 * a test method can actually start.
 *
 * Automatium framework requires that the tester specifies
 * the expected TestEntryPage class for every test class,
 * so that it calls the implemented navigateToPageFromHomePage()
 * method from the home page of the Application.
 *
 * The tester is guaranteed that before any test method (or custom
 * setup method, if any) the Application Under Test (AUT) is at the
 * given TestEntryPage.
 *
 */
public abstract class TestEntryPage extends BasePage {
    /**
     * Contains steps to navigate to the current page from the given
     * homePage.
     *
     * Automatium calls this method from homePage if the AUT is not
     * in the expected entry page before a test method.
     *
     * @param homePage
     */
    public abstract void navigateToPageFromHomePage(BaseHomePage homePage);
}
