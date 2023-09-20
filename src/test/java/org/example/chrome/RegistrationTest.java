package org.example.chrome;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.example.additional.ApiUser;
import org.example.pageobject.LoginPage;
import org.example.pageobject.MainPage;
import org.example.pageobject.RegisterPage;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.example.additional.RandomData.randomString;
import static org.hamcrest.CoreMatchers.startsWith;
@RunWith(Parameterized.class)
public class RegistrationTest {
    private static WebDriver driver;
    private final By buttonToGoRegistrationUser;
    public String email = randomString(8) + "@yandex.ru";
    public String password = randomString(7);
    public String name = randomString(9);

    public RegistrationTest(By buttonToGoRegistrationUser){
        this.buttonToGoRegistrationUser = buttonToGoRegistrationUser;
    }

    @Before
    public void setDriver(){
        driver = new ChromeDriver();
        driver.get("https://stellarburgers.nomoreparties.site/");
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
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
    @DisplayName("Check successful registration and receive login page")
    @Description("Registration can be from main page by click account button and click personal area button")
    @Test
    public void successfulRegistrationFromLoginToAccountButtonAndFromPersonalAreaButtonTest(){
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

        String confirmationOfOpeningLoginPage = loginPage.receiveTitleLoginPage();
        MatcherAssert.assertThat("После регистрации не открывается страница авторизации", confirmationOfOpeningLoginPage, startsWith("Вход"));
    }

    @After
    public void cleanAfterTest(){
        ApiUser user = new ApiUser(email, password);
        String authorization =  given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post("https://stellarburgers.nomoreparties.site/api/auth/login")
                .then().extract().body().path("accessToken");
        given()
                .header("Authorization", authorization)
                .body(user)
                .delete("https://stellarburgers.nomoreparties.site/api/auth/user");
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}
