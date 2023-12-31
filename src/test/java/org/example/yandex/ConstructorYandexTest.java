package org.example.yandex;

import io.qameta.allure.junit4.DisplayName;
import org.example.additional.ApiCreateUser;
import org.example.additional.ApiUser;
import org.example.additional.ForYandexSetUp;
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
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.example.additional.RandomData.randomString;


public class ConstructorYandexTest {
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
    public void setYandexDriver(){
        System.setProperty("webdriver.chrome.driver", ForYandexSetUp.PATH_TO_YANDEX_DRIVER);
        ChromeOptions options = new ChromeOptions();
        options.setBinary(ForYandexSetUp.PATH_TO_YANDEX_EXE);
        driver = new ChromeDriver(options);
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

    @DisplayName("Make sure we're in the buns section")
    @Test
    public void isCurrentConstructorSectionBuns()  {
        ConstructorPage constructorPage = new ConstructorPage(driver);
        String currentSectionBuns = constructorPage.classForCurrentSectionBuns();
        Assert.assertThat(currentSectionBuns, CoreMatchers.containsString("tab_tab_type_current"));
    }
    @DisplayName("Make sure we're in the sauces section")
    @Test
    public void isCurrentConstructorSectionSauces(){
        ConstructorPage constructorPage = new ConstructorPage(driver);
        constructorPage.clickOnSaucesSection();
        String currentSectionSauces = constructorPage.classForCurrentSectionSauces();
        Assert.assertThat(currentSectionSauces, CoreMatchers.containsString("tab_tab_type_current"));
    }
    @DisplayName("Make sure we're in the fillings section")
    @Test
    public void isCurrentConstructorSectionFillings(){
        ConstructorPage constructorPage = new ConstructorPage(driver);
        constructorPage.clickOnFillingsSection();
        String currentSectionFillings = constructorPage.classForCurrentSectionFillings();
        Assert.assertThat(currentSectionFillings, CoreMatchers.containsString("tab_tab_type_current"));
    }

}
