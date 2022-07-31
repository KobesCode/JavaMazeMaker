import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.*;
import javax.imageio.IIOException;
import java.io.File;
import java.util.Random;

/**
 * --MAZE TEST--
 */
public class MazeTest {

    Maze maze;

    @BeforeEach
    void createMaze() {
        maze = new Maze(10, 10);
    }

    @Test
    void TestHeight() {
        Assertions.assertEquals(10, maze.getHeight());
    }

    @Test
    void TestWidth() {
        Assertions.assertEquals(10, maze.getWidth());
    }

    @Test
    void TestEmptyEntrance() {
        maze.GenerateEmpty();
        Cell cell = maze.getCell(0, 0);
        Assertions.assertEquals(Cell.Cell_Sides.ENTRANCE, cell.GetTop());
    }

    @Test
    void TestEmptyExit() {
        maze.GenerateEmpty();
        Cell cell = maze.getCell(maze.getHeight()-1, maze.getWidth()-1);
        Assertions.assertEquals(Cell.Cell_Sides.EXIT, cell.GetBottom());
    }

    @Test
    void TestUpdateCellTop() {
        maze.GenerateEmpty();
        Random random = new Random(System.currentTimeMillis());
        int row = random.nextInt(maze.getHeight());
        int col = random.nextInt(maze.getWidth());
        Cell cell = maze.getCell(row, col);
        cell.SetTop(Cell.Cell_Sides.WALL);
        Assertions.assertEquals(Cell.Cell_Sides.WALL, cell.GetTop());
    }

        /**
     * 0 * 0 is not allowed.
     */
    @Test
    void TestInvaidMaze1() {
        try {
            maze = new Maze(0, 0);
            Assertions.assertTrue(false);
        } catch (IllegalArgumentException e1) {

        } catch (Exception e2) {
            Assertions.assertTrue(false);
        }
    }

    /**
     * Negative is not allowed.
     */
    @Test
    void TestInvaidMaze2() {
        try {
            maze = new Maze(-1, -1);
            Assertions.assertTrue(false);
        } catch (IllegalArgumentException e1) {

        } catch (Exception e2) {
            Assertions.assertTrue(false);
        }
    }
    /**
     * 1 * 1 is not allowed.
     */
    @Test
    void TestInvaidMaze3() {
        try {
            maze = new Maze(1, 1);
            Assertions.assertTrue(false);
        } catch (IllegalArgumentException e1) {

        } catch (Exception e2) {
            Assertions.assertTrue(false);
        }
    }

    // <editor-fold desc="10x10 Maze Tests">
    @BeforeEach
    void CreateMaze_1010() {
        maze = new Maze(10, 10);
    }

    @Test
    void TestHeight_1010() {
        Assertions.assertEquals(10, maze.getHeight());
    }

    @Test
    void TestWidth_1010() {
        Assertions.assertEquals(10, maze.getWidth());
    }

    @Test
    void TestEmptyEntrance_1010() {
        maze.GenerateEmpty();
        Cell cell = maze.getEntranceCell();
        Assertions.assertEquals(Cell.Cell_Sides.ENTRANCE, cell.GetTop());
    }

    @Test
    void TestEmptyExit_1010() {
        maze.GenerateEmpty();
        Cell cell = maze.getExitCell();
        Assertions.assertEquals(Cell.Cell_Sides.EXIT, cell.GetBottom());
    }

    @Test
    void TestUpdateCellTop_1010() {
        maze.GenerateEmpty();
        Random random = new Random(System.currentTimeMillis());
        int row = random.nextInt(maze.getHeight());
        int col = random.nextInt(maze.getWidth());
        Cell cell = maze.getCell(row, col);
        cell.SetTop(Cell.Cell_Sides.WALL);
        Assertions.assertEquals(Cell.Cell_Sides.WALL, maze.getCell(row, col).GetTop());
    }

    @Test
    void TestUpdateCellLeft_1010() {
        maze.GenerateEmpty();
        Random random = new Random(System.currentTimeMillis());
        int row = random.nextInt(maze.getHeight());
        int col = random.nextInt(maze.getWidth());
        Cell cell = maze.getCell(row, col);
        cell.SetLeft(Cell.Cell_Sides.WALL);
        Assertions.assertEquals(Cell.Cell_Sides.WALL, maze.getCell(row, col).GetLeft());
    }

    @Test
    void TestUpdateCellRight_1010() {
        maze.GenerateEmpty();
        Random random = new Random(System.currentTimeMillis());
        int row = random.nextInt(maze.getHeight());
        int col = random.nextInt(maze.getWidth());
        Cell cell = maze.getCell(row, col);
        cell.SetRight(Cell.Cell_Sides.WALL);
        Assertions.assertEquals(Cell.Cell_Sides.WALL, maze.getCell(row, col).GetRight());
    }

    @Test
    void TestUpdateCellBottom_1010() {
        maze.GenerateEmpty();
        Random random = new Random(System.currentTimeMillis());
        int row = random.nextInt(maze.getHeight());
        int col = random.nextInt(maze.getWidth());
        Cell cell = maze.getCell(row, col);
        cell.SetBottom(Cell.Cell_Sides.WALL);
        Assertions.assertEquals(Cell.Cell_Sides.WALL, maze.getCell(row, col).GetBottom());
    }


