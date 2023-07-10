package com.example.master_project_.WeightDecider;

public class Decider {
    public static Double maxSpeedDecider(Double maxSpeed){
        if (maxSpeed<30){
            return 1.3;
        }else if(maxSpeed<80) {
            return 1.6;
        }else{
            return 1.9;
        }
    }
    public static Double turningCostDecider(String turningType){
        Double turningCost;
        Double Constant = 200.0;
        Double leftCost = 100.0;

        switch (turningType) {
            case "Right":
                turningCost = Constant;
                break;
            case "Left":
                turningCost = Constant + leftCost;
                break;
            case "Continous":
                turningCost = Constant + leftCost / 2;
                break;
            case "No_Turns":
                turningCost = 0.0;
                break;
            case "Cross":
                turningCost = Constant + leftCost / 2;
                break;
            case "Second_Right":
                turningCost = Constant + leftCost / 3;
                break;
            case "Non_Restricted":
                turningCost = Constant;
                break;
            case "Cross_Right":
                turningCost = Constant + leftCost / 3;
                break;
            case "Cross_Left":
                turningCost = Constant + leftCost / 2;
                break;
            default:
                turningCost = 0.0;
                break;
        }

        return turningCost;






    }
}
