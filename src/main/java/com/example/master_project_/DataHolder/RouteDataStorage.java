package com.example.master_project_.DataHolder;

import org.locationtech.jts.geom.Coordinate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class RouteDataStorage {
    static HashMap<UUID, List<Coordinate>> routePaths;
    public static HashMap<UUID, List<Coordinate>>  getInstance() throws IOException {
        if (routePaths==null){
            routePaths  =new HashMap<>();
        }
        return routePaths;
    }

    public static List<Coordinate> getCoordinates(UUID uuid) throws IOException {
        List<Coordinate> coordinates = getInstance().get(uuid);
        if (coordinates !=null){
            return coordinates;
        }else{
            System.out.println("Not Found");
            System.out.println(getInstance().keySet());
            System.out.println(uuid);
            return null;
        }
    }

}
