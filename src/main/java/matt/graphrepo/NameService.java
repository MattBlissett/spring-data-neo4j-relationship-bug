package matt.graphrepo;

import java.util.Collection;
import java.util.List;

import matt.graph.SimpleChildOf;
import matt.graph.SimpleName;
import matt.graphrepo.NameRepository.NRM;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@Service
@Transactional
public class NameService {
	static Logger log = LoggerFactory.getLogger(NameService.class);

	@Autowired
	private NameRepository nameRepository;

	public void logGraph() {
		log.warn("▻►► SHOWING GRAPH ◄◄◅");
		for (NRM nrm : nameRepository.showEverything()) {
			if (nrm.getR() == null) {
				log.warn("({})", nrm.getN());
			}
			else {
				log.warn("({})►{}►({})", nrm.getN(), nrm.getR(), nrm.getM());
			}
		}
		log.warn("▻►► END OF GRAPH ◄◄◅");
	}

	public SimpleName findNameById(String id) {
		return nameRepository.findByPropertyValue("id", id);
	}

	public List<SimpleChildOf> addNewRelationships(Collection<SimpleChildOf> newRels, Collection<SimpleChildOf> oldRels) {
		for (SimpleChildOf rOld : oldRels) {
			rOld.setFlag("expired");
			rOld.persist();
		}

		List<SimpleChildOf> persisted = Lists.newLinkedList();

		for (SimpleChildOf rNew : newRels) {
			rNew.setFlag("current");
			rNew.persist();

			log.trace("Persisted {} to repository", rNew);
			persisted.add(rNew);
		}

		return persisted;
	}
	
	public void remove(Collection<SimpleChildOf> oldRels) {
		for (SimpleChildOf rOld : oldRels) {
			rOld.remove();
		}
	}
}
