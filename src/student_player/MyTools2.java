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
		int depth = 3;
		Map<String, Integer> map = new HashMap<>();
		ToIntFunction<PentagoBoardState> heuristic = state2 -> HeuristicFunction.compute(player_id, state2);
		PentagoMove bestMove = null;
		int bestScore = Integer.MIN_VALUE;
		for (PentagoMove move: state.getAllLegalMoves()) {
    		PentagoBoardState newState = MyTools2.applyMove(move, state);
			int score = MyTools2.minimax(heuristic, 1, depth - 1, newState, map);
			if (score > bestScore) {
				bestScore = score;
				bestMove = move;
			}
		}
		
		return bestMove;
	}
	
	public static int minimax(ToIntFunction<PentagoBoardState> heuristic, int multiple, int depth, PentagoBoardState state, Map<String, Integer> map) {
		String key = state.toString();
		if (map.containsKey(key)) {
			return map.get(key);
		}
		
		List<PentagoMove> moves = state.getAllLegalMoves();
		
		if (depth == 0 || state.gameOver() || moves.isEmpty()) {
			int score = heuristic.applyAsInt(state);
			map.put(key, score);
	    	return score;
	    } else {
	    	int score = Integer.MIN_VALUE;
	    	for (PentagoMove move: moves) {
	    		PentagoBoardState newState = (PentagoBoardState)state.clone();
	    		newState.processMove(move);
	    		int newScore = multiple * MyTools2.minimax(heuristic, -multiple, depth - 1, newState, map);
	    		if (newScore > score) {
	    			score = newScore;
	    		}
	    	}
			map.put(key, score);
	    	return score;
	    }
	}
}

class MinimaxResult {
	int score;
	PentagoMove move;
	public MinimaxResult(int score, PentagoMove move) {
		super();
		this.score = score;
		this.move = move;
	}
}
