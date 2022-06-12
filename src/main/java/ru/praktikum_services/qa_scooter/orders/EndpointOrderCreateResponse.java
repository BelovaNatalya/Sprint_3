package ru.praktikum_services.qa_scooter.orders;

//this is POJO for order create endpoint

public class EndpointOrderCreateResponse {
    private String track;

    public EndpointOrderCreateResponse(String track) {
        this.track = track;
    }

    public EndpointOrderCreateResponse() {
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }
}
