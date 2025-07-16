package gitlet;

interface CommandsInterface {

    void init();

    void commit(String message);

    void add(String filename);

    void rm(String filename);

    void log();

    void globalLog();

    void find(String message);

    void branch(String branchName);

    void rmBranch(String branchName);

    void status();

    void checkoutFile(String filename);

    void checkoutCommitFile(String commitID, String filename);

    void checkoutBranch(String branchName);

    void reset(String commitUID);

    void merge(String branchName);
}
