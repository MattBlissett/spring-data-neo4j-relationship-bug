package matt.graph;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity(type="SIMPLE_CHILD_OF")
public class SimpleChildOf {
	@StartNode
	private SimpleName child;

	@EndNode
	private SimpleName parent;

	private String flag;

	@Override
	public String toString() {
		return String.format("%s↗%s (%s)", child.getName(), parent.getName(), flag);
	}

	public final void setBoth(SimpleName from, SimpleName to) {
		setChild(from);
		setParent(to);
	}

	/* Getters and setters */
	public SimpleName getChild() {
		return child;
	}
	public void setChild(SimpleName child) {
		this.child = child;
	}

	public SimpleName getParent() {
		return parent;
	}
	public void setParent(SimpleName parent) {
		this.parent = parent;
	}

	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
}
