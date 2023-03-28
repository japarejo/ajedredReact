package com.samples.ajedrez.chess;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.samples.ajedrez.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class ChessBoard extends BaseEntity{


    public ChessBoard(){
        
    }

    public ChessBoard(ChessBoard board){
        this.jaque = board.getJaque();
        this.jaqueMate = board.getJaqueMate();
        this.turn = board.getTurn();
    }


    @Column(name = "turn")
	@NotNull
	private String turn;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "board", fetch = FetchType.EAGER)
    List<Piece> pieces;


    @Column(name= "jaque")
    private Boolean jaque;

    @Column(name= "jaque_mate")
    private Boolean jaqueMate;


    
    
}
