import javax.swing.JFrame; // A top-level container for creating a window with decorations (title bar, border, etc.)

public class flappyBird extends JFrame {
    public flappyBird() {
        add(new gameBoard());
        setTitle("Flappy Bird");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null); //placing window to center
    }

    public static void main(String[] args) {
        new flappyBird().setVisible(true);  //creating and displaying game window
    }
}
