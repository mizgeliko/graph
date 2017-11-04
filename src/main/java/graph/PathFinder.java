package graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PathFinder {

	private List<PathElement> list = new ArrayList<>();
	private Set<PathElement> visited = new HashSet<>();
	private Node finish;

	public PathFinder(Node from, Node to) {
		if (!add(new PathElement(from)) || to == null) {
			throw new IllegalArgumentException("shouldn't be null");
		}
		this.finish = to;
	}

	private boolean add(PathElement element) {
		if (element != null && !visited.contains(element)) {
			list.add(element);
			visited.add(element);
			return true;
		}
		return false;
	}

	private void walk() {
		PathElement current = list.get(getLastIndex());
		if (current.sameNode(finish)) {
			return;
		}
		if (current.hasNeighborNode(finish)) {
			add(new PathElement(finish));
			return;
		}

		PathElement temp;
		while ((temp = current.next()) != null) {
			if (add(temp)) {
				current = temp;
				if (current.sameNode(finish)) {
					break;
				}
				if (current.hasNeighborNode(finish)) {
					add(new PathElement(finish));
					break;
				}
			}
		}
	}

	private boolean endWith(Node node) {
		return list.get(getLastIndex()).sameNode(node);
	}

	private void stepBack() {
		list.remove(getLastIndex());
	}

	private int getLastIndex() {
		return list.size() - 1;
	}

	public List<Node> findFirstPath() {
		walk();
		while (!endWith(finish)) {
			stepBack();
			if (list.size() == 0) {
				break;
			}
			walk();
		}
		return list.stream().map(PathElement::getNode).collect(Collectors.toList());
	}

	private static class PathElement {
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

	public static List<Node> getPath(Node from, Node to) {
		PathFinder path = new PathFinder(from, to);
		return path.findFirstPath();
	}

}
