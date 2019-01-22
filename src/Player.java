import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 * A Player is one of the players in the game.
 * They have a unique id, a hand of cards and a location on the board.
 * They take turns rolling a die and making movements as specified by the user input.
 * @author Edwin
 *
 */
public class Player {

	private int uid;
	private String name;
	private List<Card> hand =  new ArrayList<Card>();
	private int x; //x location on board
	private int y; //y location on board
	private boolean isEliminated = false;
	private boolean playing;
	
	//this constructor is for non-playing players, who just move around
	public Player(int uid) {
		this.uid = uid;
		this.playing = false;
		initialiseLocation();
	}
	
	//this constructor is for actually playing players
	public Player(int uid, String name) {
		this.uid = uid;
		this.playing = true;
		this.name = name;
		initialiseLocation();
	}
	
	/**
	 * Initialise the player's starting location based off their user id, as specified.
	 */
	private void initialiseLocation() {
		switch(uid) {
		case 0: x=7; y=24; break;
		case 1: x=0; y=17; break;
		case 2: x=9; y=0; break;
		case 3: x=14; y=0; break;
		case 4: x=24; y=6; break;
		case 5: x=24; y=19; break;
		case -1: break; //used to represent empty player object
		default: throw new RuntimeException(); //should never happen
		}
	}
	
	/**
	 * Return this player's name as a character in the game
	 * @return
	 */
	public String getName() {
		return new Character(uid).toString();
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	/**
	 * Return this player's inputted name
	 * @return
	 */
	public String getPlayerName() {
		return name;
	}
	
	/**
	 * Return the name of the location of this player
	 * @param board
	 * @return
	 */
	public String getLoc(Board board) {
		return board.getLoc(x, y);
	}
	
	/**
	 * Return if the player has been eliminated from the game.
	 * @return
	 */
	public boolean isEliminated() {
		return isEliminated;
	}
	
	/**
	 * Return if the player is a user-controlled player or a dummy for movement purposes.
	 */
	public boolean isPlaying() {
		return playing;
	}
	
	/**
	 * Return unique player ID
	 * @return
	 */
	public int getUid() {
		return uid;
	}
	
	/**
	 * Return if player is at a given location
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isHere(int x, int y) {
		return x==this.x && y==this.y;
	}
	
	/**
	 * Return the player's hand.
	 * @return
	 */
	public List<Card> getHand() {
		return hand;
	}
	
	/**
	 * Deal a card to the player
	 * @param c
	 */
	public void dealCard(Card c) {
		hand.add(c);
	}
	
	/**
	 * Eliminate this player from the game
	 */
	public void eliminate() {
		isEliminated = true;
	}
	
	/**
	 * Compare a given array of cards against this player's hand, 
	 * and report if there exist any matches.
	 * @param cards
	 * @return
	 */
	public String compareCards(Card[] cards) {
		String output = "";
		for (Card c1 : hand) {
			for (Card c2 : cards) {
				if (c1.equals(c2)) {
					output+="Player " + getPlayerName() + " has matching card " + c1.toString();
					return output;
				}
			}
		}
		output+=("Player "+ getPlayerName() + " has no matching cards");
		return output;
	}
	
	/**
	 * Move a player to a given location.
	 * @param x
	 * @param y
	 */
	public void movePlayer(int x, int y) {
		this.x=x; this.y=y;
	}
	
	/**
	 * Draw the player on a given graphics pane, at their location.
	 * @param g
	 * @param tileX
	 * @param tileY
	 */
	public void drawSelf(Graphics g, double tileX, double tileY) {
		g.setColor(new Character(uid).getColor());
		g.fillOval((int)(tileX*x), (int)(tileY*y), (int)(tileX), (int)(tileY));
		g.setColor(Color.black);
		g.drawOval((int)(tileX*x), (int)(tileY*y), (int)(tileX), (int)(tileY));
	}
	
}
