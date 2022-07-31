import javax.swing.*;

public class Main {
    private static void createAndShowGui()  {
        new Gui();
    }
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            createAndShowGui();
        });

    }
}
