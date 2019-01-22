import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


/**
 * The Weapon card can be one of the 6 Weapons in Cluedo.
 * Can be returned as a string and compared to other cards for equality.
 * Also supports statically drawing a list of all the different weapons.
 * Not to be confused with WeaponLoc.
 * @author Edwin
 *
 */
public class Weapon implements Card {
	
	private int val;
	private static String[] weapons = {"Candlestick","Dagger","Pipe","Revolver","Rope","Spanner"};
	private static final String IMAGE_PATH = "resources/";
	private Image image;
	
	public Weapon (int i) {
		if (i<0||i>=weapons.length) throw new RuntimeException();
		val = i;
		image = loadImage(toString()+".gif");
	}
	
	public Weapon (String str) {
		boolean match = false;
		for (int i=0; i<weapons.length; i++) {
			if (weapons[i].equalsIgnoreCase(str)) {
				val = i;
				match = true;
				break;
			}
		}
		if (!match) throw new RuntimeException();
	}
	
	public String toString() {
		return weapons[val];
	}
	
	/**
	 * Statically check if a given string is a valid weapon.
	 * @param room
	 * @return
	 */
	public static boolean isWeapon(String weapon) {
		for (int i=0; i<weapons.length; i++) {
			if (weapons[i].equalsIgnoreCase(weapon)) return true;
		}
		return false;
	}
	
	/**
	 * Statically list all weapons in the game.
	 * @return
	 */
	public static String listWeapons() {
		String list = "";
		for (int i=0; i<weapons.length; i++) {
			list += weapons[i] + ", ";
		}
		return list;
	}
	
	/**
	 * Returns a list of the weapons as an array of Objects.
	 * @return
	 */
	public static Object[] listAsObject() {
		return weapons;
	}
	
	@Override
	public boolean equals(Card c) {
		if (this.getClass()!=c.getClass()) return false;
		Weapon wea = (Weapon) c;
		return (wea.val==this.val);
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
