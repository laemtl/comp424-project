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
import pentago_swap.PentagoMove;

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
				{ 2, 5, bl, br }, 
				{ 1, 4, tl, tr }, 
				{ 1, 2, tl, tr }, 
				{ 0, 5, tl, tr },
				{ 2, 0, tl, tr }, 
				{ 2, 4, tl, tr }, 
				{ 0, 5, tl, br },
				{ 4, 3, bl, br }, 
				{ 1, 5, tl, tr },
				{ 2, 2, tl, tr }, 
				{ 0, 4, tl, tr },
				{ 3, 1, tl, tr },
				{ 4, 4, tl, br }, 
				{ 3, 0, tl, br },
				{ 0, 1, tl, br }, 
				{ 4, 4, tl, br },
				{ 2, 1, tl, bl }, 
				{ 1, 3, tr, bl },
				{ 3, 4, tl, br }, 
				{ 1, 5, tl, bl }, 
				{ 1, 3, tl, br },
				{ 4, 0, tl, br }, 
				{ 3, 0, tl, br }
				//{ 5, 4, tr, br }
				//{ 3, 2, tl, tr }
				//{ 0, 0, tl, tr }
				//{ 3, 3, tl, tr }
			}
		);
		PentagoBoardState state2 = (PentagoBoardState) state1.clone();
		System.out.println(state2);

		PentagoMove myMove = MyTools2.getMove(1, state2);
		int score = HeuristicFunction.compute(1, MyTools2.applyMove(myMove, state2));
		
		System.out.println("selected score : " + score);
		System.out.println(MyTools2.applyMove(myMove, state2));			
		System.out.println("Move : " + myMove.getMoveCoord().getX() + " " + myMove.getMoveCoord().getY());
	}
}
