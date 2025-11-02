package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState {

    private int[][] tiles;
    private int size;
    private int BLANK = 0;
    private int hamming = -1;
    private int manhattan = -1;

    public Board(int[][] tiles) {
        if (tiles == null || tiles[0] == null || tiles.length != tiles[0].length) {
            throw new IllegalArgumentException();
        }
        this.size = tiles.length;
        this.tiles = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }

    public int tileAt(int i,int j) {
        if (i < 0 || i >= size() || j < 0 || j >= size()) {
            throw new IndexOutOfBoundsException();
        }
        return tiles[i][j];
    }

    public int size() {
        return size;
    }

    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == BLANK) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = BLANK;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = BLANK;
                }
            }
        }
        return neighbors;
    }

    public int hamming() {
        if (hamming == -1) {
            hamming = computeHamming();
        }
        return hamming;
    }

    private int computeHamming() {
        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] != i * size + j) {
                    count++;
                }
            }
        }
        return count;
    }

    public int manhattan() {
        if (manhattan == -1) {
            manhattan = computeManhatten();
        }
        return manhattan;
    }

    private int computeManhatten() {
        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] != BLANK) {
                    count += findGoal(i,j);
                }
            }
        }
        return count;
    }

    private int findGoal(int i ,int j) {
        int init = tileAt(i,j);
        int row = (init - 1) / size;
        int col = (init - 1) % size;
        return Math.abs(i - row) + Math.abs(j - col);
    }

    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }
        if (y == null || this.getClass() != y.getClass()) {
            return false;
        }
        Board other = (Board) y;
        if (this.size != other.size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (this.tiles[i][j] != other.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Returns the string representation of the board.
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
