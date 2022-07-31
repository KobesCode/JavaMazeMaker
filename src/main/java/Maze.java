import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Random;
/**
 * Creates and stores information on a new maze object
 */
public class Maze implements Serializable {
    private static final long serialVersionUID = 1L;
    private int width, height;
    private Cell[][] maze;
    private int id;
    private LocalDateTime CreationDateTime;
    private LocalDateTime LastEditedDateTime;
    private Cell entranceCell;
    private Cell exitCell;
    private Solution solution;
    private ArrayList<Cell> solutionCells;
    private boolean showSolution;
    private String title;
    private String author;
    private Random random;
    private ArrayList<ArrayList<Cell>> ImgCells;

    /**
     * Creates a new maze object
     * @param height The number of rows the maze will have
     * @param width The number of columns the maze will have
     */
    public Maze(int height, int width) throws IllegalArgumentException {
        if (height <= 0 || height > 100 || width <= 0 || width > 100) {
            throw new IllegalArgumentException("Invalid Maze Size");
        }
        this.width = width;
        this.height = height;
        this.maze = new Cell[height][width];
        this.solution = new Solution();
        this.random = new Random(System.currentTimeMillis());
        this.ImgCells = new ArrayList<>();
    }

    /**
     * Fills the maze with blank cells. Creates a border around the edge. Adds an entrance on the top left cell.
     * Adds an exit on the bottom right cell.
     */
    public void GenerateEmpty() {
        CreationDateTime = LocalDateTime.now();
        Cell.Cell_Size cellSize;
        int area = height * width;
        if (area <= 400) { cellSize = Cell.Cell_Size.EXTRA_LARGE; } // Less than or equal to 20x20
        else if (area <= 1600) { cellSize = Cell.Cell_Size.LARGE; } // Less that or equal to 40x40
        else if (area <= 3600) { cellSize = Cell.Cell_Size.MEDIUM; } // Less than or equal to 60x60
        else if (area <= 6400) { cellSize = Cell.Cell_Size.SMALL; } // Less than or equal to 80x80
        else { cellSize = Cell.Cell_Size.EXTRA_SMALL; } // Less than or equal to 100x100

        for (int i = 0; i < height; i++) {
            // Loop over every cell, make each cell blank
            for (int j = 0; j < width; j++) {
                Cell.Cell_Sides top = Cell.Cell_Sides.OPEN;
                Cell.Cell_Sides left = Cell.Cell_Sides.OPEN;
                Cell.Cell_Sides right = Cell.Cell_Sides.OPEN;
                Cell.Cell_Sides bottom = Cell.Cell_Sides.OPEN;
                // Add walls to the top cells
                if (i == 0) {
                    top = Cell.Cell_Sides.WALL;
                }
                // Add walls to the bottom cells
                if (i == height-1) {
                    bottom = Cell.Cell_Sides.WALL;
                }
                // Add walls to the left cells
                if (j == 0) {
                    left = Cell.Cell_Sides.WALL;
                }
                // Add walls to the right cells
                if (j == width-1) {
                    right = Cell.Cell_Sides.WALL;
                }
                // Create entrance in the first cell
                if (i == 0 && j==0) {
                    top = Cell.Cell_Sides.ENTRANCE;
                }
                // Create exit in the last cell
                if (i == height - 1 && j == width - 1) {
                    bottom = Cell.Cell_Sides.EXIT;
                }
                // Add the cell to the maze
                maze[i][j] = new Cell(top, left, right, bottom, cellSize, i, j);
                if (top == Cell.Cell_Sides.ENTRANCE) { entranceCell = maze[i][j]; }
                if (bottom == Cell.Cell_Sides.EXIT) { exitCell = maze[i][j]; }
            }
        }
        // Create Logo Area
        int logoSize;
        switch (cellSize) {
            case EXTRA_SMALL:
                logoSize = 8;
                break;
            case SMALL:
                logoSize = 6;
                break;
            case MEDIUM:
            case LARGE:
                logoSize = 4;
                break;
            case EXTRA_LARGE:
                logoSize = 2;
                break;
            default:
                logoSize = 8;
                break;
        }
        // Check that there's room for a logo of size logoSize * logoSize
        if (width > (logoSize + 2) && height > (logoSize + 2)) {
            int startX = random.nextInt(1, width-1-logoSize);
            int startY = random.nextInt(1, height-1-logoSize);
            for (int i = startY; i < startY+logoSize; i++) {
                for (int j = startX; j < startX+logoSize; j++) {
                    if (i == startY) { maze[i][j].SetTop(Cell.Cell_Sides.WALL); }
                    if (i == startY + logoSize - 1) { maze[i][j].SetBottom(Cell.Cell_Sides.WALL); }
                    if (j == startX) { maze[i][j].SetLeft(Cell.Cell_Sides.WALL); }
                    if (j == startX + logoSize - 1) { maze[i][j].SetRight(Cell.Cell_Sides.WALL); }
                }
            }
        }
    }

