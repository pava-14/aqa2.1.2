package ru.netology.order;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CreditCardOrderTest {

    @Test
    public void shouldCreditCardOrderUnsuccessWithName() {
        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=name] input").setValue("");
        form.$("[data-test-id=phone] input").setValue("+79099009090");
        form.$("[data-test-id=agreement]").click();
        form.$("button[type=button]").click();
        $(".input_invalid[data-test-id=name] .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldCreditCardOrderUnsuccessWithPhone() {
        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=name] input").setValue("Петров Иван Петрович");
        form.$("[data-test-id=phone] input").setValue("+2222222222222222222222222222222222");
        form.$("[data-test-id=agreement]").click();
        form.$("button[type=button]").click();
        $(".input_invalid[data-test-id=phone] .input__sub")
                .shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    public void shouldCreditCardOrderUnsuccessWithUncheckedAgreement() {
        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=name] input").setValue("Петров Иван Петрович");
        form.$("[data-test-id=phone] input").setValue("+79099099090");
        form.$("button[type=button]").click();
        $(".input_invalid[data-test-id=agreement] .checkbox__text")
                .shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих " +
                        "персональных данных и разрешаю сделать запрос в бюро кредитных историй"));
    }
}
