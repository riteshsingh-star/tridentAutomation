package test.web;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import page.web.CreateWidget;
import page.web.PageComponent;
import pojo.web.DashboardData;
import utils.ReadDataFile;
import page.web.Dashboard;
import base.web.BaseTest;
import org.testng.annotations.Test;
import java.util.List;
import static utils.AssertionUtil.*;

public class DashboardTest extends BaseTest {
    PageComponent pageComponent;
    DashboardData data;
    List<String> measuresName;
    Dashboard dashboard;
    CreateWidget createWidget;


    @BeforeClass
    public void dashBoardAndWidgetSetup() throws Exception {
        pageComponent =new PageComponent(page,context);
        data = ReadDataFile.loadDataFile(DashboardData.class);
        measuresName = data.measuresName();
        dashboard = new Dashboard(page,context);
        createWidget = new CreateWidget(page,context);
/*      String actualName=dashboard.createDashboard(data.dashboardName(), data.dashboardDescription(),data.visibilityType());
        assertEquals(actualName,data.dashboardName());*/
        dashboard.searchDashboard(data.dashboardName());

    }

    @BeforeMethod
    public void openWidgetTab(){
        createWidget.openWidgetCreationPage();
    }

    //@Test
    public void addEquipmentTrendWidgetTest(){
        String widgetActualName=createWidget.addEquipmentTrendWidget(data.equipmentName(), measuresName, data.time(), data.granularity());
        assertEquals(widgetActualName,"Equipment Performance"+" "+data.equipmentName(), "Equipment Trend Widget name should match expected value");
    }

    //@Test
    public void equipmentStoppageDonutWidgetTest(){
        String widgetActualName=createWidget.createEquipmentStoppageDonutWidget(data.equipmentName());
        assertEquals(widgetActualName,"Equipment Runtime"+" "+data.equipmentName(), "Equipment Stoppage Donut Widget name should match expected value");
    }

    //@Test
    public void equipmentBatchDetailsTest() {
        String widgetActualName=createWidget.createEquipmentBatchDetailsWidget(data.equipmentName());
        assertEquals(widgetActualName,"Equipment Batch Details"+" "+data.equipmentName(), "Equipment Batch Details Widget name should match expected value");
    }

    //@Test
    public void batchTrendTest(){
      String widgetValidation= createWidget.createBatchTrendWidget(data.equipmentName(), measuresName);
      assertEquals(widgetValidation,"Batch Trend"+" "+data.equipmentName(), "Batch Trend Widget name should match expected value");
    }

    //@Test
    public void equipmentWidgetTest(){
        String widgetValidation= createWidget.createEquipmentWidget(data.equipmentName(), data.viewType(),true);
        assertEquals(widgetValidation,"Equipment Runtime"+" "+data.equipmentName(), "Equipment Widget name should match expected value");
    }

}
