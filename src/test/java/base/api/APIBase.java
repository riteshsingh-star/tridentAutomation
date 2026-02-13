package base.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.util.*;

import static factory.ApiFactory.*;

public class APIBase {

    protected static final Logger log = LogManager.getLogger(APIBase.class);

    @BeforeSuite
    public void setUpAPI() {
        initApi();
        createRequestContext();
    }

    @AfterSuite
    public void tearDownAPI() {
        log.info("Closing the API Session");
        closeApi();
    }


}
