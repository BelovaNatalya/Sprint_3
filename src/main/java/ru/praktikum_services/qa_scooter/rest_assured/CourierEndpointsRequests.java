package ru.praktikum_services.qa_scooter.rest_assured;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikum_services.qa_scooter.courier.Courier;
import ru.praktikum_services.qa_scooter.courier.CourierCredentials;

import static io.restassured.RestAssured.given;

public class CourierEndpointsRequests extends BaseApiClient {

    private static final String COURIER_ENDPOINT = "/api/v1/courier";

    @Step("Создание курьера с данными {courier}")
    public Response createCourier(Courier courier) {
        return
                given()
                        .spec(getBaseSpecification())
                        .body(courier)
                        .post(COURIER_ENDPOINT);
    }

    @Step("Авторизация курьера с учетными данными {credentials}")
    public Response loginCourier(CourierCredentials credentials) {
        return
                given()
                        .spec(getBaseSpecification())
                        .body(credentials)
                        .when()
                        .post(COURIER_ENDPOINT + "/login");
    }

    @Step("Удаление курьера с ID {courierID}")
    public Response deleteCourier(int ID) {
        return
                given()
                        .spec(getBaseSpecification())
                        .delete(COURIER_ENDPOINT + ID);
    }

}
