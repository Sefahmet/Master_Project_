package com.example.master_project_.Controller;

import com.example.master_project_.Service.ShortestPathService;
import com.example.master_project_.Entity.Edge;
import com.example.master_project_.Entity.Node;
import com.example.master_project_.Entity.Weight;
import com.example.master_project_.Service.ShortestPathService;
import com.example.master_project_.WeightDecider.Calculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jgrapht.GraphPath;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/shortestPath")
public class WeightedShortestPathController {
    private final ShortestPathService shortestPathService;
    @GetMapping("/getWithcoordinates")
    public ResponseEntity<List<Coordinate>> shortestPathGetter(@Valid @RequestParam double lat1,
                                             @Valid @RequestParam double lon1,
                                             @Valid @RequestParam double lat2,
                                             @Valid @RequestParam double lon2,
                                             @Valid @RequestParam double w1,
                                             @Valid @RequestParam double w2,
                                             @Valid @RequestParam double w3,
                                             @Valid @RequestParam double w4) throws IOException {
        Weight weight = new Weight(w1,w2,w3,w4);
        Coordinate p1 = shortestPathService.LatLon2EN(lon1, lat1);
        Coordinate p2 = shortestPathService.LatLon2EN(lon2, lat2);

        GraphPath<Node, Edge> shortestpath = shortestPathService.getBestPath(p1, p2, weight);
        List<Coordinate> coordinateList = new ArrayList<>();
        List<Edge> edgeList = shortestpath.getEdgeList();
        if (edgeList.size()>0) {
            for (Edge e : edgeList) {
                if(!e.is_it_created()) {
                    Coordinate p = shortestPathService.EN2LatLon(e.getStart_point().getX(), e.getStart_point().getY());
                    coordinateList.add(p);
                    p = shortestPathService.EN2LatLon(e.getStart_point().getX(), e.getEnd_point().getY());
                    coordinateList.add(p);
                }
            }

            return new ResponseEntity(coordinateList, HttpStatus.OK);
        }else{
            return new ResponseEntity(null,HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/getOsmids")
    public ResponseEntity<List<Node[]>> shortestPathOsmidsGetter(@Valid @RequestParam double lat1,
                                                               @Valid @RequestParam double lon1,
                                                               @Valid @RequestParam double lat2,
                                                               @Valid @RequestParam double lon2,
                                                               @Valid @RequestParam double w1,
                                                               @Valid @RequestParam double w2,
                                                               @Valid @RequestParam double w3,
                                                               @Valid @RequestParam double w4) throws IOException {
        Weight weight = new Weight(w1,w2,w3,w4);
        Coordinate p1 = new Coordinate(lat1,lon1);
        Coordinate p2 = new Coordinate(lat2,lon2);

        GraphPath<Node, Edge> shortestpath = shortestPathService.getBestPath(p1, p2, weight);
        List<Coordinate> coordinateList = new ArrayList<>();
        List<Edge> edgeList = shortestpath.getEdgeList();
        List<Node[]> osmids = new ArrayList<>();
        if (edgeList.size()>0) {
            for (Edge e : edgeList) {
                if(!e.is_it_created()) {
                    Node [] nodes = {e.getU(),e.getV()};
                    osmids.add(nodes);
                }
            }

            return new ResponseEntity(osmids, HttpStatus.OK);
        }else{
            return new ResponseEntity(null,HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/forbenwa")
    public ResponseEntity<List<Node>> shortestPathOsmidsGetter1(@Valid @RequestParam double lat1,
                                                                 @Valid @RequestParam double lon1,
                                                                 @Valid @RequestParam double lat2,
                                                                 @Valid @RequestParam double lon2,
                                                                 @Valid @RequestParam double w1,
                                                                 @Valid @RequestParam double w2,
                                                                 @Valid @RequestParam double w3,
                                                                 @Valid @RequestParam double w4) throws IOException {
        Weight weight = new Weight(w1,w2,w3,w4);
        Coordinate p1 = new Coordinate(lat1,lon1);
        Coordinate p2 = new Coordinate(lat2,lon2);

        GraphPath<Node, Edge> shortestpath = shortestPathService.getBestPath(p1, p2, weight);
        List<Coordinate> coordinateList = new ArrayList<>();
        List<Edge> edgeList = shortestpath.getEdgeList();
        List<Node> osmids = new ArrayList<>();
        if (edgeList.size()>0) {
            for (Edge e : edgeList) {
                if(!e.is_it_created()) {
                    osmids.add(e.getU());
                }
            }
            Edge e = edgeList.get(edgeList.size()-1);
            if(!e.is_it_created()) {
                osmids.add(e.getV());
            }

            return new ResponseEntity(osmids, HttpStatus.OK);
        }else{
            return new ResponseEntity(null,HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/getWeight")
    public ResponseEntity<Double> getWeight(@Valid @RequestParam double length,
                                            @Valid @RequestParam double maxspeed,
                                            @Valid @RequestParam double slope,
                                            @Valid @RequestParam double distance_weight,
                                            @Valid @RequestParam double slope_weight,
                                            @Valid @RequestParam double max_speed_weight,
                                            @Valid @RequestParam double turn_weight) throws IOException {
        Weight weight = new Weight(distance_weight,slope_weight,max_speed_weight,turn_weight);
        double w = Calculator.weightCalculator(length, maxspeed, slope/100, weight);
        return new ResponseEntity(w,HttpStatus.OK);

    }
    @GetMapping("/getWithOsmids")
    public ResponseEntity<List<Coordinate>> shortestPathGetter(@Valid @RequestParam String osmid1,
                                                               @Valid @RequestParam String osmid2,
                                                               @Valid @RequestParam double w1,
                                                               @Valid @RequestParam double w2,
                                                               @Valid @RequestParam double w3,
                                                               @Valid @RequestParam double w4) throws IOException {
        Weight weight = new Weight(w1,w2,w3,w4);



        GraphPath<Node, Edge> shortestpath = shortestPathService.getBestPath(osmid1, osmid2, weight);
        List<Coordinate> coordinateList = new ArrayList<>();
        List<Edge> edgeList = shortestpath.getEdgeList();
        if (edgeList.size()>0) {
            for (Edge e : edgeList) {
                if(!e.is_it_created()) {
                    Coordinate p = shortestPathService.EN2LatLon(e.getStart_point().getX(), e.getStart_point().getY());
                    coordinateList.add(p);
                }
            }
            Edge e = edgeList.get(edgeList.size() - 1);
            if (!e.is_it_created()){
                Coordinate p = shortestPathService.EN2LatLon(e.getEnd_point().getX(),e.getEnd_point().getY());
                coordinateList.add(p);
            }


            return new ResponseEntity(coordinateList, HttpStatus.OK);
        }else{
            return new ResponseEntity(null,HttpStatus.BAD_REQUEST);
        }
    }

}
