package graph;

public class Node {
	private String name;
	private Node[] neighbors;

	public Node(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Name should not be null");
		}
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Node getNeighbor(int i) {
		return neighbors[i];
	}

	public int getNeighborsSize() {
		return neighbors == null ? 0 : neighbors.length;
	}

	public boolean hasNeighbor(Node n) {
		if (neighbors != null) {
			for (Node node : neighbors) {
				if (node.equals(n)) {
					return true;
				}
			}
		}
		return false;
	}

	public void setNeighbors(Node... nodes) {
		neighbors = nodes;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		return this.getName().equals(((Node) obj).getName());
	}
}
