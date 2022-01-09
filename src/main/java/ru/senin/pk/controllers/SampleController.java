package ru.senin.pk.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {
    @GetMapping(path = "/test/sample")
    public String get() {
        return "sample_result";
    }
}
