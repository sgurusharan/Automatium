package com.automatium.page;

import com.automatium.action.*;
import com.automatium.exception.*;
import com.automatium.logging.TestLogger;
import com.automatium.utils.GlobalUtils;
import com.automatium.utils.SeleniumUtils;
import com.automatium.utils.TestUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by Gurusharan on 19-11-2016.
 */
public abstract class BasePage {

    public static final String PAGETAG = "PAGE";

    public abstract void goBackToHomePage();

    protected static TestLogger logger = TestLogger.getSingletonInstance();

    private WebDriver driver;
    private Map<String,By> locators = new HashMap<>();

    public static BasePage getCurrentPage(WebDriver driver) throws UnknownPageException {
        BasePage page = null;
        Set pageClasses = GlobalUtils.reflections.getSubTypesOf(BasePage.class);

        logger.info(PAGETAG, "Trying to identify current page");

        for (Class pageClass : (Set<Class>) pageClasses) {
            if (Modifier.isAbstract(pageClass.getModifiers())) {
                logger.debug(PAGETAG, "Skipping '" + pageClass.getSimpleName() + "' as it is abstract.");
                continue;
            }
            logger.debug(PAGETAG, "Checking if current page is '" + pageClass.getSimpleName() + "'");
            try {
                BasePage tempPage = (BasePage) pageClass.newInstance();
                tempPage.driver = driver;
                if (tempPage.isAtPage()) {
                    page = tempPage;
                    break;
                }
            } catch (IllegalAccessException | InstantiationException e) {
                logger.warn(PAGETAG, String.format("The page class '%s' is not structured good. There is no public default constructor available.", pageClass.getName()));
            }
            logger.debug(PAGETAG, "Current page does not seem to be '" + pageClass.getSimpleName() + "'");
        }

        if (page == null) {
            throw new UnknownPageException(driver);
        }

        logger.info(PAGETAG, "Current page is identified as '" + page.getClass().getSimpleName() + "'");
        page.addLocators();
        return page;
    }

    public BasePage getCurrentPage() throws UnknownPageException {
        return getCurrentPage(driver);
    }

    public <T extends BasePage> T getCurrentPageAs(Class<T> expectedPageClass) throws UnknownPageException, NotAtExpectedPageException {
        BasePage currentPage = getCurrentPage();

        try {
            return (T) currentPage;
        }
        catch (ClassCastException e) {
            throw new NotAtExpectedPageException(currentPage, expectedPageClass);
        }
    }

    public <T extends TestEntryPage> T tryToNavigateToPageFromHomePage(Class<T> targetPageClass) throws IllegalAccessException, InstantiationException {
        TestEntryPage tempPage = targetPageClass.newInstance();
        ((BasePage) tempPage).driver = driver;
        tempPage.navigateToPageFromHomePage();
        return getCurrentPageAs(targetPageClass);
    }

    public abstract void addLocators();

    public abstract boolean isAtPage();

    protected void addLocator(String key, By locator) {
        locators.put(key, locator);
    }

    public By getLocator(String key) {
        By locator = locators.get(key);

        if (locator == null) {
            throw new LocatorNotFoundException(key, this);
        }
        return locator;
    }

    public void gotoURL(String url) {
        SeleniumUtils.gotoURL(driver, url);
    }

    public String getTitle() {
        return SeleniumUtils.getTitle(driver);
    }

    public String getUrl() {
        return SeleniumUtils.getUrl(driver);
    }

    public WebElement locateElement(By locator) {
        return SeleniumUtils.locateElement(driver, locator);
    }

    public WebElement locateElement(String locatorKey) {
        return locateElement(getLocator(locatorKey));
    }

    public Select locateSelect(By locator) {
        return SeleniumUtils.locateSelect(driver, locator);
    }

    public Select locateSelect(String locatorKey) {
        return locateSelect(getLocator(locatorKey));
    }

    public String getPageSource() {
        return SeleniumUtils.getPageSource(driver);
    }

    public Object executeJavaScript(String javaScript) {
        return SeleniumUtils.executeJavaScript(driver, javaScript);
    }

