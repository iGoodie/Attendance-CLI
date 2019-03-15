package com.programmer.igoodie.config;

import java.util.Properties;

import com.programmer.igoodie.utils.system.Syntax;

public class AttendanceCLIConfig {

	public String attendedSign;
	public char attendeeIdCol;
	public int attendeeRowStart;
	public char weekStartCol;
	public char weekFinishCol;

	public AttendanceCLIConfig(Properties props) {
		this.attendedSign = asString(props, "attended_sign", "✓");
		this.attendeeIdCol = asChar(props, "attendee_id_col", 'A');
		this.attendeeRowStart = asInt(props, "attendee_row_start", 1);
		this.weekStartCol = asChar(props, "week_start_col", 'B');
		this.weekFinishCol = asChar(props, "week_finish_col", 'G');
	}

	public int weekLength() {
		return (int) (weekFinishCol - weekStartCol) + 1;
	}
	
	private String asString(Properties props, String key, String defaultValue) {
		String value = props.getProperty(key);
		return Syntax.falsey(value) ? defaultValue : value;
	}

	private char asChar(Properties props, String key, char defaultValue) {
		String value = props.getProperty(key);
		return Syntax.falsey(value) ? defaultValue : value.charAt(0);
	}

	private int asInt(Properties props, String key, int defaultValue) {
		String value = props.getProperty(key);

		if (Syntax.falsey(value))
			return defaultValue;

		try {
			return Integer.parseInt(value);

		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

}
