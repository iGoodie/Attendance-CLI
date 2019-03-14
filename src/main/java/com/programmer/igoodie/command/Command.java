package com.programmer.igoodie.command;

import org.apache.poi.ss.usermodel.Sheet;

import com.programmer.igoodie.command.meta.CommandExample;
import com.programmer.igoodie.mode.Mode;
import com.programmer.igoodie.utils.system.Syntax;

import lombok.Getter;
import lombok.Setter;

public abstract class Command {

	protected @Setter @Getter Mode parentMode;

	public String getUsage() {
		return "USAGE IS UNKNOWN";
	}

	public String getDescription() {
		return "";
	}

	public CommandExample[] getExamples() {
		return null;
	}

	public boolean correctLength(String[] commandArgs) {
		return true;
	}

	public boolean correctSyntax(String[] commandArgs) {
		return true;
	}

	public abstract boolean execute(Sheet sheet, String[] args);

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(String.format("Usage: %s\n\n", getUsage()));
		sb.append(String.format("Description:\n %s", getDescription()));

		CommandExample[] examples = getExamples();

		if (Syntax.truthy(examples) && examples.length > 0) {
			sb.append("\n\nExamples:");
			for(CommandExample example : examples)
				sb.append(String.format("\n $ %s\t%s", example.getStatement(), example.getDescription()));
		}
		
		return sb.toString();
	}

}
