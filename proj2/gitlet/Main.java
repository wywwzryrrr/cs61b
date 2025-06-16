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
        String firstArg = args[0];
        Commands commands = new Commands();
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                commands.init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                commands.add();
                break;
            // TODO: FILL THE REST IN
            case "commit":
                commands.commit();
                break;
            case "merge":
                commands.merge();
                break;
            case "rm":
                commands.rm();
                break;
            case "log":
                commands.log();
                break;
            case "globalLog":
                commands.globalLog();
                break;
                case ""
        }
    }
}
