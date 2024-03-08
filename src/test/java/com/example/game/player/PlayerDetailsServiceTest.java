package com.example.game.player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.Principal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PlayerDetailsServiceTest {

	private static final String USERNAME = "test";
	private static final String PASSWORD = "password";

	private PlayerDetailsService service;

	private Player player;

	@Mock
	private PlayerRepository repository;

	@BeforeEach
	void setUp() {
		service = new PlayerDetailsService(repository);
		player = new Player();
		player.setUsername(USERNAME);
		player.setPassword(PASSWORD);
	}

	@Test
	void loadUserByUsername() {
		when(repository.findByUsername(anyString())).thenReturn(player);

		UserDetails userDetails = service.loadUserByUsername(player.getUsername());

		verify(repository).findByUsername(player.getUsername());

		assertThat(userDetails.getUsername()).isEqualTo(player.getUsername());
		assertThat(userDetails.getPassword()).isEqualTo(player.getPassword());
		assertThat(userDetails.getAuthorities()).hasSize(1);

		GrantedAuthority actualAuthority = userDetails.getAuthorities().iterator().next();
		assertThat(actualAuthority.getAuthority()).isEqualTo(PlayerDetailsService.USER_AUTHORITY);
	}

	@Test
	void loadUserByUsernameWithInvalidUsername() {
		when(repository.findByUsername(anyString())).thenReturn(null);

		assertThatThrownBy(() -> service.loadUserByUsername(player.getUsername()))
				.isInstanceOf(UsernameNotFoundException.class)
				.hasMessage("Invalid username: %s", player.getUsername());
	}

	@Test
	void extractInvalidPlayer() {
		when(repository.findByUsername(anyString())).thenReturn(null);
		Principal principal = () -> player.getUsername();
		assertThatThrownBy(() -> service.extractPlayer(principal.getName()))
				.isInstanceOf(UsernameNotFoundException.class)
				.hasMessage("Invalid username: %s", player.getUsername());
	}


	@Test
	void extractPlayer() {
		when(repository.findByUsername(anyString())).thenReturn(player);
		Principal principal = () -> player.getUsername();
		Player result = service.extractPlayer(principal.getName());
		assertThat(result.getUsername()).isEqualTo(player.getUsername());
		assertThat(result.getPassword()).isEqualTo(player.getPassword());
		assertThat(result.getType()).isEqualTo(player.getType());
	}

	@Test
	void save() {
		when(repository.save(any())).thenReturn(player);
		Player result = service.save(player);
		verify(repository).save(player);
	}


}