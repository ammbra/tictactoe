package com.example.game.player;


import jakarta.persistence.*;

@Entity
public class Player {

	public enum Type {
		PERSON,
		NONE,
		AI;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	private String username;

	private String password;

	@Enumerated(EnumType.STRING)
	private Type type;


	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

}