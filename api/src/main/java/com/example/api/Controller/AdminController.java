package com.example.api.Controller;

import com.example.api.Request.ExternalRequest;
import com.example.api.Service.ExternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ExternalService externalService;

    @Autowired
    public AdminController(ExternalService externalService) {
        this.externalService = externalService;
    }

    @PostMapping("/createExternal")
    public ResponseEntity<Object> createExternal(@RequestBody ExternalRequest externalRequest) {
        return externalService.registration(externalRequest, true);
    }
}


