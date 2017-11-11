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
    // @Consumes(MediaType.APPLICATION_JSON)
    // @Produces(MediaType.APPLICATION_JSON)
    @Path("/move")
    public final Response move(final String moveRequestDTO) {
       // System.out.println(moveRequestDTO);
        initialize(moveRequestDTO);

        think();

        //moveResponse.setMove(Move.left);
        // MoveResponseDTO moveResponse = think(moveRequestDTO);
         MoveResponseDTO moveResponse = decide();
        System.out.println(moveResponse);
        System.out.println("Not dead");
        final String responseBody = gson.toJson(moveResponse);
        return Response.status(Response.Status.OK).entity(responseBody).build();
    }


    private void initialize(String request) {
        Gson gson = new Gson();
        JsonElement element = gson.fromJson (request, JsonElement.class);
        JsonObject jsonObj = element.getAsJsonObject();
        JsonElement meAsASnake = jsonObj.get("you");

        com.google.gson.JsonArray snakes = jsonObj.getAsJsonArray("snakes");
        //JsonObject snakesJson = snakes.getAsJsonObject();
        System.out.println("me:" + meAsASnake);

        for(int i = 0 ; i < snakes.size(); i++){
            if (snakes.get(i).getAsJsonObject().get("id").equals( meAsASnake)){
                System.out.println("this is me" + snakes.get(i).getAsJsonObject().get("id"));
            }else{
                System.out.println("this is not me:" + snakes.get(i).getAsJsonObject().get("id"));
            }


        }
    }

    private int counter;
   /* public MoveResponseDTO think(MoveRequestDTO moveRequestDTO) {
        MoveResponseDTO conclusion = new MoveResponseDTO();
        switch(counter%4) {
            case (0):
                conclusion.setMove(Move.right);
                break;
            case (1):
                conclusion.setMove(Move.down);
                break;
            case (2):
                conclusion.setMove(Move.left);
                break;
            case (3):
                conclusion.setMove(Move.up);
                break;
        }
        System.out.println(conclusion);
        counter++;
        System.out.println(counter);

        //conclusion.setTaunt("my counter: " + counter);

        // conclusion.setMove(Move.right);


        return conclusion;
    }*/

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