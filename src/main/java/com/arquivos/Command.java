package com.arquivos;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Command {

	LIST() {
		@Override
		boolean accept(String command) {
			final var commands = command.split(" ");
			return commands.length > 0 && commands[0].startsWith("LIST") || commands[0].startsWith("list");
		}

		@Override
		Path execute(Path path) throws IOException {

			String pathStr = path.toString();

			File fileName = new File(pathStr);
			File[] fileList = fileName.listFiles();

			for (int i = 0; i < fileList.length; i++) 
				System.out.println(fileList[i].getName());

			return path;
		}
	},
	SHOW() {
		private String[] parameters = new String[]{};

		@Override
		void setParameters(String[] parameters) {
			this.parameters = parameters;
		}

		@Override
		boolean accept(String command) {
			final var commands = command.split(" ");
			return commands.length > 0 && commands[0].startsWith("SHOW") || commands[0].startsWith("show");
		}

		@Override
		Path execute(Path path) throws IOException{

			Path filePath;
			String pathStr = path.toString();       	
			String argStr = parameters[1];
			if(!argStr.contains(".txt")) {
				throw new UnsupportedOperationException("File format not supported");
			}else {
				filePath = Paths.get(pathStr + File.separator + argStr);
				FileReader fileReader = new FileReader();
				fileReader.read(filePath);
			}
			return path;
		}
	},
	BACK() {
		@Override
		boolean accept(String command) {
			final var commands = command.split(" ");
			return commands.length > 0 && commands[0].startsWith("BACK") || commands[0].startsWith("back");
		}

		@Override
		Path execute(Path path) {

			final String ROOT = File.separator + "home" + File.separator + "vitor" + File.separator + "Desktop" + File.separator + "UTFPR" + File.separator + "hd";
			Path currentPath = Paths.get(ROOT);

			if(path.equals(currentPath)) 
				return currentPath;
			else 
				return path.getParent();
		}
	},
	OPEN() {
		private String[] parameters = new String[]{};

		@Override
		void setParameters(String[] parameters) {
			this.parameters = parameters;
		}

		@Override
		boolean accept(String command) {
			final var commands = command.split(" ");
			return commands.length > 0 && commands[0].startsWith("OPEN") || commands[0].startsWith("open");
		}

		@Override
		Path execute(Path path) {

			Path dirPath;
			String pathStr = path.toString();
			dirPath = Paths.get(pathStr + File.separator + parameters[1]);
			String dirPathStr = dirPath.toString();
			// Get the file
			File file = new File(dirPathStr);

			if (file.isDirectory()) {
				return path = Paths.get(pathStr + File.separator + parameters[1]);
			}else {
				throw new UnsupportedOperationException("Not a directory.");
			}
		}
	},
	DETAIL() {
		private String[] parameters = new String[]{};

		@Override
		void setParameters(String[] parameters) {
			this.parameters = parameters;
		}

		@Override
		boolean accept(String command) {
			final var commands = command.split(" ");
			return commands.length > 0 && commands[0].startsWith("DETAIL") || commands[0].startsWith("detail");
		}

		@Override
		Path execute(Path path) throws IOException {
			String pathStr = path.toString();

			BasicFileAttributeView basicView = 
					Files.getFileAttributeView(path, BasicFileAttributeView.class);
			BasicFileAttributes basicAttribs = basicView.readAttributes();

			System.out.println("Creation time: " + basicAttribs.creationTime());
			System.out.println("Last access time: " + basicAttribs.lastAccessTime());
			System.out.println("Last modified time: " + basicAttribs.lastModifiedTime());

			return path;
		}
	},
	EXIT() {
		@Override
		boolean accept(String command) {
			final var commands = command.split(" ");
			return commands.length > 0 && commands[0].startsWith("EXIT") || commands[0].startsWith("exit");
		}

		@Override
		Path execute(Path path) {
			System.out.print("Saindo...");
			return path;
		}

		@Override
		boolean shouldStop() {
			return true;
		}
	};

	abstract Path execute(Path path) throws IOException;

	abstract boolean accept(String command);

	void setParameters(String[] parameters) {
	}

	boolean shouldStop() {
		return false;
	}

	public static Command parseCommand(String commandToParse) {

		if (commandToParse.isBlank()) {
			throw new UnsupportedOperationException("Type something...");
		}

		final var possibleCommands = values();

		for (Command possibleCommand : possibleCommands) {
			if (possibleCommand.accept(commandToParse)) {
				possibleCommand.setParameters(commandToParse.split(" "));
				return possibleCommand;
			}
		}

		throw new UnsupportedOperationException("Can't parse command [%s]".formatted(commandToParse));
	}
}