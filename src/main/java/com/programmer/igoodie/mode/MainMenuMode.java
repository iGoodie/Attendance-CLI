package com.programmer.igoodie.mode;

import com.programmer.igoodie.command.named.CommandExit;
import com.programmer.igoodie.command.named.CommandHelp;

public class MainMenuMode extends Mode {

	public MainMenuMode() {
		super("MAIN_MENU");
	}

	@Override
	protected void registerCommands() {
		registerCommand(new CommandExit());
		registerCommand(new CommandHelp());
	}

}
