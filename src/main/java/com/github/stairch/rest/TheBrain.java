package com.github.stairch.rest;

import com.github.stairch.dtos.MoveRequestDTO;
import com.github.stairch.dtos.MoveResponseDTO;
import com.github.stairch.dtos.PointDTO;
import com.github.stairch.types.Move;

import java.util.ArrayList;

public class TheBrain {


    public int findNearesFood(PointDTO myhead, ArrayList<PointDTO> foods ){

        int minDistance = 1000;
        int foodID = 0;
        for(int i = 0; i < foods.size(); i++){
            int dis = getDistance(myhead, foods.get(i) );
            if(dis < minDistance ) {
                foodID = i;
                minDistance = dis;
            }
        }
        return foodID;
    }

    public int getDistance(PointDTO from, PointDTO to){
        int distance = 0;

            distance =distance +  Math.abs( to.getX() - from.getX());
            distance =distance +  Math.abs( to.getY() - from.getY());


        return distance;
    }
}
