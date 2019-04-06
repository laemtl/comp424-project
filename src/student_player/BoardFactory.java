package student_player;

import pentago_swap.PentagoBoard;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoCoord;
import pentago_swap.PentagoMove;
import pentago_swap.PentagoBoardState.Quadrant;

public class BoardFactory {
	public static PentagoMove createMove(int[] move, int player) {
		int x = move[0];
		int y = move[1];
		Quadrant q1 = Quadrant.values()[move[2]];
		Quadrant q2 = Quadrant.values()[move[3]];
		return new PentagoMove(new PentagoCoord(x, y), q1, q2, player);
	}
	
	public static PentagoBoardState createState(int[][] moves) {
		PentagoBoard board = new PentagoBoard();
		PentagoBoardState boardState = (PentagoBoardState) board.getBoardState();
		
		int player = 0;
		
		for (int i = 0; i < moves.length; i++) {
			boardState.processMove(BoardFactory.createMove(moves[i], player));
			player = player == 0 ? 1 : 0;
		}
		
		return boardState;
	}
}
