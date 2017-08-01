package com.automatium.utils;

import com.automatium.config.TestConfiguration;
import com.automatium.logging.TestLogger;
import org.openqa.selenium.*;
import org.openqa.selenium.security.UserAndPassword;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Gurusharan on 19-11-2016.
 */
public class SeleniumUtils {

    public static final long DEFAULT_IMPLICIT_WAIT_SECONDS = 0;
    public static final long DEFAULT_PAGE_LOAD_WAIT_SECONDS = -1;
    public static final long DEFAULT_SCRIPT_WAIT_SECONDS = 0;

    private static final String UIACTIONTAG = "UIACTIONTAG";
    protected static TestLogger logger = TestLogger.getSingletonInstance();

    public static void gotoURL(WebDriver driver, String url) {
        logger.debug(UIACTIONTAG, "Navigating to URL: " + url);
        driver.get(url);
    }

    public static String getTitle(WebDriver driver) {
        return driver.getTitle();
    }

    public static String getUrl(WebDriver driver) {
        return driver.getCurrentUrl();
    }

    public static WebElement locateElement(WebDriver driver, By locator) {
        logger.debug(UIACTIONTAG, "Attempting to locate " + locator);
        return driver.findElement(locator);
    }

    public static Object executeJavaScript(WebDriver driver, String javaScript) {
        logger.debug(UIACTIONTAG, "Attempting to execute JavaScript:\n>>>>>\n" + javaScript + "\n<<<<<\n" );
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        return jsExecutor.executeScript(javaScript);
    }

    public static Object executeJavaScript(WebDriver driver, String javaScript, Object... arguments) {
        logger.debug(UIACTIONTAG, "Attempting to execute JavaScript:\n>>>>>\n" + javaScript + "\n<<<<<\nwith " + arguments.length + " arguments" );
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        return jsExecutor.executeScript(javaScript, arguments);
    }

    public static String getTextFromInputOrTextarea(WebDriver driver, WebElement textBoxOrTextArea) {
        return (String) executeJavaScript(driver, "return arguments[0].value", textBoxOrTextArea);
    }

    private static boolean isElementInputOrTextarea(WebElement webElement) {
        String tagName = webElement.getTagName();
        String type = webElement.getAttribute("type");
        return (tagName.equalsIgnoreCase("textarea") || (tagName.equalsIgnoreCase("input")));
    }

    public static String getTextFromElement(WebElement webElement) {
        return getTextFromElement(null, webElement);
    }

    public static String getTextFromElement(WebDriver driver, WebElement webElement) {
        logger.debug(UIACTIONTAG, "Attempting to get text from " + WebElementUtils.webElementAsString(webElement));
        if (isElementInputOrTextarea(webElement)) {
            // For text boxes and textareas, we can't gettext() may not work
            // Also, we need the driver object in this case to execute JS
            if (driver == null) {
                throw new NullPointerException("Looks like " + WebElementUtils.webElementAsString(webElement) + " is a text field or textarea. Please pass a non-null WebDriver object to get its updated value.");
            }
            return getTextFromInputOrTextarea(driver, webElement);
        }
        else {
            return webElement.getText();
        }
    }

    public static String getPageSource(WebDriver driver) {
        logger.debug(UIACTIONTAG, "Attempting to get HTML page source");
        return driver.getPageSource();
    }

    public static Select locateSelect(WebDriver driver, By locator) {
        return new Select(locateElement(driver, locator));
    }

    public static void click(WebElement elementToClick) {
        logger.debug(UIACTIONTAG, "Attempting to click on " + WebElementUtils.webElementAsString(elementToClick));
        elementToClick.click();
    }

    public static void type(WebElement inputElementToType, String textToType) {
        logger.debug(UIACTIONTAG, "Attempting to type in " + WebElementUtils.webElementAsString(inputElementToType));
        inputElementToType.sendKeys(textToType);
    }

    public static void selectDisplayedOption(Select selectList, String displayedOptionToSelect) {
        logger.debug(UIACTIONTAG, "Attempting to select " + WebElementUtils.mockOptionTagWithDisplayString(displayedOptionToSelect));
        selectList.selectByVisibleText(displayedOptionToSelect);
    }

    public static void selectNthOption(Select selectList, int n) {
        logger.debug(UIACTIONTAG, "Attempting to select option at index " + (n - 1));
        selectList.selectByIndex(n - 1);
    }

