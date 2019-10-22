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
    private final AuthenticationFacade authenticationFacade;

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
    public String createProperty(@ModelAttribute @Valid CreatePropertyInput createPropertyInput) {
        final PropertyEntity property = propertyService
                .createProperty(
                        createPropertyInput.toPropertyEntity(authenticationFacade.findCurrentUser()),
                        createPropertyInput.getMedias()
                                .stream()
                                .filter(e -> e.getSize() > 0)
                                .map(el -> {
                                    try {
                                        return el.getBytes();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    throw new RuntimeException();
                                }).collect(Collectors.toList())
                );
        return "redirect:/property/get/" + property.getId();
    }

    @GetMapping("/update/{id}")
    public String updateProperty(
            @PathVariable long id,
            Model model
    ) {
        final PropertyEntity property = propertyService.findPropertyById(id).orElseThrow();
        final UserEntity user = authenticationFacade.findCurrentUser();
        if (property.getCreator().getId() != user.getId()) throw new AccessDeniedException("Error");
        model.addAttribute("property", property);
        return "pages/property-form-update";
    }

    @PostMapping("/update/{id}")
    public String updateProperty(
            @PathVariable long id,
            @ModelAttribute @Valid PropertyUpdateInput propertyUpdateInput
    ) {
        final PropertyEntity property = propertyService.updateProperty(id, propertyUpdateInput.toPropertyEntity(authenticationFacade.findCurrentUser()));
        return "redirect:/property/get/" + property.getId();
    }

    @GetMapping("/get/{id}")
    public String getProperty(
            @PathVariable long id,
            Model model
    ) {
        model.addAttribute("property", propertyService.findPropertyById(id).orElseThrow());
        return "pages/property-page";
    }

    @GetMapping("/get/all")
    public String getProperty(
            @RequestParam(required = false, defaultValue = "1") int page,
            Model model
    ) {
        model.addAttribute("page", page);
        model.addAttribute("properties", propertyService.findAllProperty(page - 1));
        return "pages/property-all-page";
    }

    @GetMapping("/get/my")
    public String getMyProperty(
            @RequestParam(required = false, defaultValue = "1") int page,
            Model model
    ) {
        model.addAttribute("page", page);
        model.addAttribute("properties", propertyService.findAllUserProperty(page - 1));
        return "pages/property-my-page";
    }

    @GetMapping("/search")
    public String searchAllPropertiesByCoordinates(
            @ModelAttribute @Valid SearchAllPropertiesByCoordinatesInput input,
            Model model
    ) {
        Page<PropertyEntity> properties = propertyService.getAllPropertiesByCoordinates(
                input.getLatitude(),
                input.getLongitude(),
                input.getRadius(),
                input.getPage() - 1
        );
        model.addAttribute("input", input);
        model.addAttribute("properties", properties);
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
