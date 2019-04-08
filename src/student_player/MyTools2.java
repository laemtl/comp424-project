package student_player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.ToIntFunction;

import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoCoord;
import pentago_swap.PentagoMove;
import pentago_swap.PentagoBoardState.Piece;
import pentago_swap.PentagoBoardState.Quadrant;

public class MyTools2 {
	static HashMap<String, PentagoBoardState> processedStates;
	static HashMap<String, Integer> processedScores;
			
	public static <A> List<A> cloneList(List<A> list) {
		List<A> copy = new ArrayList<>();
		for (A a: list) {
			copy.add(a);
		}
		return copy;
	}
	
	public static <A> List<A> randomSubList(int n, List<A> list) {
		Random random = new Random();
		List<A> subList = new ArrayList<>();
		while (list.size() > 0 && subList.size() < n) {
			int index = random.nextInt(list.size());
			A a = list.get(index);
			list.remove(index);
			subList.add(a);
		}
		return subList;
	}
	
	public static <A> List<A> subList(int n, List<A> list) {
		List<A> subList = new ArrayList<>();
		int index = 0;
		while (list.size() > 0 && subList.size() < n) {
			A a = list.get(index);
			list.remove(index);
			subList.add(a);
			index+=5;
		}
		return subList;
	}

	public static String movesToString(List<PentagoMove> moves) {
		String result = "";
		for (PentagoMove move: moves) {
			result += move.toPrettyString() + "\n";
		}
		return result;
	}
	
	public static PentagoBoardState applyMove(PentagoMove move, PentagoBoardState state) {
		PentagoBoardState newState = (PentagoBoardState)state.clone();
		newState.processMove(move);
		return newState;
	}
    
	public static PentagoMove getMove(int playerColor, PentagoBoardState state) {
		processedStates = new HashMap<>();
		processedScores = new HashMap<>();
				
		for (PentagoMove m : state.getAllLegalMoves()) {
			
			PentagoBoardState newState = (PentagoBoardState)state.clone();
			newState.processMove(m);
			processedStates.put(m.toPrettyString() + state.toString(), newState);
			
			if(newState.getWinner() == playerColor) {
				return m;
			}
		}
		
		int depth = 2;
		if(state.getTurnNumber() > 10) depth = 3;		
		return MyTools2.minimax(playerColor, state, depth).moves.get(0);
	}
    
	public static MinimaxResult minimax(int playerColor, PentagoBoardState state, int depth) {
		ToIntFunction<PentagoBoardState> heuristic = state2 -> HeuristicFunction.compute(playerColor, state2);
		MinimaxResult result = MyTools2.minimaxHelper(heuristic, true, depth, state, new ArrayList<PentagoMove>(), Integer.MIN_VALUE, Integer.MAX_VALUE);
		
		System.out.println(result);
		return result;
	}
	
	private static MinimaxResult minimaxHelper(ToIntFunction<PentagoBoardState> heuristic, boolean isMaximizing, int depth, PentagoBoardState state, List<PentagoMove> previousMoves, int alpha, int beta) {		
		List<PentagoMove> moves = state.getAllLegalMoves();
		if (depth == 0 || moves.isEmpty() || state.gameOver()) {	  
			Integer score = processedScores.get(state.toString());
			if(score == null) score = heuristic.applyAsInt(state);
			MinimaxResult result = new MinimaxResult(score, state, MyTools2.cloneList(previousMoves));
			
			
			/*
			System.out.println("Depth:" + depth);
			System.out.println(result);
			System.out.println();
			//*/
			
	    	return result;
	    	
	    } else {
	    	MinimaxResult result = null;
	    	for (PentagoMove move: moves) { 	
	    		List<PentagoMove> newMoves = MyTools2.cloneList(previousMoves);
	    		newMoves.add(move);
	    		
	    		PentagoBoardState newState = processedStates.get(move.toPrettyString() + state.toString());
	    		if(newState == null) {
		    		newState = (PentagoBoardState)state.clone();
		    		newState.processMove(move);
	    		}
	    		
	    		MinimaxResult newResult = MyTools2.minimaxHelper(heuristic, !isMaximizing, depth - 1, newState, newMoves, alpha, beta);
	    		
	    		if (result == null || (!isMaximizing && newResult.score < result.score) || (isMaximizing && newResult.score > result.score)) {
	    			result = newResult.clone();
	    		}
	    		
	    		// max case
	    		if(isMaximizing) {
	    			alpha = Math.max(alpha, result.score);
	    		} else {
	    			beta = Math.min(beta, result.score);
	    		}
	    		
	    		if(beta <= alpha) break;
	    	}

	    	//*
	    	if(depth == 2) {	
	    		for (PentagoMove m : result.moves) {
					if(m.toPrettyString().equals("Player 1, Move: (3, 0), Swap: (TL, BR)")) {
						
						System.out.println("--------------- yolo");
			    		
			    		System.out.println("Depth:" + depth);
			    		System.out.println(result);
			    		System.out.println("----------------yolo");
			    		System.out.println();
			    		
					}
	    		}
	    	}
	    	//*/
	    	
	    	return result;
	    	
	    }
	}

	public static List<PentagoMove> getPossibleMoves(PentagoBoardState state) {
		List<PentagoMove> legalMoves = new ArrayList<>();
	    for (int i = 0; i < PentagoBoardState.BOARD_SIZE; i++) { //Iterate through positions on board
	        for (int j = 0; j < PentagoBoardState.BOARD_SIZE; j++) {
	            if (state.getPieceAt(i, j) == Piece.EMPTY) {
	            	
	            	// Careful, default Quadrant values
	            	legalMoves.add(new PentagoMove(i, j, Quadrant.TL, Quadrant.TR, state.getTurnPlayer()));
	            }
	        }
	    }
	    return legalMoves;
	}
}

class MinimaxResult {
	int score;
	List<PentagoMove> moves = new ArrayList<>();
	/**
	 * Represents the back propagated state
	 */
	PentagoBoardState state;
	
	public MinimaxResult(int score, PentagoBoardState state, List<PentagoMove> moves) {
		this.score = score;
		this.moves = moves;
		this.state = state;
	}
	
	public String toString() {
		String result = "";
		result += "Score:" + score + "\n";
		result += MyTools2.movesToString(moves) + "\n";
		result += state.toString();
		return result;
	}
	
	public MinimaxResult clone() {
		return new MinimaxResult(score, (PentagoBoardState)state.clone(), MyTools2.cloneList(moves));
	}
}
