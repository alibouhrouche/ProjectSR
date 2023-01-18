package com.app.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class SportData implements Serializable {
    private int id;
    private String nom;
    private int nombreJoueurs;
    private List<String> terrains = new ArrayList<>();
}