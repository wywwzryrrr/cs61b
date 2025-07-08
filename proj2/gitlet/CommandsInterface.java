package gitlet;

public interface CommandsInterface {

    public void init();

    public void commit(String message);

    public void add(String filename);

    public void rm();

    public void log();

    public void globalLog();

    public void find();

    public void branch(String branchName);

    public void rmBranch(String branchName);

    public void status();

    public void checkoutFile(String filename);

    public void checkoutCommitFile(String commitID, String filename);

    public void checkoutBranch(String branchName);

    public void reset();

    public void merge();
}
