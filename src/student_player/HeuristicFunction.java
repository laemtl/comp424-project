package student_player;

import java.util.ArrayList;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoBoardState.Piece;

public class HeuristicFunction {
	
    // 0 : win
    // 1 : center
    // 2 : 1-in-a-row 	
    // 3 : 2-in-a-row
    // 4 : 3-in-a-row
    // 5 : 4-in-a-row
    // 6 : 5-in-a-row or more

	protected static int[][] scores = {
		{	
			20,
			5,
			60,
			500,
			1500,
			50000
		},
		{
			-20,
			-5,
			-60,
			-600,
			-1800,
			-52000
		}
	};
	
    static int value;
    static ArrayList<LineAnalytics> analytics;
	   
    static public int compute(int playerColor, PentagoBoardState state) {	    	
    	if(state.gameOver()) {
    		
    		if(state.getWinner() == playerColor) {
    			//System.out.println(playerColor);
    			//System.out.println("Win state");
    			//System.out.println(state);
    			return Integer.MAX_VALUE;
    		}
    		
        	if(state.getWinner() == (1-playerColor)) {
        		//System.out.println(playerColor);
        		//System.out.println("Loose state");
    			//System.out.println(state);
    			
        		return Integer.MIN_VALUE;
        		
        	}

        	// Draw
        	return 0;    
    	} 
        			    			
    	analytics = new ArrayList<>();
    	value = 0;
    	analizeBoard(state);
    	    	
    	// For each rows, cols, diags
    	for (LineAnalytics line : analytics) {
			int score = line.getScore(playerColor);	
			value += score;
		}
    	
		return value;
	}	
	    
    private static void analizeBoard(PentagoBoardState state) {
    	checkRows(state);
        checkCols(state);
    	checkDiags(state);
    	checkTrickyDiags(state);
	}
    
    private static void checkDiags(PentagoBoardState state) {
    	Triplet[] dTriplets = new Triplet[4];
    	Triplet[] adTriplets = new Triplet[4];
		
		int l = 0;
    	for (int j = 0; j < 2; j++) {
			for (int k = 0; k < 2; k++) {
				dTriplets[l] = new Triplet(new Piece[]{
					state.getPieceAt(2 + 3*j, 0 + 3*k), 
					state.getPieceAt(1 + 3*j, 1 + 3*k), 
					state.getPieceAt(0 + 3*j, 2 + 3*k)
				});
				
				adTriplets[l] = new Triplet(new Piece[]{
					state.getPieceAt(0 + 3*j, 0 + 3*k), 
					state.getPieceAt(1 + 3*j, 1 + 3*k), 
					state.getPieceAt(2 + 3*j, 2 + 3*k)
				});
				l++;
			}	    	
		}
    	
    	// Compute analytics for black, white
    	analytics.add(new LineAnalytics(dTriplets, 0, "diag", 1));
       	analytics.add(new LineAnalytics(dTriplets, 1, "diag", 1));
        
       	analytics.add(new LineAnalytics(adTriplets, 0, "antidiag", 1));
    	analytics.add(new LineAnalytics(adTriplets, 1, "antidiag", 1));
	}

