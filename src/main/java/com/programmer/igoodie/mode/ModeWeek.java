package com.programmer.igoodie.mode;

import com.programmer.igoodie.command.named.CommandHelp;
import com.programmer.igoodie.command.named.CommandMode;
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
	public void printInputPrefix() {
		System.out.printf("Week %d $ ", week);
	}
	
	@Override
	protected void registerCommands() {
		registerCommand(new CommandHelp());
		registerCommand(new CommandMode());
		registerCommand(new CommandWeek());
		registerCommand(new CommandAttendWeekly());
	}
	
}
