package com.example.game.board;

import com.example.game.player.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface BoardRepository extends JpaRepository<Board, Long> {

    Board findFirstByPlayerOrderByCreatedOnDesc(Player player);

}