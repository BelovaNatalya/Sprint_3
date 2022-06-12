package ru.praktikum_services.qa_scooter.ordersTests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum_services.qa_scooter.rest_assured.OrderEndpointsRequests;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class CreateOrderParameterizedTest {
    private String color;
    int track;

    public CreateOrderParameterizedTest(String color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] ScooterColors() {
        return new Object[][]{
                {"GREY"},
                {"BLACK"},
                {"GREY, BLACK"},
                null,
        };
    }

    @Test
    @DisplayName("Проверка создания заказа с разными вариантами цветов самоката")
    @Description("Тест с параметрами для необязательного поля color")
    public void checkCreateOrderWithColorParams() {
        Response createOrderResponse = OrderEndpointsRequests.createOrderParameterizedColors(new String[]{color});
        track = createOrderResponse.then().extract().body().path("track");

        assertThat("Вернулся ответ, отличный от 201 Created", createOrderResponse.statusCode(), equalTo(SC_CREATED));
        assertThat(track, notNullValue());

    }

    @After
    public void cleanUp() {
        OrderEndpointsRequests.cancelOrder(track);
    }

}
