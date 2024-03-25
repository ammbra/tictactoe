package com.example.game.board;


import java.security.Principal;
import java.util.List;
import java.util.Objects;

import com.example.game.board.cell.Cell;
import com.example.game.player.Player;
import com.example.game.player.PlayerDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BoardController {

	private static final String INDEX = "index";

	private final BoardService boardService;

	private final PlayerDetailsService playerDetailsService;

	private record Match(String coordinate, boolean again) {}

	@Autowired
	public BoardController(BoardService boardService, PlayerDetailsService playerDetailsService) {
		this.boardService = boardService;
		this.playerDetailsService = playerDetailsService;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Principal principal, Model model) {
		Player player = playerDetailsService.extractPlayer(principal.getName());

		Board board = boardService.findPreviousMatch(player);

		if (Objects.isNull(board)) {
			player.setType(Player.Type.PERSON);
			board = boardService.build(player, Cell.O);
		}

		placeGameAttributes(model, board);
		return INDEX;
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String play(Model model, Principal principal, Match match) {
		Player player = playerDetailsService.extractPlayer(principal.getName());

		Board board;
		if (match.again()) {
			board = boardService.build(player, Cell.X);
			boardService.move(board, new Coordinate(1,1));
		} else {
			String[] coordinates = match.coordinate.split(":");
			Coordinate coordinate = new Coordinate(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
			board = boardService.findPreviousMatch(player);
			boardService.move(board, coordinate);
			if (board.getLines().size() > 0)
				boardService.randomMove(board);
		}

		placeGameAttributes(model, board);

		return INDEX;
	}

	private void placeGameAttributes(Model model, Board board) {
		record Game(List<List<String>> lines, Status status, boolean humanTurn) { }

		boolean humanTurn = (Player.Type.PERSON == board.getNext());
		Game game = new Game(board.getLines(), board.getStatus(), humanTurn);
		model.addAttribute("game", game);
	}

}


