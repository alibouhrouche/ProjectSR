package com.app.controllers;

import com.app.entities.Client;
import com.app.services.AuthenticationService;
import com.app.entities.auth.AuthenticationRequest;
import com.app.entities.auth.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("/register/{isClient}")
  public ResponseEntity<AuthenticationResponse> register(
      @RequestBody Client client,
      @PathVariable boolean isClient
  ) {
    return ResponseEntity.ok(service.register(client,isClient));
  }
  @PostMapping(value = {"/authenticate"},produces = {"application/json", MediaType.ALL_VALUE}, consumes = MediaType.ALL_VALUE)
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }
  @GetMapping("/currentUser")
  public ResponseEntity<Client> current(Authentication authentication) {
      return ResponseEntity.ok((Client)authentication.getPrincipal());
  }
}
