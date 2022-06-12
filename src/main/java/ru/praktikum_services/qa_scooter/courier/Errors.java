package ru.praktikum_services.qa_scooter.courier;

public class Errors {
    int responseCode;
    String message;

    public Errors(int responseCode, String message) {
        this.responseCode = responseCode;
        this.message = message;
    }

    public Errors() {
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
