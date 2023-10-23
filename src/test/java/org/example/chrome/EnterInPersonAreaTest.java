package org.example.chrome;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.example.additional.ApiCreateUser;
import org.example.additional.ApiUser;
import org.example.pageobject.LoginPage;
import org.example.pageobject.MainPage;
import org.example.pageobject.PersonalAreaPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.example.additional.RandomData.randomString;
import static org.junit.Assert.assertEquals;

public class EnterInPersonAreaTest {
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
    @Before
    public void preparing(){
        MainPage mainPage = new MainPage(driver);
        mainPage.clickPersonalAreaButton();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.setEmail(email);
        loginPage.setPassword(password);
        loginPage.clickLoginButton();
    }
    @DisplayName("Check enter in person area/account")
    @Description ("Check that we can enter the personal account and see the data of the current user")
    @Test
    public void enterInPersonAreaTest(){
        MainPage mainPage = new MainPage(driver);
        mainPage.clickPersonalAreaButton();

        PersonalAreaPage personalAreaPage = new PersonalAreaPage(driver);
        String nameUser = personalAreaPage.getTextNameField();
        String emailUser = personalAreaPage.getTextEmailField();
        assertEquals(name, nameUser);
        assertEquals(email, emailUser);
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
