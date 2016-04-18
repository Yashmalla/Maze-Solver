import java.util.List;
import java.util.ArrayList;

/**
 * @author Yash
 *
 */
public class MazeNode {
	private List<NodeProperty> properties = new ArrayList<NodeProperty>();
	private int x;
	private int y;
	private double goalDistance;

	public MazeNode() {
	}

	public MazeNode(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public MazeNode(int x, int y, NodeProperty property) {
		this.x = x;
		this.y = y;
		properties.add(property);
	}

	public MazeNode(int x, int y, List<NodeProperty> property) {
		this.x = x;
		this.y = y;
		this.properties = property;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public double getGoalDistance() {
		return goalDistance;
	}

	public void setGoalDistance(double goalDistance) {
		this.goalDistance = goalDistance;
	}

	public boolean hasProperty(NodeProperty property) {
		for (NodeProperty nodeProperty : properties) {
			if (nodeProperty == property) {
				return true;
			}
		}
		return false;
	}
	
	public void addProperty(NodeProperty property){
		if (!hasProperty(property)) {
			properties.add(property);
		}
	}
	
	public void removeProperty(NodeProperty property){
		for (NodeProperty nodeProperty : properties) {
			if (nodeProperty == property) {
				properties.remove(nodeProperty);
			}
		}
	}
	public void clearProperty(){
		properties.clear();
	}
	public void setProperty(NodeProperty property){
		clearProperty();
		addProperty(property);
	}
	public List<NodeProperty> getProperties() {
		return properties;
	}
}
