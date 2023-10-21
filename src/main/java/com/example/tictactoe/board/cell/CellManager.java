package com.example.tictactoe.board.cell;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class CellManager {

    private static final int SIDE = 3;

    public List<List<String>> init() {
        List<List<String>> board = new ArrayList<>();

        for (int index = 0; index < SIDE; index++) {
            List<String> line = Arrays.asList(Cell.EMPTY.toString(), Cell.EMPTY.toString(), Cell.EMPTY.toString());
            board.add(line);
        }

        return board;
    }

    public String findRandomFreeCell(List<List<String>> lines) {
        List<String> freeCells = IntStream.range(0, lines.size())
                .boxed()
                .flatMap(rowIndex -> IntStream.range(0, lines.get(0).size())
                        .mapToObj(columnIndex -> rowIndex + ":" + columnIndex))
                .filter(cell -> lines.get(Integer.parseInt(cell.split(":")[0])).get(Integer.parseInt(cell.split(":")[1])).isEmpty())
                .toList();

        if (freeCells.isEmpty()) {
            return Cell.X.toString();
        }

        int random = new Random().nextInt(freeCells.size());

        return freeCells.get(random);

    }

    public static List<List<String>> collectAll(List<List<String>> lines) {
        List<List<String>> borders = IntStream.range(0, SIDE)
                .mapToObj(index -> new ArrayList<>(lines.get(index)))
                .collect(Collectors.toList());

        borders.addAll(IntStream.range(0, SIDE)
                .mapToObj(index -> lines.stream()
                        .map(row -> row.get(index))
                        .collect(Collectors.toList()))
                .toList());

        borders.add(IntStream.range(0, SIDE)
                .mapToObj(rowIndex -> lines.get(rowIndex).get(rowIndex))
                .collect(Collectors.toList()));

        borders.add(IntStream.range(0, SIDE)
                .mapToObj(rowIndex -> lines.get(rowIndex).get(SIDE - 1 - rowIndex))
                .collect(Collectors.toList()));
        return lines;
    }
}