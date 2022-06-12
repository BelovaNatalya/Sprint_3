package ru.praktikum_services.qa_scooter.courierTests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum_services.qa_scooter.courier.Courier;
import ru.praktikum_services.qa_scooter.courier.CourierCredentials;
import ru.praktikum_services.qa_scooter.courier.Errors;
import ru.praktikum_services.qa_scooter.courier.RandomCourier;
import ru.praktikum_services.qa_scooter.rest_assured.CourierEndpointsRequests;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

public class LoginCourierTest {

    CourierEndpointsRequests courierEndpointsRequests;
    Courier courier;
    int courierID;

    public static final String MISSING_LOGIN_OR_PASSWORD = "Недостаточно данных для входа";
    public static final String PAIR_OF_LOGIN_PASSWORD_DOESNT_EXIST = "Учетная запись не найдена";

    @Before
    public void setUp() {
        courierEndpointsRequests = new CourierEndpointsRequests();
        courier = RandomCourier.getRandomCourier();
        courierEndpointsRequests.createCourier(courier);
    }

    @After
    public void cleanUp() {
        try {
            if (courierID == 0) {
                Response response = courierEndpointsRequests.loginCourier(new CourierCredentials(courier.getLogin(), courier.getPassword()));
                courierID = response.path("id");
                courierEndpointsRequests.deleteCourier(courierID);
            }
        } catch (NullPointerException exception) {
            System.out.println("Курьер не был удален, так как недостаточно данных для поиска его ID");
        }
    }

    @Test
    @DisplayName("Возможно авторизоваться под учетной записью курьера, используя корректные данные")
    @Description("Дополнительно проверяется наличие ID в теле ответа")
    public void shoudBePossibleForCourierToLoginWithValidData() {
        Response loginCourierResponse = courierEndpointsRequests.loginCourier(new CourierCredentials(courier.getLogin(), courier.getPassword()));
        courierID = loginCourierResponse.path("id");

        assertThat("Вернулся код ответа, отличный от 200 OK", loginCourierResponse.statusCode(), equalTo(SC_OK));
        assertThat("ID курьера отсутствует в ответе", courierID, notNullValue());
    }

    /**В спеке явно не указано, принимают ли поля логина и пароля пробелы.
     * Поэтому здесь в ассертах я следовала из логики - поля авторизации стандартно должны удалять лишние пробелы,
     * что привело бы к пустому запросу и bad request.
     * Возможно, если поменять на 404, тесты не упадут, но как бы... Пробелы - это не некорректные авторизационные данные, а их отсутствие. */


    @Test
    @DisplayName("Невозможно авторизоваться с пустыми логином и паролем")
    public void shouldBeErrorWhenLoginAndPasswordAreEmpty() {
        Response loginCourierResponse = courierEndpointsRequests.loginCourier(new CourierCredentials("", ""));
        Errors errors = loginCourierResponse.as(Errors.class);

        assertThat("Вернулся код ответа, отличный от 400 Bad request", loginCourierResponse.statusCode(), equalTo(SC_BAD_REQUEST));
        assertThat("Вернулось тело ответа, не соответствующее ожидаемому", errors.getMessage(), equalTo(MISSING_LOGIN_OR_PASSWORD));

    }

    @Test
    @DisplayName("Невозможно авторизоваться с пустым логином")
    public void shouldBeErrorWhenLoginIsEmpty() {
        Response loginCourierResponse = courierEndpointsRequests.loginCourier(new CourierCredentials("", courier.getPassword()));
        Errors errors = loginCourierResponse.as(Errors.class);

        assertThat("Вернулся код ответа, отличный от 400 Bad request", loginCourierResponse.statusCode(), equalTo(SC_BAD_REQUEST));
        assertThat("Вернулось тело ответа, не соответствующее ожидаемому", errors.getMessage(), equalTo(MISSING_LOGIN_OR_PASSWORD));
    }

    @Test
    @DisplayName("Невозможно авторизоваться с пустым паролем")
    public void shouldBeErrorWhenPasswordIsEmpty() {
        Response loginCourierResponse = courierEndpointsRequests.loginCourier(new CourierCredentials(courier.getLogin(), ""));
        Errors errors = loginCourierResponse.as(Errors.class);

        assertThat("Вернулся код ответа, отличный от 400 Bad request", loginCourierResponse.statusCode(), equalTo(SC_BAD_REQUEST));
        assertThat("Вернулось тело ответа, не соответствующее ожидаемому", errors.getMessage(), equalTo(MISSING_LOGIN_OR_PASSWORD));
    }