    public static void selectOptionByValue(Select selectList, String valueToSelect) {
        logger.debug(UIACTIONTAG, "Attempting to select " + WebElementUtils.mockOptionTagWithValue(valueToSelect));
        selectList.selectByValue(valueToSelect);
    }

    public static void deselectDisplayedOption(Select selectList, String displayedOptionToSelect) {
        logger.debug(UIACTIONTAG, "Attempting to deselect " + WebElementUtils.mockOptionTagWithDisplayString(displayedOptionToSelect));
        selectList.deselectByVisibleText(displayedOptionToSelect);
    }

    public static void deselectNthOption(Select selectList, int n) {
        logger.debug(UIACTIONTAG, "Attempting to deselect option at index " + (n - 1));
        selectList.deselectByIndex(n - 1);
    }

    public static void deselectOptionByValue(Select selectList, String valueToSelect) {
        logger.debug(UIACTIONTAG, "Attempting to deselect " + WebElementUtils.mockOptionTagWithValue(valueToSelect));
        selectList.deselectByValue(valueToSelect);
    }

    public static void selectAllOptions(Select selectList) {
        for (int i = 1; i <= selectList.getOptions().size(); i++) {
            selectNthOption(selectList, i);
        }
    }

    public static void deselectAllOptions(Select selectList) {
        for (int i = 1; i <= selectList.getOptions().size(); i++) {
            deselectNthOption(selectList, i);
        }
    }

    public static boolean waitForElement(WebDriver driver, By locator, Long timeoutInSeconds) {
        logger.debug(UIACTIONTAG, "Waiting for " + locator + "...");
        WebDriverWait waiter = new WebDriverWait(driver, timeoutInSeconds);
        try {
            waiter.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            logger.debug(UIACTIONTAG, "Timed out!");
            return false;
        }
        logger.debug(UIACTIONTAG, "Found!");
        return true;
    }

    public static boolean waitForElementToDisappear(WebDriver driver, By locator, Long timeoutInSeconds) {
        logger.debug(UIACTIONTAG, "Waiting for " + locator + " to disappear...");
        WebDriverWait waiter = new WebDriverWait(driver, timeoutInSeconds);
        try {
            waiter.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            logger.debug(UIACTIONTAG, "Timed out!");
            return false;
        }
        logger.debug(UIACTIONTAG, "Disappeared!");
        return true;
    }

    public static boolean waitForElementToEnable(WebElement element, Long timeoutInSeconds) {
        logger.debug(UIACTIONTAG, "Waiting for " + WebElementUtils.webElementAsString(element) + " to get enabled...");
        while (timeoutInSeconds > 0) {
            if (element.isEnabled()) {
                break;
            }
            timeoutInSeconds--;
            GlobalUtils.wait(GlobalUtils.ONE_SECOND_IN_MILLIS);
        }
        if (element.isEnabled()) {
            logger.debug(UIACTIONTAG, "Enabled!");
        }
        else {
            logger.debug(UIACTIONTAG, "Timed out!");
        }
        return element.isEnabled();
    }

    public static boolean waitForPageWithTitle(WebDriver driver, String title, Long timeoutInSeconds) {
        logger.debug(UIACTIONTAG, "Waiting for web page with title '" + title + "'...");
        WebDriverWait waiter = new WebDriverWait(driver, timeoutInSeconds);
        try {
            waiter.until(ExpectedConditions.titleIs(title));
        }
        catch (TimeoutException e) {
            logger.debug(UIACTIONTAG, "Timed out!");
            return false;
        }
        logger.debug(UIACTIONTAG, "Found!");
        return true;
    }

    public static boolean waitForAlert(WebDriver driver, Long timeoutInSeconds) {
        logger.debug(UIACTIONTAG, "Waiting for an alert...");
        WebDriverWait waiter = new WebDriverWait(driver, timeoutInSeconds);
        try {
            waiter.until(ExpectedConditions.alertIsPresent());
        }
        catch (TimeoutException e) {
            logger.debug(UIACTIONTAG, "Timed out!");
            return false;
        }
        logger.debug(UIACTIONTAG, "Found!");
        return true;
    }

    public static String getCurrentWindowHandle(WebDriver driver) {
        return driver.getWindowHandle();
    }

