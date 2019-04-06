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
    
	public static PentagoMove getMove(int player_id, PentagoBoardState state) {
		int depth = 4;
		Map<String, Integer> map = new HashMap<>();
		ToIntFunction<PentagoBoardState> heuristic = state2 -> HeuristicFunction.compute(player_id, state2);
		PentagoMove bestMove = null;
		int bestScore = 0;
		for (PentagoMove move: state.getAllLegalMoves()) {
    		PentagoBoardState newState = (PentagoBoardState)state.clone();
    		newState.processMove(move);
			int score = MyTools2.minimax(heuristic, -1, depth - 1, newState, map);
			if (bestMove == null) {
				bestScore = score;
				bestMove = move;
			} else if (score > bestScore) {
				bestScore = score;
				bestMove = move;
			}
		}
		System.out.println(map.size());
		return bestMove;
	}
	
	public static int minimax(ToIntFunction<PentagoBoardState> heuristic, int multiple, int depth, PentagoBoardState state, Map<String, Integer> map) {
		List<PentagoMove> moves = MyTools2.randomSubList(10, state.getAllLegalMoves());
		String key = state.toString();
		if (map.containsKey(key)) {
			return Integer.MIN_VALUE;
		} 
		map.put(key, 1);
		
		if (depth == 0 || state.gameOver() || moves.isEmpty()) {
	    	return heuristic.applyAsInt(state);
	    } else {
	    	int max = Integer.MIN_VALUE;
	    	for (PentagoMove move: moves) {
	    		PentagoBoardState newState = (PentagoBoardState)state.clone();
	    		newState.processMove(move);
	    		int value = MyTools2.minimax(heuristic, -multiple, depth - 1, newState, map);
	    		if (multiple * value > max) {
	    			max = value;
	    		}
	    	}
	    	return max;
	    }
	}
}