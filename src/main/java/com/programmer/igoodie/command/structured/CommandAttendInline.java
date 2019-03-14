package com.programmer.igoodie.command.structured;

import org.apache.poi.ss.usermodel.Sheet;

import com.programmer.igoodie.command.meta.CommandExample;
import com.programmer.igoodie.util.StringValidator;

public class CommandAttendInline extends StructuredCommand {

	@Override
	public String getUsage() {
		return "<attendee_id> <week_numbers>";
	}

	@Override
	public String getDescription() {
		return "Pushes attendance(s) of attendees for given week numbers";
	}

	@Override
	public CommandExample[] getExamples() {
		return CommandExample.of(new String[][] {
			{ "1800123 1 2 9 14", "Include attence of 1800123 on week#1, week#2, week#9, week#14." },
			{ "1800123 *", "Include attendance of 1800123 on all weeks." },
			{ "1800123 * 1 2 9 14", "Include attence of 1800123 on all weeks except week#1, week#2, week#9, week#14" }
		});
	}

	@Override
	public boolean correctLength(String[] structure) {
		return structure.length >= 2;
	}

	@Override
	public boolean correctSyntax(String[] structure) {
		if (!StringValidator.isNumeric(structure[0]))
			return false;

		if (structure[1].equals("*")) {
			for (int i = 2; i < structure.length; i++) {
				try {
					Integer.parseInt(structure[i]);
				} catch (NumberFormatException e) {
					return false;
				}
			}

		} else {
			for (int i = 1; i < structure.length; i++) {
				try {
					Integer.parseInt(structure[i]);
				} catch (NumberFormatException e) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public boolean execute(Sheet sheet, String[] args) {
		// TODO Auto-generated method stub
		return false;
	}

}
