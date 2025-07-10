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
            System.exit(1);
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
            case "merge" -> commands.merge();
            case "rm" -> commands.rm(args[1]);
            case "log" -> commands.log();
            case "globalLog" -> commands.globalLog();
            case "find" -> commands.find();
            case "branch" -> commands.branch(args[1]);
            case "rm-branch" -> commands.rmBranch(args[1]);
            case "status" -> commands.status();
            case "checkout" -> {
                // 形式 2: checkout [commit id] -- [file name]
                // 这是最具体的，有4个参数，且第3个是 "--"
                if (args.length == 4 && args[2].equals("--")) {
                    commands.checkoutCommitFile(args[1], args[3]);
                    // 形式 1: checkout -- [file name]
                    // 有3个参数，且第2个是 "--"
                } else if (args.length == 3 && args[1].equals("--")) {
                    commands.checkoutFile(args[2]);
                    // 形式 3: checkout [branch name]
                    // 只有2个参数
                } else if (args.length == 2) {
                    commands.checkoutBranch(args[1]);
                    // 其他所有情况都是格式错误
                } else {
                    System.out.println("Incorrect operands.");
                }
            }
            case "reset" -> commands.reset();
            default -> System.out.println("No command with that name exists.");
        }
    }
}