    /**
     * Random maze generation helper function to find the new cell x position
     * based on a direction
     * @param direction direction of the new cell (N,E,S,W)
     * @return +1 for E, -1 for W
     */
    private int DX(char direction) {
        switch (direction) {
            case 'N':
            case 'S':
                return 0;
            case 'E':
                return 1;
            case 'W':
                return -1;
        }
        return 0;
    }
    /**
     * Random maze generation helper function to find the new cell y position
     * based on a direction
     * @param direction direction of the new cell (N,E,S,W)
     * @return +1 for S, -1 for N
     */
    private int DY(char direction) {
        switch (direction) {
            case 'N':
                return -1;
            case 'S':
                return 1;
            case 'E':
            case 'W':
                return 0;
        }
        return 0;
    }

    /**
     * Fills the maze with cells following a growing tree algorithm
     */
    public void GenerateRandom() {
        CreationDateTime = LocalDateTime.now();
        Cell.Cell_Size cellSize;
        int area = height * width;
        if (area <= 400) { cellSize = Cell.Cell_Size.EXTRA_LARGE; } // Less than or equal to 20x20
        else if (area <= 1600) { cellSize = Cell.Cell_Size.LARGE; } // Less that or equal to 40x40
        else if (area <= 3600) { cellSize = Cell.Cell_Size.MEDIUM; } // Less than or equal to 60x60
        else if (area <= 6400) { cellSize = Cell.Cell_Size.SMALL; } // Less than or equal to 80x80
        else { cellSize = Cell.Cell_Size.EXTRA_SMALL; } // Less than or equal to 100x100
        // Create Logo Area
        int logoSize;
        switch (cellSize) {
            case EXTRA_SMALL:
                logoSize = 8;
                break;
            case SMALL:
                logoSize = 6;
                break;
            case MEDIUM:
            case LARGE:
                logoSize = 4;
                break;
            case EXTRA_LARGE:
                logoSize = 2;
                break;
            default:
                logoSize = 8;
                break;
        }
        // Check that there's room for a logo of size logoSize * logoSize
        if (width > (logoSize + 2) && height > (logoSize + 2)) {
            int startX = random.nextInt(1, width-1-logoSize);
            int startY = random.nextInt(1, height-1-logoSize);
            // Create the cells
            for (int i = startY; i < startY+logoSize; i++) {
                for (int j = startX; j < startX+logoSize; j++) {
                    maze[i][j] = new Cell(Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN,
                            cellSize, i, j);
                }
            }
            // Fill the cells
            for (int i = startY; i < startY+logoSize; i++) {
                for (int j = startX; j < startX+logoSize; j++) {
                    if (i == startY) { maze[i][j].SetTop(Cell.Cell_Sides.WALL); }
                    if (i == startY + logoSize - 1) { maze[i][j].SetBottom(Cell.Cell_Sides.WALL); }
                    if (j == startX) { maze[i][j].SetLeft(Cell.Cell_Sides.WALL); }
                    if (j == startX + logoSize - 1) { maze[i][j].SetRight(Cell.Cell_Sides.WALL); }
                }
            }
        }
        // Choose a random maze position
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        // Create an ArrayList of cells to check
        ArrayList<Cell> cellTest = new ArrayList();
        // Create and add the randomly selected cell to the cellTest ArrayList
        cellTest.add(cellTest.size(), new Cell(Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN,
                Cell.Cell_Sides.OPEN, cellSize, y, x));
        // Create an ArrayList of directions to checks
        // Using an ArrayList so we can use the Collections.shuffle method on it
        ArrayList<Character> directions = new ArrayList<>();
        directions.add('N'); directions.add('E'); directions.add('S'); directions.add('W');

        // While there are still cells to check
        while (cellTest.size() > 0) {
            // Check the newest cell added to cellTest
            int index = cellTest.size()-1;
            int col = cellTest.get(index).getCol();
            int row = cellTest.get(index).getRow();

            // Shuffle the directions to pick a random directions
            Collections.shuffle(directions);
            for (int i = 0; i < directions.size(); i++) {
                // Get the coordinates for a new cell using the chosen direction
                int ncol = col + DX(directions.get(i));
                int nrow = row + DY(directions.get(i));
                // If the new cell is within the bounds of the maze AND the new cell has not yet been created
                if (ncol >= 0 && nrow >= 0 && ncol < width && nrow < height && maze[nrow][ncol] == null) {
                    // If this current cell is empty, create it
                    if (maze[row][col] == null) {
                        maze[row][col] = new Cell(Cell.Cell_Sides.WALL, Cell.Cell_Sides.WALL, Cell.Cell_Sides.WALL,
                                Cell.Cell_Sides.WALL, cellSize, row, col);
                    }
                    // Create a new cell in the new position
                    maze[nrow][ncol] = new Cell(Cell.Cell_Sides.WALL, Cell.Cell_Sides.WALL, Cell.Cell_Sides.WALL,
                            Cell.Cell_Sides.WALL, cellSize, nrow, ncol);
                    // Add the new cell to cellTest to be checked next
                    cellTest.add(maze[nrow][ncol]);
                    // Break through the wall between this cell and the new one
                    switch (directions.get(i)) {
                        case 'N':
                            maze[row][col].SetTop(Cell.Cell_Sides.OPEN);
                            maze[nrow][ncol].SetBottom(Cell.Cell_Sides.OPEN);
                            break;
                        case 'S':
                            maze[row][col].SetBottom(Cell.Cell_Sides.OPEN);
                            maze[nrow][ncol].SetTop(Cell.Cell_Sides.OPEN);
                            break;
                        case 'E':
                            maze[row][col].SetRight(Cell.Cell_Sides.OPEN);
                            maze[nrow][ncol].SetLeft(Cell.Cell_Sides.OPEN);
                            break;
                        case 'W':
                            maze[row][col].SetLeft(Cell.Cell_Sides.OPEN);
                            maze[nrow][ncol].SetRight(Cell.Cell_Sides.OPEN);
                            break;
                    }
                    break;
                }
            }
            // Run a check on the neighbours of this cell, if they're all full remove it.
            ArrayList<Cell> checkList = new ArrayList<>();
            if (row-1 >= 0) { checkList.add(maze[row-1][col]);} // Add top cell if it exists
            if (row+1 < height) { checkList.add(maze[row+1][col]); } // Add bottom cell if it exists
            if (col-1 >= 0) { checkList.add(maze[row][col-1]);} // Add left cell if it exists
            if (col+1 < width) { checkList.add(maze[row][col+1]); } // Add right cell if it exists
            int nulls = 0;
            for (int i = 0; i < checkList.size(); i++) {
                if (checkList.get(i) == null) { nulls++; }
                if (i == checkList.size()-1 && nulls == 0) {
                    cellTest.remove(index);
                }
            }
        }
        // Add a random entrance to the top
        int entLoc = random.nextInt(width);
        maze[0][entLoc].SetTop(Cell.Cell_Sides.ENTRANCE);
        int exitLoc = random.nextInt(width);
        maze[height-1][exitLoc].SetBottom(Cell.Cell_Sides.EXIT);
        entranceCell = maze[0][entLoc];
        exitCell = maze[height-1][exitLoc];
    }
/**
 *  Clear solution cells arraylist
    */
    public void ClearSolution() {
        if (solutionCells == null) {
            solutionCells = new ArrayList<>();
        } else {
            // Clear all cells in the solution
            for (int i = 0; i < solutionCells.size(); i++) {
                Cell cell = solutionCells.get(i);
                maze[cell.getRow()][cell.getCol()].setSolution(false);
            }
            // Clear the ArrayList
            solutionCells = new ArrayList<>();
        }
    }
    /**
     * Update the solution of the maze
     */
    private void UpdateSolutionCells() {
        // Clear solution cells
        ClearSolution();
        ArrayList<Solution.Node> sol = solution.findSolution(this);
        for (int i = 0; i < sol.size()-1; i++) {
            Solution.Node node = sol.get(i);
            Solution.Node next = sol.get(i+1);
            // The next node is up from this one
            if (node.getRow() > next.getRow()) {
                int r = node.getRow();
                while (r >= next.getRow()) {
                    maze[r][node.getCol()].setSolution(true);
                    solutionCells.add(maze[r][node.getCol()]);
                    r--;
                }
            } else if (node.getRow() < next.getRow()) {
                int r = node.getRow();
                while (r <= next.getRow()) {
                    maze[r][node.getCol()].setSolution(true);
                    solutionCells.add(maze[r][node.getCol()]);
                    r++;
                }
            } else if (node.getCol() < next.getCol()) {
                int c = node.getCol();
                while (c <= next.getCol()) {
                    maze[node.getRow()][c].setSolution(true);
                    solutionCells.add(maze[node.getRow()][c]);
                    c++;
                }
            } else if (node.getCol() > next.getCol()) {
                int c = node.getCol();
                while (c >= next.getCol()) {
                    maze[node.getRow()][c].setSolution(true);
                    solutionCells.add(maze[node.getRow()][c]);
                    c--;
                }
            }
        }
        // Remove duplicates
        LinkedHashSet hashSet = new LinkedHashSet<>(solutionCells);
        solutionCells = new ArrayList<>(hashSet);
    }

