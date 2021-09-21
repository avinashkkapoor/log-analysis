package in.com.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import in.com.entity.EventData;

@DataJpaTest
public class EventDataRepoTest {
	
	@Autowired
	private EventDataRepo repository;
	
	@Test
	public void testFindOne() {
		EventData eventData = repository.findById("scsmbstgrb").get();
		assertEquals("scsmbstgrb",eventData.getId());
	}
}
