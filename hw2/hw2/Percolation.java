package hw2;

import edu.princeton.cs.algs4.UF;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[] isOpen;
    private WeightedQuickUnionUF unionUF;

    private int size;
    private int total;
    private int open;

    public Percolation(int N) {
        if (N < 0) {
            throw new IllegalArgumentException();
        }

        isOpen = new boolean[N * N + 2];
        unionUF = new WeightedQuickUnionUF(N * N + 2);
        for (int i = 0; i < N * N + 2; i++) {
            isOpen[i] = false;
        }
        size = N;
        total = N * N;
        open = 0;

        for (int i = 0; i < N; i++) {
            unionUF.union(i, N * N);
            unionUF.union(i + (N - 1) * N, N * N + 1);
        }
    }

    private int xyToOneDimension(int row, int col) {
        return row * size + col;
    }

    public void open(int row, int col) {
        if (row < 0 || row > size || col < 0 || col > size) {
            throw new IndexOutOfBoundsException();
        }

        int pos = xyToOneDimension(row, col);
        if (!isOpen[pos]) {
            isOpen[pos] = true;
            open++;
            connectOpenSite(row, col);
        }
    }

    private void connectOpenSite(int row, int col) {
        int pos = xyToOneDimension(row, col);

        if (row + 1 < size && isOpen(row + 1, col)) {
            unionUF.union(xyToOneDimension(row + 1, col), pos);
        }

        if (row - 1 >= 0 && isOpen(row - 1, col)) {
            unionUF.union(pos, xyToOneDimension(row - 1, col));
        }

        if (col - 1 >= 0 && isOpen(row, col - 1)) {
            unionUF.union(pos, xyToOneDimension(row, col - 1));
        }

        if (col + 1 < size && isOpen(row, col + 1)) {
            unionUF.union(pos, xyToOneDimension(row, col + 1));
        }
    }

    public boolean isOpen(int row, int col) {
        if (row < 0 || row > size || col < 0 || col > size) {
            throw new IndexOutOfBoundsException();
        }

        return isOpen[xyToOneDimension(row, col)];
    }

    public boolean isFull(int row, int col) {
        if (row < 0 || row > size || col < 0 || col > size) {
            throw new IndexOutOfBoundsException();
        }

        return isOpen(row, col) && unionUF.connected(size * size, xyToOneDimension(row, col));
    }

    public int numberOfOpenSites() {
        return open;
    }

    public boolean percolates() {
        if (open == 0) {
            return false;
        }

        return unionUF.connected(size * size, size * size + 1);
    }

}
