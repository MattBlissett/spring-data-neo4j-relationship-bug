package matt.graphrepo;

import matt.graph.SimpleChildOf;
import matt.graph.SimpleName;

import org.springframework.data.neo4j.annotation.MapResult;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.annotation.ResultColumn;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface NameRepository extends GraphRepository<SimpleName> {
	@Query("START n=node:SimpleName('name:*')"
			+ " MATCH n-[r?]->m"
			+ " RETURN n, r, m")
	public Iterable<NRM> showEverything();

	@MapResult
	public interface NRM {
		@ResultColumn("n")
		SimpleName getN();
		@ResultColumn("r")
		SimpleChildOf getR();
		@ResultColumn("m")
		SimpleName getM();
	}
}
