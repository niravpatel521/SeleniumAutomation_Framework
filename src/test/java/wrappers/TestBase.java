package wrappers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.*;
import utilities.PropertyManager;

/**
 * @author shwetankvashishtha
 */
public class TestBase extends WebDriverTestBase implements ISuiteListener, IInvokedMethodListener {

    PropertyManager propertyManager = new PropertyManager();

    @Override
    public WebDriver getdriver() {
        return driver;
    }

    @Override
    public void setupBrowser(String OS, String browser, String URL) {
        if (OS.equalsIgnoreCase("mac")) {
            if (browser.equalsIgnoreCase("ie")) {
                System.setProperty("webdriver.ie.driver", propertyManager.getResourceBundle.getProperty("IE_DRIVER_PATH_MAC"));
                driver = new InternetExplorerDriver();
                openURL(URL);
                driver.manage().window().maximize();
            } else if (browser.equalsIgnoreCase("firefox") || browser.equalsIgnoreCase("ff")) {
                System.setProperty("webdriver.gecko.driver",
                        propertyManager.getResourceBundle.getProperty("GECKO_DRIVER_PATH_MAC"));
                driver = new FirefoxDriver();
                openURL(URL);
                driver.manage().window().maximize();
            } else if (browser.equalsIgnoreCase("chrome")) {
                System.setProperty("webdriver.chrome.driver",
                        propertyManager.getResourceBundle.getProperty("CHROME_DRIVER_PATH_MAC"));
                driver = new ChromeDriver();
                openURL(URL);
                driver.manage().window().maximize();
            } else if (browser.equalsIgnoreCase("headless") || browser.equalsIgnoreCase("phantomjs")) {
                System.setProperty("phantomjs.binary.path",
                        propertyManager.getResourceBundle.getProperty("PHANTOMJS_DRIVER_PATH_MAC"));
                driver = new PhantomJSDriver();
                openURL(URL);
                driver.manage().window().maximize();
            } else if (browser.equalsIgnoreCase("safari")) {
                driver = new SafariDriver();
                openURL(URL);
                driver.manage().window().maximize();
            }
        } else if (OS.equalsIgnoreCase("win") || OS.equalsIgnoreCase("window")) {
            if (browser.equalsIgnoreCase("ie")) {
                System.setProperty("webdriver.ie.driver", propertyManager.getResourceBundle.getProperty("IE_DRIVER_PATH_WIN"));
                driver = new InternetExplorerDriver();
                openURL(URL);
                driver.manage().window().maximize();
            } else if (browser.equalsIgnoreCase("firefox") || browser.equalsIgnoreCase("ff")) {
                System.setProperty("webdriver.gecko.driver",
                        propertyManager.getResourceBundle.getProperty("GECKO_DRIVER_PATH_WIN"));
                driver = new FirefoxDriver();
                openURL(URL);
                driver.manage().window().maximize();
            } else if (browser.equalsIgnoreCase("chrome")) {
                System.setProperty("webdriver.chrome.driver",
                        propertyManager.getResourceBundle.getProperty("CHROME_DRIVER_PATH_WIN"));
                driver = new ChromeDriver();
                openURL(URL);
                driver.manage().window().maximize();
            } else if (browser.equalsIgnoreCase("headless") || browser.equalsIgnoreCase("phantomjs")) {
                System.setProperty("phantomjs.binary.path",
                        propertyManager.getResourceBundle.getProperty("PHANTOMJS_DRIVER_PATH_WIN"));
                driver = new PhantomJSDriver();
                openURL(URL);
                driver.manage().window().maximize();
            }
        }
        Reporter.log(browser + " launched successfully");
        Reporter.log(URL + " passed to browser");
    }

