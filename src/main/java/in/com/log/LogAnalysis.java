package in.com.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.com.entity.EventData;
import in.com.model.LogData;
import in.com.model.State;
import in.com.repo.EventDataRepo;

@Component
public class LogAnalysis implements CommandLineRunner {
	private static final Logger LOGGER = LoggerFactory.getLogger(LogAnalysis.class);

	String fileName = "logtext.txt";

	@Value("${log-event-threshold-ms}")
	private int alertThresholdMs;

	@Autowired
	private EventDataRepo eventDataRepo;

	@Override
	public void run(String... args) throws Exception {
		LOGGER.info("Start Validating and parse file input...");

		File file = validateFile(fileName);
		if(file.exists()) {
			parseFile(file);
		}
		LOGGER.info("End Validating and parse file input...");
	}

	private void parseFile(File file) {
		Map<String, LogData> logMap = new HashMap<>();
		Map<String, EventData> eventMap = new HashMap<>();

		LOGGER.info("Parsing the log and persisting the eventdata");
		try (LineIterator li = FileUtils.lineIterator(file)) {
			while (li.hasNext()) {
				LogData logData;
				try {
					logData = new ObjectMapper().readValue(li.nextLine(), LogData.class);
					
					// If yes, then find the execution time between STARTED and FINISHED states and update the alert.
					if(logMap.containsKey(logData.getId())) {
						LogData logData2 = logMap.get(logData.getId());
						long executionTime = getLogExecutionTime(logData, logData2);

						// event data created by default alert flag will be false.
						EventData eventData = new EventData(logData, (int)executionTime);
						
						// if the execution time is more than the specified threshold, flag the alert as TRUE
						if(executionTime > alertThresholdMs) {
							eventData.setAlert(Boolean.TRUE);
							LOGGER.debug("!!! Execution time for the log data {} is {}ms", eventData.getId(), executionTime);
						}

						eventMap.put(logData.getId(), eventData);
						logMap.remove(logData.getId());
					} else {
						logMap.put(logData.getId(), logData);
					}
				} catch (JsonProcessingException e) {
					LOGGER.error("Unable to parse the log! {}", e.getMessage());
				}
			}
			
			if (eventMap.size() != 0) {
				saveEventData(eventMap.values());
			}
			if (!file.exists())
				throw new FileNotFoundException("Unable to open the file " + fileName);

		} catch (IOException e) {
			LOGGER.error("!!! Unable to find the specified file '{}'", fileName);
		}
	}
	/**
	 * Validate File and return file object
	 * @param fileName
	 * @return
	 */
	private File validateFile(String fileName) {
		LOGGER.info("Validate the Log file");
		File file = null;
		try {
			file = new ClassPathResource(fileName).getFile();

			if (!file.exists())
				throw new FileNotFoundException("Unable to open the file");
		} catch (IOException e) {
			LOGGER.error("!!! Unable to find the specified file '{}'", fileName);
		}
		return file;
	}

	/**
	 * Save the Event Data
	 * @param alerts
	 */
	private void saveEventData(Collection<EventData> alerts) {
		LOGGER.debug("Persisting {} alerts...", alerts.size());
		eventDataRepo.saveAll(alerts);
	}

	/**
	 * Finding log time differences
	 * @param event1
	 * @param event2
	 * @return
	 */
	private long getLogExecutionTime(LogData event1, LogData event2) {
		LogData endEvent = Stream.of(event1, event2).filter(e -> State.FINISHED.equals(e.getState())).findFirst().orElse(null);
		LogData startEvent = Stream.of(event1, event2).filter(e -> State.STARTED.equals(e.getState())).findFirst().orElse(null);

		return Objects.requireNonNull(endEvent).getTimestamp() - Objects.requireNonNull(startEvent).getTimestamp();
	}
}
