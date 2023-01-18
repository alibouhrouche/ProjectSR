package com.app.controllers;

import com.app.entities.Sport;
import com.app.entities.SportData;
import com.app.entities.Terrain;
import com.app.entities.TerrainToSport;
import com.app.services.SortService;
import com.app.services.SportServices;
import com.app.services.TerrainServices;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class SportController {
    private final SortService sService;
    private final SportServices sportServices;
    private final TerrainServices terrainServices;

    @GetMapping(value = {"admin/sport","client/sport"},produces = {"application/json", "application/xml"})
    public ResponseEntity<List<Sport>> getAllClients(
        @RequestParam(defaultValue = "1") int _page,
        @RequestParam(defaultValue = "10") int _size,
        @RequestParam(required = false) String[] _sort,
        @RequestParam(required = false) String[] _order
    ) {
        try {
            HttpHeaders responseHeaders = new HttpHeaders();
            Pageable paging = sService.getSorter(_page, _size, _sort, _order);
            if(paging == null)
                return ResponseEntity.ok(sportServices.getAllSports());
            Page<Sport> pageSports = sportServices.getAllSports(paging);
            List<Sport> sports = pageSports.getContent();
            responseHeaders.add("x-total-count",String.valueOf(pageSports.getTotalElements()));
            return ResponseEntity.ok().headers(responseHeaders).body(sports);
        }catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(value = {"admin/sport/{id}","client/sport/{id}"},produces = {"application/json", "application/xml"})
    public ResponseEntity<Sport> getSport(@PathVariable int id) {
        Optional<Sport> s = sportServices.getSport(id);
        if(s.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(s.get());
    }

    @PostMapping(value="admin/sport/save",produces = {"application/json", "application/xml"}, consumes = {"application/json", "application/xml"})
    public ResponseEntity<Sport> saveSport(@RequestBody Sport sport){
        return ResponseEntity.ok(sportServices.save(sport));
    }
    @PutMapping(value="admin/sport/update",produces = {"application/json", "application/xml"}, consumes = {"application/json", "application/xml"})
    public ResponseEntity<Sport> updateSport(@RequestBody SportData sport){
        Optional<Sport> s = sportServices.getSport(sport.getId());
        if(s.isEmpty())
            return ResponseEntity.badRequest().build();
        Sport sport2 = s.get();
        sport2.getTerrains().removeIf(t -> (!sport.getTerrains().contains(t.getCode())));
        Set<String> oldTerrains = sport2.getTerrainsList();
        for (String terrain : sport.getTerrains()) {
            if(!oldTerrains.contains(terrain)){
                Optional<Terrain> t = terrainServices.getTerrainByCode(terrain);
                if(t.isPresent()) sport2.getTerrains().add(t.get());
            }
        }
        sport2.setNom(sport.getNom());
        sport2.setNombreJoueurs(sport.getNombreJoueurs());
        return ResponseEntity.ok(sportServices.save(sport2));
    }

    @DeleteMapping(value="admin/sport/{id}",produces = {"application/json", "application/xml"})
    public ResponseEntity<Sport> updateSport(@PathVariable Integer id){
        return ResponseEntity.ok(sportServices.delete(id));
    }

    @PostMapping(value="admin/sport/addTerrainToSport",produces = {"application/json", "application/xml"},consumes = {"application/json", "application/xml"})
    public ResponseEntity<?> addTerrainToSport(@RequestBody TerrainToSport form) {
        sportServices.saveToTerrain(String.valueOf(form.getSport()),form.getTerrain());
        return ResponseEntity.ok().build();
    }
    @DeleteMapping(value="admin/sport/removeTerrainToSport/{sport_id}",produces = {"application/json", "application/xml"})
    public ResponseEntity<?> removeTerrain(@PathVariable("sport_id") Integer sportId,
                                           @RequestParam String terrainCode){
        sportServices.removeTerrain(sportId,terrainCode);
        return ResponseEntity.ok().build();
    }
}
