package com.epam.gymapp.TrainerWorkloadService.controller.advice;

import com.epam.gymapp.TrainerWorkloadService.exceptions.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    @GetMapping("/test/notfound")
    public String throwNotFound() {
        throw new EntityNotFoundException("Entity not found");
    }

    @GetMapping("/test/missing-param")
    public String throwMissingParam(@RequestParam String param) {
        return param;
    }

    @PostMapping("/test/validate")
    public String validate(@Valid @RequestBody DummyRequest request) {
        return "ok";
    }

    @GetMapping("/test/missing-path")
    public String throwMissingPath(@PathVariable Integer id) {
        return id.toString();
    }

    @GetMapping("/test/exception")
    public String throwGeneric() {
        throw new RuntimeException("Boom");
    }
}
