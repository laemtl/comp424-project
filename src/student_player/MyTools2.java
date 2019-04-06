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
		Map<String, MinimaxResult> map = new HashMap<>();
		ToIntFunction<PentagoBoardState> heuristic = state2 -> HeuristicFunction.compute(player_id, state2);
		MinimaxResult result = MyTools2.minimax(heuristic, 1, depth, state, map);
		
		return result.move;
	}
	
	public static MinimaxResult minimax(ToIntFunction<PentagoBoardState> heuristic, int multiple, int depth, PentagoBoardState state, Map<String, MinimaxResult> map) {
		String key = state.toString();
		if (map.containsKey(key)) {
			return map.get(key);
		}
		
		List<PentagoMove> moves = MyTools2.randomSubList(2, state.getAllLegalMoves());
		
		if (depth == 0 || moves.isEmpty()) {
			MinimaxResult result = new MinimaxResult(heuristic.applyAsInt(state), null, state);
			System.out.println("Depth:" + depth);
			System.out.println("Score:" + heuristic.applyAsInt(state));
			System.out.println("No move:");
			System.out.println(state);
			System.out.println();
			//map.put(key, result);
	    	return result;
	    } else {
	    	MinimaxResult result = new MinimaxResult(Integer.MIN_VALUE, null, state);
	    	for (PentagoMove move: moves) {
	    		PentagoBoardState newState = (PentagoBoardState)state.clone();
	    		newState.processMove(move);
	    		MinimaxResult newResult = MyTools2.minimax(heuristic, -multiple, depth - 1, newState, map);
	    		int newScore = multiple * newResult.score;
	    		if (newScore > result.score) {
	    			result.score = newScore;
	    			result.move = move;
	    			result.state = newResult.state;
	    		}
	    	}
			System.out.println("Depth:" + depth);
			System.out.println("Score:" + heuristic.applyAsInt(MyTools2.applyMove(result.move, state)));
			System.out.println(result.move.toPrettyString() + ":");
			System.out.println(MyTools2.applyMove(result.move, state));
			System.out.println();
			//map.put(key, result);
	    	return result;
	    }
	}
}

class MinimaxResult {
	int score;
	PentagoMove move;
	/**
	 * Represents the back propagated state
	 */
	PentagoBoardState state;
	
	public MinimaxResult(int score, PentagoMove move, PentagoBoardState state) {
		this.score = score;
		this.move = move;
		this.state = state;
	}
}
