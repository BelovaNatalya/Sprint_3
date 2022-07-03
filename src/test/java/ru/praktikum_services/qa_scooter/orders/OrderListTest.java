package ru.praktikum_services.qa_scooter.orders;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum_services.qa_scooter.restassured.OrderEndpointsRequests;
import java.util.List;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class OrderListTest {
    OrderEndpointsRequests orderEndpointsRequests;

    @Before
    public void setUp(){
        orderEndpointsRequests = new OrderEndpointsRequests();
    }

    @Test
    @DisplayName("Должен отображаться список заказов при отправке get-запроса на /api/v1/orders")
    public void shouldBeRecievedOrderListInResponse() {
        Response getOrderListResponse = orderEndpointsRequests.getOrdersList();
        List<Object> ordersList = getOrderListResponse.body().jsonPath().getList("orders");

        assertThat("Вернулся ответ, отличный от 200 Ok", getOrderListResponse.statusCode(), equalTo(SC_OK));
        assertThat("Тело ответа не содержит принятых заказов", String.valueOf(ordersList).contains("id"));
    }
}
