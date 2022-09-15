package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.data.DataGenerator.Registration.getUser;
import static ru.netology.data.DataGenerator.getRandomLogin;
import static ru.netology.data.DataGenerator.getRandomPassword;

public class AuthTest {
    @BeforeEach
    void setup() {
        Configuration.headless = true;
        open("http://localhost:9999");
    }

    @BeforeAll
    static void setUpAll() {
    }

    @Test
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var user = getRegisteredUser("active");
        //DataGenerator.RegistrationDto user = DataGenerator.Registration.getRegisteredUser("active");
        $("[data-test-id='login'] input").sendKeys(user.getLogin());
        $("[data-test-id='password'] input").sendKeys(user.getPassword());
        $(byText("Продолжить")).click();
        $(byText("Личный кабинет")).shouldBe(Condition.visible);
    }

    @Test
    void shouldGetErrorIfNotRegisteredUser() {
        var user = getRegisteredUser("active");
        var notRegisteredUser = getUser("active");
//        DataGenerator.RegistrationDto user = getRegisteredUser("active");
//        DataGenerator.RegistrationDto notRegisteredUser = DataGenerator.Registration.getUser("active");
        $("[data-test-id='login'] input").sendKeys(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input").sendKeys(user.getPassword());
        $(byText("Продолжить")).click();
        $("[class='notification__content']").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
       // DataGenerator.RegistrationDto blockedUser = getRegisteredUser("blocked");
        $("[data-test-id='login'] input").sendKeys(blockedUser.getLogin());
        $("[data-test-id='password'] input").sendKeys(blockedUser.getPassword());
        $(byText("Продолжить")).click();
        $("[class='notification__content']").shouldHave(Condition.text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    void shouldGetErrorIfWrongLogin() {
        var user = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
//        DataGenerator.RegistrationDto user = getRegisteredUser("active");
//        String wrongLogin = DataGenerator.getRandomLogin();
        $("[data-test-id='login'] input").sendKeys(wrongLogin);
        $("[data-test-id='password'] input").sendKeys(user.getPassword());
        $(byText("Продолжить")).click();
        $("[class='notification__content']").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldGetErrorIfWrongPassword() {
        var user = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
//        DataGenerator.RegistrationDto user = getRegisteredUser("active");
//        String wrongPassword = DataGenerator.getRandomPassword();
        $("[data-test-id='login'] input").sendKeys(user.getLogin());
        $("[data-test-id='password'] input").sendKeys(wrongPassword);
        $(byText("Продолжить")).click();
        $("[class='notification__content']").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }


}
