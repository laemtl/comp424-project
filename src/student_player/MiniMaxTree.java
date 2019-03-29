package student_player;

public class MiniMaxTree extends PBSTree {
	private int score;
	private MiniMaxTree prevState;
	private int player_id;
	
	public MiniMaxTree(int player_id, PBSTree tree, int score) {
		super(tree);
		this.score = score;
		this.player_id = player_id;
		prevState = null;
	}
	
	public MiniMaxTree(int player_id, PBSTree tree) {
		this(player_id, tree, Integer.MIN_VALUE);
	}
	
	public MiniMaxTree(int player_id, MiniMaxTree tree) {
		this(player_id, tree, tree.getScore());
	}
	
	public int getScore() {
		if(score == Integer.MIN_VALUE){
			score = HeuristicFunction.compute(player_id, getState());
		}
		
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}

	public MiniMaxTree getPrevState() {
		return prevState;
	}
	
	public void setPrevState(MiniMaxTree prevState) {
		this.prevState = prevState; 
	}
}


/*Comparator of moves

if center  -> 1
if center center 2
otherwise 0 */
