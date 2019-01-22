import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ToolTipManager;

/**
 * The GUI (Graphical User Interface) displays all information related to the Cluedo game
 * to the user, and also obtains all input from the user, except that by the BoardCanvas.
 * This involves asking the user for their players' names and characters,
 * displaying the interactive buttons and informative text fields,
 * and asking the user for their suggestions and accusations.
 * 
 * @author Edwin
 *
 */
public class GUI {
	
	private JFrame frame;
	private Game game;
	private BoardCanvas canvas;
	private JTextArea text;
	private JTextField hover;
	private JLabel dieLabel;
	private JPanel handPanel;
	private JPanel rightPanel;
	private Player turn;
	private int die;
	private JButton suggestButton;
	private JMenuItem suggestItem;
	private List<JButton> buttons;
	private List<JLabel> separators;
	private JTextArea header;
	
	private String action = "";
	private double scale = 1;

	private static final int maxPlayers = 6;
	private static final int minPlayers = 3;
	private static final int dialogWidth = 300;
	private static final int dialogHeight = 250;
	private static final int frameWidth = 1300;
	private static final int frameHeight = 700;
	private static final int rightPanelWidth = 500;
	private static final int rightPanelHeight = 600;
	private static final int separatorSize = 15;
	private static final String IMAGE_PATH = "resources/";
	private static final String dicePath = "Dice/";
	
	private Font FONT = new Font("Arial", Font.BOLD, 12);
	
	public GUI() {
		frame = new JFrame("Cluedo");
	}
	
	/**
	 * Start the game of Cluedo, creating the GUI with buttons, text fields,
	 * menu bar and board.
	 * @param game
	 */
	public void startGame(Game game) {
		this.game = game;
		frame = new JFrame();
	    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    frame.addWindowListener(new java.awt.event.WindowAdapter() {
	        @Override
	        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	        	exit();
	        }
	    });
	    frame.setTitle("Cluedo");
	    frame.setSize(frameWidth, frameHeight);
	    frame.setLayout(new FlowLayout());
	    frame.addComponentListener(new Components());
	    
	    Keys keys = new Keys();
	    addMenu(keys);
	    
	    canvas = new BoardCanvas(game.board, this);
	    canvas.addKeyListener(keys);
	    JPanel canvasPanel = new JPanel();
	    canvasPanel.add(canvas);
	    frame.add(canvasPanel);
	    frame.addKeyListener(keys);
	    
	    rightPanel = new JPanel();
	    rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
	    rightPanel.setPreferredSize(new Dimension(rightPanelWidth,rightPanelHeight));
	    
	    header = new JTextArea("Cluedo\nby Edwin Phease");
	    header.setFont(FONT);
	    header.setEditable(false);
	    rightPanel.add(header);
	    
	    separators = new ArrayList<JLabel>();
	    addSeparator(rightPanel);
	    
	    text = new JTextArea("");
	    text.setFont(FONT);
	    text.setEditable(false);
	    rightPanel.add(text);

	    addSeparator(rightPanel);
	    dieLabel = new JLabel();
	    rightPanel.add(dieLabel);
	    addSeparator(rightPanel);
	    
	    addButtons(rightPanel, keys);
	    
	    hover = new JTextField("");
	    hover.setFont(FONT);
	    hover.setEditable(false);
	    rightPanel.add(hover);
	    
