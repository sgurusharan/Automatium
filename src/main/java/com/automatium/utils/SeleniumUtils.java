package com.automatium.utils;

import com.automatium.config.TestConfiguration;
import com.automatium.logging.TestLogger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Gurusharan on 19-11-2016.
 */
public class SeleniumUtils {
    private static final String UIACTIONTAG = "UIACTIONTAG";
    protected static TestLogger logger = TestLogger.getSingletonInstance();

    public static void gotoURL(WebDriver driver, String url) {
        logger.info(UIACTIONTAG, "Navigating to URL: " + url);
        driver.get(url);
    }

    public static String getTitle(WebDriver driver) {
        return driver.getTitle();
    }

    public static String getUrl(WebDriver driver) {
        return driver.getCurrentUrl();
    }

    public static WebElement locateElement(WebDriver driver, By locator) {
        logger.info(UIACTIONTAG, "Attempting to locate " + locator);
        return driver.findElement(locator);
    }

    public static Object executeJavaScript(WebDriver driver, String javaScript) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        return jsExecutor.executeScript(javaScript);
    }

    public static String getTextFromElement(WebElement webElement) {
        logger.info(UIACTIONTAG, "Attempting to get text from " + WebElementUtils.webElementAsString(webElement));
        return webElement.getText();
    }

    public static String getPageSource(WebDriver driver) {
        logger.info(UIACTIONTAG, "Attempting to get HTML page source");
        return driver.getPageSource();
    }

    public static Select locateSelect(WebDriver driver, By locator) {
        return new Select(locateElement(driver, locator));
    }

    public static void click(WebElement elementToClick) {
        logger.info(UIACTIONTAG, "Attempting to click on " + WebElementUtils.webElementAsString(elementToClick));
        elementToClick.click();
    }

    public static void type(WebElement inputElementToType, String textToType) {
        logger.info(UIACTIONTAG, "Attempting to type in " + WebElementUtils.webElementAsString(inputElementToType));
        inputElementToType.sendKeys(textToType);
    }

    public static void selectDisplayedOption(Select selectList, String displayedOptionToSelect) {
        logger.info(UIACTIONTAG, "Attempting to select " + WebElementUtils.mockOptionTagWithDisplayString(displayedOptionToSelect));
        selectList.selectByVisibleText(displayedOptionToSelect);
    }

    public static void selectNthOption(Select selectList, int n) {
        logger.info(UIACTIONTAG, "Attempting to select option at index " + (n - 1));
        selectList.selectByIndex(n - 1);
    }

    public static void selectOptionByValue(Select selectList, String valueToSelect) {
        logger.info(UIACTIONTAG, "Attempting to select " + WebElementUtils.mockOptionTagWithValue(valueToSelect));
        selectList.selectByValue(valueToSelect);
    }

    public static void deselectDisplayedOption(Select selectList, String displayedOptionToSelect) {
        logger.info(UIACTIONTAG, "Attempting to deselect " + WebElementUtils.mockOptionTagWithDisplayString(displayedOptionToSelect));
        selectList.deselectByVisibleText(displayedOptionToSelect);
    }

    public static void deselectNthOption(Select selectList, int n) {
        logger.info(UIACTIONTAG, "Attempting to deselect option at index " + (n - 1));
        selectList.deselectByIndex(n - 1);
    }

    public static void deselectOptionByValue(Select selectList, String valueToSelect) {
        logger.info(UIACTIONTAG, "Attempting to deselect " + WebElementUtils.mockOptionTagWithValue(valueToSelect));
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
        logger.info(UIACTIONTAG, "Waiting for " + locator + "...");
        WebDriverWait waiter = new WebDriverWait(driver, timeoutInSeconds);
        try {
            waiter.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            logger.info(UIACTIONTAG, "Timed out!");
            return false;
        }
        logger.info(UIACTIONTAG, "Found!");
        return true;
    }

    public static boolean waitForElementToDisappear(WebDriver driver, By locator, Long timeoutInSeconds) {
        logger.info(UIACTIONTAG, "Waiting for " + locator + " to disappear...");
        WebDriverWait waiter = new WebDriverWait(driver, timeoutInSeconds);
        try {
            waiter.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            logger.info(UIACTIONTAG, "Timed out!");
            return false;
        }
        logger.info(UIACTIONTAG, "Disappeared!");
        return true;
    }

    public static boolean waitForElementToEnable(WebElement element, Long timeoutInSeconds) {
        logger.info(UIACTIONTAG, "Waiting for " + WebElementUtils.webElementAsString(element) + " to get enabled...");
        while (timeoutInSeconds > 0) {
            if (element.isEnabled()) {
                break;
            }
            timeoutInSeconds--;
            GlobalUtils.wait(GlobalUtils.ONE_SECOND_IN_MILLIS);
        }
        if (element.isEnabled()) {
            logger.info(UIACTIONTAG, "Enabled!");
        }
        else {
            logger.info(UIACTIONTAG, "Timed out!");
        }
        return element.isEnabled();
    }

    public static boolean waitForPageWithTitle(WebDriver driver, String title, Long timeoutInSeconds) {
        logger.info(UIACTIONTAG, "Waiting for web page with title '" + title + "'...");
        WebDriverWait waiter = new WebDriverWait(driver, timeoutInSeconds);
        try {
            waiter.until(ExpectedConditions.titleIs(title));
        }
        catch (TimeoutException e) {
            logger.info(UIACTIONTAG, "Timed out!");
            return false;
        }
        logger.info(UIACTIONTAG, "Found!");
        return true;
    }

    public static boolean waitForAlert(WebDriver driver, Long timeoutInSeconds) {
        logger.info(UIACTIONTAG, "Waiting for an alert...");
        WebDriverWait waiter = new WebDriverWait(driver, timeoutInSeconds);
        try {
            waiter.until(ExpectedConditions.alertIsPresent());
        }
        catch (TimeoutException e) {
            logger.info(UIACTIONTAG, "Timed out!");
            return false;
        }
        logger.info(UIACTIONTAG, "Found!");
        return true;
    }

    public static String getCurrentWindowHandle(WebDriver driver) {
        return driver.getWindowHandle();
    }

    public static List<String> getAllWindows(WebDriver driver) {
        return driver.getWindowHandles().stream().collect(Collectors.toList());
    }

    public static void switchToWindow(WebDriver driver, String windowHandle) {
        logger.info(UIACTIONTAG, "Switching to window: " + windowHandle);
        driver.switchTo().window(windowHandle);
    }

    public static boolean isShowingAlert(WebDriver driver) {
        String currentWindowHandle = getCurrentWindowHandle(driver);
        logger.info(UIACTIONTAG, "Checking if an alert is displayed...");
        try {
            Alert alertHandle = driver.switchTo().alert();
            logger.info(UIACTIONTAG, "Found alert with text: " + getAlertText(alertHandle));

        } catch (NoAlertPresentException e) {
            logger.info(UIACTIONTAG, "No alert found!");
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
            logger.info(UIACTIONTAG, "Dismissing it.");
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
            logger.info(UIACTIONTAG, "Accepting it.");
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

    public static void quit(WebDriver driver) {
        logger.info(UIACTIONTAG, "Closing window(s): " + getTitle(driver) + " (and subwindows opened if any)");
        driver.quit();
    }

    public static WebDriver getNewWebDriver() {
        return TestConfiguration.getSingletonInstance().getTestTarget().getAsWebDriver();
    }
}
