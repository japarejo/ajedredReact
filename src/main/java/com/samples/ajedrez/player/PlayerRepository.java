package com.samples.ajedrez.player;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.samples.ajedrez.user.User;

public interface PlayerRepository extends CrudRepository<Player, String> {

    @Query("SELECT player FROM Player player WHERE player.user = :user")
    public Player findByUsername(@Param("user") User username);

    @Query("SELECT player FROM Player player")
    public List<Player> findAllPlayers();

    @Query("SELECT player FROM Player player WHERE player.id= :id")
    public Player findById(@Param("id") int id);
}
