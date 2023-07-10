package com.example.master_project_.WeightDecider;

import com.example.master_project_.DataHolder.MyDataSingleton;
import com.example.master_project_.Entity.Edge;
import com.example.master_project_.Entity.Node;
import com.example.master_project_.Entity.Weight;
import org.locationtech.jts.geom.Coordinate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Calculator {

    public static  double weightCalculator(Edge edge, Weight weight){

        Double w1 = weight.getLength_weight();
        Double w2 = weight.getMax_speed_weight_weight();
        Double w3 = weight.getSlope_weight();
        Double w4 = weight.getTurning_cost_weight();
        Double sumWeight = w1+w2+w3+w4;
        Double norm_w1 = w1 / sumWeight;
        Double norm_w2 = w2 / sumWeight;
        Double norm_w3 = w3 / sumWeight;
        Double norm_w4 = w4 / sumWeight;
        Double normal_edge_weight = edge.getLength() * (norm_w1 + norm_w2* edge.getMax_speed_weight()+ norm_w3* edge.getSlope());
        Double created_edge_weight = norm_w4 * edge.getTurning_cost();

        return normal_edge_weight+ created_edge_weight;
    }
    public static  double defaultWeightCalculator(Edge edge){

        Double norm_w1 = 0.25;
        Double norm_w2 = 0.25;
        Double norm_w3 = 0.25;
        Double norm_w4 = 0.25;
        Double normal_edge_weight = edge.getLength() * (norm_w1 + norm_w2* edge.getMax_speed_weight()+ norm_w3* edge.getSlope());
        Double created_edge_weight = norm_w4 * edge.getTurning_cost();

        return normal_edge_weight+ created_edge_weight;
    }
    public static List<Node> getClosestNode(Coordinate startPoint, Coordinate endPoint) throws IOException {
        Double startNodeDistance = Double.POSITIVE_INFINITY;
        Double endNodeDistance = Double.POSITIVE_INFINITY;
        Node startNode = new Node();
        Node endNode = new Node();
        MyDataSingleton data = new MyDataSingleton();
        Iterator<Edge> edgeIterator = data.getGraphFeatures().getGraph().edgeSet().iterator();

        Logger logger = LoggerFactory.getLogger(Calculator.class);

        while (edgeIterator.hasNext()) {
            Edge testEdge = edgeIterator.next();
            Node p1 = testEdge.getU();
            Node p2 = testEdge.getV();

            Double sd = distanceEdge2Point(p2.getEast(), p2.getNorth(), p1.getEast(), p1.getNorth(),
                    startPoint.getX(), startPoint.getY());
            Double ed = distanceEdge2Point(p2.getEast(), p2.getNorth(), p1.getEast(), p1.getNorth(),
                    endPoint.getX(), endPoint.getY());

            if (sd < startNodeDistance) {
                startNodeDistance = sd;
                if (Math.hypot(p1.getEast() - startPoint.getX(), p1.getNorth() - startPoint.getY()) <
                        Math.hypot(p2.getEast() - startPoint.getX(), p2.getNorth() - startPoint.getY())) {
                    startNode = p1;
                } else {
                    startNode = p2;
                }
            }

            if (ed < endNodeDistance) {
                endNodeDistance = ed;
                if (Math.hypot(p1.getEast() - endPoint.getX(), p1.getNorth() - endPoint.getY()) <
                        Math.hypot(p2.getEast() - endPoint.getX(), p2.getNorth() - endPoint.getY())) {
                    endNode = p1;
                } else {
                    endNode = p2;
                }
            }

        }
        logger.info("Closest Nodes Found: Start Node ->" + startNode.getOsmid() +" End Node -> " + endNode.getOsmid());
        List<Node> res = new ArrayList<>();
        res.add(startNode);
        res.add(endNode);
        return res;
    }

    public static Double distanceEdge2Point(Double x1, Double y1, Double x2, Double y2, double x,double y) {


        // Calculate the length between the start and end points of the line segment
        double length = Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));

        if (length == 0) {
            // If the line segment has zero length, return the distance from the point to the start point
            return Math.sqrt(Math.pow((x - x1), 2) + Math.pow((y - y1), 2));
        }

        // Calculate the slope and y-intercept of the line segment
        double m = (y2 - y1) / (x2 - x1);
        double b = y1 - m * x1;

        // Calculate the projection of the point onto the line segment
        double proj_x = (m * (y - y1) + x + Math.pow(m, 2) * x1) / (1 + Math.pow(m, 2));
        double proj_y = m * (proj_x - x1) + y1;

        if ((x1 <= proj_x && proj_x <= x2) || (x2 <= proj_x && proj_x <= x1)) {
            // If the projection point is on the line segment, return the distance from the point to the projection point
            return Math.sqrt(Math.pow((x - proj_x), 2) + Math.pow((y - proj_y), 2));
        }

        // If the projection point is outside the line segment, return the distance from the point to the start or end point
        double distance1 = Math.sqrt(Math.pow((x - x1), 2) + Math.pow((y - y1), 2));
        double distance2 = Math.sqrt(Math.pow((x - x2), 2) + Math.pow((y - y2), 2));
        return Math.min(distance1, distance2);
    }
}
