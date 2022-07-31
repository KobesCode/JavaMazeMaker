import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * --GUI--
 */
public class Gui extends JFrame implements MouseListener, KeyListener {
    // CONSTANTS
    private final int HEIGHT = 600;
    private final int WIDTH = 800;
    private Maze maze;
    private boolean loaded = false;
    // <editor-fold desc="Start Panel Objects">
    // Panel
    private JPanel StartPanel;
    // Buttons
    private JButton CreateMazeButton;
    private JButton ViewMazesButton;
    // Labels
    private JLabel ProgramTitleLabel;
    // </editor-fold>
    // <editor-fold desc="Maze Panel Objects">
    private JPanel MazePanel;
    private JPanel CellPanel;
    private JPanel MazeContentPanel;
    private JPanel ViewMazePanel;
    // Combo Boxes
    private JComboBox TopCombo;
    private JComboBox LeftCombo;
    private JComboBox RightCombo;
    private JComboBox BottomCombo;
    // Buttons
    private JButton AddImageButton;
    private JButton RemoveImageButton;
    private  JButton BackMainMenuButton;
    // Labels
    private JLabel DeadEndsLabel;
    private JLabel PercentReqLabel;
    // Booleans
    private Boolean showSolution;
    // </editor-fold>
    // <editor-folds desc="View Maze">
    private ArrayList<Maze> selectedMazes;
    private int currPage;
    private JPanel PaginationPanel;
    private JPanel DownloadPanel;
    private JPanel SelectMazePanel;
    private JButton DownloadButton;
    private JButton LoadButton;
    private JTextArea SelectedArea;
    private enum SortBy {
        NONE(0),
        TITLEASC(1),
        TITLEDSC(2),
        AUTHORASC(3),
        AUTHORDSC(4),
        CREATIONASC(5),
        CREATIONDSC(6),
        EDITEDASC(7),
        EDITEDDSC(8);
        private final int sort;
        private SortBy(int sort) { this.sort = sort; }
        public int getSort() { return this.sort; }
    }
    private SortBy sort;
    // </editor-fold>


    /**
     * Creates a new instance of this GUI.
     * Initialise and change moves to the start screen.
     */
    public Gui() {
        // Set GUI options
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); // End the program when the GUI is closed
        this.setTitle("MazeCo Maze Generator"); // Add a title
        this.setResizable(false);
        this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        this.setLocationByPlatform(true); // Better way of setting start location
        try {   
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (UnsupportedLookAndFeelException e) { }
        this.setVisible(true); // Make the GUI visible | THIS HAPPENS LAST IN INIT
        CreateStartPanel();
    }

