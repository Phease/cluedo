import java.awt.Image;


/**
 * An interface for the 3 types of cards in Cluedo.
 * @author Edwin
 *
 */
public interface Card {
	public String toString();
	public boolean equals(Card c);
	public Image getImage();
}
