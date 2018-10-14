package com.example.shaf.ntuhackathon2018;

import java.util.*;
import java.io.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpellChecker {
	public static String spellCheck(String input, List<String> dict) {
		String currentCheck = "";
		boolean noErrors = true;

		Scanner spellChecker = new Scanner(input);
		spellChecker.useDelimiter("\\s+");
		while(spellChecker.hasNext()) {

		    currentCheck = spellChecker.next();
			if(!isSpecial(currentCheck)) {
				if(!checkWord(currentCheck, dict)) {
					noErrors = false;
					return currentCheck;
				}
			}
		}
		return null;
	}
	
	public static boolean isSpecial (String input) {
		Pattern pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE );
		Matcher match = pattern.matcher(input);
		return match.find();
	}
	
	public static boolean checkWord(String input, List<String> dic) {
		boolean valid = false;
		int length = dic.size();
		int i = 0;
		while(!valid && i < length) {
			if(input.trim().equalsIgnoreCase(dic.get(i))) {
				valid = true;
			}
			i++;
			
		}
		return valid;
	}
	
	public static void main(String[] args) {
		
//		List<String> wordList = new ArrayList<>();
//
//		String line;
//		String filename = "words_alpha.txt";
//
//		try {
//			FileReader fileReader = new FileReader(filename);
//
//			BufferedReader bufferedReader = new BufferedReader(fileReader);
//
//			int i = 0;
//
//			while((line = bufferedReader.readLine())!=null) {
//				wordList.add(line);
//				i++;
//			}
//			bufferedReader.close();
//		}
//		catch(FileNotFoundException ex) {
//	        System.out.println("Unable to open file '" + filename + "'");
//	    }
//	    catch(IOException ex) {
//	        System.out.println("Error reading file '" + filename + "'");
//
//		String input = "Greetings it is me Mario";
//
//		if (spellCheck(input, wordList)) {
//			System.out.println("No Errors");
//		}
//		else {
//			System.out.println("Errors");
//		}
//	}
}
}
