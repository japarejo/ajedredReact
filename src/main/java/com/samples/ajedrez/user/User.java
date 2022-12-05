package com.samples.ajedrez.user;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")

public class User {

    @Id
    String username;

    @Column(nullable = false)
    String password;

    @Column(nullable = false)
    boolean enabled;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user",fetch = FetchType.EAGER)
    @JsonIgnore
	private List<Authorities> authorities;

    
}
