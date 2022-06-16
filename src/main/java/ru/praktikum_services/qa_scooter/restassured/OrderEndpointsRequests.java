package ru.praktikum_services.qa_scooter.restassured;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikum_services.qa_scooter.orders.Order;

import static io.restassured.RestAssured.given;

public class OrderEndpointsRequests extends BaseApiClient {
    private static final String ORDER_ENDPOINT = "/api/v1/orders";

    @Step("Создание заказа с параметризованными цветами самоката")
    public static Response createOrderParameterizedColors(String[] color) {
        Order order = new Order("Гэндальф", "Серый", "Средиземье", "7", "88005553535", 10, "2022-10-10", "comment", color);
    return given()
            .spec(getBaseSpecification())
            .body(order)
            .post(ORDER_ENDPOINT);


    }

    @Step("Получение списка созданных заказов")
    public Response getOrdersList() {
        return
                given()
                        .spec(getBaseSpecification())
                        .get(ORDER_ENDPOINT);
    }

    @Step("Отменить заказ")
    public static void cancelOrder(int track) {
        given()
                .spec(getBaseSpecification())
                .delete(ORDER_ENDPOINT + "/cancel/" + track);
    }
}
