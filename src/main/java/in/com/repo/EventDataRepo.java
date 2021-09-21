package in.com.repo;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import in.com.entity.EventData;

@Repository
public interface EventDataRepo extends CrudRepository<EventData, Integer> {
	Optional<EventData> findById(String id);
}
