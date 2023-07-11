package com.example.master_project_.Model;

import com.example.master_project_.DataHolder.MyDataSingleton;
import com.example.master_project_.Entity.Edge;
import com.example.master_project_.Entity.Node;
import com.example.master_project_.WeightDecider.Calculator;
import org.jgrapht.Graph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;

public class WeightUpdate {
    public static MyDataSingleton graphUpdater(MyDataSingleton data) throws IOException {
        Logger logger = LoggerFactory.getLogger(WeightUpdate.class);
        Graph<Node, Edge> graph = data.getGraphFeatures().getGraph();
        Iterator<Edge> iterator = graph.edgeSet().iterator();
        ;
        while(iterator.hasNext()){
            Edge edge = iterator.next();
            double weight = graph.getEdgeWeight(edge);
            double weightnew = Calculator.weightCalculator(edge,data.getWeight());
            graph.setEdgeWeight(edge.getU(),edge.getV(),weightnew);

        }
        logger.info("Graph Weight is updated");
        data.getGraphFeatures().setGraph(graph);
        return data;
    }
}
