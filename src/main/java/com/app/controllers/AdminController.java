package com.app.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.entities.Client;
import com.app.services.AuthenticationService;
import com.app.services.ClientServices;
import com.app.services.SortService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ClientServices clientServices;
    private final AuthenticationService aService;
    private final SortService sService;
    private final PasswordEncoder passwordEncoder;
    @GetMapping(value="/users",produces = {"application/json", "application/xml"})
    public ResponseEntity<List<Client>> getUsers(
        @RequestParam(required = false) String email,
        @RequestParam(defaultValue = "1") int _page,
        @RequestParam(defaultValue = "10") int _size,
        @RequestParam(required = false) String[] _sort,
        @RequestParam(required = false) String[] _order
    ) {
        try {
            HttpHeaders responseHeaders = new HttpHeaders();
            Pageable paging = sService.getSorter(_page, _size, _sort, _order);
            if(paging == null)
                return ResponseEntity.ok(aService.getUsers());

            Page<Client> pageClients;
            if(email == null)
                pageClients = aService.getUsers(paging);
            else
                pageClients = aService.getUsersByEmail(email, paging);
            List<Client> clients = pageClients.getContent();
            responseHeaders.add("x-total-count",String.valueOf(pageClients.getTotalElements()));
            return ResponseEntity.ok().headers(responseHeaders).body(clients);
        }catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(value="/users/{id}",produces = {"application/json", "application/xml"})
    public ResponseEntity<Client> getUser(@PathVariable int id) {
        Optional<Client> u = aService.getUser(id);
        if(u.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(u.get());
    }
    @PutMapping(value="/users",produces = {"application/json", "application/xml"}, consumes =  {"application/json", "application/xml"})
    public ResponseEntity<Client> updateClient(@RequestBody Client client) {
        Optional<Client> u = aService.getUser(client.getId());
        if(u.isEmpty())
            return ResponseEntity.badRequest().build();
        Client c = u.get();
        if(client.getPassword() == null || client.getPassword().length() == 0) {
            client.setPassword(c.getPassword());
        } else {
            client.setPassword(passwordEncoder.encode(client.getPassword()));
        }
        return ResponseEntity.ok(clientServices.saveClient(client));
    }
    @DeleteMapping(value="/users/{id}",produces = {"application/json", "application/xml"})
    public ResponseEntity<Client> deleteClient(@PathVariable Integer id){
        return ResponseEntity.ok(clientServices.delete(id));
    }
}
