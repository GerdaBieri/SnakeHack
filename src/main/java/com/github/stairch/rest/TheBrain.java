package com.github.stairch.rest;

import com.github.stairch.dtos.MoveRequestDTO;
import com.github.stairch.dtos.MoveResponseDTO;
import com.github.stairch.types.Move;

public class TheBrain {

    private MoveResponseDTO conclusion;

    public MoveResponseDTO getConclusion() {
        return conclusion;
    }

    public void setConclusion(MoveResponseDTO conclusion) {
        this.conclusion = conclusion;
    }



    public void think(MoveRequestDTO moveRequestDTO) {


        conclusion.setMove(Move.left);
        conclusion.setMove(Move.right);
        conclusion.setMove(Move.up);
        conclusion.setMove(Move.down);

    }
}
