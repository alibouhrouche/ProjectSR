package com.app.entities;

import java.io.Serializable;

import lombok.Data;

@Data
public class CarnetCreate implements Serializable{
    private int client;
    private int sport;
    private int nombreEntrees;
}