package com.storm.pages;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AmbariLoginPage extends BasePage {
	
	static public Logger logger = Logger.getLogger(AmbariLoginPage.class);
	
	@FindBy(xpath="//input[1]")
	protected WebElement usernameInputField;
	
	@FindBy(xpath="//input[2]")
	protected WebElement passwordInputField;
	
	@FindBy(xpath="//button")
	protected WebElement loginButton;
	
	@FindBy(id="dashboard-view-tab-metrics")
	public WebElement dashboardMetricsTab;
	
	public void doLogin(String username, String password) {
		
		WebDriverWait wait = new WebDriverWait(driver,200L);
		driver.manage().window().maximize();
		usernameInputField.sendKeys(username);
		passwordInputField.sendKeys(password);
		loginButton.click();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		wait.until(ExpectedConditions.not(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='progress progress-striped active']"))));
	}

	/**
	 * @return the usernameInputField
	 */
	public WebElement getUsernameInputField() {
		return usernameInputField;
	}

	/**
	 * @param usernameInputField the usernameInputField to set
	 */
	public void setUsernameInputField(WebElement usernameInputField) {
		this.usernameInputField = usernameInputField;
	}

	/**
	 * @return the passwordInputField
	 */
	public WebElement getPasswordInputField() {
		return passwordInputField;
	}

	/**
	 * @param passwordInputField the passwordInputField to set
	 */
	public void setPasswordInputField(WebElement passwordInputField) {
		this.passwordInputField = passwordInputField;
	}

	/**
	 * @return the loginButton
	 */
	public WebElement getLoginButton() {
		return loginButton;
	}

	/**
	 * @param loginButton the loginButton to set
	 */
	public void setLoginButton(WebElement loginButton) {
		this.loginButton = loginButton;
	}

	
}
