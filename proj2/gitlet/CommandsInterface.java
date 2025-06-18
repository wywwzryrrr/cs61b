package gitlet;

public interface CommandsInterface {

    public void init();

    public void commit();

    public void add(String[] args);

    public void rm();

    public void log();

    public void globalLog();

    public void find();

    public void branch();

    public void rmBranch();

    public void status();

    public void checkout();

    public void reset();

    public void merge();
}
