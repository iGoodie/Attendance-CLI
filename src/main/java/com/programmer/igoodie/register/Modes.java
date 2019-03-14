package com.programmer.igoodie.register;

import java.util.HashMap;

import com.programmer.igoodie.mode.MainMenuMode;
import com.programmer.igoodie.mode.Mode;

public final class Modes {

	private static final HashMap<String, Mode> MODES = new HashMap<>();
	
	static {
		registerMode(new MainMenuMode());
	}
	
	public static Mode getMainMode() {
		return MODES.get("MAIN_MENU");
	}
	
	public static Mode getMode(String name) {
		return MODES.get(name);
	}
	
	private static void registerMode(Mode m) {
		MODES.put(m.getName(), m);		
	}
	
}