    public Object executeJavaScript(String javaScript, Object... arguments) {
        return SeleniumUtils.executeJavaScript(driver, javaScript, arguments);
    }

    public String getTextInElement(String locatorKey) {
        return getTextInElement(getLocator(locatorKey));
    }

    public String getTextInElement(By locator) {
        return SeleniumUtils.getTextFromElement(driver, locateElement(locator));
    }

    public void click(String locatorKey) {
        click(locateElement(locatorKey));
    }

    public void click(WebElement elementToClick) {
        SeleniumUtils.click(elementToClick);
    }

    public void type(String locatorKey, String textToType) {
        type(locateElement(locatorKey), textToType);
    }

    public void type(WebElement inputElementToType, String textToType) {
        SeleniumUtils.type(inputElementToType, textToType);
    }

    public void selectDisplayedOption(Select selectList, String displayedOptionToSelect) {
        SeleniumUtils.selectDisplayedOption(selectList, displayedOptionToSelect);
    }

    public void selectNthOption(Select selectList, int n) {
        SeleniumUtils.selectNthOption(selectList, n);
    }

    public void selectOptionByValue(Select selectList, String valueToSelect) {
        SeleniumUtils.selectOptionByValue(selectList, valueToSelect);
    }

    public void deselectDisplayedOption(Select selectList, String displayedOptionToSelect) {
        SeleniumUtils.deselectDisplayedOption(selectList, displayedOptionToSelect);
    }

    public void deselectNthOption(Select selectList, int n) {
        SeleniumUtils.deselectNthOption(selectList, n);
    }

    public void deselectOptionByValue(Select selectList, String valueToSelect) {
        SeleniumUtils.deselectOptionByValue(selectList, valueToSelect);
    }

    public void selectAllOptions(Select selectList) {
        SeleniumUtils.selectAllOptions(selectList);
    }

    public void deselectAllOptions(Select selectList) {
        SeleniumUtils.deselectAllOptions(selectList);
    }

    public boolean waitForElement(By locator, Long timeoutInSeconds) {
        return SeleniumUtils.waitForElement(driver, locator, timeoutInSeconds);
    }

    public boolean waitForElementToDisappear(By locator, Long timeoutInSeconds) {
        return SeleniumUtils.waitForElementToDisappear(driver, locator, timeoutInSeconds);
    }

    public boolean waitForElement(String locatorKey, Long timeoutInSeconds) {
        return SeleniumUtils.waitForElement(driver, getLocator(locatorKey), timeoutInSeconds);
    }

    public boolean waitForElementToDisappear(String locatorKey, Long timeoutInSeconds) {
        return SeleniumUtils.waitForElementToDisappear(driver, getLocator(locatorKey), timeoutInSeconds);
    }

    public boolean waitForElementToEnable(WebElement element, Long timeoutInSeconds) {
        return SeleniumUtils.waitForElementToEnable(element, timeoutInSeconds);
    }

    public boolean waitForPageWithTitle(String title, Long timeoutInSeconds) {
        return SeleniumUtils.waitForPageWithTitle(driver, title, timeoutInSeconds);
    }

    public boolean waitForAlert(Long timeoutInSeconds) {
        return SeleniumUtils.waitForAlert(driver, timeoutInSeconds);
    }

