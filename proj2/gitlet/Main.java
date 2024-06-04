package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                if (args.length != 1) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.init();
                break;
            case "add":
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.add(args[1]);
                break;
            case "commit":
                if (args.length < 2) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                } else if (args.length > 2) {
                    System.out.println("Invalid operands.");
                    System.exit(0);
                }
                Repository.commit(args[1]);
                break;
            case "rm":
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.remove(args[1]);
                break;
            case "log":
                if (args.length != 1) {
                    System.out.println("Invalid operands.");
                    System.exit(0);
                }
                Repository.log();
                break;
            case "global-log":
                if (args.length != 1) {
                    System.out.println("Invalid operands.");
                    System.exit(0);
                }
                Repository.globalLog();
                break;
            case "checkout":
                if (args[1].equals("--") && args.length == 3) {
                    Repository.checkout(args[2]);
                } else if (args.length == 4 && args[2].equals("--")) {
                    Repository.checkout(args[3], args[1]);
                } else if (args.length == 2) {
                    Repository.checkoutBranch(args[1]);
                } else {
                    System.out.println("Invalid operands.");
                    System.exit(0);
                }
                break;
            case "find":
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.find(args[1]);
                break;
            case "status":
                if (args.length != 1) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.status();
                break;
            case "branch":
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.branch(args[1]);
                break;
            case "rm-branch":
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.removeBranch(args[1]);
                break;
            case "reset":
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.reset(args[1]);
                break;
            case "merge":
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.merge(args[1]);
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }
}