    public static List<String> getAllWindows(WebDriver driver) {
        return driver.getWindowHandles().stream().collect(Collectors.toList());
    }

    public static void switchToWindow(WebDriver driver, String windowHandle) {
        logger.debug(UIACTIONTAG, "Switching to window: " + windowHandle);
        driver.switchTo().window(windowHandle);
    }

    public static boolean isShowingAlert(WebDriver driver) {
        String currentWindowHandle = getCurrentWindowHandle(driver);
        logger.debug(UIACTIONTAG, "Checking if an alert is displayed...");
        try {
            Alert alertHandle = driver.switchTo().alert();
            logger.debug(UIACTIONTAG, "Found alert with text: " + getAlertText(alertHandle));

        } catch (NoAlertPresentException e) {
            logger.debug(UIACTIONTAG, "No alert found!");
            return false;
        }
        switchToWindow(driver, currentWindowHandle);
        return true;
    }

    private static String getAlertText(Alert alert) {
        return alert.getText();
    }

    public static String getDisplayedAlertText(WebDriver driver) {
        if (isShowingAlert(driver)) {
            String currentWindowHandle = getCurrentWindowHandle(driver);
            String toReturn = getAlertText(driver.switchTo().alert());
            switchToWindow(driver, currentWindowHandle);
            return toReturn;
        }
        logger.error(UIACTIONTAG, "No alert found!");
        return null;
    }

    public static void cancelAlert(WebDriver driver) {
        if (isShowingAlert(driver)) {
            String currentWindowHandle = getCurrentWindowHandle(driver);
            Alert alertHandle = driver.switchTo().alert();
            logger.debug(UIACTIONTAG, "Dismissing it.");
            alertHandle.dismiss();
            switchToWindow(driver, currentWindowHandle);
        }
        else {
            logger.error(UIACTIONTAG, "No alert found to cancel!");
        }
    }

    public static void acceptAlert(WebDriver driver) {
        if (isShowingAlert(driver)) {
            String currentWindowHandle = getCurrentWindowHandle(driver);
            Alert alertHandle = driver.switchTo().alert();
            logger.debug(UIACTIONTAG, "Accepting it.");
            alertHandle.accept();
            switchToWindow(driver, currentWindowHandle);
        }
        else {
            logger.error(UIACTIONTAG, "No alert found to accept!");
        }
    }

    public static void switchToIFrame(WebDriver driver, WebElement frame) {
        driver.switchTo().frame(frame);
    }

    public static void switchToFrameInFrameSet(WebDriver driver, int frameIndex) {
        driver.switchTo().frame(frameIndex);
    }

    public static void switchToParentFrame(WebDriver driver) {
        driver.switchTo().parentFrame();
    }

    public static void switchToTop(WebDriver driver) {
        driver.switchTo().defaultContent();
    }

    public static void typeTextInPrompt(WebDriver driver, String textToType) {
        logger.debug(UIACTIONTAG, "Attempting to input text in prompt - " + textToType);
        String currentWindowHandle = driver.getWindowHandle();
        driver.switchTo().alert().sendKeys(textToType);
        driver.switchTo().window(currentWindowHandle);
    }

    public static void performHTTPAuthentication(WebDriver driver, String username, String password) {
        logger.debug(UIACTIONTAG, "Attempting to authenticate as user - " + username);
        String currentWindowHandle = driver.getWindowHandle();
        driver.switchTo().alert().authenticateUsing(new UserAndPassword(username, password));
        driver.switchTo().window(currentWindowHandle);
    }

    public static void quit(WebDriver driver) {
        logger.debug(UIACTIONTAG, "Closing window(s): " + getTitle(driver) + " (and subwindows opened if any)");
        driver.quit();
    }

    public static WebDriver getNewWebDriver() {
        WebDriver driver = TestConfiguration.getSingletonInstance().getTestTarget().getAsWebDriver();
        setDefaultDriverSettingsAndConfigurations(driver);
        return driver;
    }

    private static void setDefaultDriverSettingsAndConfigurations(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(DEFAULT_IMPLICIT_WAIT_SECONDS, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(DEFAULT_PAGE_LOAD_WAIT_SECONDS, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(DEFAULT_SCRIPT_WAIT_SECONDS, TimeUnit.SECONDS);
    }
}
