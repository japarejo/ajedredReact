package com.samples.ajedrez.player;


import java.time.Instant;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.databind.util.TypeKey;
import com.samples.ajedrez.model.BaseEntity;
import com.samples.ajedrez.user.User;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "players")
@Getter
@Setter
public class Player extends BaseEntity {


	public Player(){

	}

	public Player(String firstName,String lastName, String telephone, User user){
		this.firstName = firstName;
		this.lastName = lastName;
		this.telephone = telephone;
		this.user = user;
	}

	@Column(name = "first_name")
	@NotEmpty
	private String firstName;

    @Column(name = "last_name")
	@NotEmpty
	private String lastName;


	@Column(name = "telephone")
	@NotEmpty
	@Digits(fraction = 0, integer = 9)
	private String telephone;


	@Column(name="color")
	private String colorPartida;


	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "username", referencedColumnName = "username")
	private User user;


	@Column(name = "time")
	private Integer time;


	@Column(name = "start_turn")
	private Instant inicioTurno;
}
