package com.github.stairch.rest;

import com.github.stairch.dtos.MoveRequestDTO;
import com.github.stairch.dtos.MoveResponseDTO;
import com.github.stairch.types.Move;

public class TheBrain {

    private MoveResponseDTO conclusion;

    private int counter = 0;


    public MoveResponseDTO getConclusion() {
        return this.conclusion;
    }

    public void setConclusion(MoveResponseDTO conclusion) {
        this.conclusion = conclusion;
    }



    public void think(MoveRequestDTO moveRequestDTO) {

        switch(counter%4) {
            case (0):
                this.conclusion.setMove(Move.right);
                break;
            case (1):
                this.conclusion.setMove(Move.down);
                break;
            case (2):
                this.conclusion.setMove(Move.left);
                break;
            case (3):
                this.conclusion.setMove(Move.up);
                break;
        }
            System.out.println(this.conclusion);
            counter++;
            System.out.println(counter);

            //conclusion.setTaunt("my counter: " + counter);

       // conclusion.setMove(Move.right);

    }
}
