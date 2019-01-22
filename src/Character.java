import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


/**
 * The Character card can be one of the 6 characters in Cluedo.
 * Can be returned as a string and compared to other cards for equality.
 * Also supports statically drawing a list of all the different characters.
 * @author Edwin
 *
 */
public class Character implements Card {
	
	private static String[] characters = {"Scarlett","Mustard","White","Green","Peacock","Plum"};
	private int val;
	private static final String IMAGE_PATH = "resources/";
	private Image image;
	
	public Character(int i) {
		if (i<0||i>=characters.length) throw new RuntimeException();
		val = i;
		image = loadImage(toString()+".gif");
	}
	public Character (String str) {
		boolean match = false;
		for (int i=0; i<characters.length; i++) {
			if (characters[i].equalsIgnoreCase(str)) {
				val = i;
				match = true;
				break;
			}
		}
		if (!match) throw new RuntimeException();
	}
	
	public String toString() {
		return characters[val];
	}

	/**
	 * Statically check if a given string is a valid character.
	 * @param room
	 * @return
	 */
	public static boolean isCharacter(String chara) {
		for (int i=0; i<characters.length; i++) {
			if (characters[i].equalsIgnoreCase(chara)) return true;
		}
		return false;
	}
	
	/**
	 * Statically list all characters in the game.
	 * @return
	 */
	public static String listCharacters() {
		String list = "";
		for (int i=0; i<characters.length; i++) {
			list += characters[i] + ", ";
		}
		return list;
	}
	
	public Color getColor() {
		//"Scarlett","Mustard","White","Green","Peacock","Plum"};
		switch(val) {
		case 0: return Color.red;
		case 1: return Color.yellow;
		case 2: return Color.white;
		case 3: return Color.green;
		case 4: return Color.blue;
		case 5: return Color.magenta;
		default: throw new RuntimeException();
		}
	}
	
	/**
	 * Returns a list of the characters as an array of Objects.
	 * @return
	 */
	public static Object[] listAsObject() {
		return characters;
	}
	
	@Override
	public boolean equals(Card c) {
		if (this.getClass()!=c.getClass()) return false;
		Character chara = (Character) c;
		return (chara.val==this.val);
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
