package matt.graph;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class SimpleName implements Comparable<SimpleName> {

	@GraphId
	protected String nodeId;

	@Indexed
	private String name;

	@Override
	public String toString() {
		return getNodeId() + " " + name;
	}

	@Override
	public int compareTo(SimpleName arg0) {
		return this.getName().compareTo(arg0.getName());
	}

	public SimpleName() {
	}

	public SimpleName(String name) {
		this.name = name;
	}

	/* Getters and setters */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
