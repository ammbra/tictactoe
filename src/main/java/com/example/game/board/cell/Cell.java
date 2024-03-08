package com.example.game.board.cell;


public enum Cell {

	EMPTY(""),
	X("x"),
	O("o");

	private final String content;

	Cell(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return content;
	}
}
