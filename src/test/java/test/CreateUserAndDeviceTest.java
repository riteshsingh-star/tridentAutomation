package test;

import base.web.BaseTest;
import com.microsoft.playwright.Page;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import page.web.CreateDevice;
import page.web.CreateUsers;
import page.web.PageComponent;
import pojo.UserAndDevice;
import utils.ReadJsonFile;

public class CreateUserAndDeviceTest extends BaseTest {

    PageComponent pageComponent;
    Page childPage;
    UserAndDevice data;
    CreateUsers createUsers;
    CreateDevice createDevice;

    @BeforeClass
    public void createUserAndDeviceSetup(){
        data = ReadJsonFile.readJson("testData/userAndDeviceData.json", UserAndDevice.class);
        pageComponent=new PageComponent(page);
        childPage=pageComponent.moveToAdminPage(page,context);
        createUsers=new CreateUsers(childPage,context);
        createDevice=new CreateDevice(childPage,context);
    }

    @Test
    public  void createUserTest() throws InterruptedException {
        createUsers.createNewUser(data.email(),data.firstName(),data.lastName(),data.organizationName(),data.phoneNumber(),data.language(),data.unit(),data.timeZone(),data.alertMedia(),data.alertType(),data.isAdmin(), data.plantName());
    }

    @Test
    public  void createDeviceTest(){
        createDevice.createExternalDevice(data.deviceIdentifier(),data.deviceName(),data.organizationName(),data.plantName(),data.externalDeviceType());
        //createDevice.createDevice(data.deviceName(),data.deviceType(),data.plantName());
    }
}
