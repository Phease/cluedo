import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class Tests {
	Player p1 = new Player(0,"0");
	Player p2 = new Player(1,"1");
	List<Player> players = new ArrayList<Player>();
	Board board;
	
	@Before
	public void setup() {
		players.add(p1);
		players.add(p2);
		board = new Board(players);
	}
	
	@Test
	public void testValidStart() {
		assertTrue(board.isValidMove(7, 24, 7, 23));
	}
	
	@Test
	public void testInvalidMoveOffscreen() {
		assertFalse(board.isValidMove(7, 24, 7, 25));
	}
	
	@Test
	public void testInvalidMoveFromStartToWall() {
		assertFalse(board.isValidMove(7, 24, 6, 24));
	}
	
	@Test
	public void testValidCorridorMove() {
		assertTrue(board.isValidMove(7, 23, 7, 22));
	}
	
	@Test
	public void testInvalidMoveFromCorridorToRoom() {
		assertFalse(board.isValidMove(7, 23, 6, 23));
	}
	
	@Test
	public void testValidMoveFromCorridorToDoor() {
		assertTrue(board.isValidMove(7, 19, 6, 19));
	}
	
	@Test
	public void testValidMoveFromDoorToRoom() {
		assertTrue(board.isValidMove(6, 19, 5, 19));
	}
	
	@Test
	public void testInvalidMoveFromRoomToCorridor() {
		assertFalse(board.isValidMove(5, 19, 5, 18));
	}
	
	@Test
	public void testInvalidMoveFromCorridorToWall() {
		assertFalse(board.isValidMove(9, 16, 10, 16));
	}
	
	@Test
	public void testValidSecretPassage() {
		assertTrue(board.isValidMove(1, 19, 0, 19));
	}
	
	@Test
	public void testInvalidSecretPassage() {
		assertFalse(board.isValidMove(23, 6, 23, 5));
	}
	
	@Test
	public void testInvalidPlayerBlocks() {
		p2.movePlayer(8,17);
		p1.movePlayer(7,17);
		assertFalse(board.isValidMove(7, 17, 8, 17));
	}
	
	@Test
	public void testInvalidSecretPassageBlocked1() {
		p2.movePlayer(0, 19);
		p1.movePlayer(1, 19);
		assertFalse(board.isValidMove(1, 19, 0, 19));
	}
	
	@Test
	public void testInvalidSecretPassageBlocked2() {
		p2.movePlayer(23, 5);
		p1.movePlayer(1, 19);
		assertFalse(board.isValidMove(1, 19, 0, 19));
	}
	
	@Test
	public void testValidRoom1() {
		try {
			new Room(0);
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testValidRoom2() {
		try {
			new Room("ballroom");
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testInvalidRoom1() {
		try {
			new Room(-1);
			fail();
		} catch (Exception e) {
		}
	}
	
	@Test
	public void testInvalidRoom2() {
		try {
			new Room("fake");
			fail();
		} catch (Exception e) {
		}
	}
	
	@Test
	public void testValidWeapon1() {
		try {
			new Weapon(0);
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testValidWeapon2() {
		try {
			new Weapon("revolver");
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testInvalidWeapon1() {
		try {
			new Weapon(-1);
			fail();
		} catch (Exception e) {
		}
	}
	
	@Test
	public void testInvalidWeapon2() {
		try {
			new Room("fake");
			fail();
		} catch (Exception e) {
		}
	}
	
	@Test
	public void testValidCharacter1() {
		try {
			new Character(0);
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testValidCharacter2() {
		try {
			new Character("plum");
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testInvalidCharacter1() {
		try {
			new Character(-1);
			fail();
		} catch (Exception e) {
		}
	}
	
	@Test
	public void testInvalidCharacter2() {
		try {
			new Character("fake");
			fail();
		} catch (Exception e) {
		}
	}
	
}
