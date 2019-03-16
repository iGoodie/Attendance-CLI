package com.programmer.igoodie.command.named;

import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Sheet;

import com.programmer.igoodie.AttendanceCLI;
import com.programmer.igoodie.command.meta.CommandExample;
import com.programmer.igoodie.mode.Mode;
import com.programmer.igoodie.register.Modes;
import com.programmer.igoodie.utils.system.Syntax;

public class CommandMode extends NamedCommand {

	public CommandMode() {
		super("MODE");
	}
	
	@Override
	public String getUsage() {
		return name + " <mode_name>";
	}
	
	@Override
	public String getDescription() {
		return "Enters into a modifying mode. Each mode has its unique commands and environment.";
	}
	
	@Override
	public CommandExample[] getExamples() {
		return CommandExample.of(new String[][] {
			{ "MODE MAIN_MENU|~|DEFAULT|EXIT", "Enters back to main menu mode" },
			{ "MODE WEEK", "Enters into modifying by week numbers mode" },
		});
	}
	
	@Override
	public boolean correctLength(String[] commandArgs) {
		return commandArgs.length == 1
				|| commandArgs.length == 0;
	}
	
	@Override
	public boolean execute(Sheet sheet, String[] args) {
		if(args.length == 0) {
			System.out.printf("Current mode: %s\n\n", parentMode.getName());
			System.out.printf("Description:\n %s\n\n", parentMode.getDescription());
			System.out.println("All Modes:");
			Modes.modeStream().forEach(mode -> System.out.printf(" $ %s %s\t%s\n", name, mode.getName(), mode.getDescription()));
			return true;
		}
		
		// Query for mode
		Mode mode = Modes.getMode(args[0]);
		
		// Mode not found with given name
		if (Syntax.falsey(mode)) {
			System.out.printf("X - %s mode not found.\n\n", args[0].toUpperCase());
			Stream<Mode> suggestions = getSuggestions(args[0].toUpperCase());
			if (suggestions.count() == 0) {
				System.out.println("Usable Modes:");
				Modes.modeStream().filter(m -> !m.getName().equals(parentMode.getName()))
					.forEach(m -> System.out.printf(" %s\t%s\n", m.getName(), m.getDescription()));
			} else {
				System.out.println("Did you mean:");
				getSuggestions(args[0].toUpperCase()).forEach(suggestion -> System.out.printf(" %s\t%s\n",
						suggestion.getName(), suggestion.getDescription()));
			}
			return false;
		}

		// Input mode is already active
		if(AttendanceCLI.getCurrentMode().equals(mode)) {
			System.out.printf("X - %s mode is already active.\n", mode.getName());
			return false;
		}
		
		// Change to new mode
		AttendanceCLI.changeMode(mode);
		return true;
	}
	
	private Stream<Mode> getSuggestions(String modeName) {
		String pattern = String.format(".*%s.*", modeName);
		return Modes.modeStream().filter(m -> m.getName().matches(pattern));
	}

}
