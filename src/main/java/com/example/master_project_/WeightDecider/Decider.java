package com.example.master_project_.WeightDecider;

import org.apache.tomcat.util.bcel.Const;
import scala.collection.immutable.Stream;

public class Decider {
    public static Double maxSpeedDecider(Double maxSpeed){
        if (maxSpeed<5){
            return 0.1;
        }
        else if (maxSpeed<30){
            return 0.3;
        }else if(maxSpeed<80) {
            return 0.6;
        }else{
            return 0.9;
        }
    }
    public static Double nullMaxSpeedDecider(String highwayType){
        switch (highwayType){
            case "residential":
                return 30.0;
            case"secondary":
                return 50.0;
            case "secondary_link":
                return 50.0;
            case "primary":
                return 50.0;
            case "primary_link":
                return 50.0;
            default:
                return 1.0;
        }
    }
    public static Double turningCostDecider(String turningType){
        Double turningCost;
        Double Constant = 300.0;
        Double leftCost = 500.0;

        switch (turningType) {
            case "Right":
                turningCost = Constant;
                break;
            case "Left":
                turningCost = Constant + leftCost;
                break;
            case "Continous":
                turningCost = Constant ;
                break;
            case "No_Turns":
                turningCost = 0.0;
                break;
            case "Cross":
                turningCost = Constant ;
                break;
            case "Second_Right":
                turningCost = Constant ;
                break;
            case "Non_Restricted":
                turningCost = Constant;
                break;
            case "Cross_Right":
                turningCost = Constant ;
                break;
            case "Cross_Left":
                turningCost = Constant + leftCost / 2;
                break;
            default:
                turningCost = Constant;
                break;
        }

        return turningCost;






    }
}
