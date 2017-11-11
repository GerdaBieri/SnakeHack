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

        startResponse.setHeadType(HeadType.getPixel());
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
        com.google.gson.JsonArray dead_snakes = jsonObj.getAsJsonArray("dead_snakes");
        fillOutAllSnakeInfos(dead_snakes);

        // Fill out food coordinates
        com.google.gson.JsonArray foods = jsonObj.getAsJsonArray("food");
        fillInFood(foods);
    }


    private void fillOutAllSnakeInfos(com.google.gson.JsonArray snakes){
        for(int i = 0 ; i < snakes.size(); i++){
            com.google.gson.JsonArray coordinatesArray = snakes.get(i).getAsJsonObject().get("coords").getAsJsonArray();

            for(int j = 0; j < coordinatesArray.size(); j++){
                PointDTO point = new PointDTO();
                JsonElement coordinatesEntry = coordinatesArray.get(j);
                com.google.gson.JsonArray cordinatesOfPoint = coordinatesEntry.getAsJsonArray();
                point.setX(cordinatesOfPoint.get(0).getAsInt());
                point.setY(cordinatesOfPoint.get(1).getAsInt());
                blockedPointsArray.add(point);
            }

            if (snakes.get(i).getAsJsonObject().get("id").equals( meAsASnake)){
                PointDTO point = new PointDTO();
                JsonElement coordinatesEntry = coordinatesArray.get(0);
                com.google.gson.JsonArray cordinatesOfPoint = coordinatesEntry.getAsJsonArray();
                point.setX(cordinatesOfPoint.get(0).getAsInt());
                point.setY(cordinatesOfPoint.get(1).getAsInt());
                myHead = point;
            }
        }
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
       mybrain.findNearesFood(myHead, FoodArray);

       /*
        * finished initialisation of thinking
         */

       guessForMoveUp = guessValueOfPossibleMove(possibilityUp);
       guessForMoveDown = guessValueOfPossibleMove(possibilityDown);
       guessForMoveLeft = guessValueOfPossibleMove(possibilityLeft);
       guessForMoveRight = guessValueOfPossibleMove(possibilityRight);
   }

   private int guessValueOfPossibleMove(PointDTO point) {

       int maxPossibleValue = width + height + 1;

       // Wenn er in der Wand landet

       if (hitsWall(point, width, height)) {
           System.out.println("hit wall " + width + " " + height);
           System.out.println(point.getX() + " " +point.getY());
           return maxPossibleValue;
       }

       // Wenn er in den besetzten Feldern ist


       // Wähle seinen Wert, je kleiner, desto besser


       return 2;
   }

   private boolean hitsWall(PointDTO point, int width, int height) {


       int x = point.getX();
       int y = point.getY();

       if( x < 0)
           return true;

       if (y < 0 )
           return true;


       if(x >= width)
           return true;

       if (y >= height)
           return true;
       return false;
   }

    /*
    Decide based on decision variables
     */
   private MoveResponseDTO decide() {
       MoveResponseDTO response = new MoveResponseDTO();
       response.setMove(Move.right);

       int minWert = guessForMoveUp;
       if(guessForMoveDown < minWert) {
           minWert = guessForMoveDown;
           response.setMove(Move.down);
       }
       if(guessForMoveRight < minWert) {
           minWert = guessForMoveRight;
           response.setMove(Move.right);
       }
       if(guessForMoveLeft < minWert) {
           minWert = guessForMoveLeft;
           response.setMove(Move.left);
       }

       return response;

   }

}