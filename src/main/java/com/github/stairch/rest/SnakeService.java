package com.github.stairch.rest;

import com.github.stairch.dtos.*;
import com.github.stairch.types.HeadType;
import com.github.stairch.types.Move;
import com.github.stairch.types.TailType;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
        System.out.println(moveRequestDTO);
        Gson gson = new Gson();
        JsonElement element = gson.fromJson (moveRequestDTO, JsonElement.class);
        JsonObject jsonObj = element.getAsJsonObject();

        System.out.println(jsonObj.get("coords"));
       // TheBrain mybrain = new TheBrain();
       // mybrain.think(moveRequestDTO);

        //moveResponse.setMove(Move.left);
        // MoveResponseDTO moveResponse = think(moveRequestDTO);
         MoveResponseDTO moveResponse = new MoveResponseDTO();
         moveResponse.setMove(Move.right);
        System.out.println(moveResponse);
        System.out.println("Not dead");
        final String responseBody = gson.toJson(moveResponse);
        return Response.status(Response.Status.OK).entity(responseBody).build();
    }

    private int counter;
    public MoveResponseDTO think(MoveRequestDTO moveRequestDTO) {
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
    }


}