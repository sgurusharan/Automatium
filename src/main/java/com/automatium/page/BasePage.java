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

import org.openqa.selenium.NoSuchElementException;

/**
 * Created by Gurusharan on 19-11-2016.
 *
 * Abstract class that every page class should directly or indirectly
 * inherit from. Automatium framework uses the set of all concrete
 * classes that inherit from BasePage to identify the current page that
 * the test is on.
 *
 * This class contains all interactions with the WebDriver object through
 * the classes in com.automatium.utils package. It is recommended that
 * any page class should use the methods defined in BasePage to interact
 * with WebDriver instead of directly accessing the driver object.
 *
 */
public abstract class BasePage {

    public static final String PAGETAG = "PAGE";

    /**
     * Navigate to the home page of the application under test.
     * After successful execution of this method, a call to getCurrentPage()
     * is expected to return an element of class derived from
     * BaseHomePage.
     */
    public abstract void goBackToHomePage();

    protected static TestLogger logger = TestLogger.getSingletonInstance();

    private WebDriver driver;
    private Map<String,By> locators = new HashMap<>();

    /**
     * Gets an instance of the page class that the AUT pointed to by the
     * given driver object is currently in.
     *
     * This returns a newly created instance of the first page class whose
     * isAtPage() is satisfied.
     *
     * @param driver
     * @return An instance of the page class that is currently pointed to by the driver
     * @throws UnknownPageException - if none of the available page classes' isAtPage() is satisfied
     *
     */
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

    /**
     * Returns an instance of the page class that is pointed to by the
     * WebDriver object corresponding to the caller page object.
     *
     * Recommended to use getCurrentPageAs() to specify what the current
     *  page is expected to be and avoid casting inside the test.
     *
     * @return the current page object
     * @throws UnknownPageException - if none of the available page classes' isAtPage() is satisfied
     */
    public BasePage getCurrentPage() throws UnknownPageException {
        return getCurrentPage(driver);
    }

    /**
     * Returns an instance of the page class that is pointed to by the
     * WebDriver object corresponding to the caller page object after validating
     * that it is in the expected page.
     *
     * @param expectedPageClass
     * @param <T>
     * @return an instance of the expected page class
     * @throws UnknownPageException - if none of the available page classes' isAtPage() is satisfied
     * @throws NotAtExpectedPageException - if the current page is not the expected page
     */
    public <T extends BasePage> T getCurrentPageAs(Class<T> expectedPageClass) throws UnknownPageException, NotAtExpectedPageException {
        BasePage currentPage = getCurrentPage();

        try {
            return expectedPageClass.cast(currentPage);
        }
        catch (ClassCastException e) {
            throw new NotAtExpectedPageException(currentPage, expectedPageClass);
        }
    }


    /**
     * Expected to be called on an instance of BaseHomePage, this method
     * calls the navigateToPageFromHomePage on a newly created instance of the
     * given TestEntryPage target class.
     *
     * @param targetPageClass
     * @param <T>
     * @return
     * @throws NotAtHomePageException - if the AUT is not in a home page.
     */
    public <T extends TestEntryPage> T tryToNavigateToPageFromHomePage(Class<T> targetPageClass) throws NotAtHomePageException {

        if (!(this instanceof BaseHomePage)) {
            throw new NotAtHomePageException(this);
        }

        TestEntryPage tempPage;
        try {
            tempPage = targetPageClass.newInstance();
        }
        catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }

