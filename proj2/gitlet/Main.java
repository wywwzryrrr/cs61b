package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author zng.xee
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command");
            return;
        }
        if (!args[0].equals("init") && !Repository.GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        String firstArg = args[0];
        Commands commands = new Commands();
        switch (firstArg) {
            case "init" -> commands.init();
            case "add" -> {
                if (args.length < 2) {
                    System.exit(1);
                }
                commands.add(args[1]);
            }
            case "commit" -> {
                if (args.length < 2) {
                    System.exit(1);
                }
                commands.commit(args[1]);
            }
            case "merge" -> commands.merge(args[1]);
            case "rm" -> commands.rm(args[1]);
            case "log" -> commands.log();
            case "global-log" -> commands.globalLog();
            case "find" -> commands.find(args[1]);
            case "branch" -> commands.branch(args[1]);
            case "rm-branch" -> commands.rmBranch(args[1]);
            case "status" -> commands.status();
            case "checkout" -> {
                // checkout [commit id] -- [file name]
                if (args.length == 4 && args[2].equals("--")) {
                    commands.checkoutCommitFile(args[1], args[3]);
                    // checkout -- [file name]
                } else if (args.length == 3 && args[1].equals("--")) {
                    commands.checkoutFile(args[2]);
                    // checkout [branch name]
                } else if (args.length == 2) {
                    commands.checkoutBranch(args[1]);
                } else {
                    System.out.println("Incorrect operands.");
                }
            }
            case "reset" -> commands.reset(args[1]);
            default -> System.out.println("No command with that name exists.");
        }
    }
}
