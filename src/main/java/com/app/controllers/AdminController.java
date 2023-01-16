package com.app.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.entities.Client;
import com.app.services.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AuthenticationService aService;
    @GetMapping("/users")
    public List<Client> getUsers() {
        return aService.getUsers();
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<Client> getUser(@PathVariable int id) {
        Optional<Client> u = aService.getUser(id);
        if(u.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(u.get());
    }
}