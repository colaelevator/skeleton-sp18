package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */

    private int s;
    private boolean isCycle = false;
    private Maze maze;

    public MazeCycles(Maze m) {
        super(m);
        maze = m;
        s = 0;
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    @Override
    public void solve() {
        dfs(s);
    }

    // Helper methods go here
    private void dfs(int s) {
        if (isCycle) {
            return;
        }
        marked[s] = true;
        announce();
        for (int neighbor : maze.adj(s)) {
            if (!marked[neighbor]) {
                edgeTo[neighbor] = s;
                announce();
                distTo[neighbor] = distTo[s] + 1;
                dfs(neighbor);
            } else {
                if (neighbor != edgeTo[s]) {
                    isCycle = true;
                }
            }
        }
    }
}

