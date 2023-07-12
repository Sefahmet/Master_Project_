package com.example.master_project_.Model;

import com.example.master_project_.Entity.Edge;
import com.example.master_project_.Entity.GraphFeatures;
import com.example.master_project_.Entity.Node;
import com.example.master_project_.WeightDecider.Calculator;
import com.example.master_project_.WeightDecider.Decider;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import java.io.*;
import java.util.*;

public class GraphCreator {
    private ResourceLoader resourceLoader;

    @Autowired
    public GraphCreator(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    public static Graph<Node, Edge> readAndAddNodes2Graph(String filename, GraphFeatures graphFeatures) throws IOException {
        Graph<Node, Edge> graph = graphFeatures.getGraph();
        System.out.println("Node Features Reading");
        File file = new File(filename);
        FileDataStore fileDataStore = FileDataStoreFinder.getDataStore(file);

        SimpleFeatureCollection  features = fileDataStore.getFeatureSource().getFeatures();
        SimpleFeatureIterator nodeiterator =  features.features();
        HashMap <String,Node> nodeHashMap = new HashMap<>();
        while (nodeiterator.hasNext()){

            SimpleFeature nodeFeature = nodeiterator.next();
            String id = String.valueOf(nodeFeature.getAttribute("osmid"));
            Double elev = (Double) nodeFeature.getAttribute("elev");
            Double lat = (Double) nodeFeature.getAttribute("y");
            Double lon = (Double) nodeFeature.getAttribute("x");
            Point p = (Point) nodeFeature.getDefaultGeometry();
            Double east = p.getX();
            Double north = p.getY();
            Node node = new Node(id,east,north,lat,lon,elev);
            nodeHashMap.put(id,node);
            graph.addVertex(node);
        }
        nodeiterator.close();
        fileDataStore.dispose();
        graphFeatures.setGraph(graph);
        graphFeatures.setNodeHashMap(nodeHashMap);
        return graph;
    }
    public static Graph<Node, Edge> readAndAddOrgEdges2Graph(String filename, GraphFeatures graphFeatures) throws IOException {
        Graph<Node, Edge> graph = graphFeatures.getGraph();
        HashMap<String, Node> nodeHashMap = graphFeatures.getNodeHashMap();
        SimpleFeatureCollection features = null;
        FileDataStore fileDataStore = null;
        try {
            File file = new File(filename);
            fileDataStore = FileDataStoreFinder.getDataStore(file);
            features = fileDataStore.getFeatureSource().getFeatures();
            SimpleFeatureIterator edgeiterator = features.features();


            Double maxspeed_weight;
            Coordinate p1;
            Coordinate p2;
            while (edgeiterator.hasNext()) {

                SimpleFeature edgeFeature = edgeiterator.next();
                String highway = String.valueOf(edgeFeature.getAttribute("highway"));
                if (highway.equals("living_street")) {
                    continue;
                }
                String u_id = String.valueOf(edgeFeature.getAttribute("u"));
                String v_id = String.valueOf(edgeFeature.getAttribute("v"));
                String from = String.valueOf(edgeFeature.getAttribute("from"));
                String to = String.valueOf(edgeFeature.getAttribute("to"));
                String maxSpeed = String.valueOf(edgeFeature.getAttribute("maxspeed"));
                Double length = (Double) edgeFeature.getAttribute("length");
                Double v_elev = (Double) edgeFeature.getAttribute("v_elev");
                Double u_elev = (Double) edgeFeature.getAttribute("u_elev");
                MultiLineString line = (MultiLineString) edgeFeature.getDefaultGeometry();
                Iterator<Coordinate> s = Arrays.stream(line.getCoordinates()).iterator();
                if (u_id.substring(0, u_id.length() - 2).equals(from)) {
                    p1 = s.next();
                    p2 = s.next();

                } else {
                    p2 = s.next();
                    p1 = s.next();
                    Double subelev = v_elev;
                    v_elev = u_elev;
                    u_elev = subelev;
                }

                if (maxSpeed.isEmpty()) {
                    maxspeed_weight = Decider.maxSpeedDecider(Double.valueOf(Decider.nullMaxSpeedDecider(highway)));
                } else {
                    try {
                        maxspeed_weight = Decider.maxSpeedDecider(Double.valueOf(maxSpeed));
                    } catch (Exception e) {
                        maxspeed_weight = 0.8;
                    }
                }

                Node u = nodeHashMap.get(u_id);
                Node v = nodeHashMap.get(v_id);
                double deltaH = v_elev - u_elev;
                double slope = deltaH / length;

                Edge edge = new Edge(false, u_id, v_id, length, slope, maxspeed_weight, 0.0, p1, p2, u, v);

                graph.addEdge(u, v, edge);
                graph.setEdgeWeight(u, v, Calculator.defaultWeightCalculator(edge));
                deltaH = u_elev - v_elev;
                slope = deltaH / length;
                Edge reverseedge = new Edge(false, v_id, u_id, length,slope , maxspeed_weight, 0.0, p2, p1, v, u);
                graph.addEdge(v, u, reverseedge);
                graph.setEdgeWeight(v, u, Calculator.defaultWeightCalculator(reverseedge));
            }
            graphFeatures.setGraph(graph);
            edgeiterator.close();
            fileDataStore.dispose();
            return graph;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileDataStore != null) {
                fileDataStore.dispose();
            }

        }
        return graph;
    }
    public static Graph<Node, Edge> readAndADdCreatedEdges2Graph(String filename, GraphFeatures graphFeatures) throws FileNotFoundException {
        Graph<Node, Edge> graph = graphFeatures.getGraph();
        HashMap<String, Node> nodeHashMap = graphFeatures.getNodeHashMap();
        File csvFile = new File(filename);
        String line;
        String csvSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile)))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(csvSplitBy);
                // Process or use the data

                String u_id = data[2];
                String v_id =  data[3];
                String turnsInfo =  data[8];
                Double turning_cost = Decider.turningCostDecider(turnsInfo);
                if (u_id.equals("u")){
                    continue;
                }

                Node u = nodeHashMap.get(u_id);
                Node v = nodeHashMap.get(v_id);
                Edge edge = new Edge(true,u_id,v_id,0.0,0.0,0.0,turning_cost,u,v);
                boolean res = graph.addEdge(u, v, edge);

                graph.setEdgeWeight(u,v,Calculator.defaultWeightCalculator(edge));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return graph;

    }



    public static GraphFeatures getGraphFeatrues() throws IOException {
        GraphFeatures graphFeatures = new GraphFeatures();
        Graph<Node, Edge> graph = new DefaultDirectedWeightedGraph(DefaultWeightedEdge.class);
        graphFeatures.setGraph(graph);

        graphFeatures.setGraph(readAndAddNodes2Graph("src/main/resources/data/nodes.shp", graphFeatures));
        System.out.println("Node Feature readed");

        graphFeatures.setGraph(readAndAddOrgEdges2Graph("src/main/resources/data/renamedIdsEdges.shp", graphFeatures));
        System.out.println("Edge Feature readed");

        graphFeatures.setGraph(readAndADdCreatedEdges2Graph("src/main/resources/data/created_edges_updated.csv",graphFeatures));
        return graphFeatures;
    }
}