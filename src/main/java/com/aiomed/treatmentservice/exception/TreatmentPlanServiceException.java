package com.aiomed.treatmentservice.exception;

public class TreatmentPlanServiceException extends RuntimeException {

    private static final long serialVersionUID = -2614683157082809178L;

    public TreatmentPlanServiceException(String message) {
        super(message);
    }

    public TreatmentPlanServiceException(String message, Throwable exception) {
        super(message, exception);
    }
}
