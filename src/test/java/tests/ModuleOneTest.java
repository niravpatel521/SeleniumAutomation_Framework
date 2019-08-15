package tests;

import wrappers.TestBase;
import org.testng.annotations.*;
import org.testng.ITestResult;
import utilities.PropertyManager;

@Listeners(wrappers.TestBase.class)

public class ModuleOneTest {

    PropertyManager propertyManager = new PropertyManager();
    TestBase base = new TestBase();

    @BeforeClass
    public void openTest() {
        base.setupBrowser(propertyManager.getResourceBundle.getProperty("OS"), propertyManager.getResourceBundle.getProperty("BROWSER"),
                propertyManager.getResourceBundle.getProperty("BASE_URL"));
    }

    @AfterMethod
    public void takeScreenshotOnFailure(ITestResult result) {
        base.captureScreenshotOnFailure(result);
    }

    @AfterClass
    public void closeTest() {
        base.teardownTest();
    }

    @Test
    public void checkWeb() {
        System.out.println("WEB LOOKS GOOD.");
    }

    @Test
    public void checkiOS() {
        System.out.println("IOS LOOKS GOOD.");
    }
}
