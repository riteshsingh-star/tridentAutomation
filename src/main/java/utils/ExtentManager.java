package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private static ExtentReports extent;

    /**
     * This method is used to initialize and return a singleton instance
     * of ExtentReports for the automation framework.
     * It performs the following:
     * - Creates Extent report only once (Singleton pattern)
     * - Sets report file path
     * - Configures report name and document title
     * - Attaches Spark reporter
     * - Adds system information (Framework, OS, etc.)
     *
     * @return ExtentReports instance used across the framework
     */

    public static ExtentReports getInstance() {

        if (extent == null) {
            String reportPath = System.getProperty("user.dir") + "/target/extent-reports/ExtentReport.html";
            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setReportName("Playwright Automation Report");
            spark.config().setDocumentTitle("Test Execution Report");
            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Framework", "Playwright + Java + Testng");
            extent.setSystemInfo("OS", System.getProperty("os.name"));
        }
        return extent;
    }
}
