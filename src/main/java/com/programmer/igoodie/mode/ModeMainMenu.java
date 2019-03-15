package com.programmer.igoodie.mode;

import com.programmer.igoodie.command.named.CommandExit;
import com.programmer.igoodie.command.named.CommandHelp;
import com.programmer.igoodie.command.named.CommandLegend;
import com.programmer.igoodie.command.named.CommandMode;
import com.programmer.igoodie.command.structured.CommandAttendInline;

public class ModeMainMenu extends Mode {

	public ModeMainMenu() {
		super("MAIN_MENU");
	}

	@Override
	protected void registerCommands() {
		registerCommand(new CommandExit());
		registerCommand(new CommandHelp());
		registerCommand(new CommandAttendInline());
		registerCommand(new CommandLegend());
		registerCommand(new CommandMode());
	}

}