    private static void checkTrickyDiags(PentagoBoardState state) {
    	// Tricky top diag 
    	/*Piece[][] tdDuet = new Piece[4][2];
    	Piece[] tdSolo = new Piece[4];
    	
    	// Tricky bottom diag 
    	Piece[][] bdDuet = new Piece[4][2];
    	Piece[] bdSolo = new Piece[4];
    	
    	// Tricky top adiag 
    	Piece[][] tadDuet = new Piece[4][2];
    	Piece[] tadSolo = new Piece[4];
    	
    	// Tricky bottom adiag 
    	Piece[][] badDuet = new Piece[4][2];
    	Piece[] badSolo = new Piece[4];
    	
    	int l = 0;
    	for (int i = 0; i < 2; i++) {
    		for (int j = 0; j < 2; j++) {
	    		// Tricky top diag 
	        	tdDuet[l] = new Piece[]{
	        		state.getPieceAt(3*j, 1+3*i), 
	        		state.getPieceAt(1+3*j, 2+3*i)
	        	};
	        	tdSolo[l] = state.getPieceAt(2+3*j, 3*i);
	        	
	        	// Tricky bottom diag 
	        	bdDuet[l] = new Piece[]{
	        		state.getPieceAt(1+3*j, 3*i), 
	        		state.getPieceAt(2+3*j, 1+3*i)
		        };
	        	bdSolo[l] = state.getPieceAt(3*j, 2+3*i);
	        	
	        	// Tricky top adiag 
	        	tadDuet[l] = new Piece[]{
		        	state.getPieceAt(3*j, 1+3*i), 
		        	state.getPieceAt(1+3*j, 3*i)
			    };
	        	tadSolo[l] = state.getPieceAt(2+3*j, 2+3*i);
	        	
	        	// Tricky bottom adiag 
	        	badDuet[l] = new Piece[]{
			    	state.getPieceAt(1+3*j, 2+3*i), 
			    	state.getPieceAt(2+3*j, 1+3*i)
				};
	        	badSolo[l] = state.getPieceAt(3*j, 3*i);
	        			
	        	l++;
	    	}
    	}
    	
    	int[][] list = {
			{1,2,3},
			{1,3,2},
			{2,1,3},
			
			{0,2,3},
			{0,3,2},
			{2,0,3},
			
			{0,1,3},
			{1,0,3},
			{0,3,1},
			
			{0,1,2},
			{1,0,2},
			{0,2,1}
		};
		
		/*List<List<Integer>> list = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			list.addAll(permute(a[i]));
		}
    	
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < list.length; j++) {
				int[] n = list[j];
				
				analytics.add(new LineAnalytics(new Xplet(new Piece[]{
					tdDuet[n[0]][0],
					tdDuet[n[0]][1],
					tdSolo[n[1]],
					tdDuet[n[2]][0],
					tdDuet[n[2]][1]		
				}), i, "trickyDiagup", 1));
				
				analytics.add(new LineAnalytics(new Xplet(new Piece[]{
					bdDuet[n[0]][0],
					bdDuet[n[0]][1],
					bdSolo[n[1]],
					bdDuet[n[2]][0],
					bdDuet[n[2]][1]	
				}), i, "trickyDiagdown", 1));
				
				analytics.add(new LineAnalytics(new Xplet(new Piece[]{
					tadDuet[n[0]][0],
					tadDuet[n[0]][1],
					tadSolo[n[1]],
					tadDuet[n[2]][0],
					tadDuet[n[2]][1]	
				}), i, "trickyaDiagup", 1));
				
				analytics.add(new LineAnalytics(new Xplet(new Piece[]{
					badDuet[n[0]][0],
					badDuet[n[0]][1],
					badSolo[n[1]],
					badDuet[n[2]][0],
					badDuet[n[2]][1]
				}), i, "trickyaDiagdown", 1));
			}
		}*/
    	
    	Piece[] p_diag0 = new Piece[5];
    	Piece[] p_diag1 = new Piece[5];
		
    	Piece[] p_adiag0 = new Piece[5];
    	Piece[] p_adiag1 = new Piece[5];

		for (int i = 0; i < 5; i++) {
			p_diag0[i] = state.getPieceAt(i, i+1);
			p_diag1[i] = state.getPieceAt(i+1, i);
			
			p_adiag0[i] = state.getPieceAt(4-i, i);
			p_adiag1[i] = state.getPieceAt(5-i, i+1); 
		}
		
		for (int k = 0; k < 2; k++) {
			analytics.add(new LineAnalytics(new Xplet(p_diag0), k, "col", 1));
			analytics.add(new LineAnalytics(new Xplet(p_diag1), k, "col", 1));
			analytics.add(new LineAnalytics(new Xplet(p_adiag0), k, "col", 1));
			analytics.add(new LineAnalytics(new Xplet(p_adiag1), k, "col", 1));
		}
	}

	private static void checkCols(PentagoBoardState state) {
		Triplet[] triplets = new Triplet[4];
		
		for (int i = 0; i < 3; i++) {
			int l = 0;
	    	for (int j = 0; j < 2; j++) {
				for (int k = 0; k < 2; k++) {
					triplets[l] = new Triplet(new Piece[]{
						state.getPieceAt( 3*k, i + 3*j), 
						state.getPieceAt(3*k + 1, i + 3*j), 
						state.getPieceAt(3*k + 2, i + 3*j)
					});
					l++;
				}	    	
			}
	    	
	    	// Compute analytics for black, white
	    	analytics.add(new LineAnalytics(triplets, 0, "col", i));
	    	analytics.add(new LineAnalytics(triplets, 1, "col", i));
		}
	}

	private static void checkRows(PentagoBoardState state) {
		Triplet[] triplets = new Triplet[4];
		
		for (int i = 0; i < 3; i++) {
			int l = 0;
			for (int j = 0; j < 2; j++) {
				for (int k = 0; k < 2; k++) {
					triplets[l] = new Triplet(new Piece[]{
						state.getPieceAt(i + 3*j, 3*k), 
						state.getPieceAt(i + 3*j, 3*k + 1), 
						state.getPieceAt(i + 3*j, 3*k + 2)
					});
					l++;
				}	    	
			}
	    	
	    	// Compute analytics for black, white
	    	analytics.add(new LineAnalytics(triplets, 0, "row", i));
	    	analytics.add(new LineAnalytics(triplets, 1, "row", i));
		}
	}
	
