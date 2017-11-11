package com.github.stairch.rest;

import com.github.stairch.dtos.MoveRequestDTO;
import com.github.stairch.dtos.MoveResponseDTO;
import com.github.stairch.dtos.PointDTO;
import com.github.stairch.types.Move;

import java.util.ArrayList;

public class TheBrain {


    public void findNearesFood(PointDTO myhead, ArrayList<PointDTO> foods ){
        //System.out.println("hallo");
        //System.out.println(myhead);
        //System.out.println(foods);
       // int myx = myhead.getX();
       // int myy = myhead.getY();
        int maxDistants = 100;
        int counter = 0;
        for(PointDTO iterator:foods){
            int nowX = iterator.getX();
            int nowY = iterator.getY();

            int distance = 0;
            if(nowX > myhead.getX()) {
                distance = distance + nowX - myhead.getX();
            }else{
                distance = distance - (nowX - myhead.getX());
            }

            if(nowY > myhead.getY()) {
                distance = distance + nowY - myhead.getY();
            }else{
                distance = distance - (nowY - myhead.getY());
            }

           if(distance < maxDistants)
               maxDistants = distance;
        }

        System.out.println("Distance to food:" + maxDistants);

    }

}
