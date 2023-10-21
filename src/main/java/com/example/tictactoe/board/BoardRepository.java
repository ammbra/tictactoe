package com.example.tictactoe.board;

import com.example.tictactoe.player.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface BoardRepository extends JpaRepository<Board, Long> {

    Board findFirstByPlayerOrderByIdDesc(Player player);

}