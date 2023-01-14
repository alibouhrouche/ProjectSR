package com.app.controllers;

import com.app.entities.Client;
import com.app.services.AuthenticationService;
import com.app.services.auth.AuthenticationRequest;
import com.app.services.auth.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }


}
