package ru.netology.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditCardOrderTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        System.setProperty("webdriver.chrome.driver", "..\\webdriver\\chromedriver.exe");
        if (System.getProperty("os.name").contains("nux")) {
            System.setProperty("webdriver.chrome.driver", "./artifacts/chromedriver");
        }
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldCreditCardOrderSuccess() {
        driver.get("http://localhost:9999");

        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector(
                "[data-test-id=name] input")).sendKeys("Иванов Иван Петрович");
        form.findElement(By.cssSelector(
                "[data-test-id=phone] input")).sendKeys("+79099009090");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.cssSelector("button[type=button]")).click();
        String text = driver.findElement(By.cssSelector(
                "[data-test-id=order-success]")).getText();

        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    public void shouldCreditCardOrderUnsuccessWithEmptyName() {
        driver.get("http://localhost:9999");

        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector(
                "[data-test-id=name] input")).sendKeys("");
        form.findElement(By.cssSelector(
                "[data-test-id=phone] input")).sendKeys("+79099009090");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.cssSelector("button[type=button]")).click();
        String text = driver.findElement(By.cssSelector(
                ".input_invalid[data-test-id=name] .input__sub")).getText();

        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    public void shouldCreditCardOrderUnsuccessWithLatinName() {
        driver.get("http://localhost:9999");

        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector(
                "[data-test-id=name] input")).sendKeys("Ivanov Ivan");
        form.findElement(By.cssSelector(
                "[data-test-id=phone] input")).sendKeys("+79099009090");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.cssSelector("button[type=button]")).click();
        String text = driver.findElement(By.cssSelector(
                ".input_invalid[data-test-id=name] .input__sub")).getText();

        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    public void shouldCreditCardOrderUnsuccessWithPhone() {
        driver.get("http://localhost:9999");

        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector(
                "[data-test-id=name] input")).sendKeys("Иванов Петр Иванович");
        form.findElement(By.cssSelector(
                "[data-test-id=phone] input")).sendKeys("+2222222222222222222222222222222222");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.cssSelector("button[type=button]")).click();
        String text = driver.findElement(
                By.cssSelector(".input_invalid[data-test-id=phone] .input__sub")).getText();

        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    public void shouldCreditCardOrderUnsuccessUncheckedAgreement() {
        driver.get("http://localhost:9999");

        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector(
                "[data-test-id=name] input")).sendKeys("Иванов Петр Иванович");
        form.findElement(By.cssSelector(
                "[data-test-id=phone] input")).sendKeys("+79099099090");
        form.findElement(By.cssSelector("button[type=button]")).click();
        String text = driver.findElement(
                By.cssSelector(".input_invalid[data-test-id=agreement] .checkbox__text")).getText();

        assertEquals("Я соглашаюсь с условиями обработки и использования моих " +
                "персональных данных и разрешаю сделать запрос в бюро кредитных историй", text.trim());
    }
}
