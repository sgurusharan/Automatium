package com.automatium.page;

/**
 * Created by Gurusharan on 08-04-2017.
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
