package org.practice.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RouteController {
    @GetMapping("/")
    public String responseData() {
        return "Success";
    }
}
