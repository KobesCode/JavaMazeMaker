import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Finds the most efficient solution to the maze using a breadth-first search
 */
public class Solution implements Serializable {
    private static final long serialVersionUID = 3L;
    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;
    private ArrayList<Node> solution;
    private int deadends;
    public Solution() {}

    /**22
     * Finds the most efficient solution to a maze
     * @param maze The maze to find the solution of
     * @return An ArrayList of nodes in the solution
     */
    public ArrayList<Node> findSolution(Maze maze) {
        solution = new ArrayList<>();
        deadends = 0;
        findNodes(maze);
        findEdges(maze);
        BFS(nodes.get(0), maze);
        return solution;
    }

    /**
     * BFS - Performs a breadth-first search on the graph to find the most optimal path.
     * @param s Nodes object
     * @param maze Maze object
     */
    private void BFS(Node s, Maze maze) {
        boolean[] visited = new boolean[nodes.size()];
        LinkedList<Node> queue = new LinkedList<>();
        visited[nodes.indexOf(s)] = true;
        queue.add(s);
        s.setParent(null);

        while (queue.size() != 0) {
            s = queue.poll();
            // If this node is the exit, create the path
            if (maze.getCell(s.getRow(), s.getCol()).equals(maze.getExitCell())) {
                Node n = s;
                while (n.getParent() != null) {
                    solution.add(n);
                    n = n.getParent();
                }
                Collections.reverse(solution);
                solution.add(0, nodes.get(0));
            }
            for (int i = 0; i < edges.size(); i++) {
                Edge edge = edges.get(i);
                if (edge.get()[0].equals(s)) {
                    int j = nodes.indexOf(edge.get()[1]);
                    if (!visited[j]) {
                        visited[j] = true;
                        edge.get()[1].setParent(s);
                        queue.add(edge.get()[1]);
                    }
                } else if (edge.get()[1].equals(s)) {
                    int j = nodes.indexOf((edge.get()[0]));
                    if (!visited[j]) {
                        visited[j] = true;
                        edge.get()[0].setParent(s);
                        queue.add(edge.get()[0]);
                    }
                }
            }
        }
    }

    /**
     * Finds all current nodes in the maze
     * A cell is a node if it is either the entrance, exit or has 2 or more adjacent walls
     * Or three or more adjacent openings
     * @param maze Maze object
     */
    private void findNodes(Maze maze) {
        nodes = new ArrayList<>();
        // Add entrance and exit nodes
        nodes.add(new Node(maze.getEntranceCell()));
        nodes.add(new Node(maze.getExitCell()));
        // Loop over the maze and add all found nodes to nodes
        for (int i = 0; i < maze.getHeight(); i++) {
            for (int j = 0; j < maze.getWidth(); j++) {
                Cell cell = maze.getCell(i, j);
                // Make sure we don't re add the entrance and exit cells
                if (!(cell.equals(maze.getEntranceCell()) || cell.equals(maze.getExitCell()))) {
                    // Add the node if the cell has 2 adjacent sides
                    if (cell.GetTop() == Cell.Cell_Sides.WALL && cell.GetRight() == Cell.Cell_Sides.WALL) {
                        nodes.add(new Node(i, j));
                        if (cell.GetLeft() == Cell.Cell_Sides.WALL || cell.GetBottom() == Cell.Cell_Sides.WALL) { deadends++; }
                    } else if (cell.GetRight() == Cell.Cell_Sides.WALL && cell.GetBottom() == Cell.Cell_Sides.WALL) {
                        nodes.add(new Node(i, j));
                        if (cell.GetTop() == Cell.Cell_Sides.WALL || cell.GetLeft() == Cell.Cell_Sides.WALL) { deadends++; }
                    } else if (cell.GetBottom() == Cell.Cell_Sides.WALL && cell.GetLeft() == Cell.Cell_Sides.WALL) {
                        nodes.add(new Node(i, j));
                        if (cell.GetTop() == Cell.Cell_Sides.WALL || cell.GetRight() == Cell.Cell_Sides.WALL) { deadends++; }
                    } else if (cell.GetLeft() == Cell.Cell_Sides.WALL && cell.GetTop() == Cell.Cell_Sides.WALL) {
                        nodes.add(new Node(i, j));
                        if (cell.GetRight() == Cell.Cell_Sides.WALL || cell.GetBottom() == Cell.Cell_Sides.WALL) { deadends++; }
                    } else {
                        int open = 0;
                        if (cell.GetTop() == Cell.Cell_Sides.OPEN) { open++; }
                        if (cell.GetLeft() == Cell.Cell_Sides.OPEN) { open++; }
                        if (cell.GetRight() == Cell.Cell_Sides.OPEN) { open++; }
                        if (cell.GetBottom() == Cell.Cell_Sides.OPEN) { open++; }
                        if (open >= 3) { nodes.add(new Node(i, j)); }
                    }
                }
            }
        }
    }

