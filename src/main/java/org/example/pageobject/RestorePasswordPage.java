package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RestorePasswordPage {
    private final WebDriver driver;
    private final By loginButton = By.className("Auth_link__1fOlj");
    public RestorePasswordPage(WebDriver driver) {
        this.driver = driver;
    }
    public void clickLoginButton() {
        driver.findElement(loginButton).click();
    }
}
