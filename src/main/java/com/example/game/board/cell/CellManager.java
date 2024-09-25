package com.example.game.board.cell;

import com.example.game.board.Coordinate;
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
        return borders;
    }

}