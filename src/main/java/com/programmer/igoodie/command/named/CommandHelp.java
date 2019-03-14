package com.programmer.igoodie.command.named;

import org.apache.poi.ss.usermodel.Sheet;

import com.programmer.igoodie.command.Command;
import com.programmer.igoodie.command.meta.CommandExample;
import com.programmer.igoodie.utils.system.Syntax;

public class CommandHelp extends NamedCommand {

	public CommandHelp() {
		super("HELP");
	}

	@Override
	public String getUsage() {
		return "HELP [command]";
	}

	@Override
	public String getDescription() {
		return "Displays information about given command "
				+ "or displays command list";
	}

	@Override
	public CommandExample[] getExamples() {
		return CommandExample.of(new String[][] { 
			{ "HELP", "Displays a list of parsable commands." },
			{ "HELP EXIT", "Displays brief information about EXIT command." } 
		});
	}

	@Override
	public boolean correctLength(String[] commandArgs) {
		return commandArgs.length == 0 || commandArgs.length == 1;
	}

	@Override
	public boolean execute(Sheet sheet, String[] args) {
		if (args.length == 0) {
			System.out.println("Commands:");

			getParentMode().commandStream().forEach(command -> {
				System.out.printf(" $ %s\t%s\n", command.getUsage(), command.getDescription());
				
				CommandExample[] examples = command.getExamples();	
				
				if (Syntax.truthy(examples) && examples.length > 0) {
					System.out.printf("\tExamples:\n");
					for(CommandExample example : examples)
						System.out.printf("\t$ %s\t%s\n", example.getStatement(), example.getDescription());
				}
			});

		} else {
			Command cmd = parentMode.getCommand(args[0]);

			if (Syntax.truthy(cmd)) {
				System.out.println(cmd);
			} else {
				System.out.println("X - Command not found");
			}
		}

		return true;
	}

}
