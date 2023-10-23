package org.example.yandex;

import org.example.additional.ApiCreateUser;
import org.example.additional.ApiUser;
import org.example.additional.ForYandexSetUp;
import org.example.pageobject.ConstructorPage;
import org.example.pageobject.LoginPage;
import org.example.pageobject.MainPage;
import org.example.pageobject.PersonalAreaPage;
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

import static io.restassured.RestAssured.given;
import static org.example.additional.RandomData.randomString;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class GoToConstructorYandexTest {
    private static WebDriver driver;
    private final By buttonToGoToConstructor;
    public String email = randomString(8) + "@yandex.ru";
    public String password = randomString(7);
    public String name = randomString(9);

    public GoToConstructorYandexTest(By buttonToGoToConstructor){
        this.buttonToGoToConstructor = buttonToGoToConstructor;
    }
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
        mainPage.clickLoginToAccountButton();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.setEmail(email);
        loginPage.setPassword(password);
        loginPage.clickLoginButton();

        mainPage.clickPersonalAreaButton();
    }

    static PersonalAreaPage personalAreaPage = new PersonalAreaPage(driver);
    static By constructorButton = personalAreaPage.getConstructorButton();
    static By logoButton = personalAreaPage.getLogoButton();
    @Parameterized.Parameters
    public static Object[][] getButtons() {
        return new Object[][]{
                {constructorButton},
                {logoButton},
        };
    }
    @Test
    public void goToConstructorFromPersonalAreaByLogoButtonAndConstructorButtonTest(){
        PersonalAreaPage personalAreaPage = new PersonalAreaPage(driver);
        personalAreaPage.clickButtonToGoToConstructor(buttonToGoToConstructor);

        ConstructorPage constructorPage = new ConstructorPage(driver);
        String headerConstructorPage = constructorPage.getTextHeader();
        assertEquals("Нет перехода в конструктор", "Соберите бургер", headerConstructorPage);
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
