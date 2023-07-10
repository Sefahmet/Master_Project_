package com.example.master_project_.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@AllArgsConstructor
public class Weight {


    @Getter @Setter private Double length_weight;
    @Getter @Setter private Double slope_weight;
    @Getter @Setter private Double max_speed_weight_weight;
    @Getter @Setter private Double turning_cost_weight;
    @Getter @Setter private static Weight instance;
    public static Weight getInstance() throws IOException {
        if (instance == null){
            instance = new Weight(0.25,0.25,0.25,0.25);
        }
        return instance;
    }

}
