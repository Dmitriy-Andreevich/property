package space.meduzza.property.controller;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import space.meduzza.property.controller.input.PropertyGetAllByCoordinatesInput;
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
    public String createProperty() {
        return "pages/property-form-create";
    }

    @PostMapping("/create")
    String createProperty(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String address,
            @RequestParam Float latitude,
            @RequestParam Float longitude,
            @RequestParam int roomCount,
            @RequestParam float square,
            @RequestParam float cost,
            Principal principal
    ) {
        propertyService
                .createProperty(
                        new PropertyEntity(
                                title,
                                description,
                                address,
                                new GeometryFactory().createPoint(new Coordinate(latitude, longitude)),
                                roomCount,
                                square,
                                cost,
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
            @ModelAttribute PropertyGetAllByCoordinatesInput input,
            Principal principal,
            Model model
    ) {
        Page<PropertyEntity> properties = propertyService.getAllPropertiesByCoordinates(
                input.getLatitude(),
                input.getLongitude(),
                input.getRadius(),
                input.getPage()-1
        );
        model.addAttribute("input", input);
        model.addAttribute("properties", properties);
        model.addAttribute("user", userService.findUserByEmail(principal.getName()).orElseThrow());
        return "pages/property-search-page";
    }

    @PostMapping("/delete")
    String deleteProperty(
            @RequestParam int id
    ) {
        propertyService.deletePropertyById(id);
        return "redirect:/";
    }

}
