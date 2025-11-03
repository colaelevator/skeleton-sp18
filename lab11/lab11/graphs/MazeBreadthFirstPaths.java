package lab11.graphs;

import edu.princeton.cs.algs4.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */

    private int s;
    private int t;
    private Maze maze;
    private boolean targetFound = false;
    private Queue<Integer> queue;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX,sourceY);
        t = maze.xyTo1D(targetX,targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs(int s) {
        queue = new Queue<>();
        queue.enqueue(s);
        marked[s] = true;
        distTo[s] = 0;
        announce();
        while (!queue.isEmpty()) {
            int nearest = queue.dequeue();
            marked[nearest] = true;
            announce();
            for (int neighbor : maze.adj(nearest)) {
                if (!marked[neighbor]) {
                    queue.enqueue(neighbor);
                    edgeTo[neighbor] = nearest;
                    distTo[neighbor] = distTo[nearest] + 1;
                    announce();
                }
            }
            if (nearest == t) {
                targetFound = true;
            }
            if (targetFound) {
                return;
            }
        }
    }


    @Override
    public void solve() {
        bfs(s);
    }
}

