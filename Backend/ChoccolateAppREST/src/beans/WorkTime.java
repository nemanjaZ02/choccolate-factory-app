package beans;

import java.sql.Time;
import java.time.LocalDateTime;

public class WorkTime {
	private String from;
	private String to;
	
	public WorkTime() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WorkTime(String from, String to) {
		super();
		this.from = from;
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
}