    /**
     * get width of maze
     * @return Number of columns in the maze
     */
    public int getWidth() { return width; }

    /**
     * get height of maze
     * @return Number of rows in the maze
     */
    public int getHeight() { return height; }

    /**
     * get cell location in maze
     * @param row The row index of the requested cell
     * @param column The column index of the requested cell
     * @return The cell at the requested row, column co-ordinate
     */
    public Cell getCell(int row, int column) {
        return maze[row][column];
    }

    /**
     * set title to maze
     * @param title Title in string
     */
    public void setTitle(String title) { this.title = title; }

    /**
     *  getter method for the title
     * @return return maze title
     */
    public String getTitle() { return title; }

    /**
     * set author name to maze
     * @param author author name in string
     */
    public void setAuthor(String author) { this.author = author; }

    /**
     * get author name from maze
     * @return return maze author
     */
    public String getAuthor() { return author; }

    /**
     * get entrance cell location
     * @return return entrance cell location
     */
    public Cell getEntranceCell() { return entranceCell; }
    public void setEntranceCell(Cell cell) { this.entranceCell = cell;s }

    /**
     * get exit cell location
     * @return return exit cell location
     */
    public Cell getExitCell() { return exitCell; }
    public void setExitCell(Cell cell) { this.exitCell = cell; }

