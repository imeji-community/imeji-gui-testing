package spot.pages;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class UserProfilePage extends BasePage {

	@FindBy(xpath=".//*[@id='userForm:userInfos']/div[2]/div[2]/span")
	private WebElement completeNameLabel;
	
	@FindBy(xpath =".//*[@id='userForm:inputPassword']")
	private WebElement newPwdTextField;
	
	@FindBy(xpath =".//*[@id='userForm:inputRespeated']")
	private WebElement repeatNewPwdTextField;
	
	@FindBy(css=".imj_admindataSetEdit .imj_editButton")
	private WebElement confirmPwdChangeButton;
	
	@FindBy(id="userForm:lnkEditUserdata")
	private WebElement editProfileButton;
	
	public UserProfilePage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public EditUserProfilePage editProfile() {
		editProfileButton.click();
		
		return PageFactory.initElements(driver, EditUserProfilePage.class);
	}
	
	public void changePassword() {
		String newPwd = generateRandomPwd();
		enterNewPassword(newPwd);
		repeatNewPassword(newPwd);
		confirmPwdChange();
	}

	private void confirmPwdChange() {
		confirmPwdChangeButton.click();
	}

	private void repeatNewPassword(String newPwd) {
		repeatNewPwdTextField.sendKeys(newPwd);		
	}

	private void enterNewPassword(String newPwd) {
		newPwdTextField.sendKeys(newPwd);
	}

	private String generateRandomPwd() {
		return RandomStringUtils.randomAscii(15);	
	}

	public boolean checkFamilyName(String newFamilyName) {
		boolean editSucceeded = false;
		
		wait.until(ExpectedConditions.visibilityOf(completeNameLabel));
		String completeName = completeNameLabel.getText();
		
		String familyName = extractFamilyName(completeName);
		
		if (familyName.equals(newFamilyName))
			editSucceeded = true;
		
		return editSucceeded;
	}
	
	private String extractFamilyName(String completeName) {
		// completeName looks sth like this: [familyName], [givenName]
		String familyName = "";
		
		int tmpIndex = completeName.indexOf(',');
		
		familyName = completeName.substring(0, tmpIndex);
		
		return familyName;
	}
	
}
