package student_player;

import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoBoardState.Piece;

public class HeuristicFunction {
	// Logic: Maximizing row/col placement 
	// Then make a wise swap
	// 2 heuristics?
	// One for making the best choice for row/col placement
	// Another to max winning on swap
	
	
	
    // 0 : 2-in-a-row
    // 1 : 3-in-a-row
    // 2 : 4-in-a-row 	
    // 3 : 5-in-a-row
    // 4 : marble in center
    // 5 : 3-in-a-row (same block)
	
	// detect hole
	// place hole
	// detect if placement is useless (3 in a row where we can't place more than 3)
    
    // never touch the 4 corners for opening
    // Must play when my opponent have 4 in a row
    // 4 in a row with both ends open will almost always win
    
    // align stones at last minute
    // pattern 
    // x0x0x0
    // 00x0x0
    // 0000x0
    
    // when opponent forms alignment, block where there is another stone 
    // x00x0x0
    // x00*0x0
    
    // heuristic should give more point for sparse alignment
    // -> trick the opponent
    
    // logic to construct alignment on 2 different quarter
    
    // don’t  let  the  opponent  get  three  in  a  row  on  one  game block, 
    // or you’ll be on the defensive

	static int[] pattern_weight = {
    		0,		// "   "	#0
    		-10,	// "b  "	#1
    		-50,	// " b "	#2
    		-10,	// "  b"	#3
    		-500,	// "bb "	#4
    		-100,	// "b b"	#5
    		-500,	// " bb"	#6
    		-5000,	// "bbb"	#7
    		
    		20,		// "bw "	#8
    		0,		// "b w"	#9
    		500,	// "bww"	#10
    		-20,	// "wb "	#11
    		-20,	// " bw"	#12
    		0,		// "wbw"	#13
    		0,		// "w b"	#14
    		20,		// " wb"	#15
    		500,	// "wwb"	#16
    		-500,	// "bbw"	#17
    		0,		// "bwb"	#18
    		-500,	// "wbb"	#19
    		
		    10,		// "w  "	#20
			50,		// " w "	#21
    		10,		// "  w"	#22
    		500,	// "ww "	#23
			100,	// "w w"	#24
    		500,	// " ww"	#25
    		5000	// "www"	#26
    };
	
    static int value;
	private static int[][] rows_pattern_count;
	private static int[][] cols_pattern_count;
	private static int[][] diags_pattern_count;
   
    static public int compute(int player_id, PentagoBoardState state) {	
    	value = 0;
    	rows_pattern_count = new int[3][27];
    	cols_pattern_count = new int[3][27];
    	diags_pattern_count = new int[2][27];
    	
    	for (int i = 0; i < 3; i++) {
    		checkRows(state, i);
        	checkCols(state, i);
		}
    	checkDiags(state);
    	
    	int player_factor = 1;
    	float level_factor = 3;
    	float[] player_weight = {1, 3.2f};
    	
    	// Main player is black
    	if(player_id == 1) {
    		player_factor = -1;
        	player_weight[0] = 1.2f;
        	player_weight[1] = 1;
    	}
    	
    	// white 0, black 1
    	float[] rowScore = {0, 0}, colScore = {0, 0}, diagScore = {0, 0};
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 27; j++) {
				if(pattern_weight[j] > 0) {
					rowScore[0] += rows_pattern_count[i][j] * pattern_weight[j] * player_factor; 
					colScore[0] += cols_pattern_count[i][j] * pattern_weight[j] * player_factor;
					if(i < 2) diagScore[0] += diags_pattern_count[i][j] * pattern_weight[j] * player_factor;
				} else {
					rowScore[1] += rows_pattern_count[i][j] * pattern_weight[j] * player_factor; 
					colScore[1] += cols_pattern_count[i][j] * pattern_weight[j] * player_factor;
					if(i < 2) diagScore[1] += diags_pattern_count[i][j] * pattern_weight[j] * player_factor;
				}
			}
			
