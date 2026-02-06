package page.web;

import base.web.BasePage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class CreateEquipment extends BasePage {

    Page page;
    BrowserContext browserContext;
    private final Locator equipmentPage;
    private final Locator createNewEquipmentButton;
    private final Locator equipmentNameField;
    private final Locator plantNameField;
    private final Locator areaNameField;
    private final Locator equipmentTypeField;
    private final Locator make_ManufacturerField;
    private final Locator modelField;
    private final Locator serialNumberField;
    private final Locator applicationTypeField;
    private final Locator displayPosition;
    private final Locator couplingField;
    private final Locator notesField;
    private final Locator addParameterButton;
    private final Locator selectRawParameterDataType;
    private final Locator rawParameterNameBox;
    private final Locator rawParameterUnitBox;
    private final Locator LCLUCLTypeBox;
    private final Locator LCLBox;
    private final Locator UCLBox;
    private final Locator rawParameterNatureBox;
    private final Locator precesionBox;
    private final Locator toleranceBox;
    private final Locator locationTypeBox;
    private final Locator isExternalYesBox;
    private final Locator isExternalNoBox;
    private final Locator selectPlant;
    private final Locator selectArea;
    private final Locator selectEquipment;
    private final Locator selectAsset;
    private final Locator selectMeasurement;
    private final Locator addMultipleParametersButton;
    private final Locator multipleRawParameterBox;
    private final Locator validateMultipleRawParameter;
    private final Locator saveRawParameterButton;
    private final Locator searchEquipment;
    private final Locator searchEquipmentField;



    public CreateEquipment(Page page, BrowserContext browserContext) {
        super(page);
        this.page = page;
        this.browserContext = browserContext;
        this.equipmentPage=page.locator("//span[text()='Equipment']");
        this.createNewEquipmentButton=getByRoleButton("CREATE NEW",page);
        this.equipmentNameField=page.locator("#machineGroupName");
        this.plantNameField=page.getByRole(AriaRole.COMBOBOX).first();
        this.areaNameField=page.getByRole(AriaRole.COMBOBOX).nth(1);
        this.equipmentTypeField=page.getByRole(AriaRole.COMBOBOX).nth(2);
        this.make_ManufacturerField=page.getByRole(AriaRole.COMBOBOX).nth(3);
        this.modelField=page.getByRole(AriaRole.COMBOBOX).nth(4);
        this.serialNumberField=page.locator("#serialNumber");
        this.applicationTypeField=page.getByRole(AriaRole.COMBOBOX).nth(5);
        this.displayPosition=page.getByRole(AriaRole.COMBOBOX).nth(6);
        this.couplingField=page.getByRole(AriaRole.COMBOBOX).nth(7);
        this.notesField=page.locator("#machineGroupNote");
        this.addParameterButton=getByRoleButton("ADD PARAMETER",page);
        this.addMultipleParametersButton=getByRoleButton("ADD MULTIPLE PARAMETER",page);
        this.selectRawParameterDataType=getByPlaceholder("Select Raw Parameter Data Type",page);
        this.rawParameterNameBox=getByPlaceholder("Enter Raw Parameter Name",page);
        this.rawParameterUnitBox=getByPlaceholder("Select Raw Parameter Unit",page);
        this.LCLUCLTypeBox=getByPlaceholder("Select LCL UCL Type",page);
        this.LCLBox=getByPlaceholder("Enter lower control level",page);
        this.UCLBox=getByPlaceholder("Enter upper control level",page);
        this.rawParameterNatureBox=getByPlaceholder("Select Nature",page);
        this.precesionBox=getByPlaceholder("Enter Precision",page);
        this.toleranceBox=getByPlaceholder("Enter Tolerance",page);
        this.locationTypeBox=page.getByRole(AriaRole.COMBOBOX).last();
        this.isExternalNoBox=getBySpanAndText("No",page);
        this.isExternalYesBox=getBySpanAndText("Yes",page);
        this.selectPlant=page.locator("#selectedPlant");
        this.selectArea=page.locator("#selectedArea");
        this.selectEquipment=page.locator("#selectedEquipment");
        this.selectAsset=page.locator("#selectedAsset");
        this.selectMeasurement=page.locator("#selectedMesurementLocation");
        this.multipleRawParameterBox=page.getByRole(AriaRole.TEXTBOX).first();
        this.validateMultipleRawParameter=getBySpanAndText("Validate", page);
        this.saveRawParameterButton=getBySpanAndText("Save",page);
        this.searchEquipment=getByRoleButton("Search", page);
        this.searchEquipmentField=page.locator("[data-test-id=\"Search\"]").getByLabel("Search");
    }



    public void createNewEquipment(String equipmentName, String plantName, String areaName, String equipmentType, String makeManufacturer, String model, String serialNumber, String appType){
        equipmentPage.click();
        createNewEquipmentButton.click();
        equipmentNameField.fill(equipmentName);
        plantNameField.selectOption(plantName);
        areaNameField.selectOption(areaName);
        equipmentTypeField.selectOption(equipmentType);
        make_ManufacturerField.selectOption(makeManufacturer);
        modelField.selectOption(model);
        serialNumberField.fill(serialNumber);
        applicationTypeField.selectOption(appType);
        couplingField.selectOption("BELT DRIVEN COUPLING");
        notesField.fill("Equipment Creation");
    }

    public void openRawParameterPage(String machineName){
        equipmentPage.click();
        searchEquipment.click();
        searchEquipmentField.fill(machineName);
        Locator addParameterButton=page.locator("//*[local-name()='svg'   and contains(@title,'Add Parameters')   and @id='editForMachineGroup-"+machineName+"']");
        //Locator addParameterButton=page.locator("//*[local-name()='svg'   and contains(@title,'Add Parameters')]");
        addParameterButton.click();
    }

    public void createRawParameter(String rawParameterDataType, String rawParameterName, String rawParameterUnit,String lclLclType, int lclValue, int uclValue, String nature, int precision, boolean isExternal, String locationType, String name, String measurementName) throws InterruptedException {
        addParameterButton.click();
        selectRawParameterDataType.selectOption(rawParameterDataType);
        rawParameterNameBox.fill(rawParameterName);
        rawParameterUnitBox.selectOption(rawParameterUnit);
        UCLLCLConfiguration(lclLclType,lclValue,uclValue );
        rawParameterNatureBox.selectOption(nature);
        precesionBox.fill(String.valueOf(precision));
        if(isExternal)
            isExternalYesBox.click();
        else
            isExternalNoBox.click();

        selectLocationType(locationType,name,measurementName);
        saveRawParameterButton.click();
    }

    public void UCLLCLConfiguration(String type, int lclValue, int uclValue){
        switch (type) {
            case "NONE", "STATS" -> {
                LCLUCLTypeBox.selectOption(type);
            }
            case "FIXED" -> {
                LCLUCLTypeBox.selectOption(type);
                LCLBox.fill(String.valueOf(lclValue));
                UCLBox.fill(String.valueOf(uclValue));
            }
            default -> System.out.println("Invalid LCL UCL Type");
        }
    }

    public void selectLocationType(String locationType, String name, String measurementLocation){
        switch (locationType) {
            case "Plant" -> {
                locationTypeBox.selectOption(locationType);
                //selectPlant.fill(name);
            }
            case "Area" -> {
                locationTypeBox.selectOption(locationType);
                selectArea.fill(name);
            }
            case "Equipment" -> {
                locationTypeBox.selectOption(locationType);
                selectEquipment.fill(name);
            }
            case "Asset" -> {
                locationTypeBox.selectOption(locationType);
                selectAsset.fill(name);
            }
            case "Measurement Location" -> {
                locationTypeBox.selectOption(locationType);
                selectAsset.fill(name);
                selectMeasurement.fill(measurementLocation);
            }
            default -> {
                System.out.println("Invalid location type");
            }
        }
    }

    public void createMultipleRawParameter(String[] bulkData) throws Exception {
        addMultipleParametersButton.click();
        syncUntil(4000);
        addRawParameterDataFromUsingArray(bulkData,multipleRawParameterBox);

        validateMultipleRawParameter.click();
        syncUntil(8000);
        //saveRawParameterButton.click();
    }


    public void addRawParameterDataFromJson(JsonNode root, Locator textArea) {
        JsonNode rawParamsNode = root.get("rawParameters");
        if (rawParamsNode == null || !rawParamsNode.isArray()) {
            throw new RuntimeException("'rawParameters' node is missing or not an array in JSON");
        }
        StringBuilder bulkText = new StringBuilder();
        for (JsonNode node : rawParamsNode) {
            bulkText.append(node.asText()).append("\n");
        }
        textArea.fill(bulkText.toString());
    }


    public void addRawParameterDataFromUsingArray(String[] bulk, Locator textArea) {
        StringBuilder bulkText = new StringBuilder();
        for (String node : bulk) {
            bulkText.append(node).append("\n");
        }
        textArea.fill(bulkText.toString());
    }

    public JsonNode loadJsonFromResources(String resourcePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (is == null) {
            throw new RuntimeException("JSON not found: " + resourcePath);
        }
        return mapper.readTree(is);
    }
}
