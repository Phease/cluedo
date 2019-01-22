import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;



/**
 * Represents the location of the physical weapon that is moved around during the Cluedo game.
 * Does not actually have any impact on gameplay.
 * Not to be confused with Weapon (which is a card).
 * @author Edwin
 *
 */
public class WeaponLoc {
	
	private int uid;
	private int x; //x location on board
	private int y; //y location on board
	private static final char[] weaponChars = new char[]{'c','k','p','g','r','w'};
	private static final String IMAGE_PATH = "resources/";
	private Image image;
	
	public WeaponLoc(int i) {
		this.uid = i;
		image = loadImage(getName()+".gif");
	}
	
	/**
	 * Return this weapon's name
	 * @return
	 */
	public String getName() {
		return new Weapon(uid).toString();
	}
	
	/**
	 * Return if weapon is at a given location
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isHere(int x, int y) {
		return x==this.x && y==this.y;
	}
	
	/**
	 * Return the name of the location of this weapon
	 * @param board
	 * @return
	 */
	public String getLoc(Board board) {
		return board.getLoc(x, y);
	}

	/**
	 * Move a weapon to a given location.
	 * @param x
	 * @param y
	 */
	public void moveWeapon(int x, int y) {
		this.x=x; this.y=y;
	}
	
	/**
	 * Return a char representing this weapon, for drawing
	 * @return
	 */
	public char getChar() {
		return weaponChars[uid];
	}
	
	/**
	 * Draw this weapon on a given graphics pane, at their location
	 * @param g
	 * @param tileX
	 * @param tileY
	 */
	public void drawSelf(Graphics g, double tileX, double tileY) {
		g.drawImage(image, (int)(tileX*x), (int)(tileY*y), (int)(tileX), (int)(tileY), null);
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
