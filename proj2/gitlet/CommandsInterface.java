package gitlet;

public interface CommandsInterface {

    public void init();

    public void commit(String message);

    public void add(String filename);

    public void rm();

    public void log();

    public void globalLog();

    public void find();

    public void branch();

    public void rmBranch();

    public void status();

    public void checkout1(String[] args);

    public void checkout2(String[] args);

    public void checkout3(String[] args);

    public void reset();

    public void merge();
}
