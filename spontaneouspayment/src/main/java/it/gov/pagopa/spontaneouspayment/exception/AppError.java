package it.gov.pagopa.spontaneouspayment.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum AppError {

    ORGANIZATION_NOT_FOUND(HttpStatus.NOT_FOUND, "Not found a relation between organization and service", "Not found a relation between Organization Fiscal Code %s and Service Id %s"),
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


