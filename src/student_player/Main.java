package student_player;


import pentago_swap.PentagoBoard;
import pentago_swap.PentagoMove;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoBoardState.Quadrant;
import pentago_swap.PentagoCoord;

public class Main {

	public static void main(String[] args) {
		// TL, TR, BL, BR
		// Study Case
		PentagoBoard board = new PentagoBoard();
		PentagoBoardState state = (PentagoBoardState) board.getBoardState();
		
		int tl = 0;
		int tr = 1;
		int bl = 2;
		int br = 3;
		int w = 0;
		int b = 1;

		PentagoBoardState state1 = BoardFactory
			.createState(new int[][] { 
				{ 1, 1, tl, tr }, 
				{ 1, 1, tl, tr }, 
				{ 4, 1, tl, tr }, 
				{ 4, 4, tl, tr },
				{ 0, 1, tl, tr }, 
				{ 2, 4, tl, tr },
			}
		);
		PentagoBoardState state2 = (PentagoBoardState) state1.clone();
			
		//PentagoMove move = new PentagoMove(new PentagoCoord(1, 0), Quadrant.TL, Quadrant.BR, 0);
		//state2.processMove(move);
		System.out.println(state1);
		int score3 = HeuristicFunction.compute(0, state2);
		System.out.println(score3);
		
		PentagoMove m = MyTools.getMove(0, state2);
		state2.processMove(m);
		
		PentagoBoardState state3 = (PentagoBoardState) state1.clone();
		PentagoMove m2 = new PentagoMove(new PentagoCoord(3, 1), Quadrant.TL, Quadrant.BR, 0);
		state3.processMove(m2);
		System.out.println(state3);
		int score4 = HeuristicFunction.compute(0, state3);
		System.out.println(score4);
		
		
	}
}
