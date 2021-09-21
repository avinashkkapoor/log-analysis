package in.com;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import in.com.entity.EventData;
import in.com.model.LogType;
import in.com.repo.EventDataRepo;

@SpringBootTest
public class LogAnaysisTest {

	@Autowired
    private EventDataRepo repository;

    @Test
    public void whenFindingCustomerById_thenCorrect() {
    	EventData event = new EventData();
    	event.setId("event-1");
    	event.setDuration(3);
    	event.setHost("127.0.0.1");
    	event.setType(LogType.APPLICATION_LOG);

        repository.save(event);
        assertThat(repository.findById("event-1")).isInstanceOf(Optional.class);
    }

    @Test
    public void whenFindingAllCustomers_thenCorrect() {
    	EventData event1 = new EventData();
    	event1.setId("event-1");
    	event1.setDuration(3);
    	event1.setHost("127.0.0.1");
    	event1.setType(LogType.APPLICATION_LOG);

        EventData event2 = new EventData();
        event2.setId("event-2");
        event2.setDuration(7);
        event2.setHost(null);
        event2.setType(null);
        event2.setAlert(Boolean.TRUE);

        repository.save(event2);
        repository.save(event2);
        assertThat(repository.findAll()).isInstanceOf(List.class);
    }
	
}
