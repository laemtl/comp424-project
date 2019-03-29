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
   
    static public int compute(int player_id, PentagoBoardState state) {	
    	value = 0;
    	rows_pattern_count = new int[3][27];
    	cols_pattern_count = new int[3][27];
    	
    	for (int i = 0; i < 3; i++) {
    		checkRows(state, i);
        	checkCols(state, i);
		}
    	
    	int player_factor = 1;
    	float[] player_weight = {1, 1.2f};
    	
    	// Main player is black
    	if(player_id == 1) {
    		player_factor = -1;
        	player_weight[0] = 1.2f;
        	player_weight[1] = 1;
    	}
    	
    	// white 0, black 1
    	int[] rowScore = {0, 0}, colScore = {0, 0};
		for (int i = 0; i < 3; i++) {
			rowScore[0] = 0;
			colScore[0] = 0;
			rowScore[1] = 0;
			colScore[1] = 0;
			
			for (int j = 0; j < 27; j++) {
				if(pattern_weight[j] > 0) {
					rowScore[0] += rows_pattern_count[i][j] * pattern_weight[j] * player_factor; 
					colScore[0] += cols_pattern_count[i][j] * pattern_weight[j] * player_factor;
				} else {
					rowScore[1] += rows_pattern_count[i][j] * pattern_weight[j] * player_factor; 
					colScore[1] += cols_pattern_count[i][j] * pattern_weight[j] * player_factor;
				}
			}
			
			// Triplet Maximizer
			// If 4 on the same level
			// Multiply by 2
			if(Math.abs(rowScore[0]) > pattern_weight[26]) rowScore[0] *= 2;
			if(Math.abs(colScore[0]) > pattern_weight[26]) colScore[0] *= 2;
			if(Math.abs(rowScore[1]) > pattern_weight[26]) rowScore[1] *= 2;
			if(Math.abs(colScore[1]) > pattern_weight[26]) colScore[1] *= 2;
			
			value += (rowScore[0] + colScore[0]) * player_weight[0];
			value += (rowScore[1] + colScore[1]) * player_weight[1];
		}
		
		return value;
	}	
	    
    private static void checkCols(PentagoBoardState state, int i) {

    	for (int j = 0; j < 2; j++) {
			for (int k = 0; k < 2; k++) {
				Triplet col = new Triplet(
					state.getPieceAt(3*k, i + 3*j), 
					state.getPieceAt(3*k + 1, i + 3*j), 
					state.getPieceAt(3*k + 2, i + 3*j)
				);
				
		    	switch (col.toString()) {
		    		case "   ": cols_pattern_count[i][0] += 1; break;
		    		
		    		case "b  ": cols_pattern_count[i][1] += 1; break;
		        	case " b ": cols_pattern_count[i][2] += 1; break;
		        	case "  b": cols_pattern_count[i][3] += 1; break;
		        	case "bb ": cols_pattern_count[i][4] += 1; break;
		        	case "b b": cols_pattern_count[i][5] += 1; break;
		        	case " bb": cols_pattern_count[i][6] += 1; break;
		        	case "bbb": cols_pattern_count[i][7] += 1; break;
		        		
		        	case "bw ": cols_pattern_count[i][8] += 1; break;
		        	case "b w": cols_pattern_count[i][9] += 1; break;
		        	case "bww": cols_pattern_count[i][10] += 1; break;
		        	case "wb ": cols_pattern_count[i][11] += 1; break;
		        	case " bw": cols_pattern_count[i][12] += 1; break;
		        	case "wbw": cols_pattern_count[i][13] += 1; break;
		        	case "w b": cols_pattern_count[i][14] += 1; break;
		        	case " wb": cols_pattern_count[i][15] += 1; break;
		        	case "wwb": cols_pattern_count[i][16] += 1; break;
		        	case "bbw": cols_pattern_count[i][17] += 1; break;
		        	case "bwb": cols_pattern_count[i][18] += 1; break;
		        	case "wbb": cols_pattern_count[i][19] += 1; break;
		        		
		        	case "w  ": cols_pattern_count[i][20] += 1; break;
		        	case " w ": cols_pattern_count[i][21] += 1; break;
		        	case "  w": cols_pattern_count[i][22] += 1; break;
		        	case "ww ": cols_pattern_count[i][23] += 1; break;
		        	case "w w": cols_pattern_count[i][24] += 1; break;
		        	case " ww": cols_pattern_count[i][25] += 1; break;
		        	case "www": cols_pattern_count[i][26] += 1; break;
		    			
		    		default: break;
		    	}
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
				
		    	switch (row.toString()) {
		    		case "   ": rows_pattern_count[i][0] += 1; break;
		    		
		    		case "b  ": rows_pattern_count[i][1] += 1; break;
		        	case " b ": rows_pattern_count[i][2] += 1; break;
		        	case "  b": rows_pattern_count[i][3] += 1; break;
		        	case "bb ": rows_pattern_count[i][4] += 1; break;
		        	case "b b": rows_pattern_count[i][5] += 1; break;
		        	case " bb": rows_pattern_count[i][6] += 1; break;
		        	case "bbb": rows_pattern_count[i][7] += 1; break;
		        		
		        	case "bw ": rows_pattern_count[i][8] += 1; break;
		        	case "b w": rows_pattern_count[i][9] += 1; break;
		        	case "bww": rows_pattern_count[i][10] += 1; break;
		        	case "wb ": rows_pattern_count[i][11] += 1; break;
		        	case " bw": rows_pattern_count[i][12] += 1; break;
		        	case "wbw": rows_pattern_count[i][13] += 1; break;
		        	case "w b": rows_pattern_count[i][14] += 1; break;
		        	case " wb": rows_pattern_count[i][15] += 1; break;
		        	case "wwb": rows_pattern_count[i][16] += 1; break;
		        	case "bbw": rows_pattern_count[i][17] += 1; break;
		        	case "bwb": rows_pattern_count[i][18] += 1; break;
		        	case "wbb": rows_pattern_count[i][19] += 1; break;
		        		
		        	case "w  ": rows_pattern_count[i][20] += 1; break;
		        	case " w ": rows_pattern_count[i][21] += 1; break;
		        	case "  w": rows_pattern_count[i][22] += 1; break;
		        	case "ww ": rows_pattern_count[i][23] += 1; break;
		        	case "w w": rows_pattern_count[i][24] += 1; break;
		        	case " ww": rows_pattern_count[i][25] += 1; break;
		        	case "www": rows_pattern_count[i][26] += 1; break;
		    			
		    		default: break;
		    	}
			}
		}
	}
}

class Triplet {
	Piece p1;
	Piece p2;
	Piece p3;
	
	public Triplet(Piece p1, Piece p2, Piece p3) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}
	
	@Override
	public String toString() {
		return p1.toString() + p2.toString() + p3.toString();
	}
}