			// Triplet Maximizer
			// If 4 on the same level
			// Multiply by level_factor
			if(Math.abs(rowScore[0]) > pattern_weight[26]) rowScore[0] *= level_factor;
			if(Math.abs(colScore[0]) > pattern_weight[26]) colScore[0] *= level_factor;
			if(Math.abs(diagScore[0]) > pattern_weight[26]) diagScore[0] *= level_factor;
			if(Math.abs(rowScore[1]) > pattern_weight[26]) rowScore[1] *= level_factor;
			if(Math.abs(colScore[1]) > pattern_weight[26]) colScore[1] *= level_factor;
			if(Math.abs(diagScore[1]) > pattern_weight[26]) diagScore[1] *= level_factor;
			
			value += (rowScore[0] + colScore[0] + diagScore[0]) * player_weight[0];
			value += (rowScore[1] + colScore[1] + diagScore[1]) * player_weight[1];
			
			rowScore[0] = 0;
			colScore[0] = 0;
			diagScore[0] = 0;
			rowScore[1] = 0;
			colScore[1] = 0;
			diagScore[1] = 0;
		}
		
		return value;
	}	
	    
    private static void checkDiags(PentagoBoardState state) {
    	for (int j = 0; j < 2; j++) {
			for (int k = 0; k < 2; k++) {
				Triplet diag = new Triplet(
					state.getPieceAt(2 + 3*j, 0 + 3*k), 
					state.getPieceAt(1 + 3*j, 1 + 3*k), 
					state.getPieceAt(0 + 3*j, 2 + 3*k)
				);
				
				Triplet antidiag = new Triplet(
					state.getPieceAt(0 + 3*j, 0 + 3*k), 
					state.getPieceAt(1 + 3*j, 1 + 3*k), 
					state.getPieceAt(2 + 3*j, 2 + 3*k)
				);
				
				diags_pattern_count[0][diag.getTripletValue().ordinal()] += 1;
				diags_pattern_count[1][antidiag.getTripletValue().ordinal()] += 1;
			}
		}
	}

    private static void checkTrickyDiags(PentagoBoardState state) {
    	for (int j = 0; j < 2; j++) {
			for (int k = 0; k < 2; k++) {
				/*state.getPieceAt(0 + 3*j, 0 + 3*k), 
				state.getPieceAt(1 + 3*j, 1 + 3*k), 
				state.getPieceAt(2 + 3*j, 2 + 3*k)
			
				diags_pattern_count[0][diag.getTripletValue().ordinal()] += 1;
				diags_pattern_count[1][antidiag.getTripletValue().ordinal()] += 1;*/
			}
		}
	}

	private static void checkCols(PentagoBoardState state, int i) {
    	for (int j = 0; j < 2; j++) {
			for (int k = 0; k < 2; k++) {
				Triplet col = new Triplet(
					state.getPieceAt(3*k, i + 3*j), 
					state.getPieceAt(3*k + 1, i + 3*j), 
					state.getPieceAt(3*k + 2, i + 3*j)
				);
				
				cols_pattern_count[i][col.getTripletValue().ordinal()] += 1;
			}
		}
	}

	private static void checkRows(PentagoBoardState state, int i) {
    	for (int j = 0; j < 2; j++) {
			for (int k = 0; k < 2; k++) {
				Triplet row = new Triplet(
					state.getPieceAt(i + 3*j, 3*k), 
					state.getPieceAt(i + 3*j, 3*k + 1), 
					state.getPieceAt(i + 3*j, 3*k + 2)
				);
				
		    	rows_pattern_count[i][row.getTripletValue().ordinal()] += 1;	    	}
			}
		}
	}


enum TripletValue {
	___, b__, _b_, __b, bb_, b_b, _bb, bbb, bw_, b_w, bww, wb_, _bw, wbw, w_b,
	_wb, wwb, bbw, bwb, wbb, w__, _w_, __w, ww_, w_w, _ww, www;
}

class Triplet {
	Piece p1;
	Piece p2;
	Piece p3;
	
	TripletValue tv;
	
	public Triplet(Piece p1, Piece p2, Piece p3) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		
		tv = TripletValue.valueOf("" 
		+ p1.toString().replace(" ", "_") 
		+ p2.toString().replace(" ", "_") 
		+ p3.toString().replace(" ", "_"));
	}
	
	TripletValue getTripletValue() {
		return tv;
	}
	
	@Override
	public String toString() {
		return p1.toString() + p2.toString() + p3.toString();
	}
}
