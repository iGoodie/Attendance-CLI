package com.programmer.igoodie;

import org.junit.Assert;
import org.junit.Test;

import com.programmer.igoodie.command.meta.CommandParser;

public class UtilityTest {

	@Test
	public void shouldParseCorrectly() {
		String cmd = "FOO BAR \"LOREM IPSUM\" BAZ";
		
		String[] tokens= CommandParser.intoTokens(cmd);
		
		Assert.assertArrayEquals(new String[] {
			"FOO", "BAR", "LOREM IPSUM", "BAZ"
		}, tokens);
	}
	
}
