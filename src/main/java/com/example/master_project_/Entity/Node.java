package com.example.master_project_.Entity;

import lombok.*;

@NoArgsConstructor
public class Node {
    @Getter @Setter private String osmid;
    @Getter @Setter private Double east;
    @Getter @Setter private Double north;
    @Getter @Setter private Double lat;
    @Getter @Setter private Double lon;
    @Getter @Setter private Double elevation;

    public Node(String osmid, Double east, Double north,Double lat, Double lon,Double elev) {
        this.osmid = osmid;
        this.east = east;
        this.north = north;
        this.lat = lat;
        this.lon = lon;
        this.elevation = elev;
    }
}
