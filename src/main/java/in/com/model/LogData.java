package in.com.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LogData {

	@JsonProperty("id")
    private String id;

    @JsonProperty("state")
    private State state;

    @JsonProperty("type")
    private LogType type;

    @JsonProperty("host")
    private String host;

    @JsonProperty("timestamp")
    private long timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

	@Override
	public String toString() {
		return "LogData [id=" + id + ", state=" + state + ", type=" + type + ", host=" + host + ", timestamp="
				+ timestamp + "]";
	}
    
    
}
