package commands;

public class Command {

    private String command;
    private String output;
    private String error;

    public Command(String command) {
        this.command = command;
        this.output = "";
        this.error = "";
    }

    public String toString() {
        return getOutput() + "\n" + getError();
    }

    public String getOutput() {
        return this.output;
    }

    public String getError() {
        return this.error;
    }

    public String getCommand() {
        return this.command;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void RunCommand() {
        try {
            ProcessBuilder p = new ProcessBuilder(this.getCommand());
            Process process = p.start();

            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            while ((output = input.readLine()) != null) {
                this.setOutput(this.getOutput() + output + "\n");
            }

            while ((output = error.readLine()) != null) {
                this.setError(this.getOutput() + output + "\n");
            }
        } catch (IOException e) {
            System.out.println("Invalid Command");
            e.printStackTrace();
        }
    }
}