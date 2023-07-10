package com.example.master_project_.DataHolder;

import com.example.master_project_.Entity.GraphFeatures;
import com.example.master_project_.Entity.Weight;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class MyDataSingleton {
    @Getter @Setter private GraphFeatures graphFeatures;
    @Getter @Setter private Weight weight;

    @Autowired
    public MyDataSingleton() throws IOException {
        this.graphFeatures = GraphFeatures.getInstance();
        this.weight = Weight.getInstance();
    }
}
