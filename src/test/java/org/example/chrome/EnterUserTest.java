package org.example.chrome;

import io.qameta.allure.junit4.DisplayName;
import org.example.additional.ApiCreateUser;
import org.example.additional.ApiUser;
import org.example.pageobject.LoginPage;
import org.example.pageobject.MainPage;
import org.example.pageobject.RegisterPage;
import org.example.pageobject.RestorePasswordPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.example.additional.RandomData.randomString;
import static org.junit.Assert.assertEquals;

public class EnterUserTest {
    private static WebDriver driver;
    public String email = randomString(8) + "@yandex.ru";
    public String password = randomString(7);
    public String name = randomString(9);

    @Before
    public void createUser(){
        ApiCreateUser newUser = new ApiCreateUser(email, password, name);
        given()
                .header("Content-type", "application/json")
                .and()
                .body(newUser)
                .post("https://stellarburgers.nomoreparties.site/api/auth/register");
    }

    @Before
    public void setDriver(){
        driver = new ChromeDriver();
        driver.get("https://stellarburgers.nomoreparties.site/");
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }
    @DisplayName("Check enter user from main page by account button")
    @Test
    public void enterUserFromLoginToAccountButton(){
        MainPage mainPage = new MainPage(driver);
        mainPage.clickLoginToAccountButton();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.setEmail(email);
        loginPage.setPassword(password);
        loginPage.clickLoginButton();

        String textOrderButton = mainPage.getTextOrderButton();
        assertEquals("Название кнопки не поменялось - вход не выполнен", "Оформить заказ", textOrderButton);
    }
    @DisplayName("Check enter user from main page by personal area button")
    @Test
    public void enterUserFromPersonalAreaButton(){
        MainPage mainPage = new MainPage(driver);
        mainPage.clickPersonalAreaButton();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.setEmail(email);
        loginPage.setPassword(password);
        loginPage.clickLoginButton();

        String textOrderButton = mainPage.getTextOrderButton();
        assertEquals("Название кнопки не поменялось - вход не выполнен", "Оформить заказ", textOrderButton);
    }
    @DisplayName("Check enter user from registration page by login button")
    @Test
    public void enterUserFromButtonOnRegistrationPage(){
        MainPage mainPage = new MainPage(driver);
        mainPage.clickPersonalAreaButton();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.scrollToRegistrationButton();
        loginPage.clickRegistrationButton();

        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.scrollToLoginButton();
        registerPage.clickLoginButton();

        loginPage.setEmail(email);
        loginPage.setPassword(password);
        loginPage.clickLoginButton();

        String textOrderButton = mainPage.getTextOrderButton();
        assertEquals("Название кнопки не поменялось - вход не выполнен", "Оформить заказ", textOrderButton);
    }
    @DisplayName("Check enter user from restore password page by login button")
    @Test
    public void enterUserFromRestorePasswordPage(){
        MainPage mainPage = new MainPage(driver);
        mainPage.clickLoginToAccountButton();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.scrollToRestorePasswordButton();
        loginPage.clickRestorePasswordButton();

        RestorePasswordPage restorePasswordPage = new RestorePasswordPage(driver);
        restorePasswordPage.clickLoginButton();

        loginPage.setEmail(email);
        loginPage.setPassword(password);
        loginPage.clickLoginButton();

        String textOrderButton = mainPage.getTextOrderButton();
        assertEquals("Название кнопки не поменялось - вход не выполнен", "Оформить заказ", textOrderButton);
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
