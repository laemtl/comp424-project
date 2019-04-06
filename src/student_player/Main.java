package student_player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import boardgame.BoardState;
import boardgame.Move;
import pentago_swap.PentagoBoard;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoCoord;
import pentago_swap.PentagoMove;
import pentago_swap.PentagoBoardState.Quadrant;

public class Main {

	public static void main(String[] args) {
		// TL, TR, BL, BR
		// Study Case
		PentagoBoard board = new PentagoBoard();
		PentagoBoardState state = (PentagoBoardState) board.getBoardState();
		
		PentagoMove move1 = new PentagoMove(new PentagoCoord(0, 0), Quadrant.TL, Quadrant.TR, 0);
		PentagoMove move2 = new PentagoMove(new PentagoCoord(1, 1), Quadrant.TL, Quadrant.BL, 1);
		PentagoMove move3 = new PentagoMove(new PentagoCoord(1, 4), Quadrant.TL, Quadrant.TR, 0);
		PentagoMove move4 = new PentagoMove(new PentagoCoord(3, 1), Quadrant.TL, Quadrant.TR, 1);
		PentagoMove move5 = new PentagoMove(new PentagoCoord(2, 2), Quadrant.TL, Quadrant.TR, 1);
		PentagoMove move6 = new PentagoMove(new PentagoCoord(2, 5), Quadrant.TL, Quadrant.TR, 0);
		//PentagoMove move4 = new PentagoMove(new PentagoCoord(0, 1), Quadrant.TL, Quadrant.TR, 1);
		//PentagoMove move5 = new PentagoMove(new PentagoCoord(2, 5), Quadrant.TL, Quadrant.TR, 0);
		
		
		state.processMove(move1);
		state.processMove(move2);	
		state.processMove(move3);	
		//state.processMove(move4);	
		//state.processMove(move5);	


		PentagoMove myMove = MyTools.getMove(1, state);
		//int v= HeuristicFunction.compute(1, state);
		//int score = HeuristicFunction.compute(1, MyTools2.applyMove(myMove, state));
	
		
		//System.out.println("selected score : " + score);
		System.out.println(MyTools2.applyMove(myMove, state));			
		System.out.println("Move : " + myMove.getMoveCoord().getX() + " " + myMove.getMoveCoord().getY());
		
		System.out.println("Board state");
	    System.out.println(state);
	    //System.out.println(v);

		/*System.out.println(HeuristicFunction.compute(1, MyTools2.applyMove(move4, boardState)));
		System.out.println(MyTools2.applyMove(move4, boardState));

		System.out.println(HeuristicFunction.compute(1, MyTools2.applyMove(move5, boardState)));
		System.out.println(MyTools2.applyMove(move5, boardState));

		System.out.println(HeuristicFunction.compute(1, MyTools2.applyMove(move6, MyTools2.applyMove(move4, boardState))));
		System.out.println(MyTools2.applyMove(move6, MyTools2.applyMove(move4, boardState)));
		
		
		
		System.out.println("Board state");
	    System.out.println(boardState);
	    System.out.println(v);*/
	    
	    /*
		int tl = 0;
		int tr = 1;
		int bl = 2;
		int br = 3;
		int w = 0;
		int b = 1;

		PentagoBoardState state1 = BoardFactory
			.createState(new int[][] { 
				{ 1, 1, tl, tr }, 
				{ 0, 0, tl, tr }, 
				{ 0, 0, tl, tr }, 
				{ 2, 5, tl, br },
				{ 0, 4, tl, tr }, 
				{ 1, 2, tl, tr }, 
				{ 0, 5, tl, tr }, 
				{ 0, 4, tl, tr }
			}
		);
		PentagoBoardState state2 = (PentagoBoardState) state1.clone();

		//state1.processMove(BoardFactory.createMove(new int[] { 2, 4, tl, tr }, w));
		//state2.processMove(BoardFactory.createMove(new int[] { 3, 1, tl, bl }, w));

		System.out.println(String.format("Score: %d", HeuristicFunction.compute(0, state1)));
		System.out.println(state1);

		System.out.println(String.format("Score: %d", HeuristicFunction.compute(0, state2)));
		System.out.println(state2);
		*/
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
