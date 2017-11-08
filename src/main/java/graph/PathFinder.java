package graph;


import java.util.*;
import java.util.stream.Collectors;

public class PathFinder {

	private Node start;
	private Node finish;

	public PathFinder(Node from, Node to) {
		if (from == null || to == null) {
			throw new IllegalArgumentException("shouldn't be null");
		}
		this.start = from;
		this.finish = to;
	}

	private State explorePath(State state, boolean singePath) {
		walk(state, singePath);
		while (!state.endsWith(finish)) {
			if (!state.stepBack()) {
				break;
			}
			walk(state, singePath);
		}
		return state;
	}

	private void walk(State state, boolean singePath) {
		PathElement current = state.getLastInPath();
		if (current.sameNode(finish)) {
			return;
		}
		if (singePath && current.hasNeighborNode(finish)) {
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
				if (singePath && current.hasNeighborNode(finish)) {
					state.add(new PathElement(finish));
					break;
				}
			}
		}
	}

	public List<List<Node>> findAllPaths() {
		List<List<Node>> paths = new ArrayList<>();

		State state = new State();
		state.add(new PathElement(start));
		do {
			if (state.getLastInPath().hasNext()) {
				state.syncVisited();
				paths.add(explorePath(state, false).getNodeList());
			}
		} while (state.stepBack());
		paths.sort(Comparator.comparingInt(List::size));
		return paths;
	}

	public List<Node> findFirstPath() {
		State state = new State();
		state.add(new PathElement(start));
		return explorePath(state, true).getNodeList();
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

		boolean hasNext() {
			return node.getNeighborsSize() > neighborIndex;
		}

		PathElement next() {
			PathElement next = hasNext() ? new PathElement(node.getNeighbor(neighborIndex)) : null;
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

		private void syncVisited() {
			this.visited.retainAll(this.list);
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
