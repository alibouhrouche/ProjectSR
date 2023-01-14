package com.app.controllers;

import com.app.entities.Sport;
import com.app.services.SportServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class SportController {
    private final SportServices sportServices;

    @GetMapping(value = {"admin/sport","client/sport"},produces = { "application/json", MediaType.ALL_VALUE }, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<List<Sport>> getAllClients() {
        return ResponseEntity.ok(sportServices.getAllSports());
    }

    @PostMapping("admin/sport/save")
    public ResponseEntity<Sport> saveSport(@RequestBody Sport sport){
        return ResponseEntity.ok(sportServices.save(sport));
    }
    @PutMapping("admin/sport/update")
    public ResponseEntity<Sport> updateSport(@RequestBody Sport sport){
        return ResponseEntity.ok(sportServices.save(sport));
    }

    @DeleteMapping("admin/sport/delete/{id}")
    public ResponseEntity<Sport> updateSport(@PathVariable Integer id){
        return ResponseEntity.ok(sportServices.delete(id));
    }
    @PostMapping("admin/sport/addTerrainToSport")
    public ResponseEntity<?> addTerrainToSport(@RequestBody Map<String,String> form) {
        sportServices.saveToTerrain(form.get("sport_id"),form.get("terrain_code"));
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("admin/sport/removeTerrainToSport/{sport_id}")
    public ResponseEntity<?> removeTerrain(@PathVariable("sport_id") Integer sportId,
                                           @RequestParam String terrainCode){
        sportServices.removeTerrain(sportId,terrainCode);
        return ResponseEntity.ok().build();
    }
}
