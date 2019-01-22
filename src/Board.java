import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The board class represents the physical Cluedo game board.
 * Facilitates valid movement by the players along itself.
 * Draws itself with the players on it.
 * Also creates and holds the list of weapon locations.
 * @author Edwin
 *
 */
public class Board {
	//board represented as a list of strings of chars
	private String[] brd = new String[25];
	//reference to list of players
	private List<Player> players;
	//list of weapons for drawing in their corresponding rooms
	private List<WeaponLoc> weapons = new ArrayList<WeaponLoc>();
	private static final int nWeapons = 6;
	private static final int moveCap = 10;
	
	public Board(List<Player> players) {
		this.players = players;
		initialiseBoard();
		addWeaponLocs();
	}
	
	/** 
	 * Initialise the Cluedo board as specified.
	 */
	public void initialiseBoard() {
		 brd[0]="#########?####?##########";
		 brd[1]="KKKKKs____BBBB____CCCCCCC";
		 brd[2]="KKKKKK__BBBBBBBB__CCCCCCC";
		 brd[3]="KKKKKK__BBBBBBBB__CCCCCCC";
		 brd[4]="KKKKKK__BBBBBBBB__dCCCCCC";
		 brd[5]="KKKKKK__dBBBBBBd___CCCCs#";
		 brd[6]="#KKKdK__BBBBBBBB________?";
		 brd[7]="________BdBBBBdB________#";
		 brd[8]="#_________________PPPPPPP";
		 brd[9]="DDDDD_____________dPPPPPP";
		brd[10]="DDDDDDDD__#####___PPPPPPP";
		brd[11]="DDDDDDDD__#####___PPPPPPP";
		brd[12]="DDDDDDDd__#####___PPPPPdP";
		brd[13]="DDDDDDDD__#####_________#";
		brd[14]="DDDDDDDD__#####___LLLdLL#";
		brd[15]="DDDDDDdD__#####__LLLLLLLL";
		brd[16]="#_________#####__dLLLLLLL";
		brd[17]="?________________LLLLLLLL";
		brd[18]="#________HHddHH___LLLLLL#";
		brd[19]="sRRRRRd__HHHHHH_________?";
		brd[20]="RRRRRRR__HHHHHd_________#";
		brd[21]="RRRRRRR__HHHHHH__dSSSSSSs";
		brd[22]="RRRRRRR__HHHHHH__SSSSSSSS";
		brd[23]="RRRRRRR__HHHHHH__SSSSSSSS";
		brd[24]="RRRRRR#?#HHHHHH#_#SSSSSSS";
	}
	
	/**
	 * Add the WeaponLocs to the game and place them in random rooms.
	 */
	public void addWeaponLocs() {
		String[] rooms = Room.rooms();
		List<String> roomList = new ArrayList<String>();
		for (String s : rooms) roomList.add(s);
		Collections.shuffle(roomList);
		for (int i=0; i<nWeapons; i++) {
			WeaponLoc weapon = new WeaponLoc(i);
			weapons.add(weapon);
			moveObj(weapon.getName(),roomList.get(i));
		}
	}
	
	/**
	 * Draw the items (players and weapons) on the graphics pane.
	 * @param g
	 * @param tileX
	 * @param tileY
	 */
	public void drawItems(Graphics g, double tileX, double tileY) {
		for (WeaponLoc w : weapons) {
			w.drawSelf(g, tileX, tileY);
		}
		for (Player p : players) {
			p.drawSelf(g, tileX, tileY);
		}
	}
	
