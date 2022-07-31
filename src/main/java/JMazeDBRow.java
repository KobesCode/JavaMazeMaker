import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;

/**
 * --GUI to interact with database--
 */
public class JMazeDBRow extends JPanel {
    private Maze maze;
    private JButton ViewMazeButton;
    private JLabel TitleLabel;
    private JLabel AuthorLabel;
    private JLabel CreationDateTimeLabel;
    private JLabel LastEditiedDateTimeLabel;
    private JCheckBox SolutionCh;
    private boolean showSolution;

    /**
     * Gui to interact with database
     * @param maze maze object
     */
    public JMazeDBRow(Maze maze) {
        this.setLayout(new GridLayout(1, 6));
        this.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(3.0f), Color.BLACK));
        this.maze = maze;
        this.setPreferredSize(new Dimension(650, 38));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");

        JPanel TitlePanel = new JPanel(); TitlePanel.setBackground(Color.WHITE);
        JPanel AuthorPanel = new JPanel(); AuthorPanel.setBackground(Color.WHITE);
        JPanel CreationTimePanel = new JPanel(); CreationTimePanel.setBackground(Color.WHITE);
        JPanel EditedTimePanel = new JPanel(); EditedTimePanel.setBackground(Color.WHITE);
        JPanel SolutionPanel = new JPanel(); SolutionPanel.setBackground(Color.WHITE);
        JPanel ViewButtonPanel = new JPanel(); ViewButtonPanel.setBackground(Color.WHITE);

        TitlePanel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.0f), Color.BLACK));
        AuthorPanel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.0f), Color.BLACK));
        CreationTimePanel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.0f), Color.BLACK));
        EditedTimePanel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.0f), Color.BLACK));
        SolutionPanel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.0f), Color.BLACK));
        ViewButtonPanel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.0f), Color.BLACK));

        TitleLabel = new JLabel(maze.getTitle());
        AuthorLabel = new JLabel(maze.getAuthor());
        CreationDateTimeLabel = new JLabel(maze.getCreationDateTime().format(formatter));
        LastEditiedDateTimeLabel = new JLabel(maze.getLastEditedDateTime().format(formatter));
        SolutionCh = new JCheckBox();
        ViewMazeButton = new JButton("View Maze");

        TitlePanel.add(TitleLabel);
        AuthorPanel.add(AuthorLabel);
        CreationTimePanel.add(CreationDateTimeLabel);
        EditedTimePanel.add(LastEditiedDateTimeLabel);
        SolutionPanel.add(SolutionCh);
        ViewButtonPanel.add(ViewMazeButton);
        this.add(TitlePanel);
        this.add(AuthorPanel);
        this.add(CreationTimePanel);
        this.add(EditedTimePanel);
        this.add(SolutionPanel);
        this.add(ViewButtonPanel);

        ViewMazeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    JFrame ViewMazeFrame = new JFrame();
                    ViewMazeFrame.setMaximumSize(new Dimension(1000, 1000));
                    JMazeImagePanel mip = new JMazeImagePanel(maze);
                    mip.setPreferredSize(new Dimension((maze.getWidth()*maze.getCell(0, 0).getSize().getSize()),
                            (maze.getHeight()*maze.getCell(0, 0).getSize().getSize())));
                    JScrollPane scrollPane = new JScrollPane(mip);
                    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                    scrollPane.getViewport().setMaximumSize(new Dimension(800, 600));
                    scrollPane.getViewport().setPreferredSize(new Dimension((maze.getWidth()*maze.getCell(0, 0).getSize().getSize()),
                            (maze.getHeight()*maze.getCell(0, 0).getSize().getSize())));
                    scrollPane.setMaximumSize(new Dimension(800, 600));
                    ViewMazeFrame.add(scrollPane);
                    ViewMazeFrame.setVisible(true);
                    ViewMazeFrame.pack();
                });
            }
        });

        SolutionCh.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    showSolution = true;
                    maze.setShowSolution(true);
                    maze.updateSolution();
                } else {
                    showSolution = false;
                    maze.setShowSolution(false);
                    maze.ClearSolution();
                }
            }
        });
    }


    public void Select() { this.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(3.0f), Color.BLUE)); }
    public void Unselect() { this.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(3.0f), Color.BLACK)); }
    public Maze getMaze() { return this.maze; }
    public boolean getSolution() { return this.showSolution; }
}
