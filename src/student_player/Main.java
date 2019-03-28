package student_player;

import boardgame.BoardState;
import boardgame.Move;
import pentago_swap.PentagoBoard;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoCoord;
import pentago_swap.PentagoMove;
import pentago_swap.PentagoBoardState.Quadrant;

public class Main {
	public static void main(String[] args) {
		PentagoBoard board = new PentagoBoard();
		PentagoBoardState boardState = (PentagoBoardState) board.getBoardState();
		
		// TL, TR, BL, BR
		PentagoMove move1 = new PentagoMove(new PentagoCoord(0, 0), Quadrant.TL, Quadrant.TR, 0);
		PentagoMove move2 = new PentagoMove(new PentagoCoord(5, 0), Quadrant.TR, Quadrant.BL, 1);
		PentagoMove move3 = new PentagoMove(new PentagoCoord(0, 2), Quadrant.TL, Quadrant.BR, 0);
		PentagoMove move4 = new PentagoMove(new PentagoCoord(1, 5), Quadrant.TR, Quadrant.BR, 1);
		PentagoMove move5 = new PentagoMove(new PentagoCoord(0, 4), Quadrant.TL, Quadrant.BL, 0);
		PentagoMove move6 = new PentagoMove(new PentagoCoord(4, 4), Quadrant.TL, Quadrant.BL, 1);
		PentagoMove move7 = new PentagoMove(new PentagoCoord(0, 3), Quadrant.TL, Quadrant.BL, 0);
		PentagoMove move8 = new PentagoMove(new PentagoCoord(4, 3), Quadrant.TL, Quadrant.BL, 1);
		PentagoMove move9 = new PentagoMove(new PentagoCoord(3, 1), Quadrant.TL, Quadrant.BL, 0);
		
		boardState.processMove(move1);
		boardState.processMove(move2);
		boardState.processMove(move3);
		boardState.processMove(move4);
		boardState.processMove(move5);
		/*boardState.processMove(move6);
		boardState.processMove(move7);
		boardState.processMove(move8);
		boardState.processMove(move9);*/
		
		Move myMove = MyTools.getMove(1, boardState);
		
		System.out.println("test");

	}
}
