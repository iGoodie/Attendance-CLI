package com.programmer.igoodie.register;

import java.util.HashMap;
import java.util.stream.Stream;

import com.programmer.igoodie.mode.ModeMainMenu;
import com.programmer.igoodie.mode.ModeWeek;
import com.programmer.igoodie.mode.Mode;

public final class Modes {

	private static final HashMap<String, Mode> MODES = new HashMap<>();

	static {
		registerMode(new ModeMainMenu());
		registerMode(new ModeWeek());
	}

	public static Mode getMainMode() {
		return MODES.get("MAIN_MENU");
	}

	public static Mode getMode(String name) {
		if (name.toUpperCase().matches("~|DEFAULT|EXIT"))
			return getMainMode();
		
		return MODES.get(name.toUpperCase());
	}

	public static Stream<Mode> modeStream() {
		return MODES.values().stream();
	}

	private static void registerMode(Mode m) {
		MODES.put(m.getName(), m);
	}

}
