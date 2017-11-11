package com.github.stairch.rest;

import com.github.stairch.dtos.MoveRequestDTO;
import com.github.stairch.dtos.MoveResponseDTO;
import com.github.stairch.types.Move;

public class TheBrain {

    private MoveResponseDTO conclusion;

    private int counter = 0;


    public MoveResponseDTO getConclusion() {
        return conclusion;
    }

    public void setConclusion(MoveResponseDTO conclusion) {
        this.conclusion = conclusion;
    }



    public void think(MoveRequestDTO moveRequestDTO) {

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

            counter++;


       // conclusion.setMove(Move.right);

    }
}
