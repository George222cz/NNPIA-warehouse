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
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;

import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Import(Creator.class)
public class LoginTest {

    private WebDriver driver;

    @LocalServerPort
    private int localServerPort;

    @Autowired
    Creator creator;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeAll
    public static void setupWebdriverChromeDriver() {
        String chromeDriverPath = LoginTest.class.getResource("/chromedriver.exe").getFile();
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
    }

    @BeforeEach
    public void setup() {
        driver = new ChromeDriver();
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
        driver.get("http://localhost:3000/login");
        driver.findElement(By.id("username")).sendKeys("Test username");
        driver.findElement(By.id("password")).sendKeys("heslo");
        driver.findElement(By.xpath("//input[@type='submit']")).click();

        new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.id("welcome")));

        Assert.assertEquals("http://localhost:3000/profile", driver.getCurrentUrl());
    }

    @Test
    public void failedLoginTest() {
        driver.get("http://localhost:3000/login");
        driver.findElement(By.id("username")).sendKeys("Test username");
        driver.findElement(By.id("password")).sendKeys("spatne heslo");
        driver.findElement(By.xpath("//input[@type='submit']")).click();

        new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.id("unauthorized")));

        Assert.assertEquals("http://localhost:3000/login", driver.getCurrentUrl());
    }
}