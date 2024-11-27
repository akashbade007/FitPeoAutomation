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

import java.time.Duration;

public class FitPeoAutomation {

    public static void main(String[] args) {
      
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
           
            driver.manage().window().maximize();

           
            driver.get("https://www.fitpeo.com");

           
            WebElement calculatorLink = driver.findElement(By.linkText("Revenue Calculator"));
            calculatorLink.click();

           
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement slider = wait.until(ExpectedConditions.visibilityOfElementLocated
            		(By.xpath("//span[@class='MuiSlider-root MuiSlider-colorPrimary MuiSlider-sizeMedium css-16i48op']")));
            scrollIntoView(driver, slider);

          
            Actions actions = new Actions(driver);
            actions.dragAndDropBy(slider, 50, 0).perform(); // Adjust the offset to set the slider to 820
            WebElement sliderValue = driver.findElement(By.xpath("//span[@class='MuiSlider-root MuiSlider-colorPrimary MuiSlider-sizeMedium css-16i48op']"));
            assert sliderValue.getAttribute("value").equals("820") : "Slider not set to 820";

            // Step 5: Update Text Field to 560
            WebElement textField = driver.findElement(By.xpath("//div[@class='MuiInputBase-root MuiOutlinedInput-root MuiInputBase-colorPrimary MuiInputBase-formControl MuiInputBase-sizeSmall css-129j43u']//input[@type='number']")); 
            textField.clear();
            textField.sendKeys("560");
            assert sliderValue.getAttribute("value").equals("560") : "Slider not updated to 560";

            // Step 6: Select CPT Codes
            String[] cptCodes = {"99091", "99453", "99454", "99474"};

            for (String code : cptCodes) {
                
                String cssSelector = "label:contains('" + code + "') + input[type='checkbox']";
               
                WebElement checkbox = driver.findElement(By.cssSelector(cssSelector));
                checkbox.click();
                
                Assert.assertTrue(checkbox.isSelected(), "CPT-" + code + " checkbox was not selected!");
            }
            // Step 7: Validate Total Recurring Reimbursement
            WebElement reimbursementTotal = driver.findElement(By.cssSelector("div[class='MuiBox-root css-m1khva'] p[class='MuiTypography-root MuiTypography-body1 inter css-12bch19']"));
            String expectedTotal = "$110700";
            assert reimbursementTotal.getText().equals(expectedTotal) : "Reimbursement value mismatch";

            System.out.println("Automation completed successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();
        }
    }

    // Helper function to scroll an element into view
    private static void scrollIntoView(WebDriver driver, WebElement element) {
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }
}
