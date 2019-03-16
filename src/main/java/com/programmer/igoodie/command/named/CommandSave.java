package com.programmer.igoodie.command.named;

import java.nio.file.Path;

import org.apache.poi.ss.usermodel.Sheet;

import com.programmer.igoodie.AttendanceCLI;
import com.programmer.igoodie.command.meta.CommandExample;
import com.programmer.igoodie.util.WorkbookUtils;
import com.programmer.igoodie.utils.io.FileUtils;

public class CommandSave extends NamedCommand {

	public CommandSave() {
		super("SAVE");
	}
	
	@Override
	public String getUsage() {
		return name + " <file|exactPath>";
	}
	
	@Override
	public String getDescription() {
		return "Saves currently open Excel file to given file path.";
	}
	
	@Override
	public CommandExample[] getExamples() {
		return CommandExample.of(new String[][] {
			{ "SAVE somefile.xlsx", "Saves currently open Excel file into /workspace/somefile.xlsx" },
			{ "SAVE C:/somefolder/somefile.xlsx", "Saves currently open Excel file into C:/somefolder/somefile.xlsx" },
		});
	}
	
	@Override
	public boolean correctLength(String[] commandArgs) {
		return commandArgs.length == 1;
	}
	
	@Override
	public boolean execute(Sheet sheet, String[] args) {
		String savePathRaw = args[0];
		Path savePath = Path.of(args[0]);
		
		// Path is relative to /workspace
		if(!savePath.isAbsolute()) 
			savePath = Path.of(FileUtils.getExternalDataPath(), savePathRaw);
		
		// Invalid output format
		if(!savePathRaw.matches(".*\\.(xlsx|xlsb)$")) {
			System.out.printf("X - Given path should end with XLSX or XLSB extension. (E.g test_output.xlsx)\n");
			return false;
		}
			
		// Save workbook to given path
		WorkbookUtils.save(AttendanceCLI.getWorkbook(), savePath.toFile());
		System.out.printf("✓ - Successfully saved into %s\n", savePath);

		return true;
	}

}
