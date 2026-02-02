package listeners;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.microsoft.playwright.Page;
import base.web.BaseTest;
import org.apache.logging.log4j.Logger;
import utils.ExtentManager;
import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.apache.logging.log4j.LogManager;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Listeners implements ITestListener {

    private static final Logger logger = LogManager.getLogger(Listeners.class);
    private static ExtentReports extent = ExtentManager.getInstance();
    private static ThreadLocal<ExtentTest> testReport = new ThreadLocal<>();

    @Override
    public void onTestSkipped(ITestResult result) {
        testReport.get().pass("Test Skipped");
        logger.info("{}Skipped", result.getMethod().getMethodName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extent.createTest(result.getMethod().getMethodName());
        testReport.set(test);
        test.log(com.aventstack.extentreports.Status.INFO, "Test Started");
        logger.info("{}Test Started", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        Allure.step(result.getMethod().getMethodName()+" Test Success");
        testReport.get().pass("Test Passed");
        logger.info("{}Passed", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Allure.step(result.getMethod().getMethodName()+" Failed");
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
                    logger.error("{}Failed", result.getMethod().getMethodName());
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
