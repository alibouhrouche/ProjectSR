package com.app.controllers;

import com.app.entities.Terrain;
import com.app.services.TerrainServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TerrainController {
    private final TerrainServices terrainServices;

    @GetMapping(value = {"admin/terrain","client/terrain"},produces = { "application/json", "application/xml" }, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<List<Terrain>> getAllClients() {
        return ResponseEntity.ok(terrainServices.getAllTerrains());
    }

    @PostMapping("admin/terrain/save")
    public ResponseEntity<Terrain> saveSport(@RequestBody Terrain terrain){
        return ResponseEntity.ok(terrainServices.save(terrain));
    }
    @PutMapping("admin/terrain/update")
    public ResponseEntity<Terrain> updateSport(@RequestBody Terrain terrain){
        return ResponseEntity.ok(terrainServices.save(terrain));
    }
    @DeleteMapping("admin/terrain/delete/{id}")
    public ResponseEntity<Terrain> updateSport(@PathVariable Integer id){
        return ResponseEntity.ok(terrainServices.delete(id));
    }
}
