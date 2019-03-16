package com.programmer.igoodie.command.meta;

import java.util.LinkedList;

public class CommandParser {

	public static final char DELIMITER = ' ';
	public static final char ESCAPE_CHAR = '"';

	public static String[] intoTokens(String commandLine) {
		if (commandLine.chars().filter(ch -> ch == ESCAPE_CHAR).count() % 2 != 0) {
			throw new IllegalArgumentException("Unbalanced amount of escape characters");
		}

		// Trim and modify input
		commandLine = commandLine.trim().strip().replaceAll("\\s+", " ");

		LinkedList<String> tokenList = new LinkedList<>();
		boolean escaping = false;
		int startingIndex = 0;

		for (int i = 0, l = commandLine.length(); i < l; i++) {
			char ch = commandLine.charAt(i);

			if (ch == DELIMITER) {
				if (!escaping) {
					tokenList.add(commandLine.substring(startingIndex, i));
					startingIndex = i + 1;
				}
			}

			else if (ch == ESCAPE_CHAR) {
				escaping = !escaping;
			}
		}

		// End of line reached so add last token as whole
		tokenList.add(commandLine.substring(startingIndex, commandLine.length()));
		
		// Convert into array and return
		String[] tokens = new String[tokenList.size()];
		//tokenList.set(0, "\"A\"B\"C\"");
		tokens = tokenList.stream().map(t->t.replaceAll("(\"$|^\")", ""))
				.toArray(String[]::new);
		return tokens;
	}

}
