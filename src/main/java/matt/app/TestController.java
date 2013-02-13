package matt.app;

import java.util.Collection;

import matt.graph.SimpleChildOf;
import matt.graphrepo.NameService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

@Controller
public class TestController {
	static Logger log = LoggerFactory.getLogger(TestController.class);

	@Autowired
	NameService nameService;

	@Autowired
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		Assert.notNull(transactionManager, "The 'transactionManager' argument must not be null.");
		this.transactionTemplate = new TransactionTemplate(transactionManager);
	}

	PlatformTransactionManager transactionManager;
	
	private TransactionTemplate transactionTemplate;

//	@Transactional
	public String change(final Collection<SimpleChildOf> allNew, final Collection<SimpleChildOf> allOld, final boolean rollback) {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			public void doInTransactionWithoutResult(TransactionStatus status) {
				log.info("•• Transaction {} isNewTransaction {}", status, status.isNewTransaction());

				nameService.addNewRelationships(allNew, allOld);

				if (rollback) {
					status.setRollbackOnly();
				}
			}
		});
		
		return "change";
	}
}
