package beans;

import java.sql.Time;

public class WorkTime {
	private Time from;
	private Time to;
	
	public WorkTime() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WorkTime(Time from, Time to) {
		super();
		this.from = from;
		this.to = to;
	}

	public Time getFrom() {
		return from;
	}

	public void setFrom(Time from) {
		this.from = from;
	}

	public Time getTo() {
		return to;
	}

	public void setTo(Time to) {
		this.to = to;
	}
}
