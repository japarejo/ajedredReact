package com.samples.ajedrez.chess;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.Positive;

import com.samples.ajedrez.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class ChessBoard extends BaseEntity {

    String background;
    @Positive
    int width;

    @Positive
    int height;


    public ChessBoard(){
        this.background = "resources/images/tablero.jpg";
        this.width=800;
        this.height= 800;
    }


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "board", fetch = FetchType.EAGER)
    List<Piece> pieces;
    
}
