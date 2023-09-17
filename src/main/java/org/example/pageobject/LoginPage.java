package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage {
    private final WebDriver driver;
    private final By emailField = By.xpath(".//input[@name='name']");
    private final By passwordField = By.xpath(".//input[@name='Пароль']");
    private final By loginButton = By.xpath(".//button[text()='Войти']");
    private final By registrationButton = By.xpath(".//a[text()='Зарегистрироваться']");
    private final By restorePasswordButton = By.xpath(".//a[text()='Восстановить пароль']");
    private final By titleLoginPage = By.xpath(".//h2[text()='Вход']");
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }
    public void setEmail(String email) {
        driver.findElement(emailField).sendKeys(email);
    }
    public void setPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }
    public void clickRegistrationButton(){
        driver.findElement(registrationButton).click();
    }
    public void clickLoginButton(){
        driver.findElement(loginButton).click();
    }
    public void clickRestorePasswordButton(){
        driver.findElement(restorePasswordButton).click();
    }
    public void scrollToRestorePasswordButton() {
        WebElement element = driver.findElement(restorePasswordButton);
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", element);
    }
    public void scrollToRegistrationButton() {
        WebElement element = driver.findElement(registrationButton );
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", element);
    }
    public String  receiveTitleLoginPage(){
        return driver.findElement(titleLoginPage).getText();
    }
}
