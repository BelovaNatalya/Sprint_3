package ru.praktikum_services.qa_scooter.courier;

//this is POJO for response of create courier endpoint

public class EndpointCourierCreateResponse {
    private Boolean ok;

    public EndpointCourierCreateResponse(Boolean createResult) {
        this.ok = createResult;
    }

    public EndpointCourierCreateResponse() {
    }

    public Boolean getCreateResult() {
        return ok;
    }

    public void setCreateResult(Boolean createResult) {
        this.ok = createResult;
    }
}
