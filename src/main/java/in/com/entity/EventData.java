package in.com.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import in.com.model.LogData;
import in.com.model.LogType;

@Entity
@Table(name = "event_data")
public class EventData {
	
	@Id
	private String id;
	private int duration;
	private LogType type;
	private String host;
	private Boolean alert;
	
	public EventData() {
    }
	
	public EventData(LogData logData, int duration) {
		this.id = logData.getId();
		this.type = logData.getType();
		this.host = logData.getHost();
		this.duration = duration;
		this.alert = Boolean.FALSE;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public LogType getType() {
		return type;
	}

	public void setType(LogType type) {
		this.type = type;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Boolean getAlert() {
		return alert;
	}

	public void setAlert(Boolean alert) {
		this.alert = alert;
	}
	
	
	
	
}
