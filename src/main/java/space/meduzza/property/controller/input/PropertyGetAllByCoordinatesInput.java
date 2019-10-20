package space.meduzza.property.controller.input;

import lombok.Data;

@Data
public class PropertyGetAllByCoordinatesInput {
    float latitude;
    float longitude;
    int radius;
    int page;
}
