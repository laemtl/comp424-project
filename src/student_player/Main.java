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
		/*PentagoMove move1 = new PentagoMove(new PentagoCoord(0, 0), Quadrant.TL, Quadrant.TR, 0);
		boardState.processMove(move1);
		
		//PentagoMove m1 = new PentagoMove(new PentagoCoord(0, 1), Quadrant.TR, Quadrant.TL, 1);
		//PentagoMove m2 = new PentagoMove(new PentagoCoord(0, 0), Quadrant.TR, Quadrant.TL, 0);
		
		//boardState.processMove(m1);
		
		//int v = HeuristicFunction.compute(1, boardState);
		//PBSTree bsTree = new PBSTree(boardState, null);
		
		//PBSTree c = bsTree.getMaxChildren();
		
		//Move myMove = MyTools.getMove(1, boardState);
		*/
		
		
		// Study Case
		
		PentagoMove move1 = new PentagoMove(new PentagoCoord(1, 1), Quadrant.TL, Quadrant.TR, 0);
		PentagoMove move2 = new PentagoMove(new PentagoCoord(0, 0), Quadrant.TL, Quadrant.TR, 1);
		PentagoMove move3 = new PentagoMove(new PentagoCoord(0, 1), Quadrant.TL, Quadrant.TR, 0);
		PentagoMove move4 = new PentagoMove(new PentagoCoord(1, 1), Quadrant.TL, Quadrant.BR, 1);
		PentagoMove move5 = new PentagoMove(new PentagoCoord(2, 4), Quadrant.TL, Quadrant.TR, 0);
		PentagoMove move6 = new PentagoMove(new PentagoCoord(2, 5), Quadrant.TL, Quadrant.TR, 1);
		
		boardState.processMove(move1);
		boardState.processMove(move2);	
		boardState.processMove(move3);
		boardState.processMove(move4);
		boardState.processMove(move5);
		boardState.processMove(move6);
		
		Move myMove = MyTools.getMove(0, boardState);
		
		

		
		/*PBSTree min = null;
		PBSTree bsTree = new PBSTree(boardState, null);
		for (PBSTree child : bsTree.getChildren()) {
			child.setScore(HeuristicFunction.compute(1, child.getState()));
		
	    	if (min == null) {
	    		min = child;
	    	} else if (child.getScore() > min.getScore()) {
	    		min = child;
	    	}
		}
		
		System.out.println("min score : " + min.getScore());
	    System.out.println(min.getState());*/	
	    
		/*boardState.processMove(move9); */
		//int score = HeuristicFunction.compute(1, boardState);
		
		System.out.println("Board state");
	    System.out.println(boardState);


	}
}
