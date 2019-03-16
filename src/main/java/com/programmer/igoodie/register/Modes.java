package com.programmer.igoodie.register;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

import com.programmer.igoodie.mode.ModeMainMenu;
import com.programmer.igoodie.mode.ModeWeek;
import com.programmer.igoodie.command.named.CommandAutosave;
import com.programmer.igoodie.command.named.CommandExit;
import com.programmer.igoodie.command.named.CommandHelp;
import com.programmer.igoodie.command.named.CommandLegend;
import com.programmer.igoodie.command.named.CommandMode;
import com.programmer.igoodie.command.named.CommandSave;
import com.programmer.igoodie.mode.Mode;

public final class Modes {

	private static final HashMap<String, Mode> MODES = new HashMap<>();

	static {
		registerMode(new ModeMainMenu(), "DEFAULT", "~", "EXIT");
		registerMode(new ModeWeek());
	}

	public static Mode getMainMode() {
		return MODES.get("MAIN_MENU");
	}

	public static Mode getMode(String name) {
		return MODES.get(name.toUpperCase());
	}

	public static Stream<Mode> modeStream() {
		return MODES.values().stream().distinct();
	}

	private static void registerMode(Mode m, String...aliases) {
		registerGlobalCommands(m);
		MODES.put(m.getName(), m);
		Arrays.stream(aliases).forEach(alias -> MODES.put(alias, m));
	}
	
	private static void registerGlobalCommands(Mode m) {
		m.registerCommand(new CommandHelp());
		m.registerCommand(new CommandExit());
		m.registerCommand(new CommandAutosave());
		m.registerCommand(new CommandMode());
		m.registerCommand(new CommandLegend());
		m.registerCommand(new CommandSave());
	}

}
