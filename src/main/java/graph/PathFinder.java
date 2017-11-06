package graph;


import java.util.*;
import java.util.stream.Collectors;

public class PathFinder {

	private List<Node> path;
	private Node start;
	private Node finish;

	public PathFinder(Node from, Node to) {
		if (from == null || to == null) {
			throw new IllegalArgumentException("shouldn't be null");
		}
		this.start = from;
		this.finish = to;
	}

	private void walk(State state) {
		PathElement current = state.getLastInPath();
		if (current.sameNode(finish)) {
			return;
		}
		if (current.hasNeighborNode(finish)) {
			state.add(new PathElement(finish));
			return;
		}

		PathElement temp;
		while ((temp = current.next()) != null) {
			if (state.add(temp)) {
				current = temp;
				if (current.sameNode(finish)) {
					break;
				}
				if (current.hasNeighborNode(finish)) {
					state.add(new PathElement(finish));
					break;
				}
			}
		}
	}

	public List<Node> findFirstPath() {
		if (path == null) {
			State state = new State();
			state.add(new PathElement(start));
			walk(state);
			while (!state.endsWith(finish)) {
				if (!state.stepBack()) {
					break;
				}
				walk(state);
			}
			path = state.getNodeList();
		}
		return path;
	}

	private class PathElement {
		private Node node;
		private int neighborIndex = 0;

		PathElement(Node node) {
			this.node = node;
		}

		Node getNode() {
			return node;
		}

		PathElement next() {
			PathElement next = node.getNeighborsSize() > neighborIndex ? new PathElement(node.getNeighbor(neighborIndex)) : null;
			neighborIndex++;
			return next;
		}

		boolean sameNode(Node node) {
			return node != null && node.getName().equals(this.node.getName());
		}

		boolean hasNeighborNode(Node node) {
			return node != null && this.node.hasNeighbor(node);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			PathElement that = (PathElement) o;
			return node.getName().equals(that.node.getName());
		}

		@Override
		public int hashCode() {
			return node.getName().hashCode();
		}
	}

	private class State {
		private Deque<PathElement> list = new ArrayDeque<>();
		private Set<PathElement> visited = new HashSet<>();

		private boolean endsWith(Node node) {
			return list.getLast().sameNode(node);
		}

		private boolean stepBack() {
			list.removeLast();
			return list.size() > 0;
		}

		private PathElement getLastInPath() {
			return list.getLast();
		}

		private boolean add(PathElement element) {
			if (element != null && !visited.contains(element)) {
				list.add(element);
				visited.add(element);
				return true;
			}
			return false;
		}

		private List<Node> getNodeList() {
			return list.stream().map(PathElement::getNode).collect(Collectors.toList());
		}
	}

	public static List<Node> getPath(Node from, Node to) {
		PathFinder path = new PathFinder(from, to);
		return path.findFirstPath();
	}

}
