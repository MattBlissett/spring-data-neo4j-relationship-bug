package matt.app;

import java.util.List;

import matt.graph.SimpleChildOf;
import matt.graph.SimpleName;
import matt.graphrepo.NameService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.common.collect.Lists;

public class App {
	static Logger log = LoggerFactory.getLogger(App.class);

	public static void main( String[] args ){
		ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext("app-context.xml");

		NameService nameService = applicationContext.getBean(NameService.class);
		TestController testController = applicationContext.getBean(TestController.class);

		log.info("→→→ Adding three test names and a test relationship");
		SimpleName a, b, c;
		SimpleChildOf bToA;
		{
			a = new SimpleName("a");
			a.persist();
			
			b = new SimpleName("b");
			b.persist();
			
			c = new SimpleName("c");
			c.persist();
			
			bToA = new SimpleChildOf();
			bToA.setBoth(b, a);
			bToA.setFlag("Matt");

			log.info("B→A's entity state {}", bToA.getEntityState());
			log.info("B→A's persistent state {}", bToA.getPersistentState());
			log.info("B→A's has persistent state {}", bToA.hasPersistentState());

			bToA.persist();
		}

		log.info("→→→ Dumping repository (before)");
		nameService.logGraph();

		log.info("→→→ Performing transactional change (with rollback)");
		SimpleChildOf cToA;
		{
			List<SimpleChildOf> newRels = Lists.newArrayList();
			List<SimpleChildOf> oldRels = Lists.newArrayList();

			cToA = new SimpleChildOf();
			cToA.setBoth(c, a);
			cToA.setFlag("Matt");

			newRels.add(cToA);
			oldRels.add(bToA);

			log.info("C→A's entity state {}", cToA.getEntityState());
			log.info("C→A's persistent state {}", cToA.getPersistentState());
			log.info("C→A's has persistent state {}", cToA.hasPersistentState());

			testController.change(newRels, oldRels, true);

			if (cToA.hasPersistentState()) {
				log.error("C→A has persistent state, but wasn't persisted.");
			}
			log.info("C→A's entity state {}", cToA.getEntityState());
			log.info("C→A's persistent state {}", cToA.getPersistentState());
			log.info("C→A's has persistent state {}", cToA.hasPersistentState());
		}

		log.info("→→→ Dumping repository (after rollback)");
		nameService.logGraph();

		log.info("→→→ Performing transactional change (successfully)");
		{
			List<SimpleChildOf> newRels = Lists.newArrayList();
			List<SimpleChildOf> oldRels = Lists.newArrayList();

			newRels.add(cToA);
			//cToA.setPersistentState(null);

			oldRels.add(bToA);
			testController.change(newRels, oldRels, false);

			log.info("C→A's entity state {}", cToA.getEntityState());
			log.info("C→A's persistent state {}", cToA.getPersistentState());
			log.info("C→A's has persistent state {}", cToA.hasPersistentState());
		}

		log.info("→→→ Dumping repository (after success)");
		nameService.logGraph();

		log.info("→→→ Remove relationship");
		{
			List<SimpleChildOf> oldRels = Lists.newArrayList();
			oldRels.add(bToA);

			nameService.remove(oldRels);
		}

		log.info("→→→ Dumping repository (after remove)");
		nameService.logGraph();

		applicationContext.close();
	}
}
