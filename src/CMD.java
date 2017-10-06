import java.util.ArrayList;
import java.io.*;
import java.nio.file.Files;


public class CMD {

	//Make different method for each command and delegate?
	//http://stackoverflow.com/questions/16757783/java-application-to-execute-commands-in-command-prompt
	
	public static void commandWindowsADB(ArrayList<Command> cm){
		String output = null;
		String[] command = new String[cm.size()+1];
		File dir = new File(getDrive() + "\\Users\\" + getUser() + "\\Documents\\" + "ADB");
		//command[0] = "ipconfig";
		for(int i = 0; i < cm.size(); i++){
		command[i+1] = cm.get(i).getCommand();
		}
		command[0] = getDrive() + "\\Users\\" + getUser() + "\\Documents\\" + "ADB\\adb.exe";
		try {
			ProcessBuilder p = new ProcessBuilder(command);
			p.directory(dir);
			//Process p = Runtime.getRuntime().exec(command);
			Process process = p.start();
			
			BufferedReader input = new BufferedReader(new
	                 InputStreamReader(process.getInputStream()));
			
			BufferedReader error = new BufferedReader(new
	                 InputStreamReader(process.getErrorStream()));
			
			while ((output = input.readLine()) != null) {
				cm.get(0).setOutput(cm.get(0).getOutput() + "\n" + output);
                //System.out.println(output);
            }
			
			while ((output = error.readLine()) != null) {
				cm.get(0).setOutput(cm.get(0).getOutput() + "\n" + output);
                //System.out.println(output);
            }
			
		} catch (IOException e) {
			System.out.println("File Not Found");
			e.printStackTrace();
		}
		System.out.println(cm.get(0).getOutput());
		
	}
	public static void commandWindowsADB(Command cm){
		String output = null;
		String[] command = new String[1];
		File dir = new File(getDrive() + "\\Users\\" + getUser() + "\\Documents\\" + "ADB");
		
		//File dir = new File(getDrive() + "\\Users\\" + getUser() + "\\Desktop\\");
		//command[0] = "cd " + getDrive() + "\\Users\\" + getUser() + "\\Documents\\" + "ADB";
		String s = getDrive() + "\\Users\\" + getUser() + "\\Documents\\" + "ADB\\adb.exe";
		
		try {
			ProcessBuilder p = new ProcessBuilder(s, cm.getCommand());
			p.directory(dir);
			Process process = p.start();
			
			BufferedReader input = new BufferedReader(new
	                 InputStreamReader(process.getInputStream()));
			
			BufferedReader error = new BufferedReader(new
	                 InputStreamReader(process.getErrorStream()));
			
			while ((output = input.readLine()) != null) {
				if(cm.getOutput() == null){
					cm.setOutput(output);
				}
				else{
				cm.setOutput(cm.getOutput() + "\n" + output);
				}
            }
			
			while ((output = error.readLine()) != null) {
				if(cm.getOutput() == null){
					cm.setOutput(output);
				}
				else{
				cm.setOutput(cm.getOutput() + "\n" + output);
				}
            }
			System.out.println(cm.getOutput());
			
		} catch (IOException e) {
			System.out.println("File Not Found");
			e.printStackTrace();
		}
	}
	public static String getOutput(ArrayList<Command> cm){
		String s = "";
		for(int i = 0; i < cm.size(); i++){
			s+= cm.get(i).getOutput();
		}
		return s;
	}
	public static String getUser(){
		return System.getProperty("user.name");
	}
	public static String getDrive(){
		return System.getenv("SystemDrive");
	}
	public static void main(String[] args){
		ArrayList<Command> cm = new ArrayList<>();
		Command c = new Command("reboot");
		Command c1 = new Command("bootloader");
		cm.add(c);
		cm.add(c1);
		commandWindowsADB(cm);
	}
	//finish with adb files on laptop
	//http://examples.javacodegeeks.com/core-java/io/file/4-ways-to-copy-file-in-java/
	public static void installFiles() throws IOException{
		File firstRun = new File(getDrive() + "\\Users\\" + getUser() + "\\documents\\ADB\\hasRan.txt");
		if(!firstRun.exists()){
			firstRun.createNewFile();
			File adbSrc = null;
			File fastSrc = null;
			File adbDest = new File(getDrive() + "\\Users\\" + getUser() + "\\documents\\ADB\\ADB.exe");
			File fastDest = new File(getDrive() + "\\Users\\" + getUser() + "\\documents\\ADB\\fastboot.exe");
			installFiles(adbSrc,adbDest);
			installFiles(fastSrc, fastDest);
			File backupDir = new File(getDrive() + "\\Users\\" + getUser() + "\\documents\\ADB\\backups");
			if(!backupDir.exists()){
				backupDir.mkdir();
			}
		}
	}
	public static void installFiles(File src, File dest) throws IOException{
		Files.copy(src.toPath(), dest.toPath());
	}
}