    @Test
    @DisplayName("Невозможно авторизоваться с пробелами на месте логина и пароля")
    public void shouldBeErrorWhenLoginAndPasswordAreSpaceFilled() {
        Response loginCourierResponse = courierEndpointsRequests.loginCourier(new CourierCredentials(" ", " "));
        Errors errors = loginCourierResponse.as(Errors.class);

        assertThat("Вернулся код ответа, отличный от 400 Bad request", loginCourierResponse.statusCode(), equalTo(SC_BAD_REQUEST));
        assertThat("Вернулось тело ответа, не соответствующее ожидаемому", errors.getMessage(), equalTo(MISSING_LOGIN_OR_PASSWORD));
    }

    @Test
    @DisplayName("Невозможно авторизоваться с пробелом вместо логина")
    public void shouldBeErrorWhenLoginIsSpaceFilled() {
        Response loginCourierResponse = courierEndpointsRequests.loginCourier(new CourierCredentials(" ", courier.getPassword()));
        Errors errors = loginCourierResponse.as(Errors.class);

        assertThat("Вернулся код ответа, отличный от 400 Bad request", loginCourierResponse.statusCode(), equalTo(SC_BAD_REQUEST));
        assertThat("Вернулось тело ответа, не соответствующее ожидаемому", errors.getMessage(), equalTo(MISSING_LOGIN_OR_PASSWORD));
    }

    @Test
    @DisplayName("Невозможно авторизоваться с пробелом вместо пароля")
    public void shouldBeErrorWhenPasswordIsSpaceFilled() {
        Response loginCourierResponse = courierEndpointsRequests.loginCourier(new CourierCredentials(courier.getLogin(), " "));
        Errors errors = loginCourierResponse.as(Errors.class);

        assertThat("Вернулся код ответа, отличный от 400 Bad request", loginCourierResponse.statusCode(), equalTo(SC_BAD_REQUEST));
        assertThat("Вернулось тело ответа, не соответствующее ожидаемому", errors.getMessage(), equalTo(MISSING_LOGIN_OR_PASSWORD));
    }

    @Test
    @DisplayName("Невозможно авторизоваться с неверными (несуществующими) логином и паролем")
    public void shouldBeErrorWhenLoginAndPasswordAreIncorrect() {
        String incorrectLogin = courier.getLogin().concat("wrong");
        String incorrectPassword = courier.getPassword().concat("wrong");
        Response loginCourierResponse = courierEndpointsRequests.loginCourier(new CourierCredentials(incorrectLogin, incorrectPassword));
        Errors errors = loginCourierResponse.as(Errors.class);

        assertThat("Вернулся код ответа, отличный от 404 Not found", loginCourierResponse.statusCode(), equalTo(SC_NOT_FOUND));
        assertThat("Вернулось тело ответа, не соответствующее ожидаемому", errors.getMessage(), equalTo(PAIR_OF_LOGIN_PASSWORD_DOESNT_EXIST));
    }

    @Test
    @DisplayName("Невозможно авторизоваться с неверным логином")
    public void shouldBeErrorWhenLoginIncorrect() {
        String incorrectLogin = courier.getLogin().concat("wrong");
        Response loginCourierResponse = courierEndpointsRequests.loginCourier(new CourierCredentials(incorrectLogin, courier.getPassword()));
        Errors errors = loginCourierResponse.as(Errors.class);

        assertThat("Вернулся код ответа, отличный от 404 Not found", loginCourierResponse.statusCode(), equalTo(SC_NOT_FOUND));
        assertThat("Вернулось тело ответа, не соответствующее ожидаемому", errors.getMessage(), equalTo(PAIR_OF_LOGIN_PASSWORD_DOESNT_EXIST));

    }

    @Test
    @DisplayName("Невозможно авторизоваться с неверным паролем")
    public void shouldBeErrorWhenPasswordIncorrect() {
        String incorrectPassword = courier.getPassword().concat("wrong");
        Response loginCourierResponse = courierEndpointsRequests.loginCourier(new CourierCredentials(courier.getLogin(), incorrectPassword));
        Errors errors = loginCourierResponse.as(Errors.class);

        assertThat("Вернулся код ответа, отличный от 404 Not found", loginCourierResponse.statusCode(), equalTo(SC_NOT_FOUND));
        assertThat("Вернулось тело ответа, не соответствующее ожидаемому", errors.getMessage(), equalTo(PAIR_OF_LOGIN_PASSWORD_DOESNT_EXIST));
    }
}
