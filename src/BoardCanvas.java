import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


/**
 * The BoardCanvas is the graphical pane on which the Board is drawn.
 * Draws the board, checks for mouse clicks and movements,
 * and then moves the player and updates the hover text appropriately.
 * Can be scaled.
 * @author Edwin
 *
 */
public class BoardCanvas extends Canvas implements MouseListener, MouseMotionListener {
	
	private Board board;
	private GUI gui;

	private static final String IMAGE_PATH = "resources/";
	private static final String boardName = "board.jpg";
	private static final Image brd = loadImage(boardName);
	
	private int tileX = 36;
	private int tileY = 36;
	private static final double defaultScale = 612.0/900.0;;
	private double scale = defaultScale;
	private static final int brdSize = 900;
	
	private int clickX;
	private int clickY;
	
	public BoardCanvas(Board board, GUI gui) {
		setSize((int)(scale*brdSize),(int)(scale*brdSize));
		this.board = board;
		this.gui = gui;
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	/**
	 * Draw the Graphics pane.
	 */
	public void paint(Graphics g) {
		Image offsc = createImage((int)(scale*brdSize),(int)(scale*brdSize));
		Graphics tmp = offsc.getGraphics();
		tmp.drawImage(brd, 0, 0, (int)(brd.getWidth(null)*scale), 
				(int)(brd.getHeight(null)*scale), null);
		
		board.drawItems(tmp, scale*tileX, scale*tileY);
		tmp.setColor(Color.black);
		
		for (int i=0; i<25; i++) {
			for (int j=0; j<25; j++) {
				Rectangle rect = new Rectangle((int)(tileX*i*scale), (int)(tileY*j*scale), 
						(int)(tileX*scale), (int)(tileY*scale));
				if (clickX!=0 && rect.contains(clickX,clickY)) {
					tmp.drawRect(rect.x, rect.y, rect.width, rect.height);
				}
			}
		}
		g.drawImage(offsc,0,0,this);
	}
	
	/**
	 * Rescale the board.
	 * @param newScale
	 */
	public void scale(double newScale) {
		scale = defaultScale * newScale;
		setSize((int)(scale*brdSize),(int)(scale*brdSize));
		repaint();
	}
	
	//I had plans to animate the movement of the players, but this failed disastrously.
	/*private void animate(Player p, int x, int y) {
		animateX = x;
		animateY = y;
		int pX = p.getX();
		int pY = p.getY();
		while (animateX!=pX || animateY!= pY) {
			if (animateX<pX) {
				animateX++;
			} else if (animateX>pX) {
				animateX--;
			}
			if (animateY<pY) {
				animateY++;
			} else if (animateY>pY) {
				animateY--;
			}
			p.movePlayer(animateX,animateY);
			repaint((int)(animateX*tileX*scale),(int)(animateY*tileY*scale),
					(int)(tileX*scale),(int)(tileY*scale));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		animateX = -1;
	}*/
	


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

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * When the user clicks on the board, move the player if possible and update board.
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		clickX = e.getX();
		clickY = e.getY();
		int x = -1;
		int y = 0;
		for (int i=0; i<25; i++) {
			for (int j=0; j<25; j++) {
				Rectangle rect = new Rectangle((int)(tileX*i*scale), (int)(tileY*j*scale), 
						(int)(tileX*scale), (int)(tileY*scale));
				if (rect.contains(clickX,clickY)) {
					x = i;
					y = j;
				}
			}
		}
		if (x!=-1) {
			//animate code, non-working
			/*int initX = gui.getTurn().getX();
			int initY = gui.getTurn().getY();*/
			int moves = board.movePlayer(gui.getTurn(), x, y, gui.getDie());
			/*if (moves>-1) {
				animate(gui.getTurn(),initX,initY);
			}*/
			gui.tryMove(moves);
			repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		
	}

	/**
	 * When the user moves the mouse, find the appropriate tile and update hover text.
	 */
	@Override
	public void mouseMoved(MouseEvent arg0) {
		int x = arg0.getX();
		int y = arg0.getY();
		for (int i = 0; i < 25; i++) {
			for (int j = 0; j < 25; j++) {
				Rectangle rect = new Rectangle((int) (tileX * i * scale),
						(int) (tileY * j * scale), (int) (tileX * scale),
						(int) (tileY * scale));
				if (rect.contains(x, y)) {
					gui.hover(board.hover(i, j));
				}
			}
		}
	}
	
}
