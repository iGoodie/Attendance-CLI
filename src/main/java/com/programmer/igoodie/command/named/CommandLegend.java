package com.programmer.igoodie.command.named;

import org.apache.poi.ss.usermodel.Sheet;

public class CommandLegend extends NamedCommand {
	
	private static final String[] LEGEND_LABELS = {
		"✓ = Successful system response",
		"X = Unsuccessful system response",
		"! = System warning/notice",
		"? = User needs to choose one of the following",
		"$ = User input is waited",
		"[arg] = optional argument",
		"<arg> = required argument",
		"a|b|c = a or b or c",
	};

	public CommandLegend() {
		super("LEGEND");
	}
	
	@Override
	public String getDescription() {
		return "Displays the list of symbols/characters used by the CLI.";
	}
	
	@Override
	public boolean correctLength(String[] commandArgs) {
		return commandArgs.length == 0;
	}
	
	@Override
	public boolean execute(Sheet sheet, String[] args) {
		for(String legendLabel : LEGEND_LABELS) {
			System.out.printf(" %s\n", legendLabel);
		}
		
		return true;
	}
	
}