    @Override
    public void setUpiOS() throws MalformedURLException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("app", "iOS");
        desiredCapabilities.setCapability("platformName", propertyManager.getResourceBundle.getProperty("IOS_PLATFORM_NAME"));
        desiredCapabilities.setCapability("platformVersion", propertyManager.getResourceBundle.getProperty("IOS_PLATFORM_VERSION"));
        desiredCapabilities.setCapability("deviceName", propertyManager.getResourceBundle.getProperty("IOS_DEVICE_NAME"));
        Reporter.log("Setting up iOS Appium capabilities...");
        driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), desiredCapabilities);
    }

    @Override
    public void setUpAndroid(Boolean skipUnlock, Boolean noReset) throws MalformedURLException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("BROWSER_NAME", "Android");
        desiredCapabilities.setCapability("platformName", propertyManager.getResourceBundle.getProperty("ANDROID_PLATFORM_NAME"));
        desiredCapabilities.setCapability("VERSION", propertyManager.getResourceBundle.getProperty("ANDROID_PLATFORM_VERSION"));
        desiredCapabilities.setCapability("deviceName", propertyManager.getResourceBundle.getProperty("ANDROID_DEVICE_NAME"));
        desiredCapabilities.setCapability("noReset", noReset);
        desiredCapabilities.setCapability("skipUnlock", skipUnlock);
        desiredCapabilities.setCapability("appPackage", propertyManager.getResourceBundle.getProperty("ANDROID_APP_PACKAGE"));
        desiredCapabilities.setCapability("appActivity", propertyManager.getResourceBundle.getProperty("ANDROID_APP_ACTIVITY"));
        Reporter.log("Setting up Android Appium capabilities...");
        driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), desiredCapabilities);
    }

    @Override
    public void openURL(String AUT_URL) {
        driver.get(AUT_URL);
        Reporter.log(AUT_URL + " has been opened in browser successfully");
    }

    @Override
    public void teardownTest() {
        refreshPage();
        shutdownBrowser();
        generateTestReport();
    }

    @Override
    public void shutdownBrowser() {
        clearCache();
        closeBrowser();
    }

    @Override
    public void refreshPage() {
        Reporter.log("On Request: Refreshing Page...");
        driver.navigate().refresh();
        Reporter.log("Refreshed page " + driver.getTitle() + " successfully");
    }

    @Override
    public void clearCache() {
        Reporter.log("Clearing browser cache...");
        driver.manage().deleteAllCookies();
    }

    @Override
    public void closeBrowser() {
        driver.quit();
        Reporter.log("On Request: Closed all browser instances successfully");
    }

    @Override
    public void generateTestReport() {
        Reporter.log("Opening Test Reports in browser...");
        setupBrowser(propertyManager.getResourceBundle.getProperty("OS"), propertyManager.getResourceBundle.getProperty("BROWSER"),
                propertyManager.getResourceBundle.getProperty("REPORT_LOCATION"));
        Reporter.log("Opened ReportNG based Test Report " + propertyManager.getResourceBundle.getProperty("REPORT_LOCATION") + " in browser successfully");
    }

    @Override
    public void openCurrentBrowserInstance() {
        driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "n");
    }

    @Override
    public void openNewBrowserTab() {
        driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
    }

    @Override
    public void pause(long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void implicitWait(long timeout) {
        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
    }

    @Override
    public void fluentWait(long timeout) {
        new FluentWait(driver).withTimeout(timeout, TimeUnit.SECONDS).pollingEvery(5, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);

    }

    @Override
    public void waitForPageLoad(long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver wdriver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        });
    }

    @Override
    public void waitForElementVisible(long timeout, WebElement element) {
        new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOf(element));
        // new WebDriverWait(driver,
        // timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("")));
    }

    @Override
    public void waitForElementDisappear(long timeout, WebElement element) {
        new WebDriverWait(driver, timeout).until(ExpectedConditions.invisibilityOf(element));
    }

    @Override
    public void waitForElementToBeClickable(long timeout, WebElement element) {
        new WebDriverWait(driver, timeout).until(ExpectedConditions.elementToBeClickable(element));
    }

    @Override
    public void waitForPageExpire(long timeout) {
        driver.manage().timeouts().pageLoadTimeout(timeout, TimeUnit.SECONDS);
    }

    @Override
    public void setAsyncScriptTimeout(long timeout) {
        driver.manage().timeouts().setScriptTimeout(timeout, TimeUnit.SECONDS);
    }

    @Override
    public void waitForTextToBePresentInElement(long timeout, WebElement element, String text) {
        new WebDriverWait(driver, timeout).until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    @Override
    public void waitForAlertPresent(long timeout, WebElement element) {

    }

    @Override
    public void waitForPageTitle(long timeout, String pageTitle) {
        new WebDriverWait(driver, timeout).until(ExpectedConditions.titleIs(pageTitle));
    }

    @Override
    public void frameToBeAvailableAndSwitch(long timeout, String frameID) {
        new WebDriverWait(driver, timeout).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id(frameID)));
    }

    @Override
    public void captureScreenshotOnFailure(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            captureScreenshot(result.getName());
        }
    }

    @Override
    public void captureScreenshot(String screenshotName) {
        Calendar cal = Calendar.getInstance();
        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        File screenshotSrc = takesScreenshot.getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenshotSrc,
                    new File(propertyManager.getResourceBundle.getProperty("SCREENSHOT_LOCATION") + cal.getTime()
                            + screenshotName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean verifyTrue(Boolean Condition, String SuccessMessage, String FailureMessage) {
        if (Condition) {
            Reporter.log(SuccessMessage, true);
            return true;
        } else {
            Reporter.log(FailureMessage, false);
            return false;
        }
    }

    @Override
    public boolean verifyFalse(Boolean Condition, String SuccessMessage, String FailureMessage) {
        if (!Condition) {
            Reporter.log(SuccessMessage, true);
            return true;
        } else {
            Reporter.log(FailureMessage, false);
            return false;
        }
    }

    @Override
    public void assertTrue(boolean condition, String successMessage, String failureMessage) {
        if (condition) {
            Reporter.log(successMessage, true);
        } else {
            Reporter.log(failureMessage, false);
            throw new AssertionError("Assertion Error");
        }
    }

    @Override
    public void assertFalse(boolean condition, String successMessage, String failureMessage) {
        if (condition) {
            Reporter.log(successMessage, true);
        } else {
            Reporter.log(failureMessage, false);
            throw new AssertionError("Assertion Error");
        }
    }

    @Override
    public void printTestResults(ITestResult result) {
        Reporter.log("Test Method resides in " + result.getTestClass().getName(), true);
        if (result.getParameters().length != 0) {
            String params = null;
            for (Object parameter : result.getParameters()) {
                params += parameter.toString() + ",";
            }
            Reporter.log("Test Method had the following parameters : " + params, true);
        }
        String status = null;
        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                status = "Pass";
                break;
            case ITestResult.FAILURE:
                status = "Failed";
                break;
            case ITestResult.SKIP:
                status = "Skipped";
        }
        Reporter.log("Test Status: " + status, true);
    }

    @Override
    public Integer getMethodName(ITestResult result) {
        String method = result.getMethod().getMethodName();
        String[] str = method.split("C");
        int methodName = Integer.parseInt(str[1]);
        return methodName;
    }

    @Override
    public void onStart(ISuite arg0) {
        Reporter.log("About to begin executing Suite " + arg0.getName(), true);
    }

    @Override
    public void onFinish(ISuite arg0) {
        Reporter.log("About to end executing Suite " + arg0.getName(), true);
    }

    @Override
    public void onStart(ITestContext arg0) {
        Reporter.log("About to begin executing Test " + arg0.getName(), true);
    }

    @Override
    public void onFinish(ITestContext arg0) {
        Reporter.log("Completed executing test " + arg0.getName(), true);
    }

    @Override
    public void beforeInvocation(IInvokedMethod arg0, ITestResult arg1) {
        String textMsg = "About to begin executing following method : " + returnMethodName(arg0.getTestMethod());
        Reporter.log(textMsg, true);
    }

    @Override
    public void afterInvocation(IInvokedMethod arg0, ITestResult arg1) {
        String textMsg = "Completed executing following method : " + returnMethodName(arg0.getTestMethod());
        Reporter.log(textMsg, true);
    }

    @Override
    public String returnMethodName(ITestNGMethod method) {
        return method.getRealClass().getSimpleName() + "." + method.getMethodName();
    }
}