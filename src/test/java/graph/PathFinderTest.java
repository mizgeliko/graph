package graph;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PathFinderTest {

	private Node[] nodes;

	@BeforeEach
	void before() {
		nodes = new Node[7];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new Node(String.format("node#%s", i));
		}
	}


	@Test
	void test1() {
		assertTrue(PathFinder.getPath(nodes[1], nodes[0]).isEmpty());
	}


	@Test
	void test2() {
		List<Node> path = PathFinder.getPath(nodes[0], nodes[0]);
		assertFalse(path.isEmpty());
		assertEquals(nodes[0], path.get(0));
	}


	@Test
	void test3() {
		nodes[0].setNeighbors(nodes[0], nodes[1], nodes[2]);
		nodes[1].setNeighbors(nodes[0], nodes[2], nodes[3]);

		List<Node> path = PathFinder.getPath(nodes[0], nodes[2]);
		assertFalse(path.isEmpty());
		assertEquals(2, path.size());
		assertEquals(nodes[0], path.get(0));
		assertEquals(nodes[2], path.get(1));
	}

	@Test
	void test4() {
		nodes[0].setNeighbors(nodes[0], nodes[1]);
		nodes[1].setNeighbors(nodes[1], nodes[2], nodes[3]);
		nodes[2].setNeighbors(nodes[2], nodes[0]);
		nodes[3].setNeighbors(nodes[3], nodes[2]);

		List<Node> path = PathFinder.getPath(nodes[0], nodes[3]);
		assertFalse(path.isEmpty());
		assertEquals(3, path.size());
		assertEquals(nodes[0], path.get(0));
		assertEquals(nodes[3], path.get(2));
	}

	@Test
	void test5_1() {
		nodes[0].setNeighbors(nodes[1], nodes[3]);
		nodes[1].setNeighbors(nodes[1], nodes[0], nodes[2]);
		nodes[2].setNeighbors(nodes[0], nodes[2], nodes[1]);
		nodes[3].setNeighbors(nodes[3], nodes[2], nodes[1], nodes[0], nodes[4]);
		nodes[4].setNeighbors(nodes[2], nodes[1], nodes[5]);
		nodes[5].setNeighbors(nodes[3], nodes[1]);

		List<Node> path = PathFinder.getPath(nodes[5], nodes[0]);
		assertEquals(3, path.size());
		assertEquals(nodes[5], path.get(0));
		assertEquals(nodes[0], path.get(2));

		path = PathFinder.getPath(nodes[0], nodes[5]);
		assertEquals(4, path.size());
		assertEquals(nodes[0], path.get(0));
		assertEquals(nodes[5], path.get(3));
	}

	@Test
	void test5_2() {
		nodes[0].setNeighbors(nodes[1], nodes[2]);
		nodes[1].setNeighbors(nodes[0], nodes[2], nodes[3], nodes[4]);
		nodes[2].setNeighbors(nodes[3]);
		nodes[3].setNeighbors(nodes[4], nodes[0], nodes[5]);
		nodes[4].setNeighbors(nodes[0], nodes[2], nodes[6]);
		nodes[5].setNeighbors(nodes[2], nodes[4], nodes[6]);

		List<Node> path = PathFinder.getPath(nodes[0], nodes[6]);
		assertEquals(6, path.size());
		assertEquals(nodes[0], path.get(0));
		assertEquals(nodes[6], path.get(5));

		path = PathFinder.getPath(nodes[5], nodes[1]);
		assertEquals(6, path.size());
		assertEquals(nodes[5], path.get(0));
		assertEquals(nodes[2], path.get(1));
		assertEquals(nodes[3], path.get(2));
		assertEquals(nodes[4], path.get(3));
		assertEquals(nodes[0], path.get(4));
		assertEquals(nodes[1], path.get(5));
	}

}
