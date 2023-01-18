package com.app.entities;

import java.util.HashSet;
import java.util.Set;

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

@Entity
@Table(name = "terrains")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JacksonXmlRootElement
public class Terrain {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = AccessMode.READ_ONLY)
    private Integer id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "surface", nullable = false, length = 45)
    private String surface;

    @JsonIgnore
    @ManyToMany(mappedBy = "terrains")
    private Set<Sport> sports = new HashSet<>();

    @JsonProperty("sportsNames")
    @Schema(accessMode = AccessMode.READ_ONLY)
    public Set<String> getSportsList() {
        Set<String> ret = new HashSet<>();
        for (Sport sport : sports) {
            ret.add(sport.getNom());
        }
        return ret;
    }

    @JsonProperty("sports")
    @Schema(accessMode = AccessMode.READ_ONLY)
    public Set<Integer> getSportsIDsList() {
        Set<Integer> ret = new HashSet<>();
        for (Sport sport : sports) {
            ret.add(sport.getId());
        }
        return ret;
    }
}