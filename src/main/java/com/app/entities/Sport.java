package com.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sports")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JacksonXmlRootElement
public class Sport {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = AccessMode.READ_ONLY)
    private Integer id;

    @Column(name = "nom", nullable = false, length = 45)
    private String nom;

    @Column(name = "nombre_joueurs", nullable = false)
    private Integer nombreJoueurs;

    // @Column(name = "sports_terrain", nullable = false)
    // @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "sports_terrains", 
        joinColumns = @JoinColumn(name = "identifant_sport"), 
        inverseJoinColumns = @JoinColumn(name = "identifiant_terrain"))
    private Set<Terrain> terrains = new HashSet<>();

    @JsonProperty("terrains")
    @Schema(accessMode = AccessMode.READ_ONLY)
    public Set<String> getTerrainsList() {
        Set<String> ret = new HashSet<>();
        for (Terrain terrain : terrains) {
            ret.add(terrain.getCode());
        }
        return ret;
    }

}