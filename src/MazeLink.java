
public class MazeLink {
	private MazeNode node;
	private MazeNode parent;
	public MazeLink() {

	}
	public MazeLink(MazeNode node, MazeNode parent){
		this.node = node;
		this.parent  = parent;
	}
	public MazeNode getNode() {
		return node;
	}
	public MazeNode getParent() {
		return parent;
	}
}
