package com.samples.ajedrez.game;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.samples.ajedrez.model.BaseEntity;
import com.samples.ajedrez.player.Player;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "games")
@Getter
@Setter
public class Game extends BaseEntity {

	@Column(name = "name",unique = true)
	@NotEmpty
	private String name;

	@Column(name = "tiempo")
	@NotNull
	private Integer tiempo;

    @Column(name = "espectadores")
    @NotNull
    private Boolean espectadores;


	@Column(name = "numeroJugadores")
	@NotNull
	private Integer numeroJugadores;

	@ManyToMany
	@JsonIgnore
	private List<Player> player;


}
