package ru.praktikum_services.qa_scooter.courier;
import org.apache.commons.lang3.RandomStringUtils;

public class RandomCourier {

    private String login;
    private String password;
    private String firstName;

    public static Courier getRandomCourier() {
        String login = RandomStringUtils.randomAlphabetic(2,12);
        String password = RandomStringUtils.randomAlphabetic(10);
        String firstName = RandomStringUtils.randomAlphabetic(2,12);
        return new Courier(login, password, firstName);
    }
}
