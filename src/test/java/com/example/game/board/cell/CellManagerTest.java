package com.example.game.board.cell;

import com.example.game.board.Coordinate;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CellManagerTest {

	@Test
	void init() {
		CellManager cellManager = new CellManager();
		List<List<String>> board = cellManager.init();
		assertThat(board.size()).isEqualTo(3);
	}

	@Test
	void findRandomFreeCell() {
		CellManager cellManager =  new CellManager();
		List<List<String>> lines =  new ArrayList<>();
		lines.addAll(Collections.singleton(Arrays.asList(Cell.EMPTY.toString(), Cell.EMPTY.toString(), Cell.EMPTY.toString())));

		Coordinate freeCell = cellManager.findRandomFreeCell(lines);
		assertThat(freeCell.horizontal()).isNotNegative();
		assertThat(freeCell.vertical()).isNotNegative();

	}

	@Test
	void collectAll() {
		List<List<String>> lines = new ArrayList<>();
		for (int index = 0; index < 3; index++) {
			List<String> line = Arrays.asList(Cell.EMPTY.toString(), Cell.EMPTY.toString(), Cell.EMPTY.toString());
			lines.add(line);
		}

		List<List<String>> allLines = CellManager.collectAll(lines);
		assertThat(allLines.size()).isEqualTo(8);

	}
}