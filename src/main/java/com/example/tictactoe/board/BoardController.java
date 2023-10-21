package com.example.tictactoe.board;


import java.security.Principal;
import java.util.List;
import java.util.Objects;

import com.example.tictactoe.board.cell.Cell;
import com.example.tictactoe.player.Player;
import com.example.tictactoe.player.PlayerDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BoardController {

	private static final String INDEX = "index";

	private final BoardService boardService;

	private final PlayerDetailsService playerDetailsService;


	@Autowired
	public BoardController(BoardService boardService, PlayerDetailsService playerDetailsService) {
		this.boardService = boardService;
		this.playerDetailsService = playerDetailsService;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Principal principal, Model model) {
		Player player = playerDetailsService.extractPlayer(principal);

		Board board = boardService.findPreviousGame(player);

		if (Objects.isNull(board)) {
			player.setType(Player.Type.HUMAN);
			board = boardService.build(player, Cell.O);
		}

		placeGameAttributes(model, board);
		return INDEX;
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String play(Model model, Principal principal, @RequestParam("location") String location,
					   @RequestParam(value = "again", required = false, defaultValue = "false") boolean playAgain) {
		Player player = playerDetailsService.extractPlayer(principal);

		if (playAgain) {
			Board board = boardService.build(player, Cell.X);
			boardService.move(board, "1:1");
			placeGameAttributes(model, board);
		} else {
			Board board = boardService.findPreviousGame(player);
			boardService.move(board, location);
			boardService.randomMove(board);
			placeGameAttributes(model, board);
		}

		return INDEX;
	}

	private void placeGameAttributes(Model model, Board board) {
		boolean humanTurn = (Player.Type.HUMAN == board.getNext());
		Game game = new Game(board.getLines(), board.getStatus(), humanTurn);
		model.addAttribute("game", game);
	}

}

record Game(List<List<String>> lines, Status status, boolean humanTurn) { }