package spot.pages.notAdmin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import spot.pages.BasePage;
import spot.pages.CollectionEntryPage;

public class CreateNewCollectionPage extends BasePage {

	/** error occurred while filling in 'create new collection' form */
	private boolean errorOccurred;
	
	@FindBy(name="editContainer:mediaContainerForm:inputTitleText")
	private WebElement titleTextField;
	
	@FindBy(name="editContainer:mediaContainerForm:inputDescription")
	private WebElement descriptionTextField;
	
	@FindBy(name="editContainer:mediaContainerForm:persons:0:collectionAuthor:inputFamilyNameText1")
	private WebElement familyNameTextField;
	
	@FindBy(name="editContainer:mediaContainerForm:persons:0:collectionAuthor:inputGiveNameText1")
	private WebElement givenNameTextField;
	
	@FindBy(name="editContainer:mediaContainerForm:persons:0:collectionAuthor:inputAlternativeName1")
	private WebElement alternativeNameTextField;
	
	@FindBy(name="editContainer:mediaContainerForm:persons:0:collectionAuthor:inputIdentifier1")
	private WebElement identifierTextField;
	
	@FindBy(css="div.imj_organisation>div:nth-of-type(1)>div.imj_admindataValue>div.imj_admindataValueEntry>input")
	private WebElement organizationNameTextField;
	
	@FindBy(css="div.imj_organisation>div:nth-of-type(2)>div.imj_admindataValue>div.imj_admindataValueEntry>textarea")
	private WebElement organizationDecriptionTextField;
	
	@FindBy(css="div.imj_organisation>div:nth-of-type(3)>div.imj_admindataValue>div.imj_admindataValueEntry>input")
	private WebElement organizationIdentifierTextField;
	
	@FindBy(css="div.imj_organisation>div:nth-of-type(4)>div.imj_admindataValue>div.imj_admindataValueEntry>input")
	private WebElement organizationCityTextField;
	
	@FindBy(css="div.imj_organisation>div:nth-of-type(5)>div.imj_admindataValue>div.imj_admindataValueEntry>input")
	private WebElement organizationCountryTextField;
	
	@FindBy(css="table>tbody>tr>td:nth-of-type(1)>input:nth-of-type(1)")
	private WebElement defineMetaDataProfileLaterRadioBox;
	
	@FindBy(css="table>tbody>tr>td:nth-of-type(2)>input:nth-of-type(1)")
	private WebElement selectExistingMetaDataProfileRadioBox;
	
	@FindBy(css=".imj_descriptionArea select")
	private WebElement profileTemplatesDropBoxWebElement;
	
	private Select profileTemplatesDrobBox;
	
	private final String defaultProfileIdentifier = "default profile";
	
	@FindBy(css=".imj_submitButton")
	private WebElement saveButton;
	
	@FindBy(css=".imj_cancelButton")
	private WebElement cancelButton;
	
	@FindBy(name="editContainer:mediaContainerForm:persons:0:j_idt214")
	private WebElement addAuthorButton;
	
	@FindBy(name="editContainer:mediaContainerForm:persons:0:collectionAuthor:j_idt247:0:j_idt276")
	private WebElement addOrganizationButton;
	
	public CreateNewCollectionPage(WebDriver driver) {
		super(driver);
		
		errorOccurred = false;
		
		PageFactory.initElements(driver, this);
	}

	public void cancelCollectionCreation() {
		cancelButton.click();
	}
	
	public void clearForm() {
	
		clearPreFilledTextFields();
		submitForm();
	}
	
	private void clearPreFilledTextFields() {
		familyNameTextField.clear();
		givenNameTextField.clear();
		identifierTextField.clear();
		
		organizationNameTextField.clear();
		organizationIdentifierTextField.clear();
	}

