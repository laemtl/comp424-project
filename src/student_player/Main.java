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
				{ 0, 1, tl, bl },
				{ 5, 1, tl, tr },
				{ 4, 4, tl, tr },
				{ 5, 0, tl, tr },
				{ 5, 2, tl, tr },
				{ 3, 2, tl, tr },
				{ 3, 0, tl, tr },
				
				{ 2, 1, tr, br },
				
				{ 2, 5, tl, bl },
				{ 3, 1, tl, bl },
				
				{ 5, 4, tl, br },
				{ 3, 3, tl, br },
				
				{ 3, 4, bl, br },
				{ 0, 2, bl, br },
				
				{ 0, 3, tr, br },
				{ 2, 0, tr, br },
				
				{ 3, 5, tl, tr },
				{ 3, 3, tl, tr },
				
				{ 2, 2, bl, br },
				{ 1, 0, bl, br },
				
				{ 4, 3, tl, tr },
				{ 4, 0, tl, tr },
				{ 5, 5, tl, tr },
				{ 1, 5, tl, tr }
			}
		);
		PentagoBoardState state2 = (PentagoBoardState) state1.clone();
			
		//PentagoMove move = new PentagoMove(new PentagoCoord(1, 0), Quadrant.TL, Quadrant.BR, 0);
		//state2.processMove(move);
		System.out.println(state1);
		int score3 = HeuristicFunction.compute(1, state2);
		System.out.println(score3);
		
		PentagoMove m = MyTools.getMove(1, state2);
		System.out.println(m.toPrettyString());
		state2.processMove(m);
		int score4 = HeuristicFunction.compute(1, state2);
		System.out.println("Score : " + score4);		
		PentagoMove move = new PentagoMove(new PentagoCoord(4, 2), Quadrant.TL, Quadrant.BR, 1);
		
		PentagoBoardState state3 = (PentagoBoardState) state1.clone();
		state3.processMove(move);
		int score5 = HeuristicFunction.compute(1, state2);
		System.out.println("Score : " + score5);
		System.out.println(state3);
		
		
		PentagoMove m2 = MyTools.getMove(0, state3);
		System.out.println(m2.toPrettyString());
		state3.processMove(m2);
		int score6 = HeuristicFunction.compute(1, state3);
		System.out.println("Score : " + score6);		

		
	}
}
