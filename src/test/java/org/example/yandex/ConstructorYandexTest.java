package org.example.chrome;

import org.example.additional.ApiCreateUser;
import org.example.additional.ApiUser;
import org.example.pageobject.ConstructorPage;
import org.example.pageobject.LoginPage;
import org.example.pageobject.MainPage;
import org.example.pageobject.PersonalAreaPage;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.example.additional.RandomData.randomString;


public class ConstructorTest {
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

        mainPage.clickPersonalAreaButton();

        PersonalAreaPage personalAreaPage = new PersonalAreaPage(driver);
        personalAreaPage.clickConstructorButton();
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

    @Test
    public void switchConstructorSection()  {
        ConstructorPage constructorPage = new ConstructorPage(driver);
        String currentSectionBuns = constructorPage.classForCurrentSectionBuns();
        Assert.assertThat(currentSectionBuns, CoreMatchers.containsString("tab_tab_type_current"));
        constructorPage.clickOnSaucesSection();
        String currentSectionSauces = constructorPage.classForCurrentSectionSauces();
        Assert.assertThat(currentSectionSauces, CoreMatchers.containsString("tab_tab_type_current"));
        constructorPage.clickOnFillingsSection();
        String currentSectionFillings = constructorPage.classForCurrentSectionFillings();
        Assert.assertThat(currentSectionFillings, CoreMatchers.containsString("tab_tab_type_current"));
    }

}