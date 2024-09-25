package com.example.game.board;

import com.example.game.board.cell.Cell;
import com.example.game.board.cell.CellManager;
import com.example.game.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    private final CellManager cellManager;

    @Autowired
    public BoardService(BoardRepository boardRepository, CellManager cellManager) {
        this.boardRepository = boardRepository;
        this.cellManager = cellManager;
    }

    @Transactional
    public Board findPreviousMatch(Player player) {
        return boardRepository.findFirstByPlayerOrderByCreatedOnDesc(player);
    }


    @Transactional
    public Board build(Player player, Cell option) {
        Board board = new Board();
        board.setPlayer(player);
        board.setOption(option);
        board.setStatus(Status.ACTIVE);
        board.setNext(Player.Type.AI);
        board.setLines(cellManager.init());
        boardRepository.save(board);
        return board;
    }


    public void randomMove(Board board) {
        List<List<String>> lines = board.getLines();
        List<Coordinate> freeCells = IntStream.range(0, lines.size())
                .boxed()
                .flatMap(rowIndex -> IntStream.range(0, lines.getFirst().size())
                        .mapToObj(columnIndex -> new Coordinate(rowIndex, columnIndex)))
                .filter(cell -> lines.get(cell.horizontal()).get(cell.vertical()).isEmpty())
                .toList();
        if (!freeCells.isEmpty()) {
            int random = new Random().nextInt(freeCells.size());
            move(board, freeCells.get(random));
        }
    }

    public void move(Board board, Coordinate location) {
        if (board.getStatus() != Status.ACTIVE) {
            return;
        }

        Cell cell;
        if (board.getNext().equals(Player.Type.PERSON)) {
            cell = Cell.X;
            board.setNext(Player.Type.AI);
        } else {
            cell = Cell.O;
            board.setNext(Player.Type.PERSON);
        }

        board.getLines().get(location.horizontal()).set(location.vertical(), cell.toString());

        Status nextState = checkStatus(board);
        board.setStatus(nextState);

        boardRepository.save(board);
    }

    private Status checkStatus(Board board) {
        List<List<String>> lines = board.getLines();
        for (List<String> line : CellManager.collectAll(lines)) {
            String firstCell = line.getFirst();
            if (firstCell.isEmpty()) {
                continue;
            }

            if (line.stream().allMatch(cell -> cell.equals(firstCell)) ) {
                board.setNext(Player.Type.NONE);
                return firstCell.equals(board.getOption().toString()) ? Status.PERSON_WIN : Status.AI_WIN;
            }
        }
        
        for (List<String> line : lines) {
            if (line.stream().anyMatch(String::isEmpty)) {
                return Status.ACTIVE;
            }
        }

        return Status.TIE;
    }
}