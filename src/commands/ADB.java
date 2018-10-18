package commands;

import java.io.File;

public class ADB {

	public static String wipeCache() {
		Command wipe = new Command("adb wipe cache");
		wipe.RunCommand();
		return wipe.toString();
	}

	public static String wipeData() {
		Command wipe = new Command("adb wipe data");
		wipe.RunCommand();
		return wipe.toString();
	}

	public static String flash(String partition, File file) {
		String c = String.format("fastboot flash %s %s", partition, file.getPath());
		Command flash = new Command(c);
		flash.RunCommand();
		return flash.toString();
	}

	public static String checkADBDevices() {
		Command devices = new Command("adb devices");
		devices.RunCommand();
		return devices.toString();
	}

	public static String checkFastbootDevices() {
		Command devices = new Command("fastboot devices");
		devices.RunCommand();
		return devices.toString();
	}

	public static String install(File file) {
		String c = String.format("adb install %s", file.getPath());
		Command install = new Command(c);
		install.RunCommand();
		return install.toString();
	}

	public static String push(File file) {
		String c = String.format("adb push %s /sdcard/pushedfiles", file.getPath());
		Command push = new Command(c);
		push.RunCommand();
		return push.toString();
	}

	public static String pwd() {
		Command pwd = new Command("pwd");
		pwd.RunCommand();
		return pwd.toString();
	}

}
