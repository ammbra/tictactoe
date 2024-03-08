package com.example.game.player;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Component
public class PlayerDetailsService implements UserDetailsService {

	public static final String USER_AUTHORITY = "USER";

	public static final PasswordEncoder CRYPT_PASSWORD_ENCODER = new BCryptPasswordEncoder();


	private final PlayerRepository repository;

	@Autowired
	public PlayerDetailsService(PlayerRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Player player = extractPlayer(username);
		List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(USER_AUTHORITY);
		return new User(player.getUsername(), player.getPassword(), authorities);
	}

	public Player extractPlayer(String username) {
		Player player = repository.findByUsername(username);
		if (Objects.isNull(player)) {
			throw new UsernameNotFoundException(String.format("Invalid username: %s", username));
		}
		return player;
	}

	public Player save(Player player) {
		String encodedPassword = CRYPT_PASSWORD_ENCODER.encode(player.getPassword());
		player.setType(Player.Type.PERSON);
		player.setPassword(encodedPassword);
		return repository.save(player);
	}
}