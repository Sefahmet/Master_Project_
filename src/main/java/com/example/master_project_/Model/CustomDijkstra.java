package com.example.master_project_.Model;
import com.example.master_project_.DataHolder.MyDataSingleton;
import  com.example.master_project_.Entity.Edge;
import  com.example.master_project_.Entity.Node;
import com.example.master_project_.Entity.Weight;
import com.example.master_project_.Service.ShortestPathService;
import com.example.master_project_.WeightDecider.Calculator;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.locationtech.jts.geom.Coordinate;

import javax.xml.transform.Source;
import java.io.IOException;
import java.util.*;

public class CustomDijkstra {
    public static void UnUsefulAlgo() throws IOException {


        double lat1 = 50.1493320323922;
        double lon1 = 7.04519992019021;
        double lat2 = 50.7413320323922;
        double lon2 = 7.157052374740448;
        List<Node> s_e = Calculator.getClosestNode(new Coordinate(lat1, lon1), new Coordinate(lat2, lon2));
        Node source = s_e.get(0);
        Node target = s_e.get(1);
        System.out.println(source.getOsmid() + " to " + target.getOsmid());
        Weight weight = new Weight(0.25, 0.25, 0.25, 0.25);
        MyDataSingleton data = new MyDataSingleton();
        Graph<Node, Edge> graph = data.getGraphFeatures().getGraph();
        Iterator<Node> it = graph.vertexSet().iterator();

        // Özel Dijkstra algoritmasını kullanarak en kısa yolu hesaplama
        Map<Node, Double> distances = new HashMap<>();
        distances.put(source, 0.0);
        for (Node node : graph.vertexSet()) {
            if (!node.equals(source)) {
                distances.put(node, Double.POSITIVE_INFINITY);
            }
        }
        Map<Node, Node> previousNodes = new HashMap<>();
        Set<Node> visitedNodes = new HashSet<>();
        Map<Node, Edge> previousEdges = new HashMap<>();

        distances.put(source, 0.0);
        previousNodes.put(source, null);
        double newDistance = 100.0;
        Node currentNode = source;
        Edge previousEdge = null;
        while (currentNode != null) {
            visitedNodes.add(currentNode);
            List<Edge> outgoingEdges = new ArrayList(graph.outgoingEdgesOf(currentNode));
            previousEdge = previousEdges.get(currentNode);

            for (Edge edge : outgoingEdges) {
                Node neighbor = Graphs.getOppositeVertex(graph, edge, currentNode);


                if (!visitedNodes.contains(neighbor)) {
                    if (previousEdge != null && previousEdge.is_it_created() && edge.is_it_created()) {

                        if (!distances.containsKey(neighbor) ) {
                            distances.put(neighbor, Double.POSITIVE_INFINITY);
                            previousEdges.put(neighbor, edge);
                            previousNodes.put(neighbor, currentNode);
                        }else{
                            // Önceki kenarı güncelle
                            previousNodes.put(neighbor, currentNode);
                        }

                    } else {
                        double edgeWeight = Calculator.weightCalculator(edge, weight);
                        newDistance = distances.get(currentNode) + edgeWeight;

                        if (!distances.containsKey(neighbor) || newDistance < distances.get(neighbor)) {
                            distances.put(neighbor, newDistance);
                            previousNodes.put(neighbor, currentNode);
                            previousEdges.put(neighbor, edge);
                        }else {
                            // Önceki kenarı güncelle
                            previousNodes.put(neighbor, currentNode);
                        }
                    }


                }
            }

            currentNode = null;
            double minDistance = Double.MAX_VALUE;
            for (Node node : distances.keySet()) {
                if (!visitedNodes.contains(node) && distances.get(node) < minDistance) {
                    currentNode = node;
                    minDistance = distances.get(node);
                }
            }
        }
        it = visitedNodes.iterator();
        while (it.hasNext()) {
            Node node = it.next();
            System.out.println(node.getOsmid() + " " + distances.get(node));
        }


        // En kısa yolun oluşturulması
        List<Node> shortestPath = new ArrayList<>();
        currentNode = target;

        while (currentNode != null) {
            shortestPath.add(0, currentNode);
            currentNode = previousNodes.get(currentNode);
        }

        if (shortestPath.size() > 1) {
            System.out.println("En kısa yol: " + shortestPath);
        } else {
            System.out.println("Hedefe ulaşılamadı.");
        }
    }


        }
