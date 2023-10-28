package com.example.tictactoe.board;

import com.example.tictactoe.board.cell.Cell;
import com.example.tictactoe.board.cell.CellManager;
import com.example.tictactoe.player.Player;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.parameters.P;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

	private BoardService service;

	@Mock
	private BoardRepository repository;

	@Mock
	private CellManager cellManager;

	@BeforeEach
	public void setUp() {
		service = new BoardService(repository, cellManager);
	}

	@Test
	void build() {
		Player player = new Player();
		player.setType(Player.Type.HUMAN);
		when(cellManager.init()).thenCallRealMethod();

		Board board = service.build(player, Cell.X);

		assertThat(board.getPlayer()).isSameAs(player);
		assertThat(board.getStatus()).isEqualTo(Status.ACTIVE);
		assertThat(board.getNext()).isEqualTo(Player.Type.AI);
		assertThat(board.getPlayer().getType()).isEqualTo(Player.Type.HUMAN);
		assertThat(board.getLines()).isEqualTo(cellManager.init());

		verify(repository).save(any());
	}


	@Test
	void findPreviousGame() {
		Player player = new Player();
		service.findPreviousGame(player);
		verify(repository).findFirstByPlayerOrderByIdDesc(player);
	}

	@Test
	void randomMove() {
		when(cellManager.init()).thenCallRealMethod();

		Board board = service.build(new Player(), Cell.X);
		when(cellManager.findRandomFreeCell(board.getLines())).thenCallRealMethod();

		service.randomMove(board);

		assertThat(board.getNext()).isEqualTo(Player.Type.HUMAN);
		assertThat(board.getStatus()).isEqualTo(Status.ACTIVE);

	}

	@Test
	void moveAndGameIsActive() {
		when(cellManager.init()).thenCallRealMethod();

		Board board = service.build(new Player(), Cell.X);
		service.move(board, "1:1");

		assertThat(board.getNext()).isEqualTo(Player.Type.HUMAN);
		assertThat(board.getStatus()).isEqualTo(Status.ACTIVE);

		service.move(board, "0:1");

		assertThat(board.getNext()).isEqualTo(Player.Type.AI);
		assertThat(board.getStatus()).isEqualTo(Status.ACTIVE);
	}

	@Test
	void moveAndHumanWins() {
		when(cellManager.init()).thenCallRealMethod();

		Board board = service.build(new Player(), Cell.X);
		board.getLines().set(0, Arrays.asList("x", "x", ""));
		board.setNext(Player.Type.HUMAN);
		service.move(board, "0:2");

		assertThat(board.getNext()).isEqualTo(Player.Type.NONE);
		assertThat(board.getStatus()).isEqualTo(Status.HUMAN_WIN);
	}

	@Test
	void moveAndAIWins() {
		when(cellManager.init()).thenCallRealMethod();
		Board board = service.build(new Player(), Cell.X);

		board.getLines().set(0, Arrays.asList("o", "o", ""));
		board.getLines().set(1, Arrays.asList("x", "o", "x"));
		board.getLines().set(2, Arrays.asList("", "o", "o"));

		service.move(board, "2:0");

		assertThat(board.getNext()).isEqualTo(Player.Type.NONE);
		assertThat(board.getStatus()).isEqualTo(Status.AI_WIN);
	}

	@Test
	void moveAndTie() {
		when(cellManager.init()).thenCallRealMethod();

		Board board = service.build(new Player(), Cell.X);
		board.getLines().set(0, Arrays.asList("o", "x", "x"));
		board.getLines().set(1, Arrays.asList("x", "o", "o"));
		board.getLines().set(2, Arrays.asList("o", "", "x"));

		service.move(board, "2:1");

		assertThat(board.getNext()).isEqualTo(Player.Type.HUMAN);
		assertThat(board.getStatus()).isEqualTo(Status.TIE);
	}
}