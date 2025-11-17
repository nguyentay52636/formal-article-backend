package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GraphiqlController {

    @GetMapping("/graphiql")
    public String graphiql() {
        return "graphiql";
    }

    @GetMapping("favicon.ico")
    @ResponseBody
    public void favicon() {
        // Intentionally empty to avoid 404 noise from browsers requesting /favicon.ico
    }
}

