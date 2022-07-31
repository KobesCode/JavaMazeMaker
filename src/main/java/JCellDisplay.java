import javax.swing.*;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * --GUI COMPONENT--
 */
public class JCellDisplay extends JPanel implements FocusListener {
    private Cell cell;
    private final int BORDER_STROKE;
    public JCellDisplay(Cell cell) {
        this.cell = cell;
        if (cell.inSolution()) {
            setBackground(Color.RED);
        } else {
            setBackground(Color.WHITE);
        }
        setFocusable(true);
        addFocusListener(this);
        switch (cell.getSize()) {
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

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(cell.getSize().getSize(), cell.getSize().getSize());
    }
    public Cell getCell() { return this.cell; }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (cell.getImage() != null) {
            g.drawImage(cell.getImage(), 0, 0, null);
        }
        if (cell.GetTop() == Cell.Cell_Sides.WALL) {
            g.fillRect(0, 0, cell.getSize().getSize(), BORDER_STROKE);
        } else if (cell.GetTop() == Cell.Cell_Sides.ENTRANCE) {
            int x1 = Math.floorDiv(cell.getSize().getSize(), 3);
            int x2 = x1 * 2;
            int x3 = Math.floorDiv(x1 + x2, 2);
            int y1 = Math.floorDiv(cell.getSize().getSize(), 5);
            int y2 = y1 * 2;
            int[] xPoints = {x1, x2, x3};
            int[] yPoints = {y1, y1, y2};
            g.fillPolygon(xPoints, yPoints, 3);
        }
        if (cell.GetLeft() == Cell.Cell_Sides.WALL) {
            g.fillRect(0, 0, BORDER_STROKE, cell.getSize().getSize());
        }
        if (cell.GetRight() == Cell.Cell_Sides.WALL) {
            g.fillRect(cell.getSize().getSize()-BORDER_STROKE, 0, BORDER_STROKE, cell.getSize().getSize());
        }
        if (cell.GetBottom() == Cell.Cell_Sides.WALL) {
            g.fillRect(0, cell.getSize().getSize()-BORDER_STROKE, cell.getSize().getSize(), BORDER_STROKE);
        } else if (cell.GetBottom() == Cell.Cell_Sides.EXIT) {
            int x1 = Math.floorDiv(cell.getSize().getSize(), 3);
            int x2 = x1 * 2;
            int x3 = Math.floorDiv(x1 + x2, 2);
            int y1 = Math.floorDiv(cell.getSize().getSize(), 5)*3;
            int y2 = (y1/3)*4;
            int[] xPoints = {x1, x2, x3};
            int[] yPoints = {y1, y1, y2};
            g.fillPolygon(xPoints, yPoints, 3);
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        this.setBorder(BorderFactory.createLineBorder(Color.BLUE));
    }

    @Override
    public void focusLost(FocusEvent e) {
        this.setBorder(null);
    }
}
