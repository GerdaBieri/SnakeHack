package com.github.stairch.rest;

import com.github.stairch.dtos.*;
import com.github.stairch.types.HeadType;
import com.github.stairch.types.Move;
import com.github.stairch.types.TailType;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.glassfish.grizzly.utils.ArrayUtils;

import javax.json.Json;
import javax.json.JsonArray;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.github.stairch.RestInPeace.BASE_URI;

@Path("/")
public class SnakeService {

    /**
     * Used for json serialization/deserialization.
     */
    private final Gson gson = new Gson();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "yeaay, your starter snake is up and running :)";
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/start")
    public final Response start(final StartRequestDTO startRequestDTO) {
        System.out.println(startRequestDTO);

        final StartResponseDTO startResponse = new StartResponseDTO();
        startResponse.setColor("#ff63f2");
        startResponse.setSecondaryColor("#ff8a2b");
        startResponse.setHeadUrl(BASE_URI + "static/head.png");
        startResponse.setName("Sneaky Snake");
        startResponse.setTaunt("I like trains!");

        startResponse.setHeadType(HeadType.getSmile());
        startResponse.setTailType(TailType.getBlockbum());
        final String responseBody = gson.toJson(startResponse);
        return Response.status(Response.Status.OK).entity(responseBody).build();
    }

    @POST
    @Path("/move")
    public final Response move(final String moveRequestDTO) {
        initialize(moveRequestDTO);
        think();
        MoveResponseDTO moveResponse = decide();
        System.out.println(moveResponse);
        final String responseBody = gson.toJson(moveResponse);
        return Response.status(Response.Status.OK).entity(responseBody).build();
    }

    private ArrayList blockedPointsArray;
    private ArrayList FoodArray;
    private PointDTO myHead = new PointDTO();
    int myLength;
    JsonElement meAsASnake;
    int width;
    int height;

    private void initialize(String request) {
        blockedPointsArray = new ArrayList<PointDTO>();
        FoodArray = new ArrayList<PointDTO>();

        // Get Myself first
        JsonElement element = gson.fromJson (request, JsonElement.class);
        JsonObject jsonObj = element.getAsJsonObject();
        meAsASnake = jsonObj.get("you");
        //How's the field?
        width = jsonObj.get("width").getAsInt();
        height = jsonObj.get("height").getAsInt();

        // Fill out blocked Coordinates by Snakes
        com.google.gson.JsonArray snakes = jsonObj.getAsJsonArray("snakes");
        fillOutAllSnakeInfos(snakes);
        //com.google.gson.JsonArray dead_snakes = jsonObj.getAsJsonArray("dead_snakes");
       // fillOutAllSnakeInfos(dead_snakes);

        // Fill out food coordinates
        com.google.gson.JsonArray foods = jsonObj.getAsJsonArray("food");
        fillInFood(foods);
    }


    private void fillOutAllSnakeInfos(com.google.gson.JsonArray snakes){
        for(int i = 0 ; i < snakes.size(); i++){
            com.google.gson.JsonArray coordinatesArray = snakes.get(i).getAsJsonObject().get("coords").getAsJsonArray();
            if (snakes.get(i).getAsJsonObject().get("id").equals( meAsASnake)){
                PointDTO point = new PointDTO();
                JsonElement coordinatesEntry = coordinatesArray.get(0);
                com.google.gson.JsonArray cordinatesOfPoint = coordinatesEntry.getAsJsonArray();
                point.setX(cordinatesOfPoint.get(0).getAsInt());
                point.setY(cordinatesOfPoint.get(1).getAsInt());
                myHead = point;
                myLength = snakes.get(i).getAsJsonObject().get("coords").getAsJsonArray().size();
            }
        }

        for(int i = 0 ; i < snakes.size(); i++){
            com.google.gson.JsonArray coordinatesArray = snakes.get(i).getAsJsonObject().get("coords").getAsJsonArray();
            for(int j = 0; j < coordinatesArray.size()-1; j++){
                PointDTO point = new PointDTO();
                JsonElement coordinatesEntry = coordinatesArray.get(j);
                com.google.gson.JsonArray cordinatesOfPoint = coordinatesEntry.getAsJsonArray();
                point.setX(cordinatesOfPoint.get(0).getAsInt());
                point.setY(cordinatesOfPoint.get(1).getAsInt());
                blockedPointsArray.add(point);
            }
            if (!(snakes.get(i).getAsJsonObject().get("id").equals( meAsASnake))) {
                int size = snakes.get(i).getAsJsonObject().get("coords").getAsJsonArray().size();
                if (size >= myLength) {
                    PointDTO point = new PointDTO();
                    JsonElement otherSnakesHead = coordinatesArray.get(0);
                    com.google.gson.JsonArray cordinatesOfPoint = otherSnakesHead.getAsJsonArray();
                    point.setX(cordinatesOfPoint.get(0).getAsInt());
                    point.setY(cordinatesOfPoint.get(1).getAsInt());
                    addPointsOfOtherHead(point);
                }
            }
        }
    }

