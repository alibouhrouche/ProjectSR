package com.app.controllers;

import com.app.entities.Client;
import com.app.securityConfig.JwtService;
import com.app.services.ClientServices;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class ClientController {
    private final PasswordEncoder passwordEncoder;
    private final ClientServices clientServices;
    private final JwtService jwtService;
    @GetMapping(produces = {"application/json", MediaType.ALL_VALUE}, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Client> getClient(@RequestHeader("Authorization") String authorization) {
        String email = jwtService.extractUsername(authorization.split(" ")[1]);
        return ResponseEntity.ok(clientServices.getClient(email).get());
    }
    @PostMapping(value="save",produces = {"application/json", "application/xml"}, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Client> saveClient(@RequestHeader("Authorization") String authorization,@RequestBody Client requestClient) {
        String email = jwtService.extractUsername(authorization.split(" ")[1]);

        Client client = clientServices.getClient(email).get();
        client.setPrenom(requestClient.getPrenom());
        client.setNom(requestClient.getNom());
        client.setRue(requestClient.getRue());
        client.setVille(requestClient.getVille());
        client.setCodePostal(requestClient.getCodePostal());

        if(!client.getEmail().equals(requestClient.getEmail())){
            Optional<Client> client1 = clientServices.getClient(requestClient.getEmail());
            if(client1.isPresent()){
                throw new IllegalStateException("This Email is taken!");
            }
            client.setEmail(requestClient.getEmail());
        }
        if(!requestClient.getMotDePasse().equals("") && !requestClient.getMotDePasse().isEmpty()){
            client.setMotDePasse(passwordEncoder.encode(requestClient.getMotDePasse()));
        }
        return ResponseEntity.ok(clientServices.saveClient(client));
    }
    @PutMapping(value="update",produces = {"application/json", "text/json"}, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Client> updateClient(@RequestBody Client client) {
        return ResponseEntity.ok(clientServices.saveClient(client));
    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Client> deleteClient(@PathVariable Integer id){
        return ResponseEntity.ok(clientServices.delete(id));
    }
}
