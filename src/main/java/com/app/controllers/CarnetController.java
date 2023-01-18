package com.app.controllers;

import com.app.entities.Carnet;
import com.app.entities.CarnetId;
import com.app.services.CarnetService;
import com.app.services.SortService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin/carnet")
public class CarnetController {
    private final SortService sService;
    private final CarnetService carnetService;
    @PostMapping
    public Carnet createCarnet(@RequestBody CarnetService.CarnetCreate carnet) {
        return carnetService.createCarnet(carnet);
    }
    @PutMapping
    public Carnet updateCarnet(@RequestBody CarnetService.CarnetUpdate carnet) {
        return carnetService.updateCarnet(carnet);
    }
    @GetMapping(value={"/{idClient}-{idSport}","/{idClient}/{idSport}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Carnet> getCarnetById(@PathVariable Integer idClient, @PathVariable Integer idSport) {
        CarnetId id = new CarnetId(idClient, idSport);
        return carnetService.getCarnetById(id);
    }
    @GetMapping
    public ResponseEntity<List<Carnet>> getAllCarnets(
        @RequestParam(defaultValue = "1") int _page,
        @RequestParam(defaultValue = "10") int _size,
        @RequestParam(required = false) String[] _sort,
        @RequestParam(required = false) String[] _order
    ) {
        try {
            HttpHeaders responseHeaders = new HttpHeaders();
            Pageable paging = sService.getSorter(_page, _size, _sort, _order);
            if(paging == null)
                return ResponseEntity.ok(carnetService.getAllCarnets());
            Page<Carnet> pageCarnets = carnetService.getAllCarnets(paging);
            List<Carnet> carnets = pageCarnets.getContent();
            responseHeaders.add("x-total-count",String.valueOf(pageCarnets.getTotalElements()));
            return ResponseEntity.ok().headers(responseHeaders).body(carnets);
        }catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/{id}")
    public void deleteCarnetById(@PathVariable String id) {
        String[] s = id.split("-");
        CarnetId c = new CarnetId(Integer.parseInt(s[0]),Integer.parseInt(s[1]));
        carnetService.deleteCarnetById(c);
    }
    @DeleteMapping
    public void deleteCarnet(@RequestBody Carnet carnet) {
        carnetService.deleteCarnet(carnet);
    }
    @DeleteMapping("/all")
    public void deleteAllCarnets() {
        carnetService.deleteAllCarnets();
    }
    @PostMapping(value={"/{idClient}-{idSport}/buy","/{idClient}/{idSport}/buy"})
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