    /**
     *  get dead ends in solution
     * @return return solution dead ends
     */
    public int getDeadEnds() { return solution.getDeadends(); }

    /**
     * percentage of total cells solution path takes
     * @return return percentage of the maze that the solution path takes
     */
    public float getPercentReq() { return  (solutionCells.size() / (float) (height*width))*100; }

    /**
     * get creation date of maze
     * @return return creation date of the maze
     */
    public LocalDateTime getCreationDateTime() { return CreationDateTime; }

    /**
     * get last edit date of the maze
     * @return return last edit date of the maze
     */
    public LocalDateTime getLastEditedDateTime() { return LastEditedDateTime; }

    /**
     * Set the seed for maze generation
     * @param seed seed value for randomising maze
     */
    public void setRandom(long seed) { this.random = new Random(seed); }

    /**
     * sets a boolean that determines whether or not to display the solution.When you click the "Show Solution" button
     * @param val new boolean to set showSolution
     */
    public void setShowSolution(boolean val) { this.showSolution = val; }

    /**
     * gets a boolean that determines whether or not to display the solution.When you click the "Show Solution" button
     * @return return showSolution
     */
    public boolean getShowSolution() { return this.showSolution; }

    /**
     * set id of maze
     * @param id id value of maze
     */
    public void setID(int id) { this.id = id; }

    /**
     * get id value of maze
     * @return return maze id
     */
    public int getID() { return id; }

