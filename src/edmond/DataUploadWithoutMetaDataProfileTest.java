package edmond;

import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.components.MessageComponent.MessageType;
import spot.pages.CollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.MultipleUploadPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.CreateNewCollectionPage;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class DataUploadWithoutMetaDataProfileTest extends BaseSelenium {

	private LoginPage loginPage;
	private AdminHomePage adminHomePage;
	private CollectionEntryPage collectionEntryPage;
	
	private HashMap<String, String> files;
	private MultipleUploadPage multipleUploadPage;

	@BeforeMethod
	public void beforeMethod() {
	}

	@AfterMethod
	public void afterMethod() {
	}

	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();
		
		loginPage = new StartPage(driver).openLoginForm();
		
		files = new HashMap<String, String>();
		files.put("Chrysanthemum.jpg", "C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
		files.put("Desert.jpg", "C:\\Users\\Public\\Pictures\\Sample Pictures\\Desert.jpg");
		files.put("Hydrangeas.jpg", "C:\\Users\\Public\\Pictures\\Sample Pictures\\Hydrangeas.jpg");
		files.put("Jellyfish.jpg", "C:\\Users\\Public\\Pictures\\Sample Pictures\\Jellyfish.jpg");
	}

	@AfterClass
	public void afterClass() {
	}

	@Test(groups = "login")
	public void loginTest() {
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute("aSpotUserName"),
				getPropertyAttribute("aSpotPassword"));

		String adminFullName = getPropertyAttribute("aGivenName") + " " + getPropertyAttribute("aFamilyName");
		Assert.assertEquals(adminHomePage.getLoggedInUserFullName(), adminFullName, "User name doesn't match");
	}

	@Test(groups="collectionCreated", dependsOnGroups = "login")
	public void createCollectionWithoutMetaDataProfileTest() {
		CreateNewCollectionPage createNewCollectionPage = adminHomePage.goToCreateNewCollectionPage();

		String collectionTitle = "Testsammlung Edmond";
		String collectionDescription = "Das ist eine Testbeschreibung f�r eine neue Sammlung mit dem Titel " + collectionTitle + ".";

		collectionEntryPage = createNewCollectionPage.fillForm(collectionTitle,
				collectionDescription, getPropertyAttribute("aGivenName"), getPropertyAttribute("aFamilyName"),
				getPropertyAttribute("aOrganizationName"));

		Assert.assertTrue(
				collectionEntryPage.getMessageComponent().getMessageTypeOfPageMessageArea() == MessageType.INFO,
				"Collection couldn't be created");

		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");
	}

	@Test (dependsOnGroups="collectionCreated")
	public void uploadFilesTest() throws AWTException {
		multipleUploadPage = collectionEntryPage.uploadContent();		
		
		for (Map.Entry<String, String> file : files.entrySet()) {
			multipleUploadPage.addFile(file.getValue());
		}
		
		List<String> fileNames = new ArrayList<String>(files.keySet());
		
		multipleUploadPage.startUpload();
		
		boolean isVerificationSuccessfull = multipleUploadPage.verifyUploadedFiles(fileNames);
		
		Assert.assertTrue(isVerificationSuccessfull, "The list of uploaded files is probably incomplete.");
	}

	@Test
	public void sendCollectionUrlToUserPerMailTest() {
		
		String emailUser = getPropertyAttribute("tuEmailAddress");
		
		multipleUploadPage.shareCollectionWithUser(emailUser);
	}

	@Test
	public void publishCollectionTest() {
	}
	
	
}
