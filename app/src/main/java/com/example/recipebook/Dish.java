package com.example.recipebook;

import java.util.ArrayList;

public class Dish {
    public String name, imgUrl;
    public ArrayList<String> ingridients;
    public ArrayList<String> instruction;
    public int time;
    public int servings;
    public String databasePath;

    public Dish(){}

    public Dish(String name, String imgUrl, ArrayList<String> ingridients,
                ArrayList<String> instruction, int time, int servings, String databasePath){
        this.instruction = instruction;
        this.name = name;
        this.imgUrl = imgUrl;
        this.ingridients = ingridients;
        this.time = time;
        this.servings = servings;
        this.databasePath = databasePath;
    }

}

