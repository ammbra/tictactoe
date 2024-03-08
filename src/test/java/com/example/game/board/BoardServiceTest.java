package com.example.game.board;

import com.example.game.board.cell.Cell;
import com.example.game.board.cell.CellManager;
import com.example.game.player.Player;
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
		player.setType(Player.Type.PERSON);
		when(cellManager.init()).thenCallRealMethod();

		Board board = service.build(player, Cell.X);

		assertThat(board.getPlayer()).isSameAs(player);
		assertThat(board.getStatus()).isEqualTo(Status.ACTIVE);
		assertThat(board.getNext()).isEqualTo(Player.Type.AI);
		assertThat(board.getPlayer().getType()).isEqualTo(Player.Type.PERSON);
		assertThat(board.getLines()).isEqualTo(cellManager.init());

		verify(repository).save(any());
	}


	@Test
	void findPreviousMatch() {
		Player player = new Player();
		service.findPreviousMatch(player);
		verify(repository).findFirstByPlayerOrderByCreatedOnDesc(player);
	}

	@Test
	void randomMove() {
		when(cellManager.init()).thenCallRealMethod();

		Board board = service.build(new Player(), Cell.X);
		when(cellManager.findRandomFreeCell(board.getLines())).thenCallRealMethod();

		service.randomMove(board);

		assertThat(board.getNext()).isEqualTo(Player.Type.PERSON);
		assertThat(board.getStatus()).isEqualTo(Status.ACTIVE);

	}

	@Test
	void moveAndGameIsActive() {
		when(cellManager.init()).thenCallRealMethod();

		Board board = service.build(new Player(), Cell.X);
		service.move(board, new Coordinate(1,1));

		assertThat(board.getNext()).isEqualTo(Player.Type.PERSON);
		assertThat(board.getStatus()).isEqualTo(Status.ACTIVE);

		service.move(board, new Coordinate(0,1));

		assertThat(board.getNext()).isEqualTo(Player.Type.AI);
		assertThat(board.getStatus()).isEqualTo(Status.ACTIVE);
	}

	@Test
	void moveAndHumanWins() {
		when(cellManager.init()).thenCallRealMethod();

		Board board = service.build(new Player(), Cell.X);
		board.getLines().set(0, Arrays.asList("x", "x", ""));
		board.setNext(Player.Type.PERSON);
		service.move(board, new Coordinate(0,2));

		assertThat(board.getNext()).isEqualTo(Player.Type.NONE);
		assertThat(board.getStatus()).isEqualTo(Status.PERSON_WIN);
	}

	@Test
	void moveAndAIWins() {
		when(cellManager.init()).thenCallRealMethod();
		Board board = service.build(new Player(), Cell.X);

		board.getLines().set(0, Arrays.asList("o", "o", ""));
		board.getLines().set(1, Arrays.asList("x", "o", "x"));
		board.getLines().set(2, Arrays.asList("", "o", "o"));

		service.move(board, new Coordinate(0,2));

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

		service.move(board, new Coordinate(2,1));

		assertThat(board.getNext()).isEqualTo(Player.Type.PERSON);
		assertThat(board.getStatus()).isEqualTo(Status.TIE);
	}
}