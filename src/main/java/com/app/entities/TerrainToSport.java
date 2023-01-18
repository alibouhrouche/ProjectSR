package com.app.entities;

import java.io.Serializable;

import lombok.Data;

@Data
public class TerrainToSport implements Serializable {
    private int Sport;
    private String Terrain;
}