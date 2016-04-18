import java.util.List;
import java.util.Scanner;

public class main {

	public static void main(String[] args) {
		/*
		 * Best to run from command prompt Eclipse does not show color rather
		 * just the ANSI code
		 */
		Scanner input = new Scanner(System.in);
		String path = System.getProperty("user.dir");
		System.out.println(
				"Best to run from command prompt if not the ANSI code in the PrintMaze() at line 80 in Maze.java has to be removed");
		System.out.print("Enter the name of the Maze text file to be solved: ");
		// String file = path + "//src//"+ input.nextLine() + ".txt"; IF THE
		// COMPIILER IS ECLIPSE OR SIMILAR
		String file = path + "/" + input.nextLine() + ".txt";
		Maze maze = new Maze();
		maze.loader(file);
		maze.PrintMaze();
		System.out.println("Choose the stragety you want to use to solve the Maze BFS/DFS: ");
		String stragety = input.nextLine().toUpperCase();
		
		SearchStrategy search = SearchStrategy.BFS;

		switch (stragety) {
		case "BFS":
			search = SearchStrategy.BFS;
			break;
		case "DFS":
			search = SearchStrategy.DFS;
			break;

		}
		List<MazeLink> solutionLink;
		try {
			solutionLink = maze.solver(search);
			maze.Solution(solutionLink);
			maze.PrintMaze();
		}  catch (NullPointerException e) {
			System.out.println("Sorry the maze you provided does not seem to have a solution");
			System.exit(0);
		}

		System.out.println("The length of the solution path is " + maze.getSolutionLength());
	}

}
