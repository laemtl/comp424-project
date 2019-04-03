package student_player;

import java.util.ArrayList;
import java.util.List;

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
		int tl = 0;
		int tr = 1;
		int bl = 2;
		int br = 3;
		int w = 0;
		int b = 1;
		
		PentagoBoardState state1 = BoardFactory.createState(new int[][]{
			{1, 1, tl, tr},
			{0, 0, tl, tr},
			{0, 0, tl, tr},
			{2, 5, tl, br},
			{0, 4, tl, tr},
			{1, 2, tl, tr},
			{0, 5, tl, tr},
			{0, 4, tl, tr},
		});
		PentagoBoardState state2 = (PentagoBoardState)state1.clone();

		state1.processMove(BoardFactory.createMove(new int[] {2, 4, tl, tr}, w));
		state2.processMove(BoardFactory.createMove(new int[] {3, 1, tl, bl}, w));

		System.out.println(String.format("Score: %d", HeuristicFunction.compute(0, state1)));
	    System.out.println(state1);

		System.out.println(String.format("Score: %d", HeuristicFunction.compute(0, state2)));
	    System.out.println(state2);

	    Node<Integer> node = new Node<>(0, () -> {
	    	List<Node<Integer>> children = new ArrayList<>();
	    	children.add(new Node<>(1, () -> new ArrayList<>()));
	    	children.add(new Node<>(2, () -> new ArrayList<>()));
	    	children.add(new Node<>(3, () -> new ArrayList<>()));
	    	return children;
	    });
	    
	    String result = node.reduce((value, folds) -> {
	    	String str = "";
	    	for (String fold: folds) {
	    		str += (str.length() == 0 ? "" : ", ") + fold;
	    	}
	    	str = str.length() != 0 ? String.format(", children: {%s}", str) : str;
	    	return String.format("Node(%s%s)", value.toString(), str);
	    });
	    System.out.println(result);
	}
}