    /**
     * Initialises the StartPanel
     */
    private void CreateStartPanel() {
        Container contentPane = this.getContentPane();
        StartPanel = new JPanel();
        StartPanel.setLayout(new BoxLayout(StartPanel,BoxLayout.Y_AXIS));
        ProgramTitleLabel = new JLabel("MazeCo Maze Generator");
        ProgramTitleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        ProgramTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        StartPanel.add(Box.createRigidArea(new Dimension(0, 120)));
        StartPanel.add(ProgramTitleLabel);

        StartPanel.add(Box.createRigidArea(new Dimension(0, 60)));
        CreateMazeButton = new JButton("Create A Maze");
        CreateMazeButton.setPreferredSize(new Dimension(400, 50));
        CreateMazeButton.setMaximumSize(new Dimension(400, 50));
        CreateMazeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        StartPanel.add(CreateMazeButton);
        StartPanel.add(Box.createRigidArea(new Dimension(0, 60)));
        ViewMazesButton = new JButton("View Mazes");
        ViewMazesButton.setPreferredSize(new Dimension(400, 50));
        ViewMazesButton.setMaximumSize(new Dimension(400, 50));
        ViewMazesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        StartPanel.add(ViewMazesButton);
        StartPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        SpinnerNumberModel widthModel = new SpinnerNumberModel(1, 1, 100, 1);
        SpinnerNumberModel heightModel = new SpinnerNumberModel(1, 1, 100, 1);
        JSpinner widthSpinner = new JSpinner(widthModel);
        JSpinner heightSpinner = new JSpinner(heightModel);
        String[] mazeOptions = {
                "Create Empty Maze",
                "Create Random Maze"
        };
        JComboBox mazeOptionCombo = new JComboBox<>(mazeOptions);

        GridLayout MazeTypePanelLayout = new GridLayout(3,2);
        JPanel SelectMazeTypePanel = new JPanel(MazeTypePanelLayout);
        SelectMazeTypePanel.add(new JLabel("Width:"));
        SelectMazeTypePanel.add(widthSpinner);
        SelectMazeTypePanel.add(new JLabel("Height:"));
        SelectMazeTypePanel.add(heightSpinner);
        SelectMazeTypePanel.add(new JPanel());
        SelectMazeTypePanel.add(mazeOptionCombo);
        contentPane.add(StartPanel);
        pack();
        ViewMazesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object src = e.getSource();
                if (src == ViewMazesButton) {
                    //JOptionPane.showMessageDialog(ViewMazesButton, "View the Database", "Database", 2);
                    contentPane.remove(StartPanel);
                    CreateViewMazePanel();
                }
            }
        });

        CreateMazeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, SelectMazeTypePanel,
                        "New Maze Options", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        maze = new Maze((Integer) heightSpinner.getValue(), (Integer) widthSpinner.getValue());
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(),
                                ex.getMessage(), JOptionPane.ERROR_MESSAGE);
                    }
                    if (mazeOptionCombo.getSelectedIndex() == 0) {
                        maze.GenerateEmpty();
                    } else if (mazeOptionCombo.getSelectedIndex() == 1) {
                        maze.GenerateRandom();
                    } else {
                        return;
                    }
                    contentPane.remove(StartPanel);
                    CreateMazePanel();
                }
            }
        });
    }
    private void CreateViewMazePanel() {
        BackMainMenuButton = new JButton("Back to Main Menu");
        BackMainMenuButton.setBackground(Color.RED);
        sort = SortBy.NONE;
        JDBCConnection connection = new JDBCConnection();
        int totalPages = 0;
        try {
            totalPages = (int) Math.floor(connection.GetSize()/9) + 1;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error",
                    "Error retrieving from database!", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        // Initialse ArrayList to hold selected mazes
        selectedMazes = new ArrayList<>();
        Container contentPane = this.getContentPane();

        ViewMazePanel = new JPanel(new BorderLayout());

        ViewMazePanel.add(BackMainMenuButton);
        // Panels for selecting, browsing and downloading selected mazes from the DB
        SelectMazePanel = new JPanel(new GridLayout(10, 1));
        UpdateDBRowPanel(1);
        SelectMazePanel.setPreferredSize(new Dimension(WIDTH, HEIGHT/10 * 7));
        CreatePaginationPanel(totalPages);
        PaginationPanel.setBackground(Color.GRAY);
        PaginationPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT/10));
        JPanel DBViewPanel = new JPanel(new BorderLayout());
        JPanel BottomPanel = new JPanel(new BorderLayout());
        BottomPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT/10 * 2));
        BottomPanel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(2.0f), Color.BLACK));
        DownloadPanel = new JPanel();
        DownloadPanel.setPreferredSize(new Dimension(WIDTH/6 * 5, HEIGHT/10 * 2));
        JPanel DownloadButtonPanel = new JPanel(new GridLayout(3, 1));
        DownloadButtonPanel.setPreferredSize(new Dimension(WIDTH/6, HEIGHT/10 * 2));
        SelectedArea = new JTextArea(); SelectedArea.setEditable(false);
        SelectedArea.setPreferredSize(DownloadPanel.getPreferredSize());
        JScrollPane SelectScroll = new JScrollPane(SelectedArea);
        SelectScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        SelectScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        DownloadButton = new JButton("Download Mazes"); DownloadButton.setEnabled(false);
        LoadButton = new JButton("Load"); LoadButton.setEnabled(false);

        DownloadButtonPanel.add(DownloadButton);
        DownloadButtonPanel.add(LoadButton);
        DownloadButtonPanel.add(BackMainMenuButton);

        DownloadPanel.add(SelectScroll);
        BottomPanel.add(DownloadPanel, BorderLayout.WEST);
        BottomPanel.add(DownloadButtonPanel, BorderLayout.EAST);

        DBViewPanel.add(SelectMazePanel, BorderLayout.NORTH);
        DBViewPanel.add(PaginationPanel, BorderLayout.SOUTH);
        ViewMazePanel.add(DBViewPanel, BorderLayout.NORTH);
        ViewMazePanel.add(BottomPanel, BorderLayout.SOUTH);
        contentPane.add(ViewMazePanel);
        pack();

        DownloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int option = fileChooser.showOpenDialog(null);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File path = fileChooser.getSelectedFile();
                    try {
                        for (int i = 0; i < selectedMazes.size(); i++) {
                            selectedMazes.get(i).toImage(path);
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error Saving Image!",
                                "Error!", JOptionPane.ERROR_MESSAGE);
                        return;
                    } catch (MissingFieldException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(),
                                "Missing Title!", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        LoadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loaded = true;
                contentPane.remove(ViewMazePanel);
                maze = selectedMazes.get(0);
                CreateMazePanel();
            }
        });
        BackMainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object src = e.getSource();
                if (src == BackMainMenuButton) {
                    //JOptionPane.showMessageDialog(ViewMazesButton, "View the Database", "Database", 2);
                    contentPane.remove(ViewMazePanel);
                    CreateStartPanel();
                }
            }
        });
    }



    /**
     * Initialises the MazePanel
     **/
    private void CreateMazePanel() {
        Container contentPane = this.getContentPane();
        MazePanel = new JPanel(new BorderLayout());
        CellPanel = new JPanel(new GridLayout(6, 2));

        JPanel MazeScrollPanel = new JPanel();
        JPanel BottomPanel = new JPanel(new GridLayout(1, 2));
        JPanel LeftBottomPanel = new JPanel();
        JPanel MazeOptionsPanel = new JPanel(new GridLayout(5,2));

        JPanel SolutionOptionsPanel = new JPanel(new GridLayout(4,1));
        JButton SaveMazeImageButton = new JButton("Save as Image");
        JButton SaveToDBButton = new JButton("Save to Database");
        JButton FindSolutionButton = new JButton("Find Solution");
        BackMainMenuButton = new JButton("<>Back to Main Menu<>");
        JTabbedPane OptionsPane = new JTabbedPane();
        JLabel MazeTitleLabel = new JLabel("Title");
        JLabel MazeAuthorLabel = new JLabel("Author");
        DeadEndsLabel = new JLabel("Dead Ends: ");
        PercentReqLabel = new JLabel("Percent Required: ");
        JTextField MazeTitleField = new JTextField(20);
        JTextField MazeAuthorField = new JTextField(20);
        JCheckBox ShowSolutionCB = new JCheckBox("Show Solution", false);
        showSolution = false;
        MazePanel.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        MazePanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        MazeContentPanel = new JPanel(new GridLayout(maze.getHeight(), maze.getWidth()));
        MazeContentPanel.setPreferredSize(new Dimension(maze.getWidth()*maze.getCell(0, 0).getSize().getSize(),
                maze.getHeight()*maze.getCell(0, 0).getSize().getSize()));
        for (int i = 0; i < maze.getHeight(); i++) {
            for (int j = 0; j < maze.getWidth(); j++) {
                JCellDisplay cellDisplay = new JCellDisplay(maze.getCell(i, j));
                cellDisplay.addMouseListener(this);
                cellDisplay.addKeyListener(this);
                MazeContentPanel.add(cellDisplay);
            }
        }
        JScrollPane mazeScrollPane = new JScrollPane(MazeContentPanel);
        mazeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        mazeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        mazeScrollPane.getViewport().setMaximumSize(new Dimension(WIDTH, HEIGHT));
        mazeScrollPane.getViewport().setPreferredSize(new Dimension((maze.getWidth()*maze.getCell(0, 0).getSize().getSize()),
                (maze.getHeight()*maze.getCell(0, 0).getSize().getSize())));
        mazeScrollPane.setMaximumSize(new Dimension(WIDTH, (int) (HEIGHT*0.65)));
        int mspWidth, mspHeight;
        if (maze.getWidth()*maze.getCell(0, 0).getSize().getSize() < WIDTH) {
            mspWidth = maze.getWidth()*maze.getCell(0, 0).getSize().getSize();
        } else { mspWidth = WIDTH; }
        if (maze.getHeight()*maze.getCell(0, 0).getSize().getSize() < (int) (HEIGHT*0.65)) {
            mspHeight = maze.getHeight()*maze.getCell(0, 0).getSize().getSize();
        } else { mspHeight = (int) (HEIGHT*0.7); }
        mazeScrollPane.setPreferredSize(new Dimension(mspWidth, mspHeight));
        MazeScrollPanel.add(mazeScrollPane);
        MazeScrollPanel.setMaximumSize(new Dimension(WIDTH, (int) (HEIGHT*0.75)));
        MazeScrollPanel.setPreferredSize(new Dimension(WIDTH, (int) (HEIGHT*0.75)));

        OptionsPane.setTabPlacement(JTabbedPane.TOP);
        OptionsPane.addTab("Maze Options", MazeOptionsPanel);
        OptionsPane.addTab("Solution Options", SolutionOptionsPanel);

        MazeOptionsPanel.add(MazeTitleLabel);
        MazeOptionsPanel.add(MazeTitleField);
        MazeOptionsPanel.add(MazeAuthorLabel);
        MazeOptionsPanel.add(MazeAuthorField);
        // Spacing JPanels
        MazeOptionsPanel.add(new JPanel()); MazeOptionsPanel.add(new JPanel());
        MazeOptionsPanel.add(SaveMazeImageButton);
        MazeOptionsPanel.add(SaveToDBButton);
        MazeOptionsPanel.add(new JPanel());MazeOptionsPanel.add(BackMainMenuButton);
        SolutionOptionsPanel.add(ShowSolutionCB);
        SolutionOptionsPanel.add(FindSolutionButton);
        SolutionOptionsPanel.add(DeadEndsLabel);
        SolutionOptionsPanel.add(PercentReqLabel);

        LeftBottomPanel.add(OptionsPane);
        BottomPanel.add(LeftBottomPanel);
        BottomPanel.add(CellPanel);
        BottomPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT/4));

        MazePanel.add(MazeScrollPanel, BorderLayout.NORTH);
        MazePanel.add(BottomPanel, BorderLayout.SOUTH);
        contentPane.add(MazePanel);

        // If this maze was loaded from the database, disable field editing
        if (loaded) {
            MazeTitleField.setEnabled(false); MazeTitleField.setText(maze.getTitle());
            MazeAuthorField.setEnabled(false); MazeAuthorLabel.setText(maze.getAuthor());
        }
        pack();
        // Add some action and focus listeners for certain components
        MazeTitleField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                maze.setTitle(MazeTitleField.getText());
            }
        });

        MazeAuthorField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                maze.setAuthor(MazeAuthorField.getText());
            }
        });

        BackMainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object src = e.getSource();
                if (src == BackMainMenuButton) {
                    //JOptionPane.showMessageDialog(ViewMazesButton, "View the Database", "Database", 2);
                    contentPane.remove(MazePanel);
                    CreateStartPanel();
                }
            }
        });
        SaveMazeImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int option = fileChooser.showOpenDialog(null);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File path = fileChooser.getSelectedFile();
                    try {
                        maze.toImage(path);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error Saving Image!",
                                "Error!", JOptionPane.ERROR_MESSAGE);
                        return;
                    } catch (MissingFieldException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(),
                                "Missing Title!", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        });
        SaveToDBButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    maze.saveToDB();
                    JOptionPane.showMessageDialog(null, "Maze saved",
                            "Maze saved successfully!", JOptionPane.PLAIN_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error saving to database!",
                            "Error saving to database!", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
        ShowSolutionCB.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    showSolution = true;
                    maze.updateSolution();
                    ArrayList<Cell> solutionCells = maze.getSolutionCells();
                    for (int i = 0; i < solutionCells.size(); i++) {
                        UpdateMazePanel(solutionCells.get(i).getRow(), solutionCells.get(i).getCol());
                    }
                } else {
                    showSolution = false;
                    ArrayList<Cell> solutionCells = maze.getSolutionCells();
                    maze.ClearSolution();
                    for (int i = 0; i < solutionCells.size(); i++) {
                        UpdateMazePanel(solutionCells.get(i).getRow(), solutionCells.get(i).getCol());
                    }
                }
            }
        });
        FindSolutionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSolution = true;
                UpdateSolution();
                showSolution = false;
            }
        });
    }

    private void CreatePaginationPanel(int totalPages) {
        currPage = 1;
        PaginationPanel = new JPanel(new GridLayout(1, 3));
        JPanel middlePanel = new JPanel(new GridLayout(1, 3));
        JLabel PageLabel = new JLabel(String.format("%d/%d", currPage, totalPages));
        PageLabel.setFont(new Font("Verdana", Font.PLAIN, 24));
        JPanel PageLabelPanel = new JPanel(new GridBagLayout());
        PageLabelPanel.add(PageLabel);
        PaginationPanel.add(new JPanel());
        PaginationPanel.add(middlePanel);
        PaginationPanel.add(new JPanel());

        JPanel leftButtonPanel = new JPanel();
        try {
            BufferedImage LeftArrowImage = ImageIO.read(new File("src/main/resources/img/left_arrow.png"));
            JButton leftArrowButton = new JButton(new ImageIcon(LeftArrowImage));
            leftButtonPanel.add(leftArrowButton);
            middlePanel.add(leftButtonPanel);
            leftArrowButton.setContentAreaFilled(false);
            leftArrowButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (currPage > 1) {
                        currPage--;
                        PageLabel.setText(String.format("%d/%d", currPage, totalPages));
                        UpdateDBRowPanel(currPage);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        middlePanel.add(PageLabelPanel);
        JPanel rightButtonPanel = new JPanel();
        try {
            BufferedImage RightArrowImage = ImageIO.read(new File("src/main/resources/img/right_arrow.png"));
            JButton rightArrowButton = new JButton(new ImageIcon(RightArrowImage));
            rightButtonPanel.add(rightArrowButton);
            middlePanel.add(rightButtonPanel);
            rightArrowButton.setContentAreaFilled(false);
            rightArrowButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (currPage+1 <= totalPages) {
                        currPage++;
                        PageLabel.setText(String.format("%d/%d", currPage, totalPages));
                        UpdateDBRowPanel(currPage);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void UpdateDBRowPanel(int page) {
        JDBCConnection conn = new JDBCConnection();
        Maze[] mazes = new Maze[0];
        try {
            if (sort == SortBy.NONE) {
                mazes = conn.GetMazes(page);
            } else {
                mazes = conn.GetSortedMazes(page, sort.getSort());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error retrieving from database!",
                    "Error!", JOptionPane.ERROR_MESSAGE);
        }

        JPanel HeaderRowPanel = new JPanel();
        JPanel HeaderRow = new JPanel(new GridLayout(1, 6));
        JPanel TitlePanel = new JPanel(); TitlePanel.setBackground(Color.WHITE);
        JPanel AuthorPanel = new JPanel(); AuthorPanel.setBackground(Color.WHITE);
        JPanel CreationTimePanel = new JPanel(); CreationTimePanel.setBackground(Color.WHITE);
        JPanel EditedTimePanel = new JPanel(); EditedTimePanel.setBackground(Color.WHITE);
        JPanel SolutionPanel = new JPanel(); SolutionPanel.setBackground(Color.WHITE);
        JPanel SpacerPanel = new JPanel(); SpacerPanel.setBackground(Color.WHITE);
        JCheckBox allCh = new JCheckBox();
        HeaderRow.setPreferredSize(new Dimension(650, 30));
        HeaderRow.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(3.0f), Color.BLACK));
        TitlePanel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.0f), Color.BLACK));
        AuthorPanel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.0f), Color.BLACK));
        CreationTimePanel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.0f), Color.BLACK));
        EditedTimePanel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.0f), Color.BLACK));
        SolutionPanel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.0f), Color.BLACK));
        SpacerPanel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.0f), Color.BLACK));
        JButton TitleButton;
        switch (sort) {
            case TITLEASC:
                TitleButton = new JButton("Title ▲"); RemoveButtonStyle(TitleButton);
                break;
            case TITLEDSC:
                TitleButton = new JButton("Title ▼"); RemoveButtonStyle(TitleButton);
                break;
            default:
                TitleButton = new JButton("Title"); RemoveButtonStyle(TitleButton);
        }
        JButton AuthorButton;
        switch (sort) {
            case AUTHORASC:
                AuthorButton = new JButton("Author ▲"); RemoveButtonStyle(AuthorButton);
                break;
            case AUTHORDSC:
                AuthorButton = new JButton("Author ▼"); RemoveButtonStyle(AuthorButton);
                break;
            default:
                AuthorButton = new JButton("Author"); RemoveButtonStyle(AuthorButton);
        }
        JButton CreatedButton;
        switch (sort) {
            case CREATIONASC:
                CreatedButton = new JButton("Created ▲"); RemoveButtonStyle(CreatedButton);
                break;
            case CREATIONDSC:
                CreatedButton = new JButton("Created ▼"); RemoveButtonStyle(CreatedButton);
                break;
            default:
                CreatedButton = new JButton("Created"); RemoveButtonStyle(CreatedButton);
        }
        JButton EditedButton;
        switch (sort) {
            case EDITEDASC:
                EditedButton = new JButton("Last Edited ▲"); RemoveButtonStyle(EditedButton);
                break;
            case EDITEDDSC:
                EditedButton = new JButton("Last Edited ▼"); RemoveButtonStyle(EditedButton);
                break;
            default:
                EditedButton = new JButton("Last Edited"); RemoveButtonStyle(EditedButton);
        }
        TitlePanel.add(TitleButton); AuthorPanel.add(AuthorButton); CreationTimePanel.add(CreatedButton);
        EditedTimePanel.add(EditedButton); SolutionPanel.add(new Label("Solution"));
        HeaderRow.add(TitlePanel); HeaderRow.add(AuthorPanel); HeaderRow.add(CreationTimePanel);
        HeaderRow.add(EditedTimePanel); HeaderRow.add(SolutionPanel); HeaderRow.add(SpacerPanel);
        HeaderRowPanel.add(allCh); HeaderRowPanel.add(HeaderRow);
        SelectMazePanel.removeAll();
        SelectMazePanel.repaint();
        SelectMazePanel.add(HeaderRowPanel);

        for (int i = 0; i < mazes.length; i++) {
            JMazeDBRow dbRow = new JMazeDBRow(mazes[i]);
            JCheckBox dbCh = new JCheckBox();
            JPanel rowPanel = new JPanel();
            if (selectedMazes.contains(mazes[i])) {
                dbCh.setSelected(true);
                dbRow.Select();
            }
            rowPanel.add(dbCh);
            rowPanel.add(dbRow);
            dbCh.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        dbRow.Select();
                        selectedMazes.add(dbRow.getMaze());
                    }
                    else {
                        dbRow.Unselect();
                        selectedMazes.remove(dbRow.getMaze());
                    }
                    String s = "";
                    int c = 1;
                    for (int i = 0; i < selectedMazes.size(); i++) {
                        if (s.length() > 50*c) { s+= "\n"; c++; }
                        s += selectedMazes.get(i).getTitle();
                        if (i < selectedMazes.size() - 1) {s += ", "; }
                    }
                    SelectedArea.setText(s);
                    if (selectedMazes.size() == 0) {
                        DownloadButton.setEnabled(false);
                    } else {
                        DownloadButton.setEnabled(true);
                    }
                    if (selectedMazes.size() == 1) {
                        LoadButton.setEnabled(true);
                    } else {
                        LoadButton.setEnabled(false);
                    }
                }
            });
            SelectMazePanel.add(rowPanel);
        }
        pack();


        TitleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (sort) {
                    case TITLEASC:
                        sort = SortBy.TITLEDSC;
                        UpdateDBRowPanel(page);
                        break;
                    case TITLEDSC:
                        sort = SortBy.NONE;
                        UpdateDBRowPanel(page);
                        break;
                    default:
                        sort = SortBy.TITLEASC;
                        UpdateDBRowPanel(page);
                        break;
                }
            }
        });
        AuthorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (sort) {
                    case AUTHORASC:
                        sort = SortBy.AUTHORDSC;
                        UpdateDBRowPanel(page);
                        break;
                    case AUTHORDSC:
                        sort = SortBy.NONE;
                        UpdateDBRowPanel(page);
                        break;
                    default:
                        sort = SortBy.AUTHORASC;
                        UpdateDBRowPanel(page);
                        break;
                }
            }
        });
        CreatedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (sort) {
                    case CREATIONASC:
                        sort = SortBy.CREATIONDSC;
                        UpdateDBRowPanel(page);
                        break;
                    case CREATIONDSC:
                        sort = SortBy.NONE;
                        UpdateDBRowPanel(page);
                        break;
                    default:
                        sort = SortBy.CREATIONASC;
                        UpdateDBRowPanel(page);
                        break;
                }
            }
        });
        EditedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (sort) {
                    case EDITEDASC:
                        sort = SortBy.EDITEDDSC;
                        UpdateDBRowPanel(page);
                        break;
                    case EDITEDDSC:
                        sort = SortBy.NONE;
                        UpdateDBRowPanel(page);
                        break;
                    default:
                        sort = SortBy.EDITEDASC;
                        UpdateDBRowPanel(page);
                        break;
                }
            }
        });
    }

    private void RemoveButtonStyle(JButton button) {
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
    }

    /**
     * Updates the CellPanel with information from the currently focused JCellDisplay component
     * @param cellDisplay JCellDisplay object to display information from
     */
    private void UpdateCellPanel(JCellDisplay cellDisplay) {
        CellPanel.removeAll(); // Clear the current cell panel ready for new info
        Cell cell = cellDisplay.getCell();
        JLabel CellIDLabel = new JLabel(String.format("Cell: %s,%s", cell.getRow(), cell.getCol()));
        JLabel TopLabel = new JLabel("Top");
        JLabel LeftLabel = new JLabel("Left");
        JLabel RightLabel = new JLabel("Right");
        JLabel BottomLabel = new JLabel("Bottom");
        AddImageButton = new JButton( "Add Image");
        RemoveImageButton = new JButton("Remove Image");
        Cell.Cell_Sides[] ComboModel = {
                Cell.Cell_Sides.OPEN,
                Cell.Cell_Sides.WALL,
                Cell.Cell_Sides.ENTRANCE,
                Cell.Cell_Sides.EXIT
        };
        TopCombo = new JComboBox(ComboModel); TopCombo.setSelectedItem(cell.GetTop());
        LeftCombo = new JComboBox(ComboModel); LeftCombo.setSelectedItem(cell.GetLeft());
        RightCombo = new JComboBox(ComboModel); RightCombo.setSelectedItem(cell.GetRight());
        BottomCombo = new JComboBox(ComboModel); BottomCombo.setSelectedItem(cell.GetBottom());

        TopCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Cell.Cell_Sides top_side = (Cell.Cell_Sides) TopCombo.getSelectedItem();
                if (cell.GetTop() == Cell.Cell_Sides.ENTRANCE && top_side != Cell.Cell_Sides.ENTRANCE) {
                    maze.se
                }
                cell.SetTop(top_side);
                // If there exists a cell above this one
                // Update the bottom of that cell to match this change
                // If the change is to a WALL or OPEN
                Cell topCell;
                if (cell.getRow() > 0 && (top_side == Cell.Cell_Sides.WALL || top_side == Cell.Cell_Sides.OPEN)) {
                    Cell oldCell = maze.getCell(cell.getRow()-1, cell.getCol());
                    topCell = new Cell(oldCell.GetTop(), oldCell.GetLeft(), oldCell.GetRight(), top_side, cell.getSize(),
                            cell.getRow()-1, cell.getCol());
                    maze.updateCell(topCell);
                    UpdateMazePanel(topCell.getRow(), topCell.getCol());
                }
                maze.updateCell(cell);
                // Update and show the solution
                UpdateSolution();
                UpdateMazePanel(cell.getRow(), cell.getCol());
            }
        });
        LeftCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Cell.Cell_Sides left_side = (Cell.Cell_Sides) LeftCombo.getSelectedItem();
                cell.SetLeft(left_side);
                // If there exists a cell to the left of this one
                // Update the right of that cell to match this change
                // If the change is to a WALL or OPEN
                Cell leftCell;
                if (cell.getCol() > 0 && (left_side == Cell.Cell_Sides.WALL || left_side == Cell.Cell_Sides.OPEN)) {
                    Cell oldCell = maze.getCell(cell.getRow(), cell.getCol()-1);
                    leftCell = new Cell(oldCell.GetTop(), oldCell.GetLeft(), left_side, oldCell.GetBottom(), cell.getSize(),
                            cell.getRow(), cell.getCol()-1);
                    maze.updateCell(leftCell);
                    UpdateMazePanel(leftCell.getRow(), leftCell.getCol());
                }
                maze.updateCell(cell);
                // Update and show the solution
                UpdateSolution();
                UpdateMazePanel(cell.getRow(), cell.getCol());
            }
        });
        RightCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Cell.Cell_Sides right_side = (Cell.Cell_Sides) RightCombo.getSelectedItem();
                cell.SetRight(right_side);
                // If there exists a cell to the right of this one
                // Update the left of that cell to match this change
                // If the change is to a WALL or OPEN
                Cell rightCell;
                if (cell.getCol() < maze.getWidth()-1 && (right_side == Cell.Cell_Sides.WALL || right_side == Cell.Cell_Sides.OPEN)) {
                    Cell oldCell = maze.getCell(cell.getRow(), cell.getCol()+1);
                    rightCell = new Cell(oldCell.GetTop(), right_side, oldCell.GetRight(), oldCell.GetBottom(), cell.getSize(),
                            cell.getRow(), cell.getCol()+1);
                    maze.updateCell(rightCell);
                    UpdateMazePanel(rightCell.getRow(), rightCell.getCol());
                }
                maze.updateCell(cell);
                // Update and show the solution
                UpdateSolution();
                UpdateMazePanel(cell.getRow(), cell.getCol());
            }
        });
        BottomCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Cell.Cell_Sides bottom_side = (Cell.Cell_Sides) BottomCombo.getSelectedItem();
                cell.SetBottom(bottom_side);
                // If there exists a cell below this one
                // Update the top of that cell to match this change
                // If the change is to a WALL or OPEN
                Cell bottomCell;
                if (cell.getRow() < maze.getHeight()-1 && (bottom_side == Cell.Cell_Sides.WALL || bottom_side == Cell.Cell_Sides.OPEN)) {
                    Cell oldCell = maze.getCell(cell.getRow()+1, cell.getCol());
                    bottomCell = new Cell(bottom_side, oldCell.GetLeft(), oldCell.GetRight(), oldCell.GetBottom(), cell.getSize(),
                            cell.getRow()+1, cell.getCol());
                    maze.updateCell(bottomCell);
                    UpdateMazePanel(bottomCell.getRow(), bottomCell.getCol());
                }
                maze.updateCell(cell);
                // Update and show the solution
                UpdateSolution();
                UpdateMazePanel(cell.getRow(), cell.getCol());
            }
        });
        AddImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                // Filter images only
                FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("Image Files",
                        ImageIO.getReaderFileSuffixes());
                fileChooser.addChoosableFileFilter(imageFilter);
                // Remove All Files Filter
                fileChooser.removeChoosableFileFilter(fileChooser.getChoosableFileFilters()[0]);
                // Get the image
                int option = fileChooser.showOpenDialog(null);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();

                    SpinnerNumberModel widthModel = new SpinnerNumberModel(1, 1, maze.getWidth()-1, 1);
                    SpinnerNumberModel heightModel = new SpinnerNumberModel(1, 1, maze.getHeight(), 1);
                    JSpinner widthSpinner = new JSpinner(widthModel);
                    JSpinner heightSpinner = new JSpinner(heightModel);
                    JPanel ImageOptionsPanel = new JPanel(new GridLayout(2, 2));
                    ImageOptionsPanel.add(new JLabel("Width (in cells):"));
                    ImageOptionsPanel.add(widthSpinner);
                    ImageOptionsPanel.add(new JLabel("Height (in cells):"));
                    ImageOptionsPanel.add(heightSpinner);
                    int result = JOptionPane.showConfirmDialog(null, ImageOptionsPanel,
                            "New Maze Options", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        try {
                            ArrayList<Cell> cInArr = maze.addImage(cell.getRow(), cell.getCol(), (int) heightSpinner.getValue(), (int) widthSpinner.getValue(), file);
                            for (int i = 0; i < cInArr.size(); i++) {
                                UpdateMazePanel(cInArr.get(i).getRow(), cInArr.get(i).getCol());
                            }
                        } catch (ImageHandlerException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage(),
                                    ex.getMessage(), JOptionPane.ERROR_MESSAGE);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "Error reading image",
                                    "Error reading image", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
        RemoveImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ArrayList<Cell> cellList = maze.removeImage(cell.getRow(), cell.getCol());
                    for (int i = 0; i < cellList.size(); i++) {
                        UpdateMazePanel(cellList.get(i).getRow(), cellList.get(i).getCol());
                    }
                } catch (ImageHandlerException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(),
                            ex.getMessage(), JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        CellPanel.add(CellIDLabel);
        CellPanel.add(new JPanel()); // To take up empty space
        CellPanel.add(TopLabel);    CellPanel.add(TopCombo);
        CellPanel.add(LeftLabel);   CellPanel.add(LeftCombo);
        CellPanel.add(RightLabel);  CellPanel.add(RightCombo);
        CellPanel.add(BottomLabel); CellPanel.add(BottomCombo);
        if (cell.getImage() == null) {
            CellPanel.add(AddImageButton);
        } else {
            CellPanel.add(RemoveImageButton);
        }
        pack();
    }

    private void UpdateSolution() {
        ArrayList<Cell> solutionCells = maze.getSolutionCells();
        if (showSolution) {
            // Clear the current solution
            if (solutionCells == null) {
                maze.updateSolution();
                solutionCells = maze.getSolutionCells();
            }
            maze.ClearSolution();
            for (int i = 0; i < solutionCells.size(); i++) {
                UpdateMazePanel(solutionCells.get(i).getRow(), solutionCells.get(i).getCol());
            }
            // Update and show the new solution
            maze.updateSolution();
            solutionCells = maze.getSolutionCells();
            for (int i = 0; i < solutionCells.size(); i++) {
                UpdateMazePanel(solutionCells.get(i).getRow(), solutionCells.get(i).getCol());
            }
            DeadEndsLabel.setText(String.format("Dead Ends: %d", maze.getDeadEnds()));
            PercentReqLabel.setText(String.format("Percent Required: %3.2f%%", maze.getPercentReq()));
        } else {
            // Clear the solution
            if (solutionCells != null) {
                maze.ClearSolution();
                for (int i = 0; i < solutionCells.size(); i++) {
                    UpdateMazePanel(solutionCells.get(i).getRow(), solutionCells.get(i).getCol());
                }
            }
            DeadEndsLabel.setText("Dead Ends: ");
            PercentReqLabel.setText(("Percent Required: "));
        }
    }

    /**
     * Updates the MazePanel with the new maze information
     * @param row The row of the JCellDisplay component that requested the change
     * @param column The column of the JCellDisplay component that requested the change
     */
    private void UpdateMazePanel(int row, int column) {
        MazeContentPanel.remove((row*maze.getWidth())+column);
        JCellDisplay cellDisplay = new JCellDisplay(maze.getCell(row, column));
        cellDisplay.addMouseListener(this);
        cellDisplay.addKeyListener(this);
        MazeContentPanel.add(cellDisplay, (row*maze.getWidth())+column);
        UpdateCellPanel(cellDisplay);
        cellDisplay.grabFocus();
        pack();
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().getClass().equals(JCellDisplay.class)) {
            JCellDisplay cellDisplay = (JCellDisplay) e.getSource();
            UpdateCellPanel(cellDisplay);
            cellDisplay.grabFocus();
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {
    }
    @Override
    public void mouseReleased(MouseEvent e) {
    }
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        JCellDisplay cellDisplay = (JCellDisplay) e.getSource();
        switch (e.getKeyCode()) {
            // Left Key Pressed
            case 37:
                int col = cellDisplay.getCell().getCol();
                if (col > 0) { UpdateMazePanel(cellDisplay.getCell().getRow(), col-1); }
                break;
            // Up Key Pressed
            case 38:
                int row = cellDisplay.getCell().getRow();
                if (row > 0) { UpdateMazePanel(row-1, cellDisplay.getCell().getCol()); }
                break;
            // Right Key Pressed
            case 39:
                int rcol = cellDisplay.getCell().getCol();
                if (rcol < maze.getWidth()-1) { UpdateMazePanel(cellDisplay.getCell().getRow(), rcol+1); }
                break;
            // Down Key Pressed
            case 40:
                int drow = cellDisplay.getCell().getRow();
                if (drow < maze.getHeight()-1) { UpdateMazePanel(drow+1, cellDisplay.getCell().getCol()); }
                break;
            // B Key Pressed
            case 66:
                BottomCombo.grabFocus();
                break;
            // L Key Pressed
            case 76:
                LeftCombo.grabFocus();
                break;
            // R Key Pressed
            case 82:
                RightCombo.grabFocus();
                break;
            // T Key Pressed
            case 84:
                TopCombo.grabFocus();
                break;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}