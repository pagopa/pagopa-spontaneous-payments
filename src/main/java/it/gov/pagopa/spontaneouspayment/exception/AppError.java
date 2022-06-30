package it.gov.pagopa.spontaneouspayment.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum AppError {

    ORGANIZATION_NOT_FOUND(HttpStatus.NOT_FOUND, "Organization not found", "Not found the Organization Fiscal Code %s"),
    ORGANIZATION_SERVICE_NOT_FOUND(HttpStatus.NOT_FOUND, "Not found a relation between organization and service", "Not found a relation between Organization Fiscal Code %s and Service Id %s"),
    SERVICE_NOT_FOUND(HttpStatus.NOT_FOUND, "The service configuration was not found", "Not found a service configuration for Service Id %s"),
    ENROLLMENT_TO_SERVICE_NOT_FOUND(HttpStatus.NOT_FOUND, "Not found the enrollment for the specified id", "Not found an enrollment to the Service Id %s for the Organization Fiscal Code %s"),
    PROPERTY_MISSING(HttpStatus.BAD_REQUEST, "A property configured for the service is not present in the request", "Not found in the request the configured property %s"),
    ENTITY_DUPLICATED(HttpStatus.CONFLICT, "Entity with the specified id already exists in the system", "%s"),
    ENROLLMENT_TO_SERVICE_DUPLICATED(HttpStatus.CONFLICT, "An enrollment for the specified id already exists", "Already exists an enrollment between Organization Fiscal Code %s and Service Id %s"),
    ENTITY_VALIDATION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "Error during entity validation", "%s"),
    UNKNOWN(null, null, null);


    public final HttpStatus httpStatus;
    public final String title;
    public final String details;


    AppError(HttpStatus httpStatus, String title, String details) {
        this.httpStatus = httpStatus;
        this.title = title;
        this.details = details;
    }
}


