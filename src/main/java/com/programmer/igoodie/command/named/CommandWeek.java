package com.programmer.igoodie.command.named;

import org.apache.poi.ss.usermodel.Sheet;

import com.programmer.igoodie.AttendanceCLI;
import com.programmer.igoodie.mode.ModeWeek;
import com.programmer.igoodie.util.StringValidator;

public class CommandWeek extends NamedCommand {

	public CommandWeek() {
		super("WEEK");
	}

	@Override
	public String getUsage() {
		return name + " <week_number>";
	}

	@Override
	public String getDescription() {
		return "Changes the modifying week number of WEEK mode.";
	}

	@Override
	public boolean correctLength(String[] commandArgs) {
		return commandArgs.length == 1;
	}

	@Override
	public boolean correctSyntax(String[] commandArgs) {
		return StringValidator.isNumeric(commandArgs[0]);
	}

	@Override
	public boolean execute(Sheet sheet, String[] args) {
		if (!(parentMode instanceof ModeWeek)) {
			System.out.printf("X - Internal error while executing %s. Please contact developer.", name);
			return false;
		}

		int weekNo = Integer.parseInt(args[0]);
		int weekLength = AttendanceCLI.getConfigs().weekLength();

		if (weekNo < 1 || weekNo > weekLength) {
			System.out.printf("X - Week number must be between [1,%d]\n", weekLength);
			return false;
		}

		((ModeWeek) parentMode).setWeek(weekNo);
		System.out.printf("✓ - Switched to week %d\n", weekNo);
		return true;
	}

}