	    frame.add(rightPanel);
	    frame.setVisible(true);
	    canvas.repaint();
	}
	
	/**
	 * Add the JMenuBar and its items to the frame.
	 * @param keys
	 */
	private void addMenu(KeyListener keys) {
		JMenuBar menubar = new JMenuBar();
	    //make menubar appear above canvas
	    JPopupMenu.setDefaultLightWeightPopupEnabled(false);
	    ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
	    
	    JMenu file = new JMenu("File");
	    JMenuItem restart = new JMenuItem("Restart");
	    restart.setActionCommand("menuRestart");
	    restart.addActionListener(new listener());
	    JMenuItem exit = new JMenuItem("Exit");
	    exit.setActionCommand("menuExit");
	    exit.addActionListener(new listener());
	    file.add(restart);
	    file.add(exit);
	    menubar.add(file);
	    
	    JMenu gameMenu = new JMenu("Game");
	    JMenuItem endTurnItem = new JMenuItem("End turn");
	    endTurnItem.setActionCommand("endTurn");
	    endTurnItem.addActionListener(new listener());
	    suggestItem = new JMenuItem("Suggest");
	    suggestItem.setActionCommand("suggest");
	    suggestItem.setEnabled(false);
	    suggestItem.addActionListener(new listener());
	    JMenuItem accuseItem = new JMenuItem("Accuse");
	    accuseItem.setActionCommand("accuse");
	    accuseItem.addActionListener(new listener());
	    gameMenu.add(endTurnItem);
	    gameMenu.add(suggestItem);
	    gameMenu.add(accuseItem);
	    menubar.add(gameMenu);
	    
	    JMenuItem help = new JMenuItem("Hotkeys");
	    help.setActionCommand("help");
	    help.addActionListener(new listener());
	    JMenu helpMenu = new JMenu("Help");
	    helpMenu.add(help);
	    menubar.add(helpMenu);
	    frame.setJMenuBar(menubar);

	    restart.addKeyListener(keys);
	    exit.addKeyListener(keys);
	    help.addKeyListener(keys);
	    endTurnItem.addKeyListener(keys);
	    suggestItem.addKeyListener(keys);
	    accuseItem.addKeyListener(keys);
	}
	
	/**
	 * Add the 3 buttons to the rightPanel.
	 * @param rightPanel
	 * @param keys
	 */
	private void addButtons(JPanel rightPanel, KeyListener keys) {
		buttons = new ArrayList<JButton>();

		JButton endTurn = new JButton("End turn");
		endTurn.setActionCommand("endTurn");
		endTurn.addActionListener(new listener());
	    endTurn.addKeyListener(keys);
	    endTurn.setFont(FONT);
	    rightPanel.add(endTurn);
	    buttons.add(endTurn);
	    addSeparator(rightPanel);
	    
	    suggestButton = new JButton("Make a suggestion");
	    suggestButton.setActionCommand("suggest");
	    suggestButton.addActionListener(new listener());
	    suggestButton.setEnabled(false);
	    suggestButton.addKeyListener(keys);
	    suggestButton.setFont(FONT);
	    rightPanel.add(suggestButton);
	    buttons.add(suggestButton);
	    addSeparator(rightPanel);
	    
	    JButton accuseButton = new JButton("Make an accusation!");
	    accuseButton.setActionCommand("accuse");
	    accuseButton.addActionListener(new listener());
	    accuseButton.addKeyListener(keys);
	    accuseButton.setFont(FONT);
	    rightPanel.add(accuseButton);
	    buttons.add(accuseButton);
	    addSeparator(rightPanel);

	    handPanel = new JPanel();
	    handPanel.setLayout(new BoxLayout(handPanel, BoxLayout.Y_AXIS));
	    handPanel.setPreferredSize(new Dimension(frameWidth/3,frameHeight/2));
	    rightPanel.add(handPanel);
	    addSeparator(rightPanel);
	}
	
	/**
	 * Display a player's turn beginning, by updating the turn text, die and hand.
	 * @param p
	 */
	public void takeTurn(Player p) {
		this.turn = p;
		updateHand();
		text.setText("Player " + p.getPlayerName() + 
				" (" + p.getName() + "), please take your turn.");
		die = new Random().nextInt(6)+1;
		updateDie();
		suggestButton.setEnabled(game.board.isRoom(p.getX(), p.getY()));
		suggestItem.setEnabled(game.board.isRoom(p.getX(), p.getY()));
		
		frame.repaint();
		if (p.isEliminated()) {
			game.endTurn();
		}
	}
	
	/**
	 * Draw all the cards in the current player's hand, scaled appropriately.
	 */
	private void updateHand() {
		handPanel.removeAll();
		JPanel row = new JPanel();
		handPanel.add(row);
		int i=0;
		for (Card c : turn.getHand()) {
			JLabel cardLabel = new JLabel();
			Image image = c.getImage();
			int width = (int)(image.getWidth(null)*scale);
			int height = (int)(image.getHeight(null)*scale);
			image = getScaledImage(image,width,height);
			cardLabel.setIcon(new ImageIcon(image));
			row.add(cardLabel);
			i++;
			if (i>4) {
				row = new JPanel();
				handPanel.add(row);
				i=0;
			}
		}
	}
	
	/**
	 * Given a image, rescale it to size.
	 * This method courtesy of stackoverflow.
	 * @param srcImg
	 * @param w
	 * @param h
	 * @return
	 */
	private Image getScaledImage(Image srcImg, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
	    		RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();
	    return resizedImg;
	}
	
	/**
	 * When a player tries to move, update the die and text appropriately.
	 * @param moves
	 */
	public void tryMove(int moves) {
		if (moves>-1) {
			die-=moves;
			updateDie();
			if (die>0) {
				text.setText("Player " + turn.getPlayerName() + 
					" (" + turn.getName() + ") moved, "+die+" moves remaining.");
			} else {
				text.setText("Player " + turn.getPlayerName() + 
						" (" + turn.getName() + ") moved, no moves remaining.");
			}
		} else {
			if (die>0) {
				text.setText("Player " + turn.getPlayerName() + " (" + turn.getName() 
						+ "), that was an invalid move, "+die+" moves remaining.");
			} else {
				text.setText("Player " + turn.getPlayerName() + 
						" (" + turn.getName() + "), you cannot move, no remaining moves.");
			}
		}
		suggestButton.setEnabled(game.board.isRoom(turn.getX(), turn.getY()));
		suggestItem.setEnabled(game.board.isRoom(turn.getX(), turn.getY()));
	}
	
	/**
	 * Display the hover text for when the user hovers the mouse over the board.
	 * @param text
	 */
	public void hover(String text) {
		hover.setText("Hover: "+text);
	}
	
	public Player getTurn(){
		return turn;
	}
	
	public int getDie() {
		return die;
	}
	
	/**
	 * Add a separator to a JPanel.
	 * @param panel
	 */
	private void addSeparator(JPanel panel) {
		JLabel separator = new JLabel();
		separator.setPreferredSize(new Dimension(separatorSize,separatorSize));
	    if (separators!=null) separators.add(separator);
	    panel.add(separator);
	}
	
	/**
	 * Initialise the players in the game, by asking the user for input as to
	 * the number of players, and each player's name and chosen character.
	 * @return
	 */
	public List<Player> initialisePlayers() {
		Object[] possibilities = new Object[maxPlayers - minPlayers + 1];
		for (int i=minPlayers; i<=maxPlayers; i++) {
			possibilities[i-minPlayers] = i;
		}
		int nPlayers;
		Object choice = JOptionPane.showInputDialog(frame,
				"Welcome to Cluedo! \nby Edwin Phease \n\n"
				+ "Select number of players (3-6).",
				"Cluedo", JOptionPane.PLAIN_MESSAGE,null,possibilities,minPlayers);
		try {
			nPlayers = (int) choice;
		} catch(NullPointerException e) {
			nPlayers = 0;
		}
		while (nPlayers<minPlayers || nPlayers > maxPlayers) {
			choice =  JOptionPane.showInputDialog(frame,
					"Choice not made, please select number of players (3-6).",
					"Cluedo", JOptionPane.PLAIN_MESSAGE,null,possibilities,minPlayers);
			try {
				nPlayers = (int) choice;
			} catch(NullPointerException e) {
				nPlayers = 0;
			}
		}
		
		// create players
		List<Player> players = new ArrayList<Player>();
		List<Integer> chosen = new ArrayList<Integer>();
		for (int i = 1; i <= nPlayers; i++) {
			String name = chooseName(i);
			Integer character = chooseCharacter(name,chosen);
			players.add(new Player(character, name));
		}
		// fill in remaining players with non-active players
		for (int i = 0; i < maxPlayers; i++) {
			if (!chosen.contains(i)) players.add(new Player(i));
		}
		return players;
	}
	
	/**
	 * Let a player choose their name as a String.
	 * @param i
	 * @return
	 */
	private String chooseName(int i) {
		String name;
		name = (String) JOptionPane.showInputDialog(frame,
				"Player " + i +", please enter your desired name.",
				"Cluedo", JOptionPane.PLAIN_MESSAGE,null,null,null);
		while (name == null || name.equals("")) {
			name = (String)  JOptionPane.showInputDialog(frame,
					"Choice not made, Player " + i +", please re-enter your desired name.",
					"Cluedo", JOptionPane.PLAIN_MESSAGE,null,null,null);
		}
		return name;
	}
	
	/**
	 * Let a user choose which character to play as.
	 * Uses RadioButtons, instead of the superior inputDialog.
	 * @param name
	 * @param chosen
	 * @return
	 */
	private int chooseCharacter(String name, List<Integer> chosen) {
	    ButtonGroup group = new ButtonGroup();
	    JPanel panel = new JPanel();
	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	    JTextField text = new JTextField("Player "+name+", please select desired character.");
	    text.setFont(FONT);
	    text.setEditable(false);
	    panel.add(text);
	    addSeparator(panel);
	    
	    for (int i=0; i<maxPlayers; i++) {
			JRadioButton button = new JRadioButton(new Character(i).toString());
			button.setActionCommand("char"+i);
			if (chosen.contains(i)) button.setEnabled(false);
			button.addActionListener(new listener());
			group.add(button);
			panel.add(button);
	    }
	    
	    frame = new JFrame();
	    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    frame.addWindowListener(new java.awt.event.WindowAdapter() {
	        @Override
	        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	            exit();
	        }
	    });
	    frame.setTitle("Cluedo");
	    frame.add(panel);
	    frame.setVisible(true);
	    frame.setSize(dialogWidth, dialogHeight);
	    //put frame in centre of screen
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    frame.setLocation(dim.width/2-frame.getSize().width/2, 
	    		dim.height/2-frame.getSize().height/2);
	    //sleep until user presses a button
		while (!action.startsWith("char")) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		int character = Integer.parseInt(action.substring(4));
		action = "";
		chosen.add(character);
		frame.dispose();
		return character;
	}
	
	/**
	 * After confirming with the user, exit the game.
	 */
	public void exit() {
        if (JOptionPane.showConfirmDialog(frame, 
            "Are you sure you wish to close Cluedo?", "Cluedo", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
            System.exit(0);
        }
	}
	
	/**
	 * Restart the game.
	 */
	public void restart() {
		JOptionPane.showConfirmDialog(frame, 
				"To restart, simply close the game and reopen it."
				+ "\n(I couldn't get the restart working :( )", 
				"Cluedo",JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);
		//frame.dispose();
		//game.play();
	}
	
	/**
	 * If in a room, let the current player make a suggestion as to the solution.
	 * This involves asking the user for their suggested 3 cards,
	 * and comparing them to the other players' hands for matches.
	 * Upon finishing a suggestion, the user can either make an accusation,
	 * or end their turn.
	 */
	public void suggest() {
		if (!game.board.isRoom(turn.getX(), turn.getY())) {
			return;
		}
		Board board = game.board;
		String room = board.getLoc(turn.getX(), turn.getY());
		int suggestStart = JOptionPane.showConfirmDialog(frame, 
				"Player "+turn.getPlayerName()+", making a suggestion.\n"+
				"Suggested room: "+room+"."
				, "Cluedo Suggestion",
				JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);
		if (suggestStart!=JOptionPane.OK_OPTION) {
			return;
		}
		Object[] possibilities = Weapon.listAsObject();
		String weapon =(String) JOptionPane.showInputDialog(frame,"Enter a weapon", 
				"Cluedo Suggestion",JOptionPane.PLAIN_MESSAGE,null,possibilities,
				possibilities[0]);
		while (weapon == null) {
			weapon =(String) JOptionPane.showInputDialog(frame,
					"Weapon not selected, please enter a weapon", "Cluedo Suggestion",
					JOptionPane.PLAIN_MESSAGE,null,possibilities, possibilities[0]);
		}
		board.moveObj(weapon,room); //move this weapon to this room
		canvas.repaint();
		
		possibilities = Character.listAsObject();
		String character =(String) JOptionPane.showInputDialog(frame,"Enter a character", 
				"Cluedo Suggestion",JOptionPane.PLAIN_MESSAGE,null,possibilities,
				possibilities[0]);
		while (character == null) {
			character =(String) JOptionPane.showInputDialog(frame,
					"Character not selected, please enter a character", "Cluedo Suggestion",
					JOptionPane.PLAIN_MESSAGE,null,possibilities, possibilities[0]);
		}
		board.moveObj(character,room); //move this character to this room
		canvas.repaint();
		
		Card[] cards = new Card[]{new Room(room),new Weapon(weapon),new Character(character)};
		String output = game.compareToOtherPlayers(turn.getUid(), cards);
		JOptionPane.showConfirmDialog(frame, output, "Cluedo Suggestion",
				JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);
		
		int end = JOptionPane.showOptionDialog(frame, 
		        "Now, make an accusation?", 
		        "Cluedo Suggestion", 
		        JOptionPane.OK_CANCEL_OPTION, 
		        JOptionPane.INFORMATION_MESSAGE, 
		        null, 
		        new String[]{"Accuse!", "No, end turn."},
		        "No, end turn.");
		if (end==JOptionPane.OK_OPTION) {
			if (!accuse()) {
				game.endTurn();
			}
		} else {
			game.endTurn();
		}
	}
	
	/**
	 * Make an accusation as to the solution of the Cluedo game.
	 * This involves entering the suspected 3 cards,
	 * and checking them against the solution envelope.
	 * If correct, the game ends and this player wins, else this player is eliminated.
	 * Returns false if the accusation is cancelled.
	 * @return
	 */
	public boolean accuse() {
		int suggestStart = JOptionPane.showConfirmDialog(frame, 
				"Player "+turn.getPlayerName()+", ACCUSATION BEGINNING!\n"+
				"Press OK to confirm. ", "CLUEDO ACCUSATION",
				JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);
		if (suggestStart!=JOptionPane.OK_OPTION) {
			return false;
		}
		Object[] possibilities = Room.listAsObject();
		String room =(String) JOptionPane.showInputDialog(frame,"Enter a room", 
				"CLUEDO ACCUSATION",JOptionPane.PLAIN_MESSAGE,null,possibilities,
				possibilities[0]);
		while (room == null) {
			room =(String) JOptionPane.showInputDialog(frame,
					"Room not selected, please enter a room", "CLUEDO ACCUSATION",
					JOptionPane.PLAIN_MESSAGE,null,possibilities, possibilities[0]);
		}
		possibilities = Weapon.listAsObject();
		String weapon =(String) JOptionPane.showInputDialog(frame,"Enter a weapon", 
				"CLUEDO ACCUSATION",JOptionPane.PLAIN_MESSAGE,null,possibilities,
				possibilities[0]);
		while (weapon == null) {
			weapon =(String) JOptionPane.showInputDialog(frame,
					"Weapon not selected, please enter a weapon", "CLUEDO ACCUSATION",
					JOptionPane.PLAIN_MESSAGE,null,possibilities, possibilities[0]);
		}
		possibilities = Character.listAsObject();
		String character =(String) JOptionPane.showInputDialog(frame,"Enter a character", 
				"CLUEDO ACCUSATION",JOptionPane.PLAIN_MESSAGE,null,possibilities,
				possibilities[0]);
		while (character == null) {
			character =(String) JOptionPane.showInputDialog(frame,
					"Character not selected, please enter a character", "CLUEDO ACCUSATION",
					JOptionPane.PLAIN_MESSAGE,null,possibilities, possibilities[0]);
		}
		String output = game.checkWin(turn, new Room(room), new Character(character), 
				new Weapon(weapon));
		if (!output.endsWith("GAME OVER")) {
			turn.eliminate();
			output+=("\nPLAYER " + turn.getPlayerName() + " IS ELIMINATED");
		}
		JOptionPane.showConfirmDialog(frame, output, "CLUEDO ACCUSATION",
				JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);
		if (output.endsWith("GAME OVER")) {
			System.exit(0);
		}
		game.endTurn();
		return true;
	}
	
	/**
	 * Display the help.
	 */
	private void help() {
		JOptionPane.showConfirmDialog(frame, "Cluedo Help\n"+
				"Hotkeys:\nSpace = End turn\nS = Suggest\nA = Accuse\nESC = Exit\nH = Help", 
				"Cluedo",JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * End the game when all players but one are eliminated.
	 * @param winner
	 */
	public void endGame(Player winner) {
		JOptionPane.showConfirmDialog(frame, "EVERYONE EXCEPT PLAYER "+winner.getPlayerName()
				+ " IS OUT!\nPLAYER " + winner.getPlayerName() + " WINS", 
				"GAME OVER",
				JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);
		System.exit(0);
	}
	
	/**
	 * A multi-purpose listener for buttons with set ActionCommands.
	 * Used by the chooseCharacter radioButtons and the main GUI's buttons.
	 * @author Edwin
	 *
	 */
	private class listener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			if (cmd.startsWith("char")) {
				action = cmd;
			} else if (cmd.startsWith("menu")) {
				if (cmd.substring(4).equals("Restart")) {
					restart();
				} else if (cmd.substring(4).equals("Exit")) {
					exit();
				}
			} else if (cmd.equals("endTurn")) {
				game.endTurn();
			} else if (cmd.equals("suggest")) {
				suggest();
			} else if (cmd.equals("accuse")) {
				accuse();
			} else if (cmd.equals("help")) {
				help();
			}
		}
	}
	
	/**
	 * A KeyListener for the hotkeys.
	 * @author Edwin
	 *
	 */
	private class Keys implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
			int key = arg0.getKeyCode();
			if (key==KeyEvent.VK_SPACE) {
				game.endTurn();
			} else if (key==KeyEvent.VK_S) {
				suggest();
			} else if (key==KeyEvent.VK_A) {
				accuse();
			} else if (key==KeyEvent.VK_ESCAPE) {
				exit();
			} else if (key==KeyEvent.VK_H) {
				help();
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
		}
	}
	
	/**
	 * A ComponentListener for detecting when the window is resized, and
	 * resizing the components of the GUI appropriately.
	 * @author Edwin
	 *
	 */
	private class Components implements ComponentListener {

		@Override
		public void componentHidden(ComponentEvent arg0) {
		}

		@Override
		public void componentMoved(ComponentEvent arg0) {
		}

		@Override
		public void componentResized(ComponentEvent arg0) {
			int width = frame.getWidth();
			int height = frame.getHeight();
			if (width!=frameWidth || height!= frameHeight) {
			    handPanel.setPreferredSize(new Dimension(width/3,height/2));
			    scale = Math.min(((double)(width)/(double)(frameWidth)),
			    		((double)(height)/(double)(frameHeight)));
				FONT = new Font("Arial", Font.BOLD, (int)(scale*12));
				header.setFont(FONT);
				text.setFont(FONT);
				hover.setFont(FONT);
				for (JButton b : buttons) {
					b.setFont(FONT);
				}
				for (JLabel s : separators) {
					s.setPreferredSize(new Dimension((int)(scale*separatorSize)
							,(int)(scale*separatorSize)));
				}
				updateDie();
			    rightPanel.setPreferredSize(new Dimension(
			    		(int)(scale*rightPanelWidth),(int)(scale*rightPanelHeight)));
			    canvas.scale(scale);
			    updateHand();
			}
		}

		@Override
		public void componentShown(ComponentEvent arg0) {
		}
		
	}
	
	
	private static final List<Image> dice = initialiseDice();
	
	/**
	 * Preload the die images to save processing time.
	 * @return
	 */
	private static List<Image> initialiseDice() {
		List<Image> dice = new ArrayList<Image>();
		for (int i=0; i<=6; i++) {
			dice.add(loadImage(dicePath+i+".png"));
		}
		return dice;
	}
	
	/**
	 * Update the image of the die, scaled appropriately.
	 * @param die
	 * @return
	 */
	private void updateDie() {
		Image dieImg = dice.get(die);
		dieImg = getScaledImage(dieImg,(int)(dieImg.getWidth(null)*scale),
				(int)(dieImg.getHeight(null)*scale));
		dieLabel.setIcon(new ImageIcon(dieImg));
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