	public CollectionEntryPage createCollectionWithoutStandardMetaDataProfile(String collectionTitle, String collectionDescription, String givenName, String familyName, String orgName) {
		fillForm(collectionTitle, collectionDescription, givenName, familyName, orgName);
		
		selectRadioButtonDefineMetadataProfileLater();
		
		submitForm();
		
		if (errorOccurred)
			return null;
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	public CollectionEntryPage createCollectionWithStandardMetaDataProfile(String collectionTitle, String collectionDescription, String givenName, String familyName, String orgName) {
		fillForm(collectionTitle, collectionDescription, givenName, familyName, orgName);
		
		selectAnExistingMetaDataProfile();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".imj_metadataProfilePreviewList .imj_listBody>li:nth-of-type(1)")));
//		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".imj_metadataProfilePreviewList .imj_listBody li")));
		
		submitForm();
		
		if (errorOccurred)
			return null;
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	private void selectAnExistingMetaDataProfile() {
		if (!selectExistingMetaDataProfileRadioBox.isSelected())
			selectExistingMetaDataProfileRadioBox.click();
			
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".imj_descriptionArea select")));		
		
		profileTemplatesDrobBox = new Select(profileTemplatesDropBoxWebElement);		
		profileTemplatesDrobBox.selectByVisibleText(defaultProfileIdentifier);		
	}

	private void fillForm(String collectionTitle, String collectionDescription, String givenName, String familyName, String orgName) {
		
		// person related
		setTitle(collectionTitle);
		setDescription(collectionDescription);
		confirmFamilyName(familyName);
		confirmGivenName(givenName);
		setAlternativeName("testtest");
		confirmIdentifier();
		
		// organization related
		confirmOrganizationName(orgName);
		setOrganizationDescription("This is a test description for the organization.");
		confirmOrganizationIdentifier();
		setCity("Munich");
		setCountry("Deutschland");		
	}
	
	private void submitForm() {
		saveButton.click();
	}

	private void setTitle(String title) {
		if (title.equals(""))
			errorOccurred = true;
		
		titleTextField.sendKeys(title);
	}
	
	private void setDescription(String description) {
		descriptionTextField.sendKeys(description);
	}

	private void confirmFamilyName(String familyName) {
		String preFilledFamilyName = familyNameTextField.getAttribute("value");
		
		if (!preFilledFamilyName.equals(familyName)) 
			errorOccurred = true;
	}
	
	private boolean confirmGivenName(String givenName) {
		String preFilledGivenName = givenNameTextField.getAttribute("value");
		
		if (!preFilledGivenName.equals(givenName))
			return false;
		return true;
	}
	
	private void setAlternativeName(String alternativeName) {
		alternativeNameTextField.sendKeys(alternativeName);
	}
	
	private boolean confirmIdentifier() {
		String identifier = identifierTextField.getAttribute("value");
		
		if (identifier.length() <= 0)
			return false;
		return true;
	}
	
	private void confirmOrganizationName(String orgName) {
		
		String preFilledOrgName = organizationNameTextField.getAttribute("value");
		
		if (!preFilledOrgName.equals(orgName))
			errorOccurred = true;
	}
	
	private void setOrganizationDescription(String orgDescription) {
		organizationDecriptionTextField.sendKeys(orgDescription);
	}
	
	private boolean confirmOrganizationIdentifier() {
		String orgIdentifier = organizationIdentifierTextField.getAttribute("value");
		
		if (orgIdentifier.length() <= 0)
			return false;
		return true;
	}
	
	private void setCity(String city) {
		organizationCityTextField.sendKeys(city);
	}
	
	private void setCountry(String country) {
		organizationCountryTextField.sendKeys(country);
	}
	
	private void selectRadioButtonDefineMetadataProfileLater() {
		if (!defineMetaDataProfileLaterRadioBox.isSelected())
			defineMetaDataProfileLaterRadioBox.click();
	}

	public void addAuthor() {
		addAuthorButton.click();
	}

	public void addOrganization() {
		addOrganizationButton.click();
	}
	
}
