package com.example.aquile.mazeescape.model;

/**
 * Superclass for all games. 
* 
 * You should subclass this for your own game. In that subclass you will keep
 * track of all game-related state, like the score, who's turn it is, etc.
 * 
 * @author Paul de Groot
 */
public abstract class Game {
	private GameBoard gameBoard;

	/**
	 * Called when you create a new game.
	 * @param gameBoard
	 */
	public Game(GameBoard gameBoard) {
		this.gameBoard = gameBoard;
		gameBoard.setGame(this);
	}

	/** Returns a reference to the game board. */
	public GameBoard getGameBoard() {
		return gameBoard;
	}
}
