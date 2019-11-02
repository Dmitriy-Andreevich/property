package space.meduzza.property.controller.input;

import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Data
public class SearchAllPropertiesByCoordinatesInput {
    @DecimalMin("-90")
    @DecimalMax("90")
    private BigDecimal latitude;

    @DecimalMin("-180")
    @DecimalMax("180")
    private BigDecimal longitude;

    @Min(1)
    @Max(100_000)
    private int radius = 1;

    @Min(1)
    private int page = 1;
}
