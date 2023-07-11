package com.example.master_project_.Service;

import com.example.master_project_.DataHolder.MyDataSingleton;
import com.example.master_project_.Entity.Edge;
import com.example.master_project_.Entity.Node;
import com.example.master_project_.Entity.Weight;
import com.example.master_project_.Model.GraphCreator;
import com.example.master_project_.Model.WeightUpdate;
import geotrellis.proj4.CRS;
import geotrellis.proj4.Transform;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.locationtech.jts.geom.Coordinate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.io.IOException;
import java.util.List;

import static com.example.master_project_.WeightDecider.Calculator.*;

@Service
public class ShortestPathService {
    Logger logger = LoggerFactory.getLogger(ShortestPathService.class);
    public GraphPath<Node, Edge> getBestPath(Coordinate start, Coordinate end, Weight weight) throws IOException {

        try{
            MyDataSingleton data = new MyDataSingleton();
            List<Node> vertex = getClosestNode(start, end);

            Node startPoint = vertex.get(0);
            Node endPoint = vertex.get(1);
            logger.info("Start point is " + startPoint.getOsmid() + " and end point is " + endPoint.getOsmid());


            data.setWeight(weight);
            data = WeightUpdate.graphUpdater(data);
            return calculateShortestPath(startPoint,endPoint,data);




        }catch (Exception e) {
            throw new IOException();
        }
    }public GraphPath<Node, Edge> getBestPath(String start_id, String end_id, Weight weight) throws IOException {

        try{


            MyDataSingleton data = new MyDataSingleton();
            Node startPoint = getNodeWithId(start_id,data);
            Node endPoint = getNodeWithId(end_id,data);

            data.setWeight(weight);
            data = WeightUpdate.graphUpdater(data);
            return calculateShortestPath(startPoint,endPoint,data);




        }catch (Exception e) {
            throw new IOException();
        }
    }
    public GraphPath<Node, Edge> calculateShortestPath(Node start, Node end, MyDataSingleton data) throws IOException {
        Logger logger = LoggerFactory.getLogger(GraphCreator.class);

        // Calculate Shortest Path
        Graph<Node, Edge> graph = data.getGraphFeatures().getGraph();
        DijkstraShortestPath<Node, Edge> shortestPathAlg = new DijkstraShortestPath<>(graph);
        logger.info("Shortest path algorithm set as DijkstraShortestPath");
        GraphPath<Node, Edge> shortestPath = shortestPathAlg.getPath(start, end);

        logger.info("Shortest path calculated from " + start.getOsmid() + " to " + end.getOsmid() + ": " + shortestPath.toString());
        return shortestPath;
    }
    public static Coordinate LatLon2EN (double lat,double lon){

        CRS epsg3044 = CRS.fromEpsgCode(3044);
        CRS wgs84 = CRS.fromEpsgCode(4326);
        var fromWgs84 = Transform.apply(wgs84, epsg3044);
        Tuple2<Object, Object> latlon2EN = fromWgs84.apply(lat  , lon);
        return new Coordinate((double) latlon2EN._1(),(double) latlon2EN._2());


    }    public static Coordinate EN2LatLon (double x,double y){


        CRS epsg3044 = CRS.fromEpsgCode(3044);
        CRS wgs84 = CRS.fromEpsgCode(4326);

        var toWgs84 = Transform.apply(epsg3044, wgs84);


        Tuple2<Object, Object> EN2LatLon = toWgs84.apply(x,y);

        return new Coordinate((double)EN2LatLon._2(),(double)EN2LatLon._1());


    }
    public static Node getNodeWithId(String id, MyDataSingleton data) {
        Graph<Node, Edge> graph = data.getGraphFeatures().getGraph();
        for (Node node : graph.vertexSet()) {
            if (node.getOsmid().equals(id+"_0")) {
                return node;
            }
        }
        return null;
    }

}
