import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Creates and stores information on a new cell object
 */
public class Cell implements Serializable {
    private static final long serialVersionUID = 2L;
    /**
     * Contains information about the sides of the cell
     */
    private Cell_Sides top, left, right, bottom;
    private int row, col;
    private boolean solution;
    private Cell_Size size;
    private transient BufferedImage image;

    /**
     * Cell side states that can be used
     */
    public enum Cell_Sides {
            WALL,
            OPEN,
            ENTRANCE,
            EXIT
    }

    /**
     *  Set the cell size
     */
    public enum Cell_Size {
        EXTRA_SMALL(16),
        SMALL(24),
        MEDIUM(32),
        LARGE(48),
        EXTRA_LARGE(64);
        private final int size;
        private Cell_Size(int size) {
            this.size = size;
        }
        public int getSize() {
            return this.size;
        }
    }

    /**
     * Creates a new cell object
     * @param top The state of the top side of the cell
     * @param left The state of the left side of the cell
     * @param right The state of the right side of the cell
     * @param bottom The state of the bottom side of the cell
     * @param size The dimensions of the cell in pixels
     * @param row The row this cell is in
     * @param col The column this cell is in
     */
    public Cell (Cell_Sides top, Cell_Sides left, Cell_Sides right, Cell_Sides bottom, Cell_Size size, int row, int col) {
        this.top = top;
        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.row = row;
        this.col = col;
        this.solution = false;
        this.size = size;
    }

    /**
     * state of top side of cell
     * @return The state of the top side of the cell
     */
    public Cell_Sides GetTop() { return top; }

    /**
     * set top side of cell
     * @param side The new state of the top side of the cell
     */
    public void SetTop(Cell_Sides side) { this.top = side; }

    /**
     *state of left side of cell
     * @return The state of the left side of the cell
     */
    public Cell_Sides GetLeft() { return left; }

    /**
     * set left siode of cell
     * @param side The new state of the left side of the cell
     */
    public void  SetLeft(Cell_Sides side) { this.left = side; }

    /**
     * state of right side of cell
     * @return The state of the right side of the cell
     */
    public Cell_Sides GetRight() { return right; }

    /**
     * set right side of cell
     * @param side The new state of the right side of the cell
     */
    public void SetRight(Cell_Sides side) { this.right = side; }

    /**
     * state of bottom of cell
     * @return The state of the bottom side of the cell
     */
    public Cell_Sides GetBottom() { return bottom; }

    /**
     * set bottom side of cell
     * @param side The new state of the bottom side of the cell
     */
    public void SetBottom(Cell_Sides side) { this.bottom = side; }

    /**
     * find row of the cell location
     * @return The row of the maze this cell is in
     */
    public int getRow() { return row; }

    /**
     * find column of the cell location
     * @return The column of the maze this cell is in
     */
    public int getCol() { return col; }

    /**
     * get property solution
     * @return return property solution
     */
    public boolean inSolution() { return solution; }

    /**
     * set the property "solution" to be true or false if the cell is in the solution
     * @param solution solution property
     */
    public void setSolution(boolean solution) { this.solution = solution; }

    /**
     * returns cell size
     * @return returns cell size
     */
    public Cell_Size getSize() { return size; }

    /**
     * return image
     * @return returns iamge
     */
    public BufferedImage getImage() { return image; }

    /**
     * set image with new  buffered image
     * @param image image object
     */
    public void setImage(BufferedImage image) { this.image = image; }

    /**
     * remove image
     */
    public void removeImage() { image = null; }

    /**
     * A string representation of the object
     * @return A string representation of the object
     */
    @Override
    public String toString() {
        return  "Top: " + top.toString() + "; Left: " + left.toString() + "; Right: " + right.toString() + "; Bottom: "
                + bottom.toString();
    }

    /**
     *Write the non-static and non-transient fields of the current class to this stream
     *
     * @param out  a File to be written to
     * @serialData
     * @throws IOException if I/O errors occur while writing to the underlying OutputStream
     */
    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        if (image != null) {
            ImageIO.write(image, "png", out);
        }
    }

    /**
     *Read the non-static and non-transient fields of the current class from this stream
     *
     *
     * @param in a File to read from
     * @throws IOException if an error occurs during reading or when not able to create required ImageInputStream.
     * @throws ClassNotFoundException if the class of a serialized object could not be found
     */
    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.image = ImageIO.read(in);
    }
}
