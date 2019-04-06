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
	
	public static PentagoBoardState applyMove(PentagoMove move, PentagoBoardState state) {
		PentagoBoardState newState = (PentagoBoardState)state.clone();
		newState.processMove(move);
		return newState;
	}
    
	public static PentagoMove getMove(int player_id, PentagoBoardState state) {
		int depth = 2;
		
		ToIntFunction<PentagoBoardState> heuristic = state2 -> HeuristicFunction.compute(player_id, state2);
		PentagoMove bestMove = null;
		MinimaxResult bestResult = null;
		int bestScore = Integer.MIN_VALUE;
		
		for (PentagoMove move: state.getAllLegalMoves()) {
    		PentagoBoardState newState = MyTools2.applyMove(move, state);
    		MinimaxResult result = MyTools2.minimax(heuristic, -1, depth - 1, newState, Integer.MIN_VALUE, Integer.MAX_VALUE);
			if (result.score > bestScore) {
				bestScore = result.score;
				bestMove = move;
				bestResult = result;
			}
		}

		System.out.println(bestResult.move.toPrettyString());
		System.out.println(MyTools2.applyMove(bestResult.move, bestResult.state));
		System.out.println(bestMove.toPrettyString());
    	System.out.println(count);
    	
		return bestMove;
	}
	
	static int count = 0;
	public static MinimaxResult minimax(ToIntFunction<PentagoBoardState> heuristic, int multiple, int depth, PentagoBoardState state, int alpha, int beta) {
		List<PentagoMove> moves = state.getAllLegalMoves();
		
		if (depth == 0 || moves.isEmpty()) {
			MinimaxResult result = new MinimaxResult(heuristic.applyAsInt(state), null, state);
	    	return result;
	    	
	    } else {
	    	MinimaxResult result = new MinimaxResult(Integer.MIN_VALUE, null, state);
	    	for (PentagoMove move: moves) {
	    		count++;
	    		PentagoBoardState newState = (PentagoBoardState)state.clone();
	    		newState.processMove(move);
	    		MinimaxResult newResult = MyTools2.minimax(heuristic, -multiple, depth - 1, newState, alpha, beta);
	    		
	    		
	    		int newScore = multiple * newResult.score;
	    		
	    		
	    		if (newScore > result.score) {
	    			result.score = newScore;
	    			result.move = move;
	    		}
	    		
	    		// max case
	    		if(multiple > 0) {
	    			alpha = Math.max(alpha, result.score);
	    		} else {
	    			beta = Math.min(beta, result.score);
	    		}
	    		
	    		//if(beta <= alpha) break;
	    	}
			//map.put(key, result);
	    	return result;
	    	
	    }
	}
}

class MinimaxResult {
	int score;
	PentagoMove move;
	/**
	 * Represents the state before the move
	 */
	PentagoBoardState state;
	public MinimaxResult(int score, PentagoMove move, PentagoBoardState state) {
		super();
		this.score = score;
		this.move = move;
		this.state = state;
	}
}
