package edmondScripts;

import java.awt.AWTException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.components.MessageComponent.MessageType;
import spot.pages.CollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.MultipleUploadPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.CreateNewCollectionPage;

public class ProvidePublicWithItemsWithMetaDataProfileTest extends BaseSelenium {

	private LoginPage loginPage;
	private AdminHomePage adminHomePage;
	private CollectionEntryPage collectionEntryPage;
	
	private HashMap<String, String> files;
	private MultipleUploadPage multipleUploadPage;

	public final String collectionTitle = "Published test collection with default meta data profile";

	@AfterClass
	public void afterClass() {
		adminHomePage.logout();
	}
	
	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();		
		
		loginPage = new StartPage(driver).openLoginForm();
		
		files = new HashMap<String, String>();
		files.put("Chrysanthemum.jpg", "C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
//		files.put("SampleXLSXFile.xlsx", "C:\\Users\\Public\\Pictures\\Sample Pictures\\SampleXLSXFile.xlsx");
//		files.put("SampleSWCFile.swc", "C:\\Users\\Public\\Pictures\\Sample Pictures\\SampleSWCFile.swc");		
		
//		new StartPage(driver).selectLanguage(englishSetup);
	}

	@Test (priority = 1)
	public void loginTest() {
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));

		String adminFullName = getPropertyAttribute(ruFamilyName) + ", " + getPropertyAttribute(ruGivenName);
		Assert.assertEquals(adminHomePage.getLoggedInUserFullName(), adminFullName, "User name doesn't match");
	
	}

	@Test(priority = 2)
	public void createCollectionWithMetaDataProfileTest() {
		System.out.println("Attempt to create collection without meta data");
		CreateNewCollectionPage createNewCollectionPage = adminHomePage.goToCreateNewCollectionPage();
		
		String collectionDescription = "This collection has a default meta data profile. It is being published.";

		collectionEntryPage = createNewCollectionPage.createCollectionWithStandardMetaDataProfile(collectionTitle,
				collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));

		Assert.assertTrue(
				collectionEntryPage.getMessageComponent().getMessageTypeOfPageMessageArea() == MessageType.INFO,
				"Collection couldn't be created");

		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");

	}

	@Test (priority = 3)
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

	@Test (priority = 4)
	public void publishCollectionTest() {
		
		multipleUploadPage.publishCollection();
		
		String actualInfoMessage = multipleUploadPage.getMessageComponent().getInfoMessage();
		String expectedInfoMessage = "Collection released successfully";
		Assert.assertEquals(actualInfoMessage, expectedInfoMessage, "Something went wrong with the release of the collection.");
	}
}
