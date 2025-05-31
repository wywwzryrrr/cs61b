import java.util.*;

public class Graph<T>{



    public void printGraph() {
        for (int v = 0; v < G.V(); v++) {
            for (int w : G.adj(v)) {
                System.out.print(v + "-" + w);
            }
        }
    }
}