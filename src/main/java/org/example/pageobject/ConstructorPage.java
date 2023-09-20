package org.example.pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ConstructorPage {
    private final WebDriver driver;
    private final By headerPage = By.xpath(".//h1[text()='Соберите бургер']");
    private final By bunsSection = By.xpath(".//div[span[text()='Булки']]");
    private final By saucesSection = By.xpath(".//div[span[text()='Соусы']]");
    private final By fillingsSection = By.xpath(".//div[span[text()='Начинки']]");
    public ConstructorPage(WebDriver driver) {
        this.driver = driver;
    }
    public String getTextHeader(){
        return driver.findElement(headerPage).getText();
    }

    public void clickOnSaucesSection(){
        driver.findElement(saucesSection).click();
    }
    public void clickOnFillingsSection(){
        driver.findElement(fillingsSection).click();
    }
    public String classForCurrentSectionBuns(){
        return  driver.findElement(bunsSection).getAttribute("class");
    }
    public String classForCurrentSectionSauces(){
        return  driver.findElement(saucesSection).getAttribute("class");
    }
    public  String classForCurrentSectionFillings() {
        return driver.findElement(fillingsSection).getAttribute("class");
    }
}
