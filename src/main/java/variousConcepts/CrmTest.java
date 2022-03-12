package variousConcepts;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class CrmTest {

	String browser;
	String url;
	WebDriver driver;

	// ELEMENT LIST
	By USERNAME_LOCATOR = By.xpath("//input[@id='username']");
	By PASSWORD_LOCATOR = By.xpath("//*[@id=\"password\"]");
	By SIGNIN_BUTTON_LOCATOR = By.xpath("/html/body/div/div/div/form/div[3]/button");
	By DASHBOARD_HEADER_LOCATOR = By.xpath("//h2[contains(text(), 'Dashboard')]");
	By CUSTOMER_MENU_LOCATOR = By.xpath("//*[@id=\"side-menu\"]/li[3]/a/span[1]");
	By ADD_CUSTOMER_MENU_LOCATOR = By.xpath("//*[@id=\"side-menu\"]/li[3]/ul/li[1]/a");
	By ADD_CONTACT_HEADER_LOCATOR = By.xpath("//*[@id=\"page-wrapper\"]/div[3]/div[1]/div/div/div/div[1]/h5");
	By FULL_NAME_LOCATOR = By.xpath("//*[@id=\"account\"]");
	By COMPANY_DROPDOWN_LOCATOR = By.xpath("//select[@id=\"cid\"]");
	By EMAIL_LOCATOR = By.xpath("//*[@id=\"email\"]");
	By COUNTRY_LOCATOR = By.xpath("//select[@id=\"country\"]");

	@BeforeTest
	public void readConfig() {
		// FileReader //Scanner //InputStream //BufferedReader

		try {
			InputStream input = new FileInputStream("src\\main\\java\\config\\config.properties");
			Properties prop = new Properties();
			prop.load(input);
			browser = prop.getProperty("browser");
			System.out.println("Browser Used = " + browser);
			url = prop.getProperty("url");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@BeforeMethod
	public void init() {

		if (browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("firefox")) {
			System.setProperty("webdriver.gecko.driver", "drivers\\geckodriver.exe");
			driver = new FirefoxDriver();
		}

		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.get(url);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	// @Test(priority = 1)
	public void loginTest() {
		driver.findElement(USERNAME_LOCATOR).sendKeys("demo@techfios.com");
		driver.findElement(PASSWORD_LOCATOR).sendKeys("abc123");
		driver.findElement(SIGNIN_BUTTON_LOCATOR).click();

		String dashboardHearderText = driver.findElement(DASHBOARD_HEADER_LOCATOR).getText();
		Assert.assertEquals(dashboardHearderText, "Dashboard", "Wrong Page!!");
	}

	@Test(priority = 2)
	public void addCustomerTest() {
		loginTest();
		driver.findElement(CUSTOMER_MENU_LOCATOR).click();
		driver.findElement(ADD_CUSTOMER_MENU_LOCATOR).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(ADD_CONTACT_HEADER_LOCATOR));

		Assert.assertEquals(driver.findElement(ADD_CONTACT_HEADER_LOCATOR).getText(), "Add Contact", "Wrong page!!");

		driver.findElement(FULL_NAME_LOCATOR).sendKeys("Selenium October" + generateRandom(9999));

		
		selectFromDropdown(COMPANY_DROPDOWN_LOCATOR, "Techfios");

		driver.findElement(EMAIL_LOCATOR).sendKeys(generateRandom(999) + "demo@techfios.com");
		
		selectFromDropdown(COUNTRY_LOCATOR, "Afghanistan");

	}

	public void selectFromDropdown(By locator, String visibleText) {
		Select sel = new Select(driver.findElement(locator));
		sel.selectByVisibleText(visibleText);
	}

	public int generateRandom(int bounderyNum) {
		Random rnd = new Random();
		int generatedNum = rnd.nextInt(bounderyNum);
		return generatedNum;

	}

	// @AfterMethod
	public void tearDown() {
		driver.close();
		driver.quit();
	}

}
