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
		// Study Case
		
		PentagoMove move1 = new PentagoMove(new PentagoCoord(0, 0), Quadrant.TL, Quadrant.TR, 0);
		PentagoMove move2 = new PentagoMove(new PentagoCoord(1, 1), Quadrant.TL, Quadrant.BL, 1);
		PentagoMove move3 = new PentagoMove(new PentagoCoord(1, 4), Quadrant.TL, Quadrant.TR, 0);
		PentagoMove move4 = new PentagoMove(new PentagoCoord(0, 1), Quadrant.TL, Quadrant.TR, 1);
		PentagoMove move5 = new PentagoMove(new PentagoCoord(2, 5), Quadrant.TL, Quadrant.TR, 0);
		
		
		boardState.processMove(move1);
		boardState.processMove(move2);	
		boardState.processMove(move3);	
		boardState.processMove(move4);	
		boardState.processMove(move5);	


		Move myMove = MyTools.getMove(1, boardState);
		int v= HeuristicFunction.compute(1, boardState);
		
		System.out.println("Board state");
	    System.out.println(boardState);
	    System.out.println(v);


	}
}



/*child score : 30
--------------------------
|   |   |   || w | b |   |
|   | b |   ||   | w |   |
|   |   |   ||   |   |   |
--------------------------
| w |   |   ||   |   |   |
|   |   |   ||   |   |   |
|   |   |   ||   |   |   |
--------------------------*/
