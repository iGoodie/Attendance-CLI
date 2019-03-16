package com.programmer.igoodie.mode;

import com.programmer.igoodie.command.named.CommandLoad;
import com.programmer.igoodie.command.structured.CommandAttendInline;

public class ModeMainMenu extends Mode {

	public ModeMainMenu() {
		super("MAIN_MENU");
	}

	@Override
	public String getDescription() {
		return "Initially loaded mode. It allows the execution of base (inline) commands.";
	}
	
	@Override
	protected void registerCommands() {
		registerCommand(new CommandAttendInline());
		registerCommand(new CommandLoad());
	}

}
