package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PersonalAreaPage {
    private final WebDriver driver;
    private final By nameField = By.xpath("//*[@id='root']/div/main/div/div/div/ul/li[1]/div/div/input");
    private final By emailField = By.xpath("//*[@id='root']/div/main/div/div/div/ul/li[2]/div/div/input");
    private final By logOutButton = By.xpath(".//button[text()=''Выход]");
    private final By constructorButton = By.className("AppHeader_header__linkText__3q_va ml-2");
    private final By logoButton = By.xpath("//*[@id='root']/div/header/nav/div/a");

    public PersonalAreaPage(WebDriver driver) {
        this.driver = driver;
    }
    public String getTextNameField(){
        return driver.findElement(nameField).getAttribute("value");
    }
    public String getTextEmailField(){
        return driver.findElement(emailField).getAttribute("value");
    }
    public void clickLogOutButton(){
        driver.findElement(logOutButton).click();
    }
    public void clickConstructorButton(){
        driver.findElement(constructorButton).click();
    }
    public void clickLogoButton(){
        driver.findElement(logoButton).click();
    }
}
