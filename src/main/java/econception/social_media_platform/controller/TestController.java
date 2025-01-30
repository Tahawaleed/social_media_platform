package econception.social_media_platform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Test Controller", description = "Test Swagger Configuration")
@RequestMapping("/test")
public class TestController {

    @Operation(summary = "Test Endpoint", description = "This is a simple test API.")
    @GetMapping
    public String test() {
        return "Api Server is working!";
    }
}