	/**
	 * Check if the given move by a player from co-ords (x1,y1) to (x2,y2) is valid.
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public boolean isValidMove(int x1, int y1, int x2, int y2) {
		String loc1 = getLoc(x1,y1);
		String loc2 = getLoc(x2,y2);
		//location not on map
		if (loc1==null || loc2==null) return false; 
		//another player is already at location
		for (Player p : players) {
			if (p.isHere(x2, y2)) return false;
		}
		//note moving on top of WeaponLocs is allowed, as otherwise players might become blocked
		//moving from door, start or corridor, to door or corridor is acceptable
		if ((loc1.equals("corridor") || loc1.equals("door") || loc1.equals("start")) 
				&& (loc2.equals("corridor")||loc2.equals("door"))) return true;
		//if moving to passage, must check other end of passage is also clear
		if (loc2.equals("passage")&&!loc1.equals("passage")) {
			int x3 = teleport(x2,y2)[0];
			int y3 = teleport(x2,y2)[1];
			for (Player p:players) {
				if (p.isHere(x3, y3)) return false;
			}
		}
		//moving from door, room or passage, to door, room or passage is acceptable
		if ((isRoom(x1,y1) || loc1.equals("door") || loc1.equals("passage")) 
				&& (isRoom(x2,y2) || loc2.equals("door") || loc2.equals("passage"))) return true;
		//any other move is illegal
		return false;
	}
	
	/**
	 * Given co-ordinates (x,y), return as a string what tile is present at that location.
	 * @param x
	 * @param y
	 * @return
	 */
	public String getLoc(int x, int y) {
		if (x<0 || y<0 || y>24 || x>24) return "offscreen";
		switch(brd[y].charAt(x)) {
		case('_'): return "corridor";
		case('#'): return "wall";
		case('d'): return "door";
		case('s'): return "passage";
		case('?'): return "start";
		
		case('K'): return "kitchen";
		case('D'): return "dining";
		case('R'): return "lounge";
		case('H'): return "hall";
		case('S'): return "study";
		case('L'): return "library";
		case('P'): return "billiard";
		case('C'): return "conservatory";
		case('B'): return "ballroom";
		default: return null;
		}
	}
	
	/**
	 * Given co-ordinates (x,y), return if that location is within a room or not.
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isRoom(int x, int y) {
		String room = getLoc(x,y);
		if (room==null) return false;
		return (room.equals("kitchen") || room.equals("dining") 
				||room.equals("lounge") || room.equals("hall") 
				||room.equals("study") || room.equals("library") 
				||room.equals("billiard") || room.equals("conservatory") 
				||room.equals("ballroom") );
	}
	
	/**
	 * Given co-ordinates (x,y) on a secret passage, return, as a pair of ints,
	 * the co-ords of the point the passage links to.
	 * @param x
	 * @param y
	 * @return
	 */
	public int[] teleport(int x, int y) {
		if (!getLoc(x,y).equals("passage")) return null;
		int[] loc = new int[2];
		if (x==5 && y==1) {
			loc[0] = 24;
			loc[1] = 21;
		} else if (x==24 && y==21) {
			loc[0] = 5;
			loc[1] = 1;
		} else if (x==23 && y==5) {
			loc[0] = 0;
			loc[1] = 19;
		} else if (x==0 && y==19) {
			loc[0] = 23;
			loc[1] = 5;
		} else {
			throw new RuntimeException(); //should never happen
		}
		return loc;
	}
	
	/**
	 * Move a character/weapon to the appropriate room
	 * @param character
	 */
	public void moveObj(String object, String room) {
		for (Player p : players) {
			if (p.getName().equalsIgnoreCase(object)) {
				if (!p.getLoc(this).equalsIgnoreCase(room)) {
					int[] loc = getEmptySpaceInRoom(room);
					p.movePlayer(loc[0], loc[1]);
				}
				return;
			}
		}
		for (WeaponLoc w : weapons) {
			if (w.getName().equalsIgnoreCase(object)) {
				if (!w.getLoc(this).equalsIgnoreCase(room)) {
					int[] loc = getEmptySpaceInRoom(room);
					w.moveWeapon(loc[0], loc[1]);
				}
				return;
			}
		}
	}
	
