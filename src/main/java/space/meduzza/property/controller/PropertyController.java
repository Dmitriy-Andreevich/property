package space.meduzza.property.controller;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import space.meduzza.property.config.AuthenticationFacade;
import space.meduzza.property.controller.input.CreatePropertyInput;
import space.meduzza.property.controller.input.PropertyUpdateInput;
import space.meduzza.property.controller.input.SearchAllPropertiesByCoordinatesInput;
import space.meduzza.property.model.PropertyEntity;
import space.meduzza.property.service.property.PropertyService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/property")
public class PropertyController {
    private final PropertyService propertyService;

    private final AuthenticationFacade authenticationFacade;

    public PropertyController(
            PropertyService propertyService,
            AuthenticationFacade authenticationFacade
    ) {
        this.propertyService = propertyService;
        this.authenticationFacade = authenticationFacade;
    }

    @GetMapping("/create")
    public String createProperty() {
        return "pages/property-form-create";
    }

    @PostMapping("/create")
    public String createProperty(
            @ModelAttribute
            @Valid
            final CreatePropertyInput createPropertyInput
    ) throws FileUploadException {
        final PropertyEntity property = propertyService.createProperty(createPropertyInput.toPropertyEntity(
                authenticationFacade.findCurrentUser()),
                                                                       createPropertyInput
                                                                               .getMedias()
                                                                               .stream()
                                                                               .filter(isNotEmptyInputFilePredicate())
                                                                               .map(convertMultipartFileToByteArrayFunction())
                                                                               .collect(Collectors.toList()));
        return "redirect:/property/get/" + property.getId();
    }

    private Function<MultipartFile, byte[]> convertMultipartFileToByteArrayFunction() {
        return el -> {
            try {
                return el.getBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
            throw new RuntimeException();
        };
    }

    private Predicate<MultipartFile> isNotEmptyInputFilePredicate() {
        return e -> e.getSize() > 0;
    }

    @GetMapping("/update/{id}")
    public String updateProperty(
            @PathVariable
            final long id,
            final Model model
    ) {
        final PropertyEntity property = propertyService
                .findPropertyById(id)
                .orElseThrow();
        authenticationFacade.isOwnerResourceWithException(property
                                                                  .getCreator()
                                                                  .getId());
        model.addAttribute("property", property);
        return "pages/property-form-update";
    }

    @PostMapping("/update/{id}")
    public String updateProperty(
            @PathVariable
            final long id,
            @ModelAttribute
            @Valid
            final PropertyUpdateInput propertyUpdateInput
    ) {
        final PropertyEntity property = propertyService.updateProperty(id,
                                                                       propertyUpdateInput.toPropertyEntity(
                                                                               authenticationFacade.findCurrentUser()));
        return "redirect:/property/get/" + property.getId();
    }

    @GetMapping("/get/{id}")
    public String getProperty(
            @PathVariable
            final long id,
            final Model model
    ) {
        model.addAttribute("property",
                           propertyService
                                   .findPropertyById(id)
                                   .orElseThrow());
        return "pages/property-page";
    }

    @GetMapping("/get/all")
    public String getProperty(
            @RequestParam(required = false, defaultValue = "1")
            final int page,
            final Model model
    ) {
        model.addAttribute("page", page);
        model.addAttribute("properties", propertyService.findAllProperty(page - 1));
        return "pages/property-all-page";
    }

    @GetMapping("/get/my")
    public String getMyProperty(
            @RequestParam(required = false, defaultValue = "1")
            final int page,
            final Model model
    ) {
        model.addAttribute("page", page);
        model.addAttribute("properties", propertyService.findAllUserProperty(page - 1));
        return "pages/property-my-page";
    }

    @GetMapping("/search")
    public String searchAllPropertiesByCoordinates(
            @ModelAttribute
            @Valid
            final SearchAllPropertiesByCoordinatesInput input,
            final Model model
    ) {
        Page<PropertyEntity> properties;
        if (input.getLatitude() != null && input.getLatitude() != null) {
            properties = propertyService.getAllPropertiesByCoordinates(input.getLatitude(),
                                                                       input.getLongitude(),
                                                                       input.getRadius(),
                                                                       input.getPage() - 1);
        } else {
            properties = Page.empty();
        }
        model.addAttribute("input", input);
        model.addAttribute("properties", properties);
        return "pages/property-search-page";
    }

    @GetMapping("/delete/{id}")
    public String deleteProperty(
            @PathVariable
            final long id
    ) {
        propertyService.deletePropertyById(id);
        return "redirect:/";
    }

}
