package com.example.master_project_.Entity;

import com.example.master_project_.Model.GraphCreator;
import lombok.Getter;
import lombok.Setter;
import org.jgrapht.Graph;

import java.io.IOException;
import java.util.HashMap;

public class GraphFeatures {
    @Getter @Setter private Graph<Node, Edge> graph;
    @Getter @Setter private HashMap<String,Node> nodeHashMap;
    @Getter @Setter private static GraphFeatures instance;
    public static GraphFeatures getInstance() throws IOException {
        if (instance == null){
            instance = GraphCreator.getGraphFeatrues();
        }
        return instance;
    }

}
