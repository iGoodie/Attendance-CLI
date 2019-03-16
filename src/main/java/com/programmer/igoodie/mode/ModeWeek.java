package com.programmer.igoodie.mode;

import com.programmer.igoodie.command.named.CommandWeek;
import com.programmer.igoodie.command.structured.CommandAttendWeekly;

import lombok.Getter;
import lombok.Setter;

public class ModeWeek extends Mode {

	private @Getter @Setter int week;
	
	public ModeWeek() {
		super("WEEK");
		this.week = 1;
	}
	
	@Override
	public String getDescription() {
		return "Contains weekly attending environment. "
				+ "This mode can be used to attend attendees one by one for a specific week.";
	}
	
	@Override
	public String getInputPrefix() {
		return String.format("Week %d $ ", week);
	}
	
	@Override
	protected void registerCommands() {
		registerCommand(new CommandWeek());
		registerCommand(new CommandAttendWeekly());
	}
	
}