	/*public static List<List<Integer>> permute(int[] nums) {
	    List<List<Integer>> result = new ArrayList<>();
	    helper(0, nums, result);
	    return result;
	}
	 
	private static void helper(int start, int[] nums, List<List<Integer>> result){
	    if(start==nums.length-1){
	        ArrayList<Integer> list = new ArrayList<>();
	        for(int num: nums){
	            list.add(num);
	        }
	        result.add(list);
	        return;
	    }
	 
	    for(int i=start; i<nums.length; i++){
	        swap(nums, i, start);
	        helper(start+1, nums, result);
	        swap(nums, i, start);
	    }
	}
	 
	private static void swap(int[] nums, int i, int j){
	    int temp = nums[i];
	    nums[i] = nums[j];
	    nums[j] = temp;
	}*/
}

class LineAnalytics {
	String lineType;
	int color;
	int lineRank;
	int centersCount;
	int[] sequences;
	int[] sizes;
	
	public LineAnalytics(Triplet[] triplets, int color, String lineType, int lineRank) {
		this.lineType = lineType;
		this.color = color;
		this.lineRank = lineRank;
		this.centersCount = 0;
		this.sequences = new int[6];
		this.sizes = new int[6];
		
		analyze(triplets);
	}
	
	public LineAnalytics(Xplet xplet, int color, String lineType, int lineRank) {
		this.lineType = lineType;
		this.color = color;
		this.lineRank = lineRank;
		this.centersCount = 0;
		this.sequences = new int[6];
		this.sizes = new int[6];
		
		analyze(xplet);
	}
	
	public int getScore(int player_id) {
		int score = 0 ;
		
		// Score positive if player_id = 0 = white
		// Negative otherwise
		// color == (1 - player_id)
		int score_index = 0;
		if(color != (1 - player_id)) score_index = 1;
		
		// Center Scores
		score += centersCount * HeuristicFunction.scores[score_index][0];
		// Sequence Scores

		int max = 0;
		int size = 0;
		//int count = 0;
		for (int j = 0; j < sequences.length; j++) {
			int seq_length = Math.min(5, sequences[j]);
			int seq_availableSize = sizes[j];
			//if(seq_length == max) count++;
			//if(seq_length > max) {
				max = seq_length; 
				size = seq_availableSize;
				//count = 1;
			//}
			score += HeuristicFunction.scores[score_index][max] * (size-4);
		}
		//if(max > 0) score += HeuristicFunction.scores[score_index][max] * count;
		
		return score;
	}

	private void analyze(Xplet xplet) {
		// Cannot make 5 in a row, ignore
		if(xplet.getCount(color) > 0 && xplet.getCount(1-color) > 0) return;
		sequences[0] = xplet.getCount(color);	
		sizes[0] = 5;
	}
	
	private void analyze(Triplet[] triplets) {	
		ArrayList<Triplet> xxTriplets = new ArrayList<Triplet>();
		ArrayList<Triplet> xxxTriplets = new ArrayList<Triplet>();
	
		for (Triplet triplet : triplets) {
			if(triplet.isValid(color)) {
				if(triplet.getCount(1-color) == 0) {
					xxxTriplets.add(triplet);
				} else {
					xxTriplets.add(triplet);
				}
				
				if(triplet.getCenter() == color) centersCount++;
			}
		}
		
		// No triplets can give a sequence of 5
		if(xxxTriplets.size() < 1) return;
		if(xxxTriplets.size() + xxTriplets.size() < 2) return;
		
		int k = 0;
		for (int i = 0; i < xxxTriplets.size(); i++) {
			Triplet t3 = xxxTriplets.get(i);
			
			for (Triplet t2 : xxTriplets) {
				sequences[k] = t3.getCount(color) + t2.getCount(color); 
				sizes[k] = 5;
				k++;
			}
			
			for (int j = i + 1; j < xxxTriplets.size(); j++) {
				Triplet t3bis = xxxTriplets.get(j);
				
				if(t3 != t3bis) {
					sequences[k] = t3.getCount(color) + t3bis.getCount(color);
					sizes[k] = 6;
					k++;
				}
			}	
		}	
	}
}

class Xplet {
	Piece[] xplet;	
	int[] count;
	
	public Xplet(Piece[] pieces) {
		xplet = pieces;
						
		count = new int[3];
		for (int i = 0; i < xplet.length; i++) {
			if(xplet[i].toString().equals("w")) count[1] += 1;
			if(xplet[i].toString().equals("b")) count[0] += 1;
			if(xplet[i].toString().equals(" ")) count[2] += 1;
		}
	}
		
	int getCount(int color) {
		return count[color];
	}
		
	@Override
	public String toString() {
		String str = "";
		for(int i = 0; i < xplet.length; i++) {
			str = xplet[i].toString();
		}
		return str;
	}
}

class Triplet extends Xplet{
	int quadrant;
	
	public Triplet(Piece[] pieces) {
		super(pieces);
	}
	
	int getCenter() {
		return xplet[1].ordinal();
	}
	
	boolean isValid(int color) {
		if(getCount(1 - color) == 0 
		|| getCount(1 - color) == 1 && getCenter() != (1 - color)) return true;
		
		return false;
	}
}
