package com.app.controllers;

import com.app.entities.Client;
import com.app.services.ClientServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/client/profile")
public class ClientController {
    private final PasswordEncoder passwordEncoder;
    private final ClientServices clientServices;
    @PutMapping(produces = {"application/json", "application/xml"}, consumes = {"application/json", "application/xml"})
    public ResponseEntity<Client> updateClient(Authentication authentication,@RequestBody Client client) {
        Client c = (Client)authentication.getPrincipal();
        if(client.getPassword() != null && !client.getPassword().isEmpty()) {
            c.setPassword(passwordEncoder.encode(client.getPassword()));
        }
        c.setPrenom(client.getPrenom());
        c.setNom(client.getNom());
        c.setEmail(client.getEmail());
        c.setRue(client.getRue());
        c.setVille(client.getVille());
        c.setCodePostal(client.getCodePostal());
        return ResponseEntity.ok(clientServices.saveClient(c));
    }
}
