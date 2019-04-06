package student_player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.ToIntFunction;

import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;

public class MyTools2 {
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
    
	public static PentagoMove getMove(int player_id, PentagoBoardState state) {
		int depth = 2;
		return MyTools2.minimax(player_id, state, depth).moves.get(0);
	}
    
	public static MinimaxResult minimax(int player_id, PentagoBoardState state, int depth) {
		ToIntFunction<PentagoBoardState> heuristic = state2 -> HeuristicFunction.compute(player_id, state2);
		MinimaxResult result = MyTools2.minimaxHelper(heuristic, true, depth, state, new ArrayList<PentagoMove>(), Integer.MIN_VALUE, Integer.MAX_VALUE);
		
		System.out.println("AB count:" + count);
		return result;
	}
	

	static int count = 0;
	private static MinimaxResult minimaxHelper(ToIntFunction<PentagoBoardState> heuristic, boolean isMaximizing, int depth, PentagoBoardState state, List<PentagoMove> previousMoves, int alpha, int beta) {		
		List<PentagoMove> moves = state.getAllLegalMoves();
		/*if (depth == 1) {
			List<PentagoMove> moves = state.getAllLegalMoves();
			//moves = MyTools2.randomSubList(2, allMoves);
			for (PentagoMove move: allMoves) {
				if (heuristic.applyAsInt(MyTools2.applyMove(move, state)) <= 1970) {
					moves.add(move);
				}
				if (moves.size() >= 4) {
					break;
				}
			}
		} else {
			moves = MyTools2.randomSubList(3, state.getAllLegalMoves());
		}*/
		//List<PentagoMove> moves = state.getAllLegalMoves();
		
		if (depth == 0 || moves.isEmpty()) {
			MinimaxResult result = new MinimaxResult(heuristic.applyAsInt(state), state, MyTools2.cloneList(previousMoves));

			/*
			System.out.println("Depth:" + depth);
			System.out.println(result);
			System.out.println();
			//*/
			
	    	return result;
	    	
	    } else {
	    	MinimaxResult result = null;
	    	for (PentagoMove move: moves) {
	    		count++;
	    		List<PentagoMove> newMoves = MyTools2.cloneList(previousMoves);
	    		newMoves.add(move);
	    		PentagoBoardState newState = (PentagoBoardState)state.clone();
	    		newState.processMove(move);
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

	    	/*
			System.out.println("Depth:" + depth);
			System.out.println(result);
			System.out.println();
	    	//*/
	    	
	    	return result;
	    	
	    }
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