    /**
     * Finds edges between nodes in a given maze
     * @param maze takes the maze
     */
    private void findEdges(Maze maze) {
        edges = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            // Find node to the top, if it exists
            if (node.getRow() > 0) {
                Cell cell = maze.getCell(node.getRow(), node.getCol());
                if (cell.GetTop() != Cell.Cell_Sides.WALL) { // Not a wall to the top
                    int cRow = cell.getRow()-1;
                    while (cRow >= 0) {
                        Node cNode = new Node(cRow, node.getCol());
                        if (!node.equals(cNode) && nodes.contains(cNode)) {
                            int weight = node.getRow() - cRow;
                            Edge edge = new Edge(node, cNode, weight);
                            if (!edges.contains(edge)) {
                                edges.add(edge);
                            }
                            break;
                        }
                        cRow -= 1;
                    }
                }
            }
            // Find node to left, if it exists
            if (node.getCol() > 0) { // Node is not on the very left
                Cell cell = maze.getCell(node.getRow(), node.getCol());
                if (cell.GetLeft() != Cell.Cell_Sides.WALL) { // Not a wall to the left
                    int cCol = cell.getCol()-1;
                    while (cCol >= 0) {
                        Node cNode = new Node(node.getRow(), cCol);
                        if (!node.equals(cNode) && nodes.contains(cNode)) {
                            int weight = node.getCol() - cCol;
                            Edge edge = new Edge(node, cNode, weight);
                            if (!edges.contains(edge)) {
                                edges.add(edge);
                            }
                            break;
                        }
                        cCol -= 1;
                    }
                }
            }
            // Find node to the right, if it exists
            if (node.getCol() < maze.getWidth()-1) { // Node is not on the very right
                Cell cell = maze.getCell(node.getRow(), node.getCol());
                if (cell.GetRight() != Cell.Cell_Sides.WALL) { // Not a wall to the right
                    int cCol = cell.getCol()+1;
                    while (cCol < maze.getWidth()) {
                        Node cNode = new Node(node.getRow(), cCol);
                        if (!node.equals(cNode) && nodes.contains(cNode)) {
                            int weight = cCol - node.getCol();
                            Edge edge = new Edge(node, cNode, weight);
                            if (!edges.contains(edge)) {
                                edges.add(edge);
                            }
                            break;
                        }
                        cCol += 1;
                    }
                }
            }
            // Find node below if it exists
            if (node.getRow() < maze.getHeight()-1) {
                Cell cell = maze.getCell(node.getRow(), node.getCol());
                if (cell.GetBottom() != Cell.Cell_Sides.WALL) { // Not a wall to the bottom
                    int cRow = cell.getRow()+1;
                    while (cRow < maze.getHeight()) {
                        Node cNode = new Node(cRow, node.getCol());
                        if (!node.equals(cNode) && nodes.contains(cNode)) {
                            int weight = cRow - node.getRow();
                            Edge edge = new Edge(node, cNode, weight);
                            if (!edges.contains(edge)) {
                                edges.add(edge);
                            }
                            break;
                        }
                        cRow += 1;
                    }
                }
            }
        }
    }
    /**
     *  retrieve teh number of dead ends in solution
     * @return the number of dead ends in the solution

     */
    public int getDeadends() { return deadends; }

    /**
     * A class that contains information relating to the nodes used in a graph
     */
    class Node {
        private int row;
        private int col;
        private Node parent;
        /**
         * Sets the the and column to that of the cell
         */
        public Node(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public Node(Cell cell) {
            this.row = cell.getRow();
            this.col = cell.getCol();
        }
        /**
        *  @return Returns the row of this node
        */
        public int getRow() { return row; }
        /**
         *  @return Returns the column of this node
         */
        public int getCol() { return col; }
        /**
         *  Sets the parent node of this node.
         */
        public void setParent(Node parent) { this.parent = parent; }
        /**
         *  @return Returns the parent node of this node.
         */
        public Node getParent() { return this.parent; }

        @Override
        public String toString() {
            return String.format("Row: %d, Col: %d", row, col);
        }

        @Override
        public boolean equals(Object o) {
            if (o.getClass().equals(this.getClass())) {
                Node comp = (Node) o;
                if (comp.getRow() == row && comp.getCol() == col) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Inner class edge
     * Creates a new edge between two nodes
     */
    class Edge {
        private Node[] edge;
        private int weight;
        /**
         * @param s  add node to array
         * @param f add node to array
         * @param weight set the weight
         */
        public Edge(Node s, Node f, int weight) {
            edge = new Node[2];
            edge[0] = s;
            edge[1] = f;
            this.weight = weight;
        }
        /**
         * @return reversed edge
         */
        private Edge reverse() {
            return new Edge(edge[1], edge[0], weight);
        }
        /**
         * @return node edges
         */
        public Node[] get() { return edge; }
        /**
         * @return reversed edge
         */
        public Node[] getReverse() { return reverse().get(); }

        /**
         *
         * @return string details the weight of edges between two nodes node[0] and node[1]
         */
        @Override
        public String toString() {
            return String.format("From: %s, To: %s, Weight: %d", edge[0], edge[1], weight);
        }

        /**
         *
         * @param o object with node
         * @return true node[0] and node[1] are equal
         */
        @Override
        public boolean equals(Object o) {
            if (o.getClass().equals(this.getClass())) {
                Edge comp = (Edge) o;
                Node s1 = comp.get()[0]; Node f1 = comp.get()[1];
                if ((s1.equals(edge[0]) && f1.equals(edge[1]) || (s1.equals(edge[1]) && f1.equals(edge[0])))) {
                    return true;
                }
            }
            return false;
        }
    }
    /**
     *
     * @return output number of edges
     */
    @Override
    public String toString() {
        String output = "";
        for (int i = 0; i < edges.size(); i++) {
            output += edges.get(i) + "\n";
        }
        return output;
    }
}
