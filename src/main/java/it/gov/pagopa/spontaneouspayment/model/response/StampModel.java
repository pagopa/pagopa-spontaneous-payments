package it.gov.pagopa.spontaneouspayment.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Model class that holds MDB stamp data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StampModel {

    @NotBlank(message = "hashDocument is required")
    @Size(max = 72)
    @Schema(
            description =
                    "Document hash type is stBase64Binary72 as described in"
                            + " https://github.com/pagopa/pagopa-api.",
            required = true)
    // Stamp generally get as input a base64sha256, that is the SHA256 hash of a given string encoded
    // with Base64.
    // It is not equivalent to base64encode(sha256(“test”)), if sha256() returns a hexadecimal
    // representation.
    // The result should normally be 44 characters, to be compliant with as-is it was extended to 72
    private String hashDocument;

    @NotBlank(message = "stampType is required")
    @Size(min = 2, max = 2)
    @Schema(
            description = "The type of the stamp",
            minLength = 2,
            maxLength = 2,
            required = true)
    private String stampType;

    @NotBlank(message = "provincialResidence is required")
    @Pattern(regexp = "[A-Z]{2}")
    @Schema(
            description = "The province of residence",
            example = "RM",
            pattern = "[A-Z]{2,2}",
            required = true)
    private String provincialResidence;
}