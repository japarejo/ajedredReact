package com.samples.ajedrez.chess;

import javax.persistence.Entity;
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

    String type;
    String color;
    @Range(min=0,max=7)
    int xPosition;
    @Range(min=0,max=7)
    int yPosition;
    
    @ManyToOne
    @JsonIgnore
    ChessBoard board;
}
