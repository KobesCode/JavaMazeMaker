import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 *Class for establishing connection, creation and interaction with database(sqlite3)
 */
public class JDBCConnection {

    private static Connection connection;
    private static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS 'Mazes' (" +
                    "'MazeID' INTEGER NOT NULL UNIQUE," +
                    "'Title' TEXT NOT NULL," +
                    "'Author' TEXT NOT NULL," +
                    "'Created' TEXT NOT NULL," +
                    "'Edited' TEXT NOT NULL," +
                    "'Maze' BLOB NOT NULL," +
                    "PRIMARY KEY('MazeID' AUTOINCREMENT)" +
                    ");";

    private static final String INSERT_MAZE =
            "INSERT INTO Mazes (Title, Author, Created, Edited, Maze)" +
                    "VALUES (?, ?, ?, ?, ?);";

    private static final String SELECT_MAZES =
            "SELECT MazeID, Maze FROM Mazes LIMIT 9 OFFSET 9*?";

    private static final String SELECT_SORTED_MAZES =
            "SELECT MazeID, Maze FROM Mazes ORDER BY %s %s LIMIT 9 OFFSET 9*?";

    private static final String SELECT_COUNT =
            "SELECT COUNT(*) FROM Mazes";

    private static final String UPDATE_MAZE =
            "UPDATE Mazes SET Edited = ?, Maze = ? WHERE MazeID = ?";

    private PreparedStatement insertMaze;
    private PreparedStatement getMazes;
    private PreparedStatement getSorted;
    private PreparedStatement getCount;
    private PreparedStatement updateMaze;

    /**
     *Connect to the database sqlite 3
     */
    public JDBCConnection() {
        Properties props = new Properties();
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream("./src/main/resources/db/db.props");
            props.load(inputStream);
            inputStream.close();

            String url = props.getProperty("jdbc.url");
            String username = props.getProperty("jdbc.username");
            String password = props.getProperty("jdbc.password");
            String schema = props.getProperty("jdbc.schema");
            // Create connection
            connection = DriverManager.getConnection(url + "/" + schema, username, password);
            Statement statement = connection.createStatement();
            statement.execute(CREATE_TABLE);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * insert maze object into database which also includes author name and title
     * @param maze maze object
     * @throws SQLException
     * @throws IOException
     */
    public void InsertMaze(Maze maze) throws SQLException, IOException {
        insertMaze = connection.prepareStatement(INSERT_MAZE);
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(ba);
        try {
            objOut.writeObject(maze);
            objOut.flush();
            insertMaze.setString(1, maze.getTitle());
            insertMaze.setString(2, maze.getAuthor());
            insertMaze.setString(3, maze.getCreationDateTime().toString());
            insertMaze.setString(4, maze.getLastEditedDateTime().toString());
            insertMaze.setBytes(5, ba.toByteArray());
            insertMaze.execute();
        } finally {
            objOut.close();
            ba.close();
            insertMaze.close();
        }
    }

    /**
     * Get mazes from database
     * @param page data page index [9 rows per page]
     * @return returns Array of mazes on page
     * @throws SQLException if the columnLabel is not valid; if a database access error occurs or this method is called on a closed result set
     * @throws IOException  if an I/O error occurs while reading stream header
     * @throws ClassNotFoundException if the class of a serialized object could not be found
     */
    public Maze[] GetMazes(int page) throws SQLException, IOException, ClassNotFoundException {
        getMazes = connection.prepareStatement(SELECT_MAZES);
        ResultSet rs;
        ArrayList<Maze> mazes;
        try {
            getMazes.setInt(1, page-1);
            rs = getMazes.executeQuery();
            mazes = new ArrayList<>();
            int i = 0;
            while (rs.next()) {
                ByteArrayInputStream bais = new ByteArrayInputStream(rs.getBytes("Maze"));
                ObjectInputStream ois = new ObjectInputStream(bais);
                int id = rs.getInt("MazeID");
                mazes.add((Maze) ois.readObject());
                mazes.get(i).setID(id);
                i++;
                ois.close();
                bais.close();
            }
            return mazes.toArray(new Maze[mazes.size()]);
        } finally {
            getMazes.close();
        }
    }

    /**
     * Get mazes from database
     * @param page data page index [9 rows per page]
     * @param sort sort the maze by title, author, created or edited in Ascending or descending
     * @return returns  maze data
     * @throws SQLException  if a database access error occurs or this method is called on a closed connection
     * @throws SQLException if the columnLabel is not valid; if a database access error occurs or this method is called on a closed result set
     * @throws IOException  if an I/O error occurs while reading stream header
     * @throws ClassNotFoundException if the class of a serialized object could not be found
     */
    public Maze[] GetSortedMazes(int page, int sort) throws SQLException, IOException, ClassNotFoundException {
        String sql = SELECT_SORTED_MAZES;
        switch (sort) {
            case 1:
                sql = String.format(SELECT_SORTED_MAZES, "Title", "ASC");
                break;
            case 2:
                sql = String.format(SELECT_SORTED_MAZES, "Title", "DESC");
                break;
            case 3:
                sql = String.format(SELECT_SORTED_MAZES, "Author", "ASC");
                break;
            case 4:
                sql = String.format(SELECT_SORTED_MAZES, "Author", "DESC");
                break;
            case 5:
                sql = String.format(SELECT_SORTED_MAZES, "Created", "ASC");
                break;
            case 6:
                sql = String.format(SELECT_SORTED_MAZES, "Created", "DESC");
                break;
            case 7:
                sql = String.format(SELECT_SORTED_MAZES, "Edited", "ASC");
                break;
            case 8:
                sql = String.format(SELECT_SORTED_MAZES, "Edited", "DESC");
                break;
        }
        getSorted = connection.prepareStatement(sql);
        ResultSet rs;
        ArrayList<Maze> mazes;
        try {
            getSorted.setInt(1, page-1);
            rs = getSorted.executeQuery();
            mazes = new ArrayList<>();
            int i = 0;
            while (rs.next()) {
                ByteArrayInputStream bais = new ByteArrayInputStream(rs.getBytes("Maze"));
                ObjectInputStream ois = new ObjectInputStream(bais);
                int id = rs.getInt("MazeID");
                mazes.add((Maze) ois.readObject());
                mazes.get(i).setID(id);
                ois.close();
                bais.close();
            }
            return mazes.toArray(new Maze[mazes.size()]);
        } finally {
            getSorted.close();
        }
    }

    /**
     * Get the size of the amount of mazes stored in database
     * @return return number of mazes in database
     * @throws SQLException  if a database access error occurs or this method is called on a closed connection or the SQL statement does not return a ResultSet object
     */
    public int GetSize() throws SQLException {
        getCount = connection.prepareStatement(SELECT_COUNT);
        ResultSet rs;
        int count;
        try {
            rs = getCount.executeQuery();
            count = rs.getInt(1);
            return count;
        } finally {
            getCount.close();
        }
    }

    /**
     * Update maze
     * @param maze maze object specific maze
     * @param id id of the maze
     * @throws SQLException  if parameterIndex does not correspond to a parameter marker in the SQL statement; if a database access error occurs or this method is called on a closed PreparedStatement
     * @throws IOException if an I/O error occurs while writing stream header
     */
    public void UpdateMaze(Maze maze, int id) throws SQLException, IOException {
        updateMaze = connection.prepareStatement(UPDATE_MAZE);
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(ba);
        try {
            updateMaze.setString(1, maze.getLastEditedDateTime().toString());
            objOut.writeObject(maze);
            objOut.flush();
            updateMaze.setBytes(2, ba.toByteArray());
            updateMaze.setInt(3, maze.getID());
        } finally {
            updateMaze.close();
        }
    }

}
