import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions;

/**
 * --CELL TEST--
 */
public class CellTests {
    @Test
    void testTop() {
        Cell cell = new Cell(Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Size.SMALL, 0, 0);
        cell.SetTop(Cell.Cell_Sides.WALL);
        Assertions.assertEquals(Cell.Cell_Sides.WALL, cell.GetTop());
    }

    @Test
    void testLeft() {
        Cell cell = new Cell(Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Size.SMALL, 0, 0);
        cell.SetLeft(Cell.Cell_Sides.WALL);
        Assertions.assertEquals(Cell.Cell_Sides.WALL, cell.GetLeft());
    }

    @Test
    void testRight() {
        Cell cell = new Cell(Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Size.SMALL, 0, 0);
        cell.SetRight(Cell.Cell_Sides.WALL);
        Assertions.assertEquals(Cell.Cell_Sides.WALL, cell.GetRight());
    }

    @Test
    void testBottom() {
        Cell cell = new Cell(Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Size.SMALL, 0, 0);
        cell.SetBottom(Cell.Cell_Sides.WALL);
        Assertions.assertEquals(Cell.Cell_Sides.WALL, cell.GetBottom());
    }

    @Test
    void testRow() {
        Cell cell = new Cell(Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Size.SMALL, 10, 0);
        Assertions.assertEquals(10, cell.getRow());
    }
    @Test
    void testColumn() {
        Cell cell = new Cell(Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Size.SMALL, 0, 10);
        Assertions.assertEquals(10, cell.getCol());
    }

    @Test
    void testSetTop() {
        Cell cell = new Cell(Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Size.SMALL, 0, 10);
        cell.SetTop(Cell.Cell_Sides.WALL);
        Assertions.assertEquals(Cell.Cell_Sides.WALL, cell.GetTop());
    }

    @Test
    void testSetLeft() {
        Cell cell = new Cell(Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Size.SMALL, 0, 10);
        cell.SetLeft(Cell.Cell_Sides.WALL);
        Assertions.assertEquals(Cell.Cell_Sides.WALL, cell.GetLeft());
    }

    @Test
    void testSetRight() {
        Cell cell = new Cell(Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Size.SMALL, 0, 10);
        cell.SetRight(Cell.Cell_Sides.WALL);
        Assertions.assertEquals(Cell.Cell_Sides.WALL, cell.GetRight());
    }

    @Test
    void testSetBottom() {
        Cell cell = new Cell(Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Sides.OPEN, Cell.Cell_Size.SMALL, 0, 10);
        cell.SetBottom(Cell.Cell_Sides.WALL);
        Assertions.assertEquals(Cell.Cell_Sides.WALL, cell.GetBottom());
    }
}
