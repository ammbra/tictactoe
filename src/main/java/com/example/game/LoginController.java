package com.example.game;

import com.example.game.player.Player;
import com.example.game.player.PlayerDetailsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

	public static final String LOGIN = "login";
	public static final String REGISTER = "register";

	private final PlayerDetailsService playerDetailsService;

	@Autowired
	public LoginController(PlayerDetailsService playerDetailsService) {
		this.playerDetailsService = playerDetailsService;
	}

	@RequestMapping(value="/user", method = RequestMethod.GET)
	public String redirect() {
		return LOGIN;
	}

	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String login() {
		return LOGIN;
	}

	@RequestMapping(value="/register", method = RequestMethod.GET)
	public String register() {
		return REGISTER;
	}

	@RequestMapping(value="/register", method = RequestMethod.POST)
	public String register(@Valid Player player, Model model) {
		try {
			playerDetailsService.loadUserByUsername(player.getUsername());
			model.addAttribute("invalid", "Username already acquired. Please use another username");
			return REGISTER;
		} catch (UsernameNotFoundException ex) {
			playerDetailsService.save(player);
			return LOGIN;
		}

	}
}
