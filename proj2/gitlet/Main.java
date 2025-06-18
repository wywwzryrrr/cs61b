package gitlet;

import java.io.File;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args.length == 0 || args == null) {
            System.out.println("Please enter a command");
            System.exit(0);
        }
        String firstArg = args[0];
        Commands commands = new Commands();
        switch (firstArg) {
            case "init" -> commands.init();
            case "add" -> commands.add(args);
            case "commit" -> commands.commit();
            case "merge" -> commands.merge();
            case "rm" -> commands.rm();
            case "log" -> commands.log();
            case "globalLog" -> commands.globalLog();
            case "find" -> commands.find();
            case "branch" -> commands.branch();
            case "rmBranch" -> commands.rmBranch();
            case "status" -> commands.status();
            case "checkout" -> commands.checkout();
            case "reset" -> commands.reset();
            default -> System.out.println("No command with that name exists.");
        }
    }
}
