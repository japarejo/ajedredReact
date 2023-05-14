package com.samples.ajedrez.chess;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.samples.ajedrez.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Piece extends BaseEntity {

    public Piece(){

    }
    
    public Piece(int id,int xPosition,int yPosition){
        this.id = id;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }


    String type;
    
    String color;
    
    @Range(min=0,max=7)
    int xPosition;
    
    @Range(min=0,max=7)
    int yPosition;

    Boolean piezaMovida;

    Boolean peonPaso;  // Se utiliza por si el peon se mueve dos posiciones
    
    @ManyToOne
    @JoinColumn(name = "board_id")
    @JsonIgnore
    ChessBoard board;
}
