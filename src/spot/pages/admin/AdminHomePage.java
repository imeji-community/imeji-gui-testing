package spot.pages.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.notAdmin.HomePage;

/**
 * HomePage for logged-in admins.
 * 
 * @author kocar
 *
 */
public class AdminHomePage extends HomePage {

	@FindBy (xpath =".//*[@id='Header:j_idt59:lnkAdmin']")
	private WebElement goToAdminRightsOverviewButton;
	
	public AdminHomePage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public UserManagementOverviewPage goToUserPolicyManagementOverviewPage() {
		goToAdminRightsOverviewButton.click();
		return PageFactory.initElements(driver, UserManagementOverviewPage.class);
	}

}