    /**
     * Function to add image to specific location in maze
     * @param row the row location of the image insertion point
     * @param col the column location of the image insertion point
     * @param height the height of the image in cells
     * @param width the width of the image in cells
     * @param file file name
     * @return image into cell
     * @throws ImageHandlerException indicates that image already exists in this area.
     * @throws IOException if an error occurs during reading or when not able to create required ImageInputStream
     */
    public ArrayList<Cell> addImage(int row, int col, int height, int width, File file) throws ImageHandlerException, IOException {
        // Make sure the image size is within the grid
        if (row + height < this.height && col + width < this.width) {
            // Make sure none of the cells have images in them
            for (int i = row; i < row + height; i++) {
                for (int j = col; j < col + width; j++) {
                    if (maze[i][j].getImage() != null) {
                        throw new ImageHandlerException("Image already exists in this area.");
                    }
                }
            }
            int cellSize = maze[0][0].getSize().getSize();
            // Convert the image to an array of buffered images
            Image image = ImageIO.read(file);
            image = image.getScaledInstance(cellSize*width,
                    cellSize*height, Image.SCALE_DEFAULT);
            BufferedImage bimage = new BufferedImage(image.getWidth(null), image.getHeight(null),
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D bigraph = bimage.createGraphics();
            bigraph.drawImage(image, 0, 0, null);
            bigraph.dispose();
            ArrayList<Cell> cInArr = new ArrayList<>();
            int ind = 0;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    BufferedImage bi = new BufferedImage(cellSize, cellSize, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D imgGraph = bi.createGraphics();
                    imgGraph.drawImage(bimage, 0, 0, cellSize, cellSize, cellSize*j, cellSize*i,
                            cellSize*(j+1), cellSize*(i+1), null);
                    imgGraph.dispose();
                    maze[row+i][col+j].setImage(bi);
                    cInArr.add(maze[row+i][col+j]);
                    ind++;
                }
            }
            ImgCells.add(cInArr);
            return cInArr;
        } else {
            throw new ImageHandlerException("Image size must be within the size of the maze.");
        }
    }

    /**
     * remove image from the maze
     * @param row the row location of the image
     * @param col the column location of the image
     * @return
     * @throws ImageHandlerException Cell does not contain an image
     */
    public ArrayList<Cell> removeImage(int row, int col) throws ImageHandlerException {
        for (int i = 0; i < ImgCells.size(); i++) {
            if (ImgCells.get(i).contains(maze[row][col])) {
                ArrayList<Cell> arrL = ImgCells.get(i);
                for (int j = 0; j < ImgCells.get(i).size(); j++) {
                    maze[arrL.get(j).getRow()][arrL.get(j).getCol()].removeImage();
                }
                ImgCells.remove(arrL);
                return arrL;
            }
        }
        throw new ImageHandlerException("Cell does not contain an image");
    }

    /**
     * Updates the maze with the given cell
     * @param cell The cell to be updated
     */
    public void updateCell(Cell cell) {
        maze[cell.getRow()][cell.getCol()] = cell;
    }

    /**
     * update solution in maze
     */
    public void updateSolution() {
        solution.findSolution(this);
        UpdateSolutionCells();
    }

    /**
     * get cell solution data from maze
     * @return solution cells data
     */
    public ArrayList<Cell> getSolutionCells() { return solutionCells; }

    /**
     * insert image into maze
     * @param path file location
     * @throws IOException  if an error occurs during writing or when not able to create required ImageOutputStream.
     * @throws MissingFieldException  if a field, namely the maze title in this case, is empty
     * @throws FileNotFoundException Signals that an attempt to open the file denoted by a specified pathname has failed
     */
    public void toImage(File path) throws IOException, MissingFieldException, FileNotFoundException {
        if (title == null) { throw new MissingFieldException("Missing maze title!"); }
        BufferedImage bi = new BufferedImage(width*maze[0][0].getSize().getSize(),
                height*maze[0][0].getSize().getSize(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        g.setBackground(Color.WHITE);
        JMazeImagePanel mp = new JMazeImagePanel(this);
        mp.setSize(width*maze[0][0].getSize().getSize(), height*maze[0][0].getSize().getSize());
        mp.paintComponent(g);
        g.dispose();
        File output = new File(path + "/" + title + ".jpg");
        ImageIO.write(bi, "jpg", output);
    }

    /**
     * A method to save the maze to the database
     */
    public void saveToDB() throws SQLException, IOException {
        // Update the Last Edited Date and Time to the current datetime
        this.LastEditedDateTime = LocalDateTime.now();
        JDBCConnection connection = new JDBCConnection();
        if (id == 0) {
            connection.InsertMaze(this);
        } else {
            connection.UpdateMaze(this, id);
        }
    }

    /**
     * override method on tostring to print cell location
     * @return A string representation of this object
     */
    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                str += String.format("Cell: %s,%s: %s\n", i, j, maze[i][j]);
            }
        }
        return str;
    }

    /**
     * find out if two maze objects are equal to each other
     * @param o maze object
     * @return boolean depending on if statements
     */
    @Override
    public boolean equals(Object o) {
        if (!o.getClass().equals(this.getClass())) {
            return false;
        }
        Maze c = (Maze) o;
        if (!this.title.equals(c.title)) { return false; }
        if (!this.author.equals(c.author)) { return false; }
        if (!this.CreationDateTime.equals(c.CreationDateTime)) { return false; }
        if (!this.LastEditedDateTime.equals(c.LastEditedDateTime)) { return false; }
        return true;
    }
}
