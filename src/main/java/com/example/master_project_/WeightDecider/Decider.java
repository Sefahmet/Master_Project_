package com.example.master_project_.WeightDecider;

import org.apache.tomcat.util.bcel.Const;
import scala.collection.immutable.Stream;

public class Decider {
    public static Double maxSpeedDecider(Double maxSpeed){
        if (maxSpeed<5){
            return 0.85;
        }
        else if (maxSpeed<30){
            return 0.95;
        }else if(maxSpeed<80) {
            return 1.05;
        }else{
            return 1.15;
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
        Double Constant = 5.0;
        Double leftCost = 21.0;

        switch (turningType) {
            case "Right":
                turningCost = Constant;
                break;
            case "Left":
                turningCost = Constant+leftCost;
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
            case "Cross_Right":
                turningCost = Constant ;
                break;
            case "Cross_Left":
                turningCost = Constant;
                break;
            default:
                turningCost = Constant;
                break;
        }

        return turningCost;


    }
    public  static Double slopeDecider(Double slope){

        double a = 8.11260291157632;
        double b = 24.28555137728186;
        double slope_per = slope *100;
        double maxValue = a * 15 + b;

        if (slope_per<-b/a){
            return 0.8;
        }
        else if (slope_per<15) {
            return  0.8 + (a * slope + b) / (2.5 * maxValue);
        }
        else{
            return 1.2;
        }
    }
}
