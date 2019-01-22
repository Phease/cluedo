import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A Cluedo game implementation, by Edwin Phease.
 * The Game class holds the board, the envelope and the list of players.
 * It will repeatedly take turns until the game ends.
 * Also supports checking player's suggestions and accusations.
 * @author Edwin
 *
 */
public class Game {

	public Board board;
	public GUI gui;
	
	private List<Player> players = new ArrayList<Player>();
	private List<Card> envelope; //this holds the solution to the game
	private Player winner = null;
	private int turn = 0; //represents which player's turn it is, player 0 always goes first
	private boolean endTurn = false;
	
	private static final int nChars = 6;
	private static final int nWeapons = 6;
	private static final int nRooms = 9;
	
	/**
	 * The main playing method, calls initialiseGame() and then repeatedly
	 * gets each player to take their turns, checking if the game is over at each turn.
	 */
	public void play() {
		initialiseGame();
		while (winner == null) { // no winner means game is not over
			// players.get(turn).takeTurn(scan, this);
			if (players.get(turn).isPlaying()) {
				gui.takeTurn(players.get(turn));
				while (!endTurn) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				endTurn = false;
			}
			turn++;
			// once each player has taken their turn, it is player 0's turn
			// again
			if (turn >= players.size()) {
				turn = 0;
			}
			// if multiple players, when all but 1 are eliminated, that 1 is
			// the winner
			if (players.size() > 1) {
				int nAlive = 0;
				Player winner = null;
				for (Player p : players) {
					if (p.isPlaying() && !p.isEliminated()) {
						nAlive++;
						winner = p;
					}
				}
				if (nAlive == 1) {
					gui.endGame(winner);
					this.winner = winner;
				}
			}

		}
	}
	
	public void endTurn() {
		this.endTurn = true;
	}
	
	/**
	 * Initialise the game state. This involves:
	 * getting number of players.
	 * creating an initial board state.
	 * creating the players, who place themselves in the appropriate starting locations.
	 * creating the list of cards in the game.
	 * placing the 3 solution cards in the envelope.
	 * dealing the remaining cards out to each player.
	 */
	public void initialiseGame() {
		gui = new GUI();
		//get number of players required
		players = gui.initialisePlayers();
		//create list of cards, envelope
		List<Card> cards = new ArrayList<Card>();
		for (int i=0; i<nWeapons; i++) {
			cards.add(new Weapon(i));
		}
		for (int i=0; i<nChars; i++) {
			cards.add(new Character(i));
		}
		for (int i=0; i<nRooms; i++) {
			cards.add(new Room(i));
		}
		createEnvelope(cards);
		//deal out the remaining cards evenly to each player
		int i = 0;
		while (i<cards.size()) {
			for (Player p : players) {
				if (p.isPlaying()) {
					p.dealCard(cards.get(i));
					i++;
					if (i >= cards.size()) break;
				}
			}
		}
		board = new Board(players);
		
		gui.startGame(this);
	}
	
	/**
	 * Given a list of cards, this creates the solution envelope,
	 * with a randomly chosen character, weapon and room within.
	 * @param cards
	 */
	public void createEnvelope(List<Card> cards) {
		envelope = new ArrayList<Card>();
		Collections.shuffle(cards);
		for (Card c : cards) {
			if (c instanceof Character) {
				envelope.add(c);
				cards.remove(c);
				break;
			}
		}
		for (Card c : cards) {
			if (c instanceof Weapon) {
				envelope.add(c);
				cards.remove(c);
				break;
			}
		}
		for (Card c : cards) {
			if (c instanceof Room) {
				envelope.add(c);
				cards.remove(c);
				break;
			}
		}
		//for testing a correct accusation
		//System.out.println(envelope.toString());
	}
	
	/**
	 * Given a player id and some suggested cards, this checks all other players to see
	 * if they possess a card that matches the suggested cards given.
	 * @param uid
	 * @param cards
	 * @return
	 */
	public String compareToOtherPlayers(int uid, Card[] cards) {
		int pId = 0;
		for (int j=0; j<players.size(); j++) {
			if (players.get(j).getUid()==uid) {
				pId=j;
				break;
			}
		}
		int i = pId +1;
		String output ="";
		while (i!=pId) {
			if (i>=players.size()) {
				i=0;
			} else {
				if (players.get(i).isPlaying()) {
					output += "\nComparing to Player "
							+ players.get(i).getPlayerName() + "\n";
					output += players.get(i).compareCards(cards);
					if (!output.endsWith("no matching cards")) return output;
				}
				i++;
			}
		}
		output+="\nComparation complete, no matches were found";
		return output;
	}
	
	/**
	 * When a player makes a specific accusation, this checks the accusation
	 * against the envelope to see if the player wins or is eliminated.
	 * @param player
	 * @param room
	 * @param character
	 * @param weapon
	 * @return
	 */
	public String checkWin(Player player, Room room, Character character, Weapon weapon) {
		String output = "";
		if (envelope.get(0).equals(character)) {
			output+=("Character " + character.toString() + " was correct!");
		} else {
			output+=("Character " + character.toString() + " was WRONG!");
			return output;
		}
		if (envelope.get(1).equals(weapon)) {
			output+=("\nWeapon " + weapon.toString() + " was correct!");
		} else {
			output+=("\nWeapon " + weapon.toString() + " was WRONG!");
			return output;
		}
		if (envelope.get(2).equals(room)) {
			output+=("\nRoom " + room.toString() + " was correct!");
		} else {
			output+=("\nRoom " + room.toString() + " was WRONG!");
			return output;
		}
		output+=("\nCONGRATULATIONS TO PLAYER " + player.getUid());
		output+=("\nGAME OVER");
		winner = player;
		return output;
	}
	
	
	
}
