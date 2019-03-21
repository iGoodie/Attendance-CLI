package com.programmer.igoodie.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;

public class WorkbookUtils {

	public static boolean save(Workbook workbook, File outputFile) {
		try {
			if(!outputFile.exists()) {
				outputFile.getParentFile().mkdirs();
				outputFile.createNewFile();
			}
			
			FileOutputStream fos = new FileOutputStream(outputFile);
			workbook.write(fos);
			fos.close();
		
		} catch (FileNotFoundException e) {
			return false;
			
		} catch (IOException e) {
			return false;
		}
		
		return true;
	}
	
}
