package com.app.entities;

import java.io.Serializable;

import lombok.Data;

@Data
public class CarnetUpdate implements Serializable {
    private String id;
    private int nombreEntrees;
    public int getClient() {
        return Integer.valueOf(id.split("-")[0]);
    }
    public int getSport() {
        return Integer.valueOf(id.split("-")[1]);
    }
}