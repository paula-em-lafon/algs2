/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

    private static final int INFINITY = Integer.MAX_VALUE;
    private Digraph graph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        graph = new Digraph(G);
    }

    private void validateVertex(int a) {
        int v = graph.V();
        if (a < 0 || a >= v)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (v - 1));
    }

    private int checkPathSingle(int v, int w, boolean length) {
        boolean[] visited1 = new boolean[graph.V()];
        int[] edgeTo1 = new int[graph.V()];
        int[] distTo1 = new int[graph.V()];

        boolean[] visited2 = new boolean[graph.V()];
        int[] edgeTo2 = new int[graph.V()];
        int[] distTo2 = new int[graph.V()];

        validateVertex(v);
        validateVertex(w);

        Queue<Integer> queue1 = new Queue<Integer>();
        Queue<Integer> queue2 = new Queue<Integer>();

        queue1.enqueue(v);
        queue2.enqueue(w);

        visited1[v] = true;
        distTo1[v] = 0;

        visited2[w] = true;
        distTo1[w] = 0;


        int node1 = INFINITY;
        int node2 = INFINITY;

        while (!queue1.isEmpty() || !queue2.isEmpty()) {
            if (!queue1.isEmpty()) {
                node1 = queue1.dequeue();
                for (int z : graph.adj(node1)) {
                    if (!visited1[z]) {
                        edgeTo1[z] = node1;
                        distTo1[z] = distTo1[node1] + 1;
                        visited1[z] = true;
                        queue1.enqueue(z);
                    }
                }
            }
            if (!queue2.isEmpty()) {
                node2 = queue2.dequeue();
                for (int z : graph.adj(node2)) {
                    if (!visited2[z]) {
                        edgeTo2[z] = node2;
                        distTo2[z] = distTo2[node2] + 1;
                        visited2[z] = true;
                        queue2.enqueue(z);
                    }
                }
            }

            if (visited1[node2] || visited2[node1]) {
                int minNode = node1;
                if (visited1[node2]) {
                    minNode = node2;
                }
                if (length) {
                    return distTo1[minNode] + distTo2[minNode];
                }
                return minNode;
            }
        }
        return -1;
    }

    private int checkPathMulti(Iterable<Integer> v, Iterable<Integer> w, boolean length) {
        boolean[] visited1 = new boolean[graph.V()];
        int[] edgeTo1 = new int[graph.V()];
        int[] distTo1 = new int[graph.V()];

        boolean[] visited2 = new boolean[graph.V()];
        int[] edgeTo2 = new int[graph.V()];
        int[] distTo2 = new int[graph.V()];

        for (int s : v) {
            validateVertex(s);
        }
        for (int s : w) {
            validateVertex(s);
        }

        Queue<Integer> queue1 = new Queue<Integer>();
        Queue<Integer> queue2 = new Queue<Integer>();

        for (int s : w) {
            queue1.enqueue(s);
        }
        for (int s : w) {
            queue2.enqueue(s);
        }

        int node1 = INFINITY;
        int node2 = INFINITY;

        while (!queue1.isEmpty() || !queue2.isEmpty()) {
            if (!queue1.isEmpty()) {
                node1 = queue1.dequeue();
                for (int adj1 : graph.adj(node1)) {
                    if (!visited1[adj1]) {
                        edgeTo1[adj1] = node1;
                        distTo1[adj1] = distTo1[node1] + 1;
                        visited1[adj1] = true;
                        queue1.enqueue(adj1);
                    }
                }
            }
            if (!queue2.isEmpty()) {
                node2 = queue2.dequeue();
                for (int adj2 : graph.adj(node2)) {
                    if (!visited1[adj2]) {
                        edgeTo2[adj2] = node2;
                        distTo2[adj2] = distTo1[node2] + 1;
                        visited2[adj2] = true;
                        queue1.enqueue(adj2);
                    }
                }
            }

            if (visited1[node2] || visited2[node1]) {
                int minNode = node1;
                if (visited1[node2]) {
                    minNode = node2;
                }
                if (length) {
                    return distTo1[minNode] + distTo2[minNode];
                }
                return minNode;
            }
        }
        return -1;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return checkPathSingle(v, w, true);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return checkPathSingle(v, w, false);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return checkPathMulti(v, w, true);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return checkPathMulti(v, w, false);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }

}
