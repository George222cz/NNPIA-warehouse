package cz.upce.warehouse.ui;

import cz.upce.warehouse.datafactory.Creator;
import cz.upce.warehouse.entity.User;
import cz.upce.warehouse.repository.ProductRepository;
import cz.upce.warehouse.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;

import java.io.File;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Import(Creator.class)
public class LoginTest {

    private WebDriver driver;

    private final String frontendURL = "http://localhost";

    @Autowired
    Creator creator;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeAll
    public static void setupWebdriverChromeDriver() {
        String chromeDriverPath = LoginTest.class.getResource("/chromedriver.exe").getFile();
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        String circleCIChromeDriverPath = "/usr/local/bin/chromedriver";
        if(new File(circleCIChromeDriverPath).exists()){
            System.setProperty("webdriver.chrome.driver", circleCIChromeDriverPath);
        }
    }

    @BeforeEach
    public void setup() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setHeadless(true);
        driver = new ChromeDriver(chromeOptions);

        User tmp = new User();
        tmp.setPassword("$2a$10$991/Un.tLw9l3HqaRUb1gui75xySXIwfBRdannXLaMgDBUlL7j03O");
        user = (User) creator.saveEntity(tmp);
    }

    @AfterEach
    public void teardown() {
        userRepository.delete(user);

        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void successLoginTest() {
        driver.get(frontendURL+"/login");
        driver.findElement(By.id("username")).sendKeys("Test username");
        driver.findElement(By.id("password")).sendKeys("heslo");
        driver.findElement(By.xpath("//input[@type='submit']")).click();

        try {
            new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.id("welcome")));
        } catch (Exception e) {
            System.out.println(driver.getPageSource());
            throw e;
        }

        Assert.assertEquals(frontendURL+"/profile", driver.getCurrentUrl());
    }

    @Test
    public void failedLoginTest() {
        driver.get(frontendURL+"/login");
        driver.findElement(By.id("username")).sendKeys("Test username");
        driver.findElement(By.id("password")).sendKeys("spatne heslo");
        driver.findElement(By.xpath("//input[@type='submit']")).click();

        new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.id("unauthorized")));

        Assert.assertEquals(frontendURL+"/login", driver.getCurrentUrl());
    }
}