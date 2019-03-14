package com.programmer.igoodie.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;

public class WorkbookUtils {

	public static void save(Workbook workbook, File outputFile) {
		try {
			if(!outputFile.exists()) {
				outputFile.mkdirs();
				outputFile.createNewFile();
			}
			
			FileOutputStream fos = new FileOutputStream(outputFile);
			workbook.write(fos);
			fos.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