	/**
	 * Given the name of a room, find an empty tile to place an object in.
	 * Can not place adjacent to doors or passages, to prevent blocking them off.
	 * @param room
	 * @return
	 */
	public int[] getEmptySpaceInRoom(String room) {
		for (int i=0; i<24; i++) {
			for (int j=0; j<24; j++) {
				if (getLoc(i,j).equalsIgnoreCase(room)) {
					boolean occupied = false;
					//check if player is already at location
					for (Player p : players) {
						if (p.isHere(i, j)) {
							occupied = true;
						}
					}
					//check if weapon is already at location
					for (WeaponLoc w : weapons) {
						if (w.isHere(i, j)) {
							occupied = true;
						}
					}
					if (!occupied) {
						//cannot move to a tile adjacent to a door or passage and block them
						if (!getLoc(i-1,j).equals("passage")&&!getLoc(i-1,j).equals("door")
								&&!getLoc(i+1,j).equals("passage")&&!getLoc(i+1,j).equals("door")
								&&!getLoc(i,j-1).equals("passage")&&!getLoc(i,j-1).equals("door")
								&&!getLoc(i,j+1).equals("passage")&&!getLoc(i,j+1).equals("door")) {
							return new int[]{i,j};
						}
					}
				}
			}
		}
		//should never happen
		return null;
	}
	
	/**
	 * Given a player, a die roll and a location they want to move to, 
	 * check if they can move there.
	 * If so, move them.
	 * @param p
	 * @param x
	 * @param y
	 * @param die
	 * @return
	 */
	public int movePlayer(Player p, int x, int y, int die) {
		int depth = recursiveMove(p.getX(), p.getY(), x, y, die+1, moveCap);
		if (depth>-1) {
			p.movePlayer(x,y);
			if (die-depth==-1) return 0;
			return die-depth;
		}
		return -1;
	}
	
	/**
	 * Recursively attempt to move a player from (x1,y1) to (x2,y2).
	 * Can only move with the number of die moves remaining (depth).
	 * Since movement within a room does not require movement,
	 * the moves parameter hard-caps the depth of the recursion to prevent stack overflow.
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param depth
	 * @param moves
	 * @return
	 */
	private int recursiveMove(int x1, int y1, int x2, int y2, int depth, int moves) {
		if (!isRoom(x1,y1)) depth--;
		if (x1==x2 && y1==y2) {
			return depth;
		}
		if (depth<0||moves<=0) return -1;
		moves--;
		int maxDepth = -1;
		if (isValidMove(x1,y1,x1+1,y1)) {
			maxDepth = Math.max(maxDepth,recursiveMove(x1+1,y1,x2,y2, depth, moves));
		}
		if (isValidMove(x1,y1,x1,y1+1)) {
			maxDepth = Math.max(maxDepth,recursiveMove(x1,y1+1,x2,y2, depth, moves));
		}
		if (isValidMove(x1,y1,x1-1,y1)) {
			maxDepth = Math.max(maxDepth,recursiveMove(x1-1,y1,x2,y2, depth, moves));
		}
		if (isValidMove(x1,y1,x1,y1-1)) {
			maxDepth = Math.max(maxDepth,recursiveMove(x1,y1-1,x2,y2, depth, moves));
		}
		if (getLoc(x1,y1).equals("passage")) {
			int[] tele = teleport(x1,y1);
			if (isValidMove(x1,y1,tele[0],tele[1])) {
				maxDepth = Math.max(maxDepth,recursiveMove(tele[0],tele[1],x2,y2, depth, moves));
			}
		}
		return maxDepth;
	}
	
	/**
	 * Check a location the player hovered over for the appropriate display text.
	 * @param x
	 * @param y
	 * @return
	 */
	public String hover(int x, int y) {
		for (Player p : players) {
			if (p.isHere(x, y)) {
				if (p.isPlaying()) {
					return "Player " + p.getPlayerName() + " ("+p.getName()+")";
				} else {
					return "Player " + p.getName();
				}
			}
		}
		for (WeaponLoc w : weapons) {
			if (w.isHere(x,y)) return "Weapon " + w.getName();
		}
		return getLoc(x,y);
	}
	
}