        ((BasePage) tempPage).driver = driver;
        tempPage.navigateToPageFromHomePage((BaseHomePage) this);
        return getCurrentPageAs(targetPageClass);
    }

    /**
     * This method is called automatically when an instance of a page class
     * is returned by the getCurrentPage() call.
     *
     * This method should contain a set of calls to addLocator method,
     * for the various WebElements that need to be located in a page.
     * It does not matter if the elements are not located during the time
     * of adding the locators.
     *
     * The elements should be available when calling one of the locate...
     * methods.
     */
    public abstract void addLocators();

    /**
     * Should contain a check to determine if the user is on the page
     * represented by the page class.
     *
     * This method MUST return true if and only if the user is on the
     * page represented by the page class.
     *
     * Automatium calls this method to determine what page the Application
     * Under Test is currently on when getCurrentPage() is called.
     *
     * @return true if and only if the user is on the page represented
     *         by the page class
     */
    public abstract boolean isAtPage();

    /**
     * Adds a locator to the page's locator map under the given key
     *
     * @param key
     * @param locator
     */
    protected void addLocator(String key, By locator) {
        locators.put(key, locator);
    }

    /**
     * Returns a locator that was added to the page's locator map under
     * the given key.
     *
     * @param key
     * @return the locator from locator map
     * @throws LocatorNotFoundException if the given key is not found in the page's locator map.
     */
    public By getLocator(String key) throws LocatorNotFoundException {
        By locator = locators.get(key);

        if (locator == null) {
            throw new LocatorNotFoundException(key, this);
        }
        return locator;
    }

    /**
     * Navigates to the given URL. No syntax check is done on the given URL.
     *
     * @param url
     */
    public void gotoURL(String url) {
        SeleniumUtils.gotoURL(driver, url);
    }

    /**
     * Returns the current page title of the AUT
     *
     * @return a String representing the page title
     */
    public String getTitle() {
        return SeleniumUtils.getTitle(driver);
    }

    /**
     * Returns the URL of the browser which is running the automation.
     *
     * @return a String representing the URL
     */
    public String getUrl() {
        return SeleniumUtils.getUrl(driver);
    }

    /**
     * Finds an element in the current page using the given locator.
     *
     * @param locator
     * @return First WebElement located by the locator
     * @throws NoSuchElementException if the element is not found
     */
    public WebElement locateElement(By locator) {
        return SeleniumUtils.locateElement(driver, locator);
    }

    /**
     * Find an element in the current page using the locator taken
     * from the current page's locator map using the given key.
     *
     * @param locatorKey
     * @return First WebElement located by the locator
     * @throws LocatorNotFoundException if the given locator key is not found
     * @throws NoSuchElementException if the element is not found
     */
    public WebElement locateElement(String locatorKey) {
        return locateElement(getLocator(locatorKey));
    }

    /**
     * Same as locateElement but used to locate dropdown lists and list boxes.
     *
     * @param locator
     * @return the first Select element located by the locator
     * @throws NoSuchElementException if the element is not found
     */
    public Select locateSelect(By locator) {
        return SeleniumUtils.locateSelect(driver, locator);
    }

    /**
     * Same as locateElement using the locator map but used to locate
     * dropdown lists and list boxes.
     *
     * @param locatorKey
     * @return the first Select elemenet located by the locator
     * @throws NoSuchElementException if the element is not found
     * @throws LocatorNotFoundException if the locator is not found in the locator map
     */
    public Select locateSelect(String locatorKey) {
        return locateSelect(getLocator(locatorKey));
    }

    /**
     * Returns the HTML source of the current page.
     *
     * @return the HTML source as a String
     */
    public String getPageSource() {
        return SeleniumUtils.getPageSource(driver);
    }

    /**
     * Executes the given Javascript on the current page.
     * @param javaScript
     * @return the value returned by the javascript execution
     */
    public Object executeJavaScript(String javaScript) {
        return SeleniumUtils.executeJavaScript(driver, javaScript);
    }

    /**
     * Same as executeJavaScript with the ability to pass arguments as well
     *
     * @param javaScript
     * @param arguments
     * @return the value returned by the javascript execution
     */
    public Object executeJavaScript(String javaScript, Object... arguments) {
        return SeleniumUtils.executeJavaScript(driver, javaScript, arguments);
    }

    /**
     * Returns the innerText of the element located by the key in the locator map.
     *
     * @param locatorKey
     * @return innerText as a String
     * @throws LocatorNotFoundException if the given locator is not found in the locator map
     * @throws NoSuchElementException if the element is not found
     */
    public String getTextInElement(String locatorKey) {
        return getTextInElement(getLocator(locatorKey));
    }

    /**
     * Returns the innerText of the element located by the locator.
     *
     * @param locator
     * @return innerText as a String
     * @throws NoSuchElementException if the element is not found
     */
    public String getTextInElement(By locator) {
        return SeleniumUtils.getTextFromElement(driver, locateElement(locator));
    }

    /**
     * Clicks on the element located using the given locator from locator map.
     *
     * @param locatorKey
     * @throws NoSuchElementException if the element is not found
     * @throws LocatorNotFoundException if the given key is not found in the locator map
     */
    public void click(String locatorKey) {
        click(locateElement(locatorKey));
    }

    /**
     * Clicks on the given WebElement
     *
     * @param elementToClick
     */
    public void click(WebElement elementToClick) {
        SeleniumUtils.click(elementToClick);
    }

    /**
     * Clears the text field / text area content located using the given locator
     * key from locator map and types in the given string.
     *
     * @param locatorKey
     * @param textToType
     * @throws LocatorNotFoundException if the given key is not found in the locator map
     * @throws NoSuchElementException if the element is not found
     */
    public void clearAndType(String locatorKey, String textToType) {
        clearAndType(locateElement(locatorKey), textToType);
    }

    /**
     * Clears the text field / text area referred by the WebElement and types
     * the given text.
     *
     * @param inputElementToType
     * @param textToType
     */
    public void clearAndType(WebElement inputElementToType, String textToType) {
        SeleniumUtils.clearAndType(inputElementToType, textToType);
    }

    /**
     * Types the given string into text field / text area located
     * using the given locator key from locator map.
     *
     * @param locatorKey
     * @param textToType
     * @throws LocatorNotFoundException if the given key is not found in the locator map
     * @throws NoSuchElementException if the element is not found
     */
    public void type(String locatorKey, String textToType) {
        type(locateElement(locatorKey), textToType);
    }

    /**
     * Types the given string into the text field / text area referred
     * by the WebElement.
     *
     * @param inputElementToType
     * @param textToType
     */
    public void type(WebElement inputElementToType, String textToType) {
        SeleniumUtils.type(inputElementToType, textToType);
    }

    /**
     * Types the given string into a JavaScript prompt window.
     *
     * @param textToType
     */
    public void typeInPrompt(String textToType) {
        SeleniumUtils.typeTextInPrompt(driver, textToType);
    }

    /**
     * Enters the given username and password and submits the simple
     * HTTP authentication dialog box.
     *
     * @param username
     * @param password
     */
    public void performHTTPAuthentication(String username, String password) {
        SeleniumUtils.performHTTPAuthentication(driver, username, password);
    }

    /**
     * Selects the given option in the dropdown list / list box passed.
     *
     * @param selectList
     * @param displayedOptionToSelect
     */
    public void selectDisplayedOption(Select selectList, String displayedOptionToSelect) {
        SeleniumUtils.selectDisplayedOption(selectList, displayedOptionToSelect);
    }

    /**
     * Selects the option at the given position n (where n starts from 1) in
     * the dropdown list / list box passed.
     *
     * @param selectList
     * @param n
     */
    public void selectNthOption(Select selectList, int n) {
        SeleniumUtils.selectNthOption(selectList, n);
    }

    /**
     * Selects the option whose 'value' attribute has the given value in the
     * dropdown list / list box passed.
     *
     * @param selectList
     * @param valueToSelect
     */
    public void selectOptionByValue(Select selectList, String valueToSelect) {
        SeleniumUtils.selectOptionByValue(selectList, valueToSelect);
    }

    /**
     * Deselects the given option in the multi-select list box passed.
     *
     * @param selectList
     * @param displayedOptionToSelect
     */
    public void deselectDisplayedOption(Select selectList, String displayedOptionToSelect) {
        SeleniumUtils.deselectDisplayedOption(selectList, displayedOptionToSelect);
    }

    /**
     * Deselects the option at the given position n (where n starts from 1) in
     * the multi-select list box passed.
     *
     * @param selectList
     * @param n
     */
    public void deselectNthOption(Select selectList, int n) {
        SeleniumUtils.deselectNthOption(selectList, n);
    }

    /**
     * Deselects the option whose 'value' attribute has the given value in the
     * multi-select list box passed.
     *
     * @param selectList
     * @param valueToSelect
     */
    public void deselectOptionByValue(Select selectList, String valueToSelect) {
        SeleniumUtils.deselectOptionByValue(selectList, valueToSelect);
    }

    /**
     * Selects all options in the given multi-select list box.
     *
     * @param selectList
     */
    public void selectAllOptions(Select selectList) {
        SeleniumUtils.selectAllOptions(selectList);
    }

    /**
     * Deselects all options in the given multi-select list box.
     *
     * @param selectList
     */
    public void deselectAllOptions(Select selectList) {
        SeleniumUtils.deselectAllOptions(selectList);
    }

    /**
     * Waits for the given number of seconds until at least one element
     * is located with the given locator.
     *
     * @param locator
     * @param timeoutInSeconds
     * @return true if the element was found; false if timed out.
     */
    public boolean waitForElement(By locator, Long timeoutInSeconds) {
        return SeleniumUtils.waitForElement(driver, locator, timeoutInSeconds);
    }

    /**
     * Waits for the given number of seconds until no element is found
     * with the given locator.
     *
     * @param locator
     * @param timeoutInSeconds
     * @return true if no element was found withing the given time; false if timed out.
     */
    public boolean waitForElementToDisappear(By locator, Long timeoutInSeconds) {
        return SeleniumUtils.waitForElementToDisappear(driver, locator, timeoutInSeconds);
    }

    /**
     * Same as waitForElement, but the locator is obtained from the locator
     * map using the given locator key.
     *
     * @param locatorKey
     * @param timeoutInSeconds
     * @return true if the element was found; false if timed out
     * @throws LocatorNotFoundException if the given locator key is not found
     */
    public boolean waitForElement(String locatorKey, Long timeoutInSeconds) {
        return SeleniumUtils.waitForElement(driver, getLocator(locatorKey), timeoutInSeconds);
    }

    /**
     * Same as waitForElementToDisappear, but the locator is obtained from
     * the locator map using the given locator key.
     *
     * @param locatorKey
     * @param timeoutInSeconds
     * @return true if no element was found withing the given time; false if timed out.
     * @throws LocatorNotFoundException if the given locator key is not found
     */
    public boolean waitForElementToDisappear(String locatorKey, Long timeoutInSeconds) {
        return SeleniumUtils.waitForElementToDisappear(driver, getLocator(locatorKey), timeoutInSeconds);
    }

    /**
     * Waits for the given number of seconds for the given WebElement to be enabled.
     *
     * @param element
     * @param timeoutInSeconds
     * @return true if the element was enabled; false if timed out
     */
    public boolean waitForElementToEnable(WebElement element, Long timeoutInSeconds) {
        return SeleniumUtils.waitForElementToEnable(element, timeoutInSeconds);
    }

    /**
     * Waits for the given number of seconds until a page with the given title is
     * found.
     *
     * @param title
     * @param timeoutInSeconds
     * @return true if one such page was found; flase if timed out
     */
    public boolean waitForPageWithTitle(String title, Long timeoutInSeconds) {
        return SeleniumUtils.waitForPageWithTitle(driver, title, timeoutInSeconds);
    }

    /**
     * Waits for the AUT to load a page that confirms to the isAtPage() rule of
     * the given page class.
     *
     * @param expectedPageClass
     * @param timeoutInSeconds
     * @param <T>
     * @return true if a matching page was found; false if timed out.
     */
    public <T extends BasePage> boolean waitForPage(Class<T> expectedPageClass, Long timeoutInSeconds) {
        return TestUtils.waitForCondition(timeoutInSeconds, ()-> expectedPageClass.isInstance(getCurrentPage()));
    }

    /**
     * Waits for the given number of seconds for a JavaScript alert.
     *
     * @param timeoutInSeconds
     * @return true if an alert was found; false if timed out.
     */
    public boolean waitForAlert(Long timeoutInSeconds) {
        return SeleniumUtils.waitForAlert(driver, timeoutInSeconds);
    }

    /**
     * Executes the actions in the given ActionChain starting from the
     * current page. Before every action Automatium tries to identify
     * the current page class and invokes the action on it.
     *
     * @param actionChain
     * @return an instance of a page class representing the last page that the chain ends at.
     */
    public BasePage executeActionChain(ActionChain actionChain) {
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

        return getCurrentPage();
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
        clearAndType(locateElement(typeText.getLocator(this)), typeText.getText());
    }

    /**
     * Gets the internal representation of the currently active window of the AUT.
     * Used for switching windows within the AUT.
     *
     * @return Currently active window's handle as a String.
     */
    public String getCurrentWindowHandle() {
        return SeleniumUtils.getCurrentWindowHandle(driver);
    }

    /**
     * Gets all the window handles that can be used to switch within the AUT.
     *
     * @return a List of Strings containing all switchable window handles.
     */
    public List<String> getAllWindows() {
        return SeleniumUtils.getAllWindows(driver);
    }

    /**
     * Switch to the window represented by the given window handle.
     * Ideally, the parameter sent should be one among the handles returned
     * by getAllWindows().
     *
     * @param windowHandle
     */
    public void switchToWindow(String windowHandle) {
        SeleniumUtils.switchToWindow(driver, windowHandle);
    }

    /**
     * Determine whether an alert window is being displayed.
     *
     * @return true if an alert window is being displayed; false otherwise
     */
    public boolean isShowingAlert() {
        return SeleniumUtils.isShowingAlert(driver);
    }

    /**
     * Get the text in the displayed alert window.
     *
     * @return text in the alert as String.
     */
    public String getDisplayedAlertText() {
        return SeleniumUtils.getDisplayedAlertText(driver);
    }

    /**
     * Click on the 'Cancel' button in the displayed alert.
     */
    public void cancelAlert() {
        SeleniumUtils.cancelAlert(driver);
    }

    /**
     * Click on the 'OK' button in the displayed alert.
     */
    public void acceptAlert() {
        SeleniumUtils.acceptAlert(driver);
    }

    /**
     * Switch to the iFrame element represented by the given WebElement.
     *
     * @param frame
     */
    public void switchToIFrame(WebElement frame) {
        SeleniumUtils.switchToIFrame(driver, frame);
    }

    /**
     * Switches to the &lt;frame&gt; element at the given index (starting from 0)
     * in a page that has a &lt;frameset&gt;
     *
     * @param frameIndex
     */
    public void switchToFrameInFrameSet(int frameIndex) {
        SeleniumUtils.switchToFrameInFrameSet(driver, frameIndex);
    }

    /**
     * Switches to the parent of the currently pointed iFrame or Frame
     */
    public void switchToParentFrame() {
        SeleniumUtils.switchToParentFrame(driver);
    }

    /**
     * Switches out of all frames (and iFrames).
     */
    public void switchToTop() {
        SeleniumUtils.switchToTop(driver);
    }
}
