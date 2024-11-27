package com.fitpeo.automation;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class FitPeoAutomation2 {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setup() {
        // Setup WebDriver
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    }

    @Test
    public void testFitPeoAutomation() {
       
    	//Navigate to the FitPeo Homepage:
        driver.get("https://www.fitpeo.com");
        Assert.assertEquals(driver.getTitle(), "Remote Patient Monitoring (RPM) - fitpeo.com", "Homepage title mismatch!");

        //Navigate to the Revenue Calculator Page:
        WebElement calculatorLink = driver.findElement(By.xpath("//div[@class='satoshi MuiBox-root css-r3xbt7']"));
        calculatorLink.click();
        //Assert.assertTrue(driver.getCurrentUrl().contains("revenue-calculator"), "Failed to navigate to Revenue Calculator page!");

        //Scroll Down to the Slider section:
        WebElement slider = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='MuiSlider-root MuiSlider-colorPrimary MuiSlider-sizeMedium css-16i48op']")));
        scrollIntoView(driver, slider);
        Assert.assertTrue(slider.isDisplayed(), "Slider is not visible on the page!");

        // Adjust the Slider to 820
        Actions actions = new Actions(driver);
        actions.dragAndDropBy(slider, 50, 0).perform(); // Adjust offset for 820
        WebElement sliderValue = driver.findElement(By.xpath("//span[@class='MuiSlider-root MuiSlider-colorPrimary MuiSlider-sizeMedium css-16i48op']"));
        Assert.assertEquals(sliderValue.getAttribute("value"), "820", "Slider value mismatch!");

        // Update Text Field to 560 and validate Slider Value:
        WebElement textField =driver.findElement(By.xpath("//div[@class='MuiInputBase-root MuiOutlinedInput-root MuiInputBase-colorPrimary MuiInputBase-formControl MuiInputBase-sizeSmall css-129j43u']//input[@type='number']")); 
        textField.clear();
        textField.sendKeys("560");
        Assert.assertEquals(sliderValue.getAttribute("value"), "560", "Slider value not updated correctly!");

        // Select CPT Codes
        String[] cptCodes = {"99091", "99453", "99454", "99474"};

        for (String code : cptCodes) {
            
            String cssSelector = "label:contains('" + code + "') + input[type='checkbox']";
           
            WebElement checkbox = driver.findElement(By.cssSelector(cssSelector));
            checkbox.click();
            
            Assert.assertTrue(checkbox.isSelected(), "CPT-" + code + " checkbox was not selected!");
        }

        //Validate Total Recurring Reimbursement
        WebElement reimbursementTotal = driver.findElement(By.cssSelector("div[class='MuiBox-root css-m1khva'] p[class='MuiTypography-root MuiTypography-body1 inter css-12bch19']"));
        String actualTotal = reimbursementTotal.getText();
        String expectedTotal = "$110700";
        Assert.assertEquals(actualTotal, expectedTotal, "Total Recurring Reimbursement mismatch!");
    }

    @AfterClass
    public void tearDown() {
      
        if (driver != null) {
            //driver.quit();
        }
    }

   
    private static void scrollIntoView(WebDriver driver, WebElement element) {
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }
}
