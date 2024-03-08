package com.example.game.player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Player findByUsername(String username);
}