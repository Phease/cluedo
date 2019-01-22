import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


/**
 * The Room card can be one of the 9 rooms in Cluedo.
 * Can be returned as a string and compared to other cards for equality.
 * Also supports statically drawing a list of all the different rooms.
 * @author Edwin
 *
 */
public class Room implements Card {
	
	private int val;
	private static String[] cards = {"Kitchen","Dining","Lounge","Hall","Study","Library","Billiard","Conservatory","Ballroom"};
	private static final String IMAGE_PATH = "resources/";
	private Image image;
	
	public Room (int i) {
		if (i<0||i>=cards.length) throw new RuntimeException();
		val = i;
		image = loadImage(toString()+".gif");
	}
	
	public Room (String str) {
		boolean match = false;
		for (int i=0; i<cards.length; i++) {
			if (cards[i].equalsIgnoreCase(str)) {
				val = i;
				match = true;
				break;
			}
		}
		if (!match) throw new RuntimeException();
	}
	
	public String toString() {
		return cards[val];
	}
	
	/**
	 * Statically check if a given string is a valid room.
	 * @param room
	 * @return
	 */
	public static boolean isRoom(String room) {
		for (int i=0; i<cards.length; i++) {
			if (cards[i].equalsIgnoreCase(room)) return true;
		}
		return false;
	}
	
	/**
	 * Statically list all rooms in the game.
	 * @return
	 */
	public static String listRooms() {
		String list = "";
		for (int i=0; i<cards.length; i++) {
			list += cards[i] + ", ";
		}
		return list;
	}
	
	/**
	 * Returns a list of the rooms as an array of Objects.
	 * @return
	 */
	public static Object[] listAsObject() {
		return cards;
	}
	
	/**
	 * Statically return a clone of the list of all the different rooms
	 * @return
	 */
	public static String[] rooms() {
		return cards.clone();
	}

	@Override
	public boolean equals(Card c) {
		if (this.getClass()!=c.getClass()) return false;
		Room room = (Room) c;
		return (room.val==this.val);
	}
	
	public Image getImage() {
		return image;
	}
	
	/**
	 * Load an image from the file system, using a given filename.
	 * 
	 * @param filename
	 * @return
	 */
	public static Image loadImage(String filename) {
		Image img;
		try {
			File file = new File(IMAGE_PATH+filename);
			img = ImageIO.read(file);
		} catch (IOException e) {
			throw new RuntimeException("Unable to load image: " + filename);
		}
		return img;
	}
}
