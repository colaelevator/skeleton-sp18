package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.*;

public class Solver {


    private static class SearchNode implements Comparable<SearchNode> {
        WorldState Node;
        int moves;
        SearchNode previousNode;

        SearchNode(WorldState init) {
            Node = init;
            moves = 0;
            previousNode = null;
        }

        SearchNode(WorldState p,SearchNode pre) {
            Node = p;
            moves = pre.moves + 1;
            previousNode = pre;
        }

        @Override
        public int compareTo(SearchNode o) {
            return Integer.compare(this.moves + this.Node.estimatedDistanceToGoal(),o.moves + o.Node.estimatedDistanceToGoal());
        }
    }

    private MinPQ<SearchNode> queue;
    private List<WorldState> solution;
    private int moves;

    public Solver(WorldState initial) {
        queue = new MinPQ<>();
        solution = new ArrayList<>();
        queue.insert(new SearchNode(initial));
        while (!queue.isEmpty()) {
            SearchNode best = queue.delMin();
            if (best.Node.isGoal()) {
                getSolutionAndMoves(best);
                return;
            } else {
                for (WorldState i : best.Node.neighbors()) {
                    if (best.previousNode != null && i.equals(best.previousNode.Node)) {
                        continue;
                    } else {
                        queue.insert(new SearchNode(i,best));
                    }
                }
            }
        }
    }

    private void getSolutionAndMoves(SearchNode goalNode) {
        moves = goalNode.moves;
        SearchNode p = goalNode;
        while (p != null) {
            solution.add(p.Node);
            p = p.previousNode;
        }
        Collections.reverse(solution);
    }

    public int moves() {
        return moves;
    }

    public Iterable<WorldState> solution() {
        return solution;
    }

}
