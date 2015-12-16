package spot.pages;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import spot.util.DefaultMetaDataProfile;

public class SingleUploadPage extends BasePage {

	@FindBy(css="#uploader")
	private WebElement uploadButton;
	
	@FindBy(css="#singleUpload\\:collections")
	private WebElement selectCollectionToUploadDropBox;
	
	@FindBy(css="#singleUpload\\:submitBottom")
	private WebElement saveButton;
	
	@FindBy(css="#singleUpload\\:metadata\\:0\\:editMd\\:inputText")
	private WebElement metaDataTitleTextField;
	
	@FindBy(css="#singleUpload\\:metadata\\:1\\:editMd\\:inputNumber")
	private WebElement metaDataIDTextField;
	
	@FindBy(css="#singleUpload\\:metadata\\:2\\:editMd\\:inputPerson\\:inputFamilyNameText1")
	private WebElement metaDataAuthorFamilyNameTextField;
	
	@FindBy(css="#singleUpload\\:metadata\\:3\\:editMd\\:inputPublicationURI")
	private WebElement metaDataPublicationTextField;
	
	@FindBy(css="#singleUpload\\:metadata\\:4\\:editMd\\:inputDate")
	private WebElement metaDataDateTextField;
	
	public SingleUploadPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public DetailedItemViewPage upload(String pathToFile, String collectionTitle) throws AWTException {
		
		uploadButton.click();		
		
		selectFile(pathToFile);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#singleUpload\\:collections")));
		Select selectDropBox = new Select(selectCollectionToUploadDropBox);
		selectDropBox.selectByVisibleText(collectionTitle);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#singleUpload\\:submitBottom")));
		saveButton.click();
		
		return PageFactory.initElements(driver, DetailedItemViewPage.class);
	}
	
	private void selectFile(String pathToFile) throws AWTException {
		StringSelection stringSelection = new StringSelection(pathToFile);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		Robot robot = new Robot();
		
	    robot.keyPress(KeyEvent.VK_CONTROL);
	    robot.keyPress(KeyEvent.VK_V);
	    robot.keyRelease(KeyEvent.VK_V);
	    robot.keyRelease(KeyEvent.VK_CONTROL);
	    
	    robot.delay(3000);
	    robot.keyPress(KeyEvent.VK_ENTER);
	    robot.keyRelease(KeyEvent.VK_ENTER);
	}

	public DetailedItemViewPage uploadAndFillMetaData(String filePath, String collectionTitle) throws AWTException {
		
		uploadButton.click();		
		
		selectFile(filePath);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#singleUpload\\:collections")));
		Select selectDropBox = new Select(selectCollectionToUploadDropBox);
		selectDropBox.selectByVisibleText(collectionTitle);
		
		fillMetaData();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#singleUpload\\:submitBottom")));
		saveButton.click();
		
		return PageFactory.initElements(driver, DetailedItemViewPage.class);
	}

	private void fillMetaData() {
		
		DefaultMetaDataProfile defaultMetaDataProfile = DefaultMetaDataProfile.getInstance();
		
		
		//setting title
		wait.until(ExpectedConditions.visibilityOf(metaDataTitleTextField));
		metaDataTitleTextField.sendKeys(defaultMetaDataProfile.getTitle());

		//setting author family name
		wait.until(ExpectedConditions.visibilityOf(metaDataAuthorFamilyNameTextField));
		metaDataAuthorFamilyNameTextField.sendKeys(defaultMetaDataProfile.getAuthor());
		
		//setting id
		wait.until(ExpectedConditions.visibilityOf(metaDataIDTextField));
		metaDataIDTextField.sendKeys(defaultMetaDataProfile.getId());
		
		//setting publication link
		wait.until(ExpectedConditions.visibilityOf(metaDataPublicationTextField));
		metaDataPublicationTextField.sendKeys(defaultMetaDataProfile.getPublicationLink());
		
		//setting date
		wait.until(ExpectedConditions.visibilityOf(metaDataDateTextField));
		metaDataDateTextField.sendKeys(defaultMetaDataProfile.getDate());
		
	}
	
}
