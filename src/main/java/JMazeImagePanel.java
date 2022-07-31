import javax.swing.*;
import java.awt.*;

/**
 * Paints a maze onto a panel to be converted into an image.
 */
public class JMazeImagePanel extends JPanel {
    private Maze maze;
    private final int BORDER_STROKE;

    public JMazeImagePanel(Maze maze) {
        this.maze = maze;
        switch (maze.getCell(0, 0).getSize()) {
            case EXTRA_LARGE:
                BORDER_STROKE = 5;
                break;
            case LARGE:
                BORDER_STROKE = 4;
                break;
            case MEDIUM:
                BORDER_STROKE = 3;
                break;
            case SMALL:
                BORDER_STROKE = 2;
                break;
            case EXTRA_SMALL:
                BORDER_STROKE = 1;
                break;
            default:
                BORDER_STROKE = 3;
                break;
        }
    }

    /**
     * function that paints the cells to display proper state of the maze
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        int cellW = maze.getCell(0, 0).getSize().getSize();
        int cellH = maze.getCell(0, 0).getSize().getSize();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, maze.getWidth()*cellW, maze.getHeight()*cellH);
        g.setColor(Color.BLACK);
        for (int row = 0; row < maze.getHeight(); row++) {
            for (int col = 0; col < maze.getWidth(); col++) {
                // Paint each cell at corresponding coordinates
                Cell cell = maze.getCell(row, col);
                int xOffset = col*cellW;
                int yOffset = row*cellH;
                if (maze.getShowSolution()) {
                    if (cell.inSolution()) {
                        g.setColor(Color.RED);
                        g.fillRect(0+xOffset, 0+yOffset, cell.getSize().getSize(), cell.getSize().getSize());
                        g.setColor(Color.BLACK);
                    }
                }
                if (cell.getImage() != null) {
                    g.drawImage(cell.getImage(), 0+xOffset, 0+yOffset, null);
                }
                if (cell.GetTop() == Cell.Cell_Sides.WALL) {
                    g.fillRect(0+xOffset, 0+yOffset, cell.getSize().getSize(), BORDER_STROKE);
                } else if (cell.GetTop() == Cell.Cell_Sides.ENTRANCE) {
                    int x1 = Math.floorDiv(cell.getSize().getSize(), 3);
                    int x2 = x1 * 2;
                    int x3 = Math.floorDiv(x1 + x2, 2);
                    int y1 = Math.floorDiv(cell.getSize().getSize(), 5);
                    int y2 = y1 * 2;
                    int[] xPoints = {x1+xOffset, x2+xOffset, x3+xOffset};
                    int[] yPoints = {y1+yOffset, y1+yOffset, y2+yOffset};
                    g.fillPolygon(xPoints, yPoints, 3);
                }
                if (cell.GetLeft() == Cell.Cell_Sides.WALL) {
                    g.fillRect(0+xOffset, 0+yOffset, BORDER_STROKE, cell.getSize().getSize());
                }
                if (cell.GetRight() == Cell.Cell_Sides.WALL) {
                    g.fillRect(cell.getSize().getSize()-BORDER_STROKE+xOffset, 0+yOffset, BORDER_STROKE, cell.getSize().getSize());
                }
                if (cell.GetBottom() == Cell.Cell_Sides.WALL) {
                    g.fillRect(0+xOffset, cell.getSize().getSize()-BORDER_STROKE+yOffset, cell.getSize().getSize(), BORDER_STROKE);
                } else if (cell.GetBottom() == Cell.Cell_Sides.EXIT) {
                    int x1 = Math.floorDiv(cell.getSize().getSize(), 3);
                    int x2 = x1 * 2;
                    int x3 = Math.floorDiv(x1 + x2, 2);
                    int y1 = Math.floorDiv(cell.getSize().getSize(), 5)*3;
                    int y2 = (y1/3)*4;
                    int[] xPoints = {x1+xOffset, x2+xOffset, x3+xOffset};
                    int[] yPoints = {y1+yOffset, y1+yOffset, y2+yOffset};
                    g.fillPolygon(xPoints, yPoints, 3);
                }
            }
        }
    }
}
