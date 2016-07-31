import com.google.common.base.Verify;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.io.FileReader;

import static org.junit.Assert.fail;
@RunWith(value = Parameterized.class)
public class Convert{
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    private double money;

    public Convert( double money ){
        this.money =money;
    }
    @Parameters
    public static Collection<Object[]> data() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/DataTest"));
        List<Object[]> res = new ArrayList<Object[]>();
        String s;
        while((s = reader.readLine()) != null) {
            String[] params = s.split(" ");
            Object[] curData = new Object[1];
            curData[0] =Double.parseDouble(params[0]);
            res.add(curData);
        }
        return res;
    }


    @Before
    public void setUp() throws Exception {
        driver = new FirefoxDriver();
        baseUrl = "http://www.sberbank.ru/ru/person";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testConvert() throws Exception {
        driver.get(baseUrl + "/ru/person");
        driver.findElement(By.id("from")).clear();
        driver.findElement(By.id("from")).sendKeys(Double.toString(money));
        try {
            Assert.assertEquals(money*Double.parseDouble(driver.findElement(By.xpath("//span[5]")).getText()),
                    Double.parseDouble(driver.findElement(By.id("to")).getAttribute("value")), 0.01);
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        // ERROR: Caught exception [unknown command []]
    }

    @After
    public void tearDown() throws Exception {
        driver.close();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}
