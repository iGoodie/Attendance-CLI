package com.programmer.igoodie.util;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Weeks {

	public static final String ALL_WEEKS_TOKEN = "*";

	public static List<Integer> parseNumbers(String[] args, int weekLength) throws NumberFormatException {
		// Starts with "all weeks" token
		if (args[0].equals(ALL_WEEKS_TOKEN)) {
			
			// Cast every week argument into integer
			Set<Integer> argWeeks = Arrays.stream(args, 1, args.length)
					.map(Integer::parseInt)
					.collect(Collectors.toSet());
			
			// Create week numbers as [1,N], then remove exclusion args
			return IntStream.rangeClosed(1, weekLength)			
				.filter(weekNo -> !argWeeks.contains(weekNo))
				.boxed().collect(Collectors.toList());
		}

		// Weeks are manually declared
		return Arrays.stream(args, 0, args.length)
				.map(Integer::parseInt)
				.filter(weekNo -> weekNo <= weekLength)
				.collect(Collectors.toList());
	}

	public static boolean validateArgs(String[] args) {
		try {
			int numbersIndex = args[0].equals(ALL_WEEKS_TOKEN) ? 1 : 0;
			Arrays.stream(args, numbersIndex, args.length).forEach(Integer::parseInt);
			
		} catch (NumberFormatException e) {
			return false;
		}
		
		return true;
	}
	
}