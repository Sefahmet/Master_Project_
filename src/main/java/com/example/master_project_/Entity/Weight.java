package com.example.master_project_.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

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

    public Weight(Double length_weight, Double slope_weight, Double max_speed_weight_weight, Double turning_cost_weight) {
        this.length_weight = length_weight;
        this.slope_weight = slope_weight;
        this.max_speed_weight_weight = max_speed_weight_weight;
        this.turning_cost_weight = turning_cost_weight;
    }

}
