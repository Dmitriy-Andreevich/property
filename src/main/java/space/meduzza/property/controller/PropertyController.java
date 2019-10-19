package space.meduzza.property.controller;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import space.meduzza.property.model.PropertyEntity;
import space.meduzza.property.service.property.PropertyService;
import space.meduzza.property.service.user.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/property")
public class PropertyController {
    private final PropertyService propertyService;
    private final UserService userService;

    @Autowired
    public PropertyController(PropertyService propertyService, UserService userService) {
        this.propertyService = propertyService;
        this.userService = userService;
    }


    @GetMapping("/create")
    String createProperty(Principal principal) {
        System.out.println(principal);
        return "pages/property-form";
    }

    @PostMapping("/create")
    String createProperty(
            @RequestParam String description,
            @RequestParam String address,
            @RequestParam Float latitude,
            @RequestParam Float longitude,
            @RequestParam int roomCount,
            @RequestParam float square,
            Principal principal
    ) {
        propertyService
                .createProperty(
                        new PropertyEntity(
                                description,
                                address,
                                new GeometryFactory().createPoint(new Coordinate(latitude, longitude)),
                                roomCount,
                                square,
                                userService.findUserByEmail(principal.getName()).orElseThrow(),
                                List.of()
                        )
                );
        return "redirect:/";
    }

    @GetMapping("/get")
    String getProperty(
            @RequestParam int id
    ) {
        propertyService.getPropertyById(id);
        return "pages/property-page";
    }

    @GetMapping("/get/all/coordinates")
    String getAllPropertiesByCoordinates(
            @RequestParam float latitude,
            @RequestParam float longitude,
            @RequestParam int radius,
            @RequestParam int page
    ) {
        List<PropertyEntity> properties = propertyService.getAllPropertiesByCoordinates(latitude, longitude, radius, page);
        return "pages/property-list-page";
    }

    @PostMapping("/delete")
    String deleteProperty(
            @RequestParam int id
    ) {
        propertyService.deletePropertyById(id);
        return "redirect:/";
    }

}
