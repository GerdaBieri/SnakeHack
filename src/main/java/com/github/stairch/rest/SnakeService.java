package com.github.stairch.rest;

import com.github.stairch.dtos.*;
import com.github.stairch.types.HeadType;
import com.github.stairch.types.Move;
import com.github.stairch.types.TailType;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.json.Json;
import javax.json.JsonArray;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.util.ArrayList;
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
        System.out.println("Not dead");
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

        System.out.println("me:" + meAsASnake);


    }


    private void fillOutAllSnakeInfos(com.google.gson.JsonArray snakes){
        for(int i = 0 ; i < snakes.size(); i++){
            com.google.gson.JsonArray coordinatesArray = snakes.get(i).getAsJsonObject().get("coords").getAsJsonArray();
            PointDTO point = new PointDTO();
            for(int j = 0; j < coordinatesArray.size(); j++){
                JsonElement coordinatesEntry = coordinatesArray.get(j);
                com.google.gson.JsonArray cordinatesOfPoint = coordinatesEntry.getAsJsonArray();
                point.setX(cordinatesOfPoint.get(0).getAsInt());
                point.setY(cordinatesOfPoint.get(1).getAsInt());
                blockedPointsArray.add(point);
            }

            if (snakes.get(i).getAsJsonObject().get("id").equals( meAsASnake)){
                JsonElement coordinatesEntry = coordinatesArray.get(0);
                com.google.gson.JsonArray cordinatesOfPoint = coordinatesEntry.getAsJsonArray();
                point.setX(cordinatesOfPoint.get(0).getAsInt());
                point.setY(cordinatesOfPoint.get(1).getAsInt());
                myHead = point;
            }
        }
    }

    private void fillInFood(com.google.gson.JsonArray foods){
        PointDTO point = new PointDTO();
        for(int j = 0; j < foods.size(); j++){
            JsonElement coordinatesEntry = foods.get(j);
            com.google.gson.JsonArray cordinatesOfPoint = coordinatesEntry.getAsJsonArray();
            point.setX(cordinatesOfPoint.get(0).getAsInt());
            point.setY(cordinatesOfPoint.get(1).getAsInt());
            FoodArray.add(point);
        }
    }


   /*
   Work with initialized Request settings and set all decision variables
    */
   private void think() {

   }

    /*
    Decide based on decision variables
     */
   private MoveResponseDTO decide() {
       MoveResponseDTO response = new MoveResponseDTO();
       response.setMove(Move.right);
       /*
       char[] a = {'3', '5', '1', '4', '2'};

        List b = Arrays.asList(ArrayUtils.toObject(a));

        System.out.println(Collections.min(b));
        System.out.println(Collections.max(b));

        */
       return response;

   }

}