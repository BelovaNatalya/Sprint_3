package ru.praktikum_services.qa_scooter.courierTests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum_services.qa_scooter.courier.*;
import ru.praktikum_services.qa_scooter.rest_assured.CourierEndpointsRequests;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateCourierTest {

    CourierEndpointsRequests courierEndpointsRequests;
    Courier courier;

    CourierCredentials courierCredentials;
    int courierID;

    public static final String LOGIN_IS_ALREADY_USED = "Этот логин уже используется";
    public static final String BAD_REQUEST = "Недостаточно данных для создания учетной записи";
    public static final String SUCCESSFUL_REQUEST = "\"ok\" : \"true\"";

    @Before
    public void setUp() {
        courierEndpointsRequests = new CourierEndpointsRequests();
        courier = RandomCourier.getRandomCourier();
    }

    @After
    public void cleanUp() {
        if (courier.getLogin() != null && courier.getPassword() !=null) {
                Response response = courierEndpointsRequests.loginCourier(new CourierCredentials(courier.getLogin(), courier.getPassword()));
                courierID = response.path("id");
                courierEndpointsRequests.deleteCourier(courierID);
            } else
                System.out.println("Курьер не был удален, так как недостаточно данных для поиска его ID");
        }
    
    @Test
    @DisplayName("Возможно создать курьера с валидными данными")
    @Description("Дополнительно проверяется тело и код ответа при успешном запросе")
    public void shouldBePossibleToCreateNewCourierWithValidData() {
        Response createCourierResponse = courierEndpointsRequests.createCourier(courier);
        EndpointCourierCreateResponse endpointCourierCreateResponse = createCourierResponse.as(EndpointCourierCreateResponse.class);

        assertThat("Вернулось тело ответа, отличное от \"ok\" : \"true\"", createCourierResponse.path("ok"), equalTo(true));
        assertThat("Вернулся ответ, отличный от 201 Created", createCourierResponse.statusCode(), equalTo(SC_CREATED));

    }

    @Test
    @DisplayName("Невозможно создать курьера, указав только логин")
    public void shouldBeImpossibleToCreateNewCourierWithLoginOnly() {
        courier.setPassword(null);
        courier.setFirstName(null);

        Response createCourierResponse = courierEndpointsRequests.createCourier(courier);
        Errors errors = createCourierResponse.as(Errors.class);

        assertThat("Вернулся ответ, отличный от 400 Bad request", createCourierResponse.statusCode(), equalTo(SC_BAD_REQUEST));
        assertThat("Вернулось сообщение, не соответствующее ожидаемому", errors.getMessage(), equalTo(BAD_REQUEST));

    }

    @Test
    @DisplayName("Невозможно создать курьера, указав только пароль")
    public void shouldBeImpossibleToCreateNewCourierWithPasswordOnly() {
        courier.setLogin(null);
        courier.setFirstName(null);

        Response createCourierResponse = courierEndpointsRequests.createCourier(courier);
        Errors errors = createCourierResponse.as(Errors.class);

        assertThat("Вернулся ответ, отличный от 400 Bad request", createCourierResponse.statusCode(), equalTo(SC_BAD_REQUEST));
        assertThat("Вернулось сообщение, не соответствующее ожидаемому", errors.getMessage(), equalTo(BAD_REQUEST));

    }

    @Test
    @DisplayName("Невозможно создать курьера, указав только имя")
    public void shouldBeImpossibleToCreateNewCourierWithNameOnly() {
        courier.setLogin(null);
        courier.setPassword(null);

        Response createCourierResponse = courierEndpointsRequests.createCourier(courier);
        Errors errors = createCourierResponse.as(Errors.class);

        assertThat("Вернулся ответ, отличный от 400 Bad request", createCourierResponse.statusCode(), equalTo(SC_BAD_REQUEST));
        assertThat("Вернулось сообщение, не соответствующее ожидаемому", errors.getMessage(), equalTo(BAD_REQUEST));

    }

    @Test
    @DisplayName("Невозможно создать курьера, не указав логин")
    public void shouldBeImpossibleToCreateNewCourierWithoutLogin() {
        courier.setLogin(null);

        Response createCourierResponse = courierEndpointsRequests.createCourier(courier);
        Errors errors = createCourierResponse.as(Errors.class);

        assertThat("Вернулся ответ, отличный от 400 Bad request", createCourierResponse.statusCode(), equalTo(SC_BAD_REQUEST));
        assertThat("Вернулось сообщение, не соответствующее ожидаемому", errors.getMessage(), equalTo(BAD_REQUEST));

    }

    @Test
    @DisplayName("Невозможно создать курьера, не указав пароль")
    public void shouldBeImpossibleToCreateNewCourierWithoutPassword() {
        courier.setPassword(null);

        Response createCourierResponse = courierEndpointsRequests.createCourier(courier);
        Errors errors = createCourierResponse.as(Errors.class);

        assertThat("Вернулся ответ, отличный от 400 Bad request", createCourierResponse.statusCode(), equalTo(SC_BAD_REQUEST));
        assertThat("Вернулось сообщение, не соответствующее ожидаемому", errors.getMessage(), equalTo(BAD_REQUEST));

    }

    @Test
    @DisplayName("Невозможно создать курьера, не указав имя")
    public void shouldBeImpossibleToCreateNewCourierWithoutName() {
        courier.setFirstName(null);

        Response createCourierResponse = courierEndpointsRequests.createCourier(courier);
        Errors errors = createCourierResponse.as(Errors.class);

        assertThat("Вернулся ответ, отличный от 400 Bad request", createCourierResponse.statusCode(), equalTo(SC_BAD_REQUEST));
        assertThat("Вернулось сообщение, не соответствующее ожидаемому", errors.getMessage(), equalTo(BAD_REQUEST));

    }

    @Test
    @DisplayName("Невозможно создать двух курьеров с идентичными данными")
    public void shouldBeRecievedErrorIfTryToCreateNewCourierWithSimilarData(){
        courierEndpointsRequests.createCourier(courier);
        Response createCourierResponse = courierEndpointsRequests.createCourier(courier);
        Errors errors = createCourierResponse.as(Errors.class);

        assertThat("Вернулся ответ, отличный от 409 Conflict", createCourierResponse.statusCode(), equalTo(SC_CONFLICT));
        assertThat("Вернулось сообщение, не соответствующее ожидаемому", errors.getMessage(), equalTo(LOGIN_IS_ALREADY_USED));
    }

    @Test
    @DisplayName("Невозможно создать двух курьеров с одинаковым логином")
    public void shouldBeRecievedErrorIfTryToCreateNewCourierWithAlreadyExistLogin() {
        courierEndpointsRequests.createCourier(courier);
        Courier secondCourier = RandomCourier.getRandomCourier();
        secondCourier.setLogin(courier.getLogin());

        Response createCourierResponse = courierEndpointsRequests.createCourier(secondCourier);
        Errors errors = createCourierResponse.as(Errors.class);

        assertThat("Вернулся ответ, отличный от 409 Conflict", createCourierResponse.statusCode(), equalTo(SC_CONFLICT));
        assertThat("Вернулось сообщение, не соответствующее ожидаемому", errors.getMessage(), equalTo(LOGIN_IS_ALREADY_USED));
    }
}
