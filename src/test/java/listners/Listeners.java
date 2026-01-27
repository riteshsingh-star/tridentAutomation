package listners;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.microsoft.playwright.Page;
import base.web.BaseTest;
import com.trident.playwright.utils.ExtentManager;
import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Listeners implements ITestListener {

    static final Logger logger = Logger.getLogger(Listeners.class);
    private static ExtentReports extent = ExtentManager.getInstance();
    private static ThreadLocal<ExtentTest> testReport = new ThreadLocal<>();

    @Override
    public void onTestSkipped(ITestResult result) {
        testReport.get().pass("Test Skipped");
        logger.info(result.getMethod().getMethodName()+"Skipped");
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extent.createTest(result.getMethod().getMethodName());
        testReport.set(test);
        test.log(com.aventstack.extentreports.Status.INFO, "Test Started");
        logger.info(result.getMethod().getMethodName()+"Started");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        testReport.get().pass("Test Passed");
        logger.info(result.getMethod().getMethodName()+"Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        testReport.get().fail(result.getThrowable());
        Object testClass = result.getInstance();
        if (testClass instanceof BaseTest) {
            Page page = ((BaseTest) testClass).getPage();
            if (page != null) {
                try {
                    Files.createDirectories(Paths.get("screenshots"));
                    String path = "screenshots/" + result.getMethod().getMethodName() + ".png";
                    page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)).setFullPage(true));
                    testReport.get().addScreenCaptureFromPath(path);
                    File file=new File(path);
                    Allure.addAttachment("Screenshot", FileUtils.openInputStream(file));
                    logger.error(result.getMethod().getMethodName()+"Failed");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onFinish(org.testng.ITestContext context) {
        extent.flush();
    }
}
