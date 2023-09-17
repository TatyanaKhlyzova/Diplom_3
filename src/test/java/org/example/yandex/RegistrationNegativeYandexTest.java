package org.example.yandex;

import org.example.ForYandexSetUp;
import org.example.LoginPage;
import org.example.MainPage;
import org.example.RegisterPage;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

import static org.example.RandomData.randomString;
import static org.hamcrest.CoreMatchers.startsWith;

@RunWith(Parameterized.class)
public class RegistrationNegativeTest {
    private static WebDriver driver;
    private final By buttonToGoRegistrationUser;
    public String email = randomString(10) + "@yandex.ru";
    public String name = randomString(12);
    public RegistrationNegativeTest(By buttonToGoRegistrationUser){
        this.buttonToGoRegistrationUser = buttonToGoRegistrationUser;
    }
    static MainPage mainPage = new MainPage(driver);
    static By personalAreaButton = mainPage.getPersonalAreaButton();
    static By loginToAccountButton = mainPage.getLoginToAccountButton();
    @Parameterized.Parameters
    public static Object[][] getButtons() {
        return new Object[][]{
                {personalAreaButton},
                {loginToAccountButton},
        };
    }

    @Before
    public void setYandexDriver(){
        System.setProperty("webdriver.chrome.driver", ForYandexSetUp.PATH_TO_YANDEX_DRIVER);
        ChromeOptions options = new ChromeOptions();
        options.setBinary(ForYandexSetUp.PATH_TO_YANDEX_EXE);
        driver = new ChromeDriver(options);
        driver.get("https://stellarburgers.nomoreparties.site/");
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @Test
    public void registrationWithPasswordLessThenSixCharTest(){
        String password = randomString(5);
        MainPage mainPage = new MainPage(driver);
        mainPage.clickButtonToGoRegistrationUser(buttonToGoRegistrationUser);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.scrollToRegistrationButton();
        loginPage.clickRegistrationButton();

        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.setName(name);
        registerPage.setEmail(email);
        registerPage.setPassword(password);
        registerPage.clickRegistrationButton();

        String mistakeRegistrationWithPasswordLessThenSixChar = registerPage.getTextMistake();
        MatcherAssert.assertThat("Нет ошибки пссле ввода пароля меньше 6 символов", mistakeRegistrationWithPasswordLessThenSixChar, startsWith("Некорректный пароль"));

    }
    @After
    public void tearDown() {
        driver.quit();
    }

}
