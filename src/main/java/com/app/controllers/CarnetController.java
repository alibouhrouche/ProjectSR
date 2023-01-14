package com.app.controllers;

import com.app.entities.Carnet;
import com.app.entities.CarnetId;
import com.app.services.CarnetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin/carnet")
public class CarnetController {

    private final CarnetService carnetService;
    @PostMapping
    public Carnet createCarnet(@RequestBody Map<String,String> carnet) {
        return carnetService.createCarnet(carnet);
    }
    @PutMapping
    public Carnet updateCarnet(@RequestBody Map<String,String> carnet) {
        return carnetService.updateCarnet(carnet);
    }
    @GetMapping(value="/{idClient}/{idSport}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Carnet> getCarnetById(@PathVariable Integer idClient, @PathVariable Integer idSport) {
        CarnetId id = new CarnetId(idClient, idSport);
        return carnetService.getCarnetById(id);
    }
    @GetMapping(value = "/all")
    public ResponseEntity<List<Carnet>> getAllCarnets() {
        return ResponseEntity.ok(carnetService.getAllCarnets());
    }
    @DeleteMapping("/{id}")
    public void deleteCarnetById(@PathVariable CarnetId id) {
        carnetService.deleteCarnetById(id);
    }
    @DeleteMapping
    public void deleteCarnet(@RequestBody Carnet carnet) {
        carnetService.deleteCarnet(carnet);
    }
    @DeleteMapping("/all")
    public void deleteAllCarnets() {
        carnetService.deleteAllCarnets();
    }
    @PostMapping(value="/{idClient}/{idSport}/buy")
    public ResponseEntity<Carnet> buyTicket(@PathVariable Integer idClient, @PathVariable Integer idSport, @RequestBody Integer nombres) {
        Carnet carnet = carnetService.buyTicket(idClient, idSport, nombres);
        if(carnet != null) {
            return ResponseEntity.ok(carnet);
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }
}
