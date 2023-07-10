package com.example.master_project_.Entity;

import lombok.*;
import org.locationtech.jts.geom.Coordinate;

@NoArgsConstructor
@AllArgsConstructor
public class Edge {

    @Getter @Setter boolean is_it_created;
    @Getter @Setter private String u_id;
    @Getter @Setter private String v_id;
    @Getter @Setter private Double length;
    @Getter @Setter private Double slope;
    @Getter @Setter private Double max_speed_weight;
    @Getter @Setter private Double turning_cost;
    @Getter @Setter private Coordinate start_point;
    @Getter @Setter private Coordinate end_point;
    @Getter @Setter private Node u;
    @Getter @Setter private Node v;

    public Edge(boolean is_it_created,String u_id,String v_id,Double length,Double slope,Double max_speed_weight,Double turning_cost,Node u,Node v){
        this.is_it_created = is_it_created;
        this.u_id=u_id;
        this.v_id=v_id;
        this.length=length;
        this.slope=slope;
        this.max_speed_weight=max_speed_weight;
        this.turning_cost=turning_cost;
        this.v = v;
        this.u = u;
    }



}