    public void addPointsOfOtherHead(PointDTO point) {
        PointDTO pointUp = new PointDTO();
        PointDTO pointDown = new PointDTO();
        PointDTO pointRight = new PointDTO();
        PointDTO pointLeft = new PointDTO();

        pointUp.setX(point.getX());
        pointUp.setY(point.getY()-1);

        pointDown.setX(point.getX());
        pointDown.setY(point.getY()+1);

        pointRight.setX(point.getX()+1);
        pointRight.setY(point.getY());

        pointLeft.setX(point.getX()-1);
        pointLeft.setY(point.getY());

        blockedPointsArray.add(pointUp);
        blockedPointsArray.add(pointDown);
        blockedPointsArray.add(pointRight);
        blockedPointsArray.add(pointLeft);
    }



    private void fillInFood(com.google.gson.JsonArray foods){

        for(int j = 0; j < foods.size(); j++){
            PointDTO point = new PointDTO();
            JsonElement coordinatesEntry = foods.get(j);
            com.google.gson.JsonArray cordinatesOfPoint = coordinatesEntry.getAsJsonArray();
            point.setX(cordinatesOfPoint.get(0).getAsInt());
            point.setY(cordinatesOfPoint.get(1).getAsInt());
            FoodArray.add(point);
        }
    }



    PointDTO possibilityUp = new PointDTO();
    PointDTO possibilityDown = new PointDTO();
    PointDTO possibilityRight = new PointDTO();
    PointDTO possibilityLeft = new PointDTO();

    int guessForMoveUp ;
    int guessForMoveDown;
    int guessForMoveRight;
    int guessForMoveLeft;

    PointDTO nearestFood = new PointDTO();

   /*
   Work with initialized Request settings and set all decision variables
    */
   private void think() {
       // Set the 4 possible Points for move
       possibilityUp.setX(myHead.getX());
       possibilityUp.setY(myHead.getY()-1);

       possibilityDown.setX(myHead.getX());
       possibilityDown.setY(myHead.getY()+1);

       possibilityRight.setX(myHead.getX()+1);
       possibilityRight.setY(myHead.getY());

       possibilityLeft.setX(myHead.getX()-1);
       possibilityLeft.setY(myHead.getY());

       // Guess an order of the food
       TheBrain mybrain = new TheBrain();
       int idOfNearestFood = mybrain.findNearesFood(myHead, FoodArray);
       nearestFood = (PointDTO)FoodArray.get(idOfNearestFood);

       /*
        * finished initialisation of thinking
         */

       guessForMoveUp = guessValueOfPossibleMove(possibilityUp);
       guessForMoveDown = guessValueOfPossibleMove(possibilityDown);
       guessForMoveLeft = guessValueOfPossibleMove(possibilityLeft);
       guessForMoveRight = guessValueOfPossibleMove(possibilityRight);
   }

   private int guessValueOfPossibleMove(PointDTO point) {
       TheBrain mybrain = new TheBrain();
       int maxPossibleValue = width + height + 1;

       // Wenn er in der Wand landet

       if (mybrain.hitsWall(point, width, height)) {
           System.out.println("hit wall " + width + " " + height);
           System.out.println(point.getX() + " " +point.getY());
           return maxPossibleValue;
       }

       // Wenn er in den besetzten Feldern ist
        if(blockedPointsArray.contains(point))
            return maxPossibleValue;

       // Wähle seinen Wert, je kleiner, desto besser
       return mybrain.getDistance(point, nearestFood);

   }



    /*
    Decide based on decision variables
     */
   private MoveResponseDTO decide() {
       MoveResponseDTO response = new MoveResponseDTO();

       System.out.println(guessForMoveUp + "U " +guessForMoveDown +"D " + guessForMoveRight +"R " +guessForMoveLeft +"L" );

       int minWert = guessForMoveUp;
       response.setMove(Move.up);
       if(guessForMoveLeft < minWert) {
           minWert = guessForMoveLeft;
           response.setMove(Move.left);
       }
       if(guessForMoveDown < minWert) {
           minWert = guessForMoveDown;
           response.setMove(Move.down);
       }

       if(guessForMoveRight < minWert) {
           response.setMove(Move.right);
       }


       return response;

   }

}