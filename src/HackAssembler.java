/*
 * Written by Brently George
 */

import java.util.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class HackAssembler {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//TODO get file name from argument
		String fileName = "Pong.asm";
		//load file stream object
		Scanner myFileStream = loadAsmFile(fileName);
		//load the parser with the file stream
		Parser parser = new Parser(myFileStream);
		//load the code module
		Code code = new Code();
		//load the output file
		//TODO update the output file name based on input file name
		String outputFileName = "Pong.hack";
		//setup output file
		PrintWriter fileWriter = setupOutputFile(outputFileName);
		SymbolTable sym = new SymbolTable();
		String ret = "";
		Boolean firstPass = true;
		int lineCount = 0;
		int memCount = 16;
		
		//first pass
		while(parser.hasMoreCommands()) {
			parser.advance();
			if(parser.commandType() == "L_COMMAND") {
				sym.addEntry(parser.symbol(), lineCount);
			} else {
				lineCount++;
			}
		}
		
		myFileStream.close();
		myFileStream = loadAsmFile(fileName);
		parser = new Parser(myFileStream);
	
		  while(parser.hasMoreCommands()) { 
			  parser.advance();
			  switch(parser.commandType()) {
			  	case "A_COMMAND":
			  		if(parser.symbol().chars().allMatch(Character::isDigit)) {
			  			ret = Integer.toBinaryString(Integer.valueOf(parser.symbol()));
			  		} else {
			  			if(!sym.contains(parser.symbol())) {
			  				sym.addEntry(parser.symbol(), memCount);
			  				memCount++;
			  			}
			  			ret = Integer.toBinaryString(Integer.valueOf(sym.getAddress(parser.symbol())));
			  		}
			  		while(ret.length()<16) {
			  			ret = "0" + ret;
			  			}
			  		System.out.println(ret);
			  		fileWriter.println(ret);
			  		break;
			  	case "C_COMMAND":
			  		ret = "111";
			  		ret += code.comp(parser.comp());
			  		ret += code.dest(parser.dest());
			  		ret += code.jump(parser.jump());
			  		System.out.println(ret);
			  		fileWriter.println(ret);
			  		break;
			  	case "L_COMMAND":
			  		break;
			  	}
			  }
		 
	

		//ending close streams
		myFileStream.close();
		fileWriter.close();
		
	}
	
	public static Scanner loadAsmFile(String fileName) {
		try {
			Scanner newFileScanner = new Scanner(new File(fileName));
			return newFileScanner;
		} catch (Exception e) {
			System.out.println("Unable to load the file, please try again.");
			System.out.println("ERROR: "+e.getMessage());
			return null;
		}
	}
	
	public static PrintWriter setupOutputFile(String fileName) {
		try {
			PrintWriter newFileWriter = new PrintWriter(new FileOutputStream(fileName));
			return newFileWriter;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getFileName() {
		return "test";
	}

}
