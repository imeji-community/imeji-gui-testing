package spot.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CreateNewUserPage extends BasePage {

	@FindBy(id="userForm:inputEmailText")
	private WebElement emailTextField;
	
	@FindBy(id="userForm:userPerson:inputFamilyNameText1")
	private WebElement familyNameTextField;
	
	@FindBy(id="userForm:userPerson:inputGiveNameText1")
	private WebElement givenNameTextField;
	
	@FindBy(id="userForm:userPerson:inputAlternativeName1")
	private WebElement alternativeNameTextField;
	
	@FindBy(id="userForm:userPerson:inputIdentifier1")
	private WebElement identifier;
	
	@FindBy(css="div.imj_organisation>div:nth-of-type(1)>div.imj_admindataValue>div.imj_admindataValueEntry>input")
	private WebElement organizationNameTextField;
	
	@FindBy(css="div.imj_organisation>div:nth-of-type(2)>div.imj_admindataValue>div.imj_admindataValueEntry>textarea")
	private WebElement organizationDescriptionTextField;
	
	@FindBy(css="div.imj_organisation>div:nth-of-type(3)>div.imj_admindataValue>div.imj_admindataValueEntry>input")
	private WebElement organizationIdentifier;
	
	@FindBy(css="div.imj_organisation>div:nth-of-type(4)>div.imj_admindataValue>div.imj_admindataValueEntry>input")
	private WebElement cityTextField;
	
	@FindBy(css="div.imj_organisation>div:nth-of-type(5)>div.imj_admindataValue>div.imj_admindataValueEntry>input")
	private WebElement countryTextField;
	
	@FindBy(css=".imj_content>div:nth-last-child(2) input")
	private WebElement canCreateNewCollectionCheckBox;
	
	@FindBy(css=".imj_content>div:nth-last-child(1) input")
	private WebElement sendEmailCheckBox;
	
	@FindBy(css=".imj_submitButton")
	private WebElement saveButton;
	
	public CreateNewUserPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public AllUsersOverViewPage createNewUser(String newUserName) {
		
		fillPersonalData(newUserName);
		
		fillOrganizationData();
		
		// user shall receive email
		if (!sendEmailCheckBox.isSelected())
			sendEmailCheckBox.click();
		
		// user shall be able to create collections
		if (!canCreateNewCollectionCheckBox.isSelected())
			canCreateNewCollectionCheckBox.click();
		
		saveButton.click();
		
		return PageFactory.initElements(driver, AllUsersOverViewPage.class);
	}

	private void fillOrganizationData() {
		organizationNameTextField.sendKeys("MPDL");
		organizationDescriptionTextField.sendKeys("Max Planck Digital Library");
		
		// should be prefilled
		String orgIdentifierText = organizationIdentifier.getText();
		
		cityTextField.sendKeys("Munich");
		countryTextField.sendKeys("Germany");
	}

	private void fillPersonalData(String newUserName) {
		emailTextField.sendKeys(newUserName);
		familyNameTextField.sendKeys("Testuser");
		givenNameTextField.sendKeys("Tester");
		alternativeNameTextField.sendKeys("testuser");
		
		// should be prefilled
		String identifierText = identifier.getText();
	}

}
