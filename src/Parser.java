/*
 * Written by Brently George
 */

import java.util.*;

public class Parser {
	
	private Scanner fileStream;
	private String currentCommand;
	

	public Parser(Scanner aFileStream) {
		this.fileStream = aFileStream;
		this.currentCommand = "";
	}
	
	public String getCurrentCommand() {
		return this.currentCommand;
	}
	
	//Are there more commands in the input?
	public boolean hasMoreCommands() {
		return fileStream.hasNextLine();
	}
	
	/*
	 * Reads the next command from the input and makes it the current command.
	 * Should be called only if hasMoreCommands() is true. Initially there is no
	 * current command.
	 */
	 
	public void advance() {
		this.currentCommand = fileStream.nextLine().trim();
		while((this.currentCommand.startsWith("//") || this.currentCommand.isBlank() || this.currentCommand.isEmpty()) && this.hasMoreCommands()) {
			this.currentCommand = fileStream.nextLine().trim();
		}
	}
	
	/*
	 * Returns the type of the current command:
	 * >>> A_COMMAND for @Xxx where Xxx is either a symbol or a decimal number
	 * >>> C_COMMAND for dest=comp;jump
	 * >>> L_COMMAND (actually, pseudo- command) for (Xxx) where Xxx is a symbol.
	 */
	
	public String commandType() {
		
		String firstChar = this.currentCommand.substring(0,1);
		
		switch(firstChar) {
			case "@":
				return "A_COMMAND";
			case "(":
				return "L_COMMAND";
			default:
				return "C_COMMAND";
		}
	}
	
	/*
	 * Returns the symbol or decimal Xxx of the current command
	 * @Xxx or (Xxx). Should be called only when commandType() is A_COMMAND or
	 * L_COMMAND.
	 */
	
	public String symbol() {
		switch(this.commandType()) {
			case "A_COMMAND":
				return this.currentCommand.substring(1);
			case "L_COMMAND":
				Integer len = this.currentCommand.length();
				return this.currentCommand.substring(1, len-1);
			default:
				return null;
		}
	}
	
	/*
	 * Returns the dest mnemonic in the current C-command (8 possi- bilities).
	 * Should be called only when commandType() is C_COMMAND.
	 */
	
	public String dest() {
		int eqIndex = this.currentCommand.indexOf("=");
		if(eqIndex == -1) {
			return null;
		} else {
			return this.currentCommand.substring(0,eqIndex);
		}
	}
	
	/*
	 * Returns the comp mnemonic in the current C-command (28 pos- sibilities).
	 * Should be called only when commandType() is C_COMMAND.
	 */
	
	public String comp() {
		int eqIndex = this.currentCommand.indexOf("=");
		int semiIndex = this.currentCommand.indexOf(";");
		if (eqIndex == -1) {
			return this.currentCommand.substring(0,semiIndex);
		} else if (semiIndex == -1) {
			return this.currentCommand.substring(eqIndex+1);
		} else {
			return this.currentCommand.substring(eqIndex+1, semiIndex);
		}
	}
	
	/*
	 * Returns the jump mnemonic in the current C-command (8 pos- sibilities).
	 * Should be called only when commandType() is C_COMMAND.
	 */
	
	public String jump() {
		int semiIndex = this.currentCommand.indexOf(";");
		if (semiIndex == -1) {
			return null;
		} else {
			return this.currentCommand.substring(semiIndex+1);
		}
	}
	
}
