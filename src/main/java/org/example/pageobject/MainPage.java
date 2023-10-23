package org.example.pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MainPage {
    private final WebDriver driver;
    private final By personalAreaButton = By.xpath(".//p[text()='Личный Кабинет']");
    private final By loginToAccountButton = By.xpath(".//button[text()='Войти в аккаунт']");
    private final By createOrderButton = By.xpath(".//button[text()='Оформить заказ']");
    public MainPage(WebDriver driver) {
        this.driver = driver;
    }
    public void clickPersonalAreaButton(){
        driver.findElement(personalAreaButton).click();
    }
    public void clickLoginToAccountButton(){
        driver.findElement(loginToAccountButton).click();
    }

    public void clickButtonToGoRegistrationUser(By buttonToGoRegistrationUser){
        driver.findElement(buttonToGoRegistrationUser).click();
    }
    public By getPersonalAreaButton() {
        return personalAreaButton;
    }
    public By getLoginToAccountButton() {
        return loginToAccountButton;
    }
    public String getTextOrderButton(){
        return driver.findElement(createOrderButton).getText();
    }


}
