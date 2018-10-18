import java.util.ArrayList;
import java.io.*;
import java.nio.file.Files;


public class ADB {

	private class Command {

		private String command;
		private String output;
		private String error;

		public Command(String command){
			this.command = command;
			this.output = "";
			this.error = "";
		}

		public String toString(){
			return getOutput() + "\n" + getError();
		}

		public String getOutput(){ return this.output; }
		public String getError(){ return this.error; }
		public String getCommand(){ return this.command; }

		public void setOutput(String output){ this.output = output; }
		public void setError(String error){ this.error = error; }

	}

	private static void RunCommand(Command cm) {
		try {
			ProcessBuilder p = new ProcessBuilder(cm.getCommand());
			Process process = p.start();

			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			while ((output = input.readLine()) != null) {
				cm.setOutput(cm.getOutput() + output + "\n");
            }
			
			while ((output = error.readLine()) != null) {
				cm.setError(cm.getOutput() + output + "\n");
            }
		} catch (IOException e){
			System.out.println("Invalid Command");
			e.printStackTrace();
		}
	}
	
	public static String wipeCache() {
		Command wipe = new Command("adb wipe cache");
		RunCommand(wipe);
		return wipe.toString();
	}

	public static String wipeData() {
		Command wipe = new Command("adb wipe data");
		RunCommand(wipe);
		return wipe.toString();
	}

	public static flash(String partition, File file) {
		String c = String.format("fastboot flash %s %s", partition, file.getPath());
		Command flash = new Command(c);
		RunCommand(flash);
		return flash.toString();
	}
}