    public void executeActionChain(ActionChain actionChain) {
        for (UIAction action : actionChain.getActions()) {
            if (action instanceof AssertPageAction) {
                handleAssertPageAction((AssertPageAction) action);
            }
            if (action instanceof Click) {
                handleClickAction((Click) action);
            }
            if (action instanceof NewPage) {
                handleNewPageAction((NewPage) action);
            }
            if (action instanceof PageAction) {
                try {
                    handlePageAction((PageAction) action);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e.getMessage(), e.getCause());
                }
            }
            if (action instanceof SelectOption) {
                handleSelectOptionAction((SelectOption) action);
            }
            if (action instanceof TypeText) {
                handleTypeAction((TypeText) action);
            }
        }
    }

    private void handleAssertPageAction(AssertPageAction assertPageAction) throws PageActionNotFoundException {
        Method method = getPageMethod(assertPageAction.getPageAction());
        if (!method.getReturnType().equals(boolean.class)) {
            throw new InvalidAssertionException(assertPageAction);
        }
        try {
            boolean result = (boolean) method.invoke(getCurrentPage(), assertPageAction.getPageAction().getParameters());
            TestUtils.assertTrue(result, String.format("Assertion '%s' in '%s' failed", assertPageAction.getPageAction().getMethodName(), getCurrentPage().getClass().getSimpleName()));
        } catch (IllegalAccessException e) {
            throw new PageActionNotFoundException(assertPageAction.getPageAction(), this.getClass());
        } catch (InvocationTargetException e) {
            TestUtils.assertFail(String.format("Exception when invoking %s in page class %s", assertPageAction.getPageAction().getMethodName(), getCurrentPage().getClass().getSimpleName()));
        }
    }

    private void handleClickAction(Click clickAction) {
        click(locateElement(clickAction.getLocator(this)));
    }

    private void handleNewPageAction(NewPage newPage) {
        gotoURL(newPage.getUrl());
    }

    private void handlePageAction(PageAction pageAction) throws PageActionNotFoundException, InvocationTargetException {

        try {
            Method method = getPageMethod(pageAction);
            method.invoke(getCurrentPage(), pageAction.getParameters());
        }
        catch (IllegalAccessException e) {
            throw new PageActionNotFoundException(pageAction, getCurrentPage().getClass());
        }
    }

    private Method getPageMethod(PageAction pageAction) throws PageActionNotFoundException {
        List<Class> parameterClassesAsList = new LinkedList<>();

        for (Object param : pageAction.getParameters()) {
            parameterClassesAsList.add(param.getClass());
        }

        Class[] paramClasses = parameterClassesAsList.toArray(new Class[0]);

        try {
            return getCurrentPage().getClass().getMethod(pageAction.getMethodName(), paramClasses);
        }
        catch (NoSuchMethodException e) {
            throw new PageActionNotFoundException(pageAction, getCurrentPage().getClass());
        }

    }

    private void handleSelectOptionAction(SelectOption selectOption) {
        Select selectElement = locateSelect(selectOption.getLocator(this));

        switch (selectOption.getSelectBy()) {

            case INDEX:
                selectNthOption(selectElement, Integer.parseInt(selectOption.getSelectValue()) + 1);
                break;

            case DISPLAYED_STRING:
                selectDisplayedOption(selectElement, selectOption.getSelectValue());
                break;

            case VALUE:
                selectOptionByValue(selectElement, selectOption.getSelectValue());
                break;
        }

    }

    private void handleTypeAction(TypeText typeText) {
        type(locateElement(typeText.getLocator(this)), typeText.getText());
    }

    public String getCurrentWindowHandle() {
        return SeleniumUtils.getCurrentWindowHandle(driver);
    }

    public List<String> getAllWindows() {
        return SeleniumUtils.getAllWindows(driver);
    }

    public void switchToWindow(String windowHandle) {
        SeleniumUtils.switchToWindow(driver, windowHandle);
    }

    public boolean isShowingAlert() {
        return SeleniumUtils.isShowingAlert(driver);
    }

    public String getDisplayedAlertText() {
        return SeleniumUtils.getDisplayedAlertText(driver);
    }

    public void cancelAlert() {
        SeleniumUtils.cancelAlert(driver);
    }

    public void acceptAlert() {
        SeleniumUtils.acceptAlert(driver);
    }

    public void switchToIFrame(WebElement frame) {
        SeleniumUtils.switchToIFrame(driver, frame);
    }

    public void switchToFrameInFrameSet(int frameIndex) {
        SeleniumUtils.switchToFrameInFrameSet(driver, frameIndex);
    }

    public void switchToParentFrame() {
        SeleniumUtils.switchToParentFrame(driver);
    }

    public void switchToTop() {
        SeleniumUtils.switchToTop(driver);
    }
}
