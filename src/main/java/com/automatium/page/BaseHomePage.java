package com.automatium.page;

/**
 * Created by Gurusharan on 08-04-2017.
 *
 * Abstract class representing the Home Page of the Application
 * Under Test (AUT).
 *
 * A test suite is expected to have one concrete extention of
 * BaseHomePage.
 *
 * There can be multiple home page classes and they can all
 * extend from BaseHomePage; however, this make things complex
 * as home pages are used as starting points to navigate into
 * TestEntryPage.
 *
 * All page classes that implement
 * navigateToPageFromHomePage() should leave the AUT in one of
 * the home pages.
 */
public abstract class BaseHomePage extends TestEntryPage {
    @Override
    public final void navigateToPageFromHomePage(BaseHomePage homePage) {
        logger.info(PAGETAG, "Already at Page '" + this.getClass().getSimpleName() + "'");
    }

    @Override
    public final void goBackToHomePage() {
        logger.info(PAGETAG, "Already at HomePage '" + this.getClass().getSimpleName() + "'");
    }
}
