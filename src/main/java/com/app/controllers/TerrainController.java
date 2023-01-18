package com.app.controllers;

import com.app.entities.Sport;
import com.app.entities.Terrain;
import com.app.services.SortService;
import com.app.services.SportServices;
import com.app.services.TerrainServices;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TerrainController {
    private final SortService sService;
    private final SportServices sportServices;
    private final TerrainServices terrainServices;

    @GetMapping(value = {"admin/terrain","client/terrain"},produces = { "application/json", "application/xml" }, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<List<Terrain>> getAllClients(
        @RequestParam(defaultValue = "1") int _page,
        @RequestParam(defaultValue = "10") int _size,
        @RequestParam(required = false) String[] _sort,
        @RequestParam(required = false) String[] _order
    ) {
        try {
            HttpHeaders responseHeaders = new HttpHeaders();
            Pageable paging = sService.getSorter(_page, _size, _sort, _order);
            if(paging == null)
                return ResponseEntity.ok(terrainServices.getAllTerrains());
            Page<Terrain> pageTerrains = terrainServices.getAllTerrains(paging);
            List<Terrain> terrains = pageTerrains.getContent();
            responseHeaders.add("x-total-count",String.valueOf(pageTerrains.getTotalElements()));
            return ResponseEntity.ok().headers(responseHeaders).body(terrains);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"admin/terrain/{id}","client/terrain/{id}"},produces = { "application/json", "application/xml" }, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Terrain> getTerrain(@PathVariable int id) {
        Optional<Terrain> s = terrainServices.getTerrain(id);
        if(s.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(s.get());
    }

    @PostMapping("admin/terrain/save")
    public ResponseEntity<Terrain> saveSport(@RequestBody Terrain terrain){
        return ResponseEntity.ok(terrainServices.save(terrain));
    }
    @Data
    /**
     * InnerTerrainController
     */
    public static class TerrainData implements Serializable {
        private int id;
        private String code;
        private String surface;
        private List<Integer> sports = new ArrayList<>();
    }
    @PutMapping("admin/terrain/update")
    public ResponseEntity<Terrain> updateSport(@RequestBody TerrainData terrain){
        Optional<Terrain> t = terrainServices.getTerrain(terrain.getId());
        if(t.isEmpty())
            return ResponseEntity.badRequest().build();
        Terrain terrain2 = t.get();
        for(Sport sport : terrain2.getSports()){
            if(!terrain.getSports().contains(sport.getId()))
                sportServices.removeTerrain(sport.getId(), terrain.getCode());
        }
        Set<Integer> oldSports = terrain2.getSportsIDsList();
        for (Integer sport : terrain.getSports()) {
            if(!oldSports.contains(sport)){
                sportServices.saveToTerrain(String.valueOf(sport), terrain.getCode());
            }
        }
        terrain2.setCode(terrain.getCode());
        terrain2.setSurface(terrain.getSurface());
        return ResponseEntity.ok(terrainServices.save(terrain2));
    }
    @DeleteMapping("admin/terrain/{id}")
    public ResponseEntity<Terrain> updateSport(@PathVariable Integer id){
        return ResponseEntity.ok(terrainServices.delete(id));
    }
}