     /**
     * Test empty maze.
     */
    @Test
    void TestEmptyMaze() {
        maze = new Maze(20, 30);
        maze.GenerateEmpty();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 30; j++) {
                Cell cell = maze.getCell(i, j);
                if (i == 0 && j == 0) {
                    Assertions.assertEquals(cell.GetTop(), Cell.Cell_Sides.ENTRANCE);
                } else if (i == 19 && j == 29) {
                    Assertions.assertEquals(cell.GetBottom(), Cell.Cell_Sides.EXIT);
                } else if (i == 0) {
                    Assertions.assertEquals(cell.GetTop(), Cell.Cell_Sides.WALL);
                } else if (j == 0) {
                    Assertions.assertEquals(cell.GetLeft(), Cell.Cell_Sides.WALL);
                } else if (i == 19) {
                    Assertions.assertEquals(cell.GetBottom(), Cell.Cell_Sides.WALL);
                } else if (j == 29) {
                    Assertions.assertEquals(cell.GetRight(), Cell.Cell_Sides.WALL);
                }
            }
        }
    }

    /**
     * Test random maze, border not change.
     */
    @Test
    void TestGenerateMaze() {
        maze = new Maze(20, 30);
        maze.GenerateEmpty();
        maze.GenerateRandom();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 30; j++) {
                Cell cell = maze.getCell(i, j);
                if (i == 0 && j == 0) {
                    Assertions.assertEquals(cell.GetTop(), Cell.Cell_Sides.ENTRANCE);
                } else if (i == 19 && j == 29) {
                    Assertions.assertEquals(cell.GetBottom(), Cell.Cell_Sides.EXIT);
                } else if (i == 0) {
                    Assertions.assertEquals(cell.GetTop(), Cell.Cell_Sides.WALL);
                } else if (j == 0) {
                    Assertions.assertEquals(cell.GetLeft(), Cell.Cell_Sides.WALL);
                } else if (i == 19) {
                    Assertions.assertEquals(cell.GetBottom(), Cell.Cell_Sides.WALL);
                } else if (j == 29) {
                    Assertions.assertEquals(cell.GetRight(), Cell.Cell_Sides.WALL);
                }
            }
        }
    }

    @Test
    void TestInvalidGetCell_1010_1() {
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            maze.getCell(-1, 0);
        });
    }

    @Test
    void TestInvalidGetCell_1010_2() {
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            maze.getCell(0, -1);
        });
    }

    @Test
    void TestInvalidGetCell_1010_3() {
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            maze.getCell(10, 0);
        });
    }

    @Test
    void TestInvalidGetCell_1010_4() {
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            maze.getCell(0, 10);
        });
    }

    @Test
    void TestInvalidUpdateCell_1010_1() {
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            Cell cell = new Cell(Cell.Cell_Sides.WALL, Cell.Cell_Sides.WALL, Cell.Cell_Sides.WALL, Cell.Cell_Sides.WALL, null, -1, 0);
            maze.updateCell(cell);
        });
    }

    @Test
    void TestInvalidUpdateCell_1010_2() {
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            Cell cell = new Cell(Cell.Cell_Sides.WALL, Cell.Cell_Sides.WALL, Cell.Cell_Sides.WALL, Cell.Cell_Sides.WALL, null, 10, 0);
            maze.updateCell(cell);
        });
    }

    @Test
    void TestInvalidUpdateCell_1010_3() {
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            Cell cell = new Cell(Cell.Cell_Sides.WALL, Cell.Cell_Sides.WALL, Cell.Cell_Sides.WALL, Cell.Cell_Sides.WALL, null, 0, -1);
            maze.updateCell(cell);
        });
    }

    @Test
    void TestInvalidUpdateCell_1010_4() {
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            Cell cell = new Cell(Cell.Cell_Sides.WALL, Cell.Cell_Sides.WALL, Cell.Cell_Sides.WALL, Cell.Cell_Sides.WALL, null, 0, 10);
            maze.updateCell(cell);
        });
    }

    @Test
    void TestInvalidUpdateCell_1010_5() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            maze.updateCell(null);
        });
    }

    @Test
    void TestInvalidImage_1010_1() {
        Assertions.assertThrows(IIOException.class, () -> {
            maze.setTitle("10x10");
            maze.GenerateEmpty();
            maze.toImage(null);
        });
    }

    @Test
    void TestInvalidImage_1010_2() {
        Assertions.assertThrows(IIOException.class, () -> {
            maze.setTitle("10x10");
            maze.GenerateEmpty();
            maze.toImage(new File("./x"));
        });
    }

    @Test
    void TestInvalidImage_1010_3() {
        Assertions.assertThrows(MissingFieldException.class, () -> {
            maze.GenerateEmpty();
            maze.toImage(new File("./"));
        });
    }

    @Test
    void TestToString_1010() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            maze.GenerateRandom();
            Cell cell = new Cell(null, null, null, null, null, 0, 0);
            maze.updateCell(cell);
            maze.toString();
        });
    }
}
