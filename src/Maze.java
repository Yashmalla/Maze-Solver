import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Maze {

	private int solutionLength;
	private List<List<MazeNode>> nodes = new ArrayList<List<MazeNode>>();
	private MazeNode startNode;
	private MazeNode goalNode;
	MazeLink currentLink;
	List<MazeLink> visitedLink;
	List<MazeLink> solutionLink;

	public Maze() {
	}

	public List<List<MazeNode>> getNodes() {
		return nodes;
	}

	public void setStartNode(MazeNode startNode) {
		this.startNode = startNode;
	}

	public void setGoalNode(MazeNode goalNode) {
		this.goalNode = goalNode;
	}

	public void loader(String path) {
		try {
			InputStream myInput = new FileInputStream(path);
			Scanner myInputFile = new Scanner(myInput);
			nodes.clear();
			int row = 0;
			String input;
			while (myInputFile.hasNext()) {
				input = myInputFile.nextLine();
				List<MazeNode> rowList = new ArrayList<MazeNode>();
				int col = 0;
				for (char c : input.toCharArray()) {
					switch (c) {
					case 'X': {
						MazeNode n = new MazeNode(row, col, NodeProperty.WALLS);
						rowList.add(n);
					}
						break;
					case 'E': {
						MazeNode n = new MazeNode(row, col, NodeProperty.ENTRANCE);
						setStartNode(n);
						rowList.add(n);
					}
						break;
					case 'F': {
						MazeNode n = new MazeNode(row, col, NodeProperty.EXIT);
						setGoalNode(n);
						rowList.add(n);
					}
						break;
					default: {
						MazeNode n = new MazeNode(row, col, NodeProperty.PATH);
						rowList.add(n);
					}
						break;
					}
					++col;
				}
				nodes.add(rowList);
				++row;

			}
		} catch (Exception e) {
			System.out.println("The File does not exist" + e.getMessage());
		}
		initSetGoalDistance();
	}

	public final void PrintMaze() {
		final String red = "\033[1;31m";
		final String red_end = "\033[0m";
		final String green = "\033[1;32m";
		final String green_end = "\033[0m";

		for (int i = 0; i < nodes.size(); i++) {
			for (int j = 0; j < nodes.get(i).size(); j++) {
				MazeNode node = nodes.get(i).get(j);
				if (node.hasProperty(NodeProperty.WALLS)) {
					System.out.print("X");
				}
				if (node.hasProperty(NodeProperty.ENTRANCE)) {
					System.out.print("E");

				}
				if (node.hasProperty(NodeProperty.EXIT)) {
					System.out.print("F");
				}
				if (node.hasProperty(NodeProperty.PATH)) {
					System.out.print(" ");
				}
				if (node.hasProperty(NodeProperty.VISITED)) {
					System.out.print(red + "*" + red_end);
				}
				if (node.hasProperty(NodeProperty.SOLUTION)) {
					System.out.print(green + "o" + green_end);
				}
			}
			System.out.print("\n");
		}
	}

	public MazeNode getGoalNode() {
		return goalNode;
	}

	public MazeNode getStartNode() {
		return startNode;
	}

	private MazeNode getNodeAt(int row, int col) {
		if ((row < nodes.size()) && (col < nodes.get(row).size())) {
			return nodes.get(row).get(col);
		}
		return null;
	}

	private List<MazeNode> getNeighboursAt(int row, int col) {
		List<MazeNode> surrounding = new ArrayList<MazeNode>();
		MazeNode target = getNodeAt(row, col);

		if (target == null) {
			return surrounding;
		}
		MazeNode top = getNodeAt(row - 1, col);
		MazeNode right = getNodeAt(row, col + 1);
		MazeNode bottom = getNodeAt(row + 1, col);
		MazeNode left = getNodeAt(row, col - 1);

		if (top != null) {
			surrounding.add(top);
		}
		if (right != null) {
			surrounding.add(right);
		}
		if (bottom != null) {
			surrounding.add(bottom);
		}
		if (left != null) {
			surrounding.add(left);
		}
		return surrounding;

	}

	private List<MazeNode> getWalkableNeighboursAt(int row, int col) {
		List<MazeNode> surrounding = getNeighboursAt(row, col);
		List<MazeNode> legalSorounding = new ArrayList<MazeNode>();

		for (int i = 0; i < surrounding.size(); i++) {
			if (surrounding.get(i).hasProperty(NodeProperty.PATH)) {
				legalSorounding.add(surrounding.get(i));
			} else if (surrounding.get(i).hasProperty(NodeProperty.EXIT)) {
				legalSorounding.add(surrounding.get(i));
			}
		}
		return legalSorounding;
	}

	private void initSetGoalDistance() {
		for (int i = 0; i < nodes.size(); i++) {
			for (int j = 0; j < nodes.get(i).size(); j++) {
				int current_x, current_y, goal_x, goal_y;
				current_x = nodes.get(i).get(j).getX();
				current_y = nodes.get(i).get(j).getY();
				goal_x = goalNode.getX();
				goal_y = goalNode.getY();
				double distance = Math.sqrt(Math.pow((current_x - goal_x), 2) + Math.pow((current_y - goal_y), 2));
				nodes.get(i).get(j).setGoalDistance(distance);
			}
		}
	}

	public List<MazeLink> solver(SearchStrategy strategy) {
		return solve(startNode, strategy);
	}

	private List<MazeLink> solve(MazeNode startNode, SearchStrategy strategy) {
		solutionLink = new ArrayList<MazeLink>();
		final int interactive_mode_wait_time = 600;
		switch (strategy) {
		case DFS: {
			visitedLink = new ArrayList<MazeLink>();
			currentLink = new MazeLink(startNode, null);
			visitedLink.add(currentLink);
			while (visitedLink.size() != 0) {
				linkDfs();
				solutionLink.add(currentLink);
				if (!(currentLink.getNode().hasProperty(NodeProperty.ENTRANCE)
						|| currentLink.getNode().hasProperty(NodeProperty.EXIT))) {
					currentLink.getNode().setProperty(NodeProperty.VISITED);
				}
				printAndSleep(interactive_mode_wait_time);
				if (currentLink.getNode().hasProperty(NodeProperty.EXIT)) {
					solutionLink.add(currentLink);
					return solutionLink;
				} else {
					visitLinks();
				}
			}
		}
			break;
		case BFS: {
			visitedLink = new ArrayList<MazeLink>();
			currentLink = new MazeLink(startNode, null);
			visitedLink.add(currentLink);
			while (visitedLink.size() != 0) {
				linkBfs();
				solutionLink.add(currentLink);
				if (!(currentLink.getNode().hasProperty(NodeProperty.ENTRANCE)
						|| currentLink.getNode().hasProperty(NodeProperty.EXIT))) {
					currentLink.getNode().setProperty(NodeProperty.VISITED);
				}
				printAndSleep(interactive_mode_wait_time);
				if (currentLink.getNode().hasProperty(NodeProperty.EXIT)) {
					solutionLink.add(currentLink);
					return solutionLink;
				} else {
					visitLinks();
				}
			}
		}
			break;
		}

		return solutionLink;

	}

	public boolean compareGoalDistance(final MazeLink first, final MazeLink second) {
		return second.getNode().getGoalDistance() < first.getNode().getGoalDistance();
	}

	private void linkDfs() {
		currentLink = visitedLink.get(visitedLink.size() - 1);
		visitedLink.remove(visitedLink.get(visitedLink.size() - 1));

	}

	private void linkBfs() {
		currentLink = visitedLink.get(0);
		visitedLink.remove(visitedLink.get(0));
	}

	private void visitLinks() {
		List<MazeNode> neighbourNodes = getWalkableNeighboursAt(currentLink.getNode().getX(),
				currentLink.getNode().getY());
		for (int i = 0; i < neighbourNodes.size(); i++) {
			boolean visited = false;
			for (int j = 0; j < visitedLink.size(); j++) {
				if ((neighbourNodes.get(i)) == (visitedLink.get(j).getNode())) {
					visited = true;
				}
			}
			for (int j = 0; j < visitedLink.size(); j++) {
				if ((neighbourNodes.get(i)) == (solutionLink.get(j).getNode())) {
					visited = true;
				}
			}
			if (!visited) {
				MazeLink arc = new MazeLink((neighbourNodes.get(i)), currentLink.getNode());
				visitedLink.add(arc);
			}
		}
	}

	private void printAndSleep(int useconds) {
		try {
			Thread.sleep(useconds);
			final int emptyLinesNumber = 60;
			for (int i = 0; i != emptyLinesNumber; ++i) {
				System.out.println();
			}
			PrintMaze();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void Solution(final List<MazeLink> solutionLink) {

		if (solutionLink.size() != 0) {
			solutionLength = 0;
			int size = solutionLink.size() - 1;
			MazeLink currentLink = solutionLink.get(size);
			if (!(currentLink.getNode().hasProperty(NodeProperty.EXIT))) {
				System.out.println("Sorry the maze you provided does not seem to have a solution");
				System.exit(0);
			}
			while (currentLink.getParent() != null) {
				if (!(currentLink.getNode().hasProperty(NodeProperty.ENTRANCE)
						|| currentLink.getNode().hasProperty(NodeProperty.EXIT))) {
					currentLink.getNode().setProperty(NodeProperty.SOLUTION);
					printAndSleep(600);
					++solutionLength;
				}
				for (int i = 0; i < solutionLink.size(); i++) {
					MazeNode current_parent = currentLink.getParent();
					MazeNode parent_node = (solutionLink.get(i)).getNode();
					if (current_parent == parent_node) {
						currentLink = solutionLink.get(i);
						break;
					}

				}
			}
		}

	}

	public int getSolutionLength() {
		return solutionLength;
	}

}
