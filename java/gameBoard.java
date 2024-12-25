import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class gameBoard extends JPanel implements ActionListener {
    int birdX = 280;
    int birdY = 300;

    int birdWidth = 50;
    int birdHeight = 50;

    int gravity = 1;
    int velocity = 0;
    int jumpStrength = -10;
    int score = 0;


    //game mechanics variables
    boolean gameOver = false;
    boolean gameStarted = false;
    ArrayList<Rectangle> pipes; //list to store the piper
    int pipeWidth = 50;
    int pipeHeight = 100;
    int pipeGap = 150;
    int pipeSpeed = 15;
    Timer timer;
    Random random;
    Image backgroundImage;
    Image birdImage;
    JLabel label1;

    gameBoard(){
        setPreferredSize(new java.awt.Dimension(800, 600));

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE){
                    jump();
                }
            }
        });
        setFocusable(true);
        initGame();
    }

    void initGame(){ //initialize game variable adn start game loop
        pipes = new ArrayList<>();
        random = new Random();
        birdImage = new ImageIcon("main/resources/birdImage1.png").getImage();
        backgroundImage = new ImageIcon("main/resources/skyBackground.jpg").getImage();

        spawnPipes();
        timer = new Timer(25, this);
        timer.start();
    }
    void jump(){
        if(!gameStarted)         //start game
            gameStarted = true;

        if (gameOver) //end game
            resetGame();

        velocity = jumpStrength; //upward velocity
    }

    void resetGame(){
        gameOver = false;
        gameStarted = false;

        birdY = 300;
        birdX = 280;

        velocity = 0;
        score = 0;
        pipes.clear();  //pipes to be cleared
        spawnPipes();   //for generating new pipes

    }
    void spawnPipes() {
        for (int i = 0; i < 60; i++) {    //add 5 pipes in beginning of game //to generate pipes endlessly
            addPipe(true);
        }

    }
    void addPipe(boolean b){
        int space = pipeGap;
        int height = random.nextInt(50, 200);

        if(b) {
            pipes.add(new Rectangle(800 + pipes.size() * 200, 600 - height, pipeWidth, height));
            pipes.add(new Rectangle(800 + pipes.size() * 200, 0, pipeWidth, 600-height-space));
        }
        else{
            pipes.add(new Rectangle(pipes.get(pipes.size()-1).x + 200, 600 - height, pipeWidth, height));
            pipes.add(new Rectangle(pipes.get(pipes.size()-1).x + 200, 0, pipeWidth, 600-height-space));
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, 800, 600, null);

        if(!gameOver){
            drawPipes(g);
            drawBird(g);
            drawScore(g);

            if (!gameStarted)
                startMessage(g);
        }
        else{
            overMessage(g);
        }
    }
    void drawPipes(Graphics g){
        g.setColor(Color.GREEN);
        for(int i =0; i<pipes.size(); i++){
            Rectangle pipe = pipes.get(i);
            g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
        }
    }
    void drawBird(Graphics g){
       // add(label1);
        g.drawImage(birdImage, birdX, birdY, birdWidth, birdHeight, null);
        // g.setColor(Color.YELLOW);
        //g.fillRect(birdX, birdY, birdWidth, birdHeight);
    }

    void drawScore(Graphics g){ //
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.TRUETYPE_FONT, 15));
        g.drawString("Score: " + score,100, 100);

    }

    void startMessage(Graphics g){
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Press space to continue.", 200, 300);
    }
    void overMessage(Graphics g){
        g.setColor(Color.BLUE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Score: " + score, 340, 210);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Game Over. Press space to restart.", 180, 320);
    }

    void updateBird() {
        if (gameStarted) {
            velocity += gravity;
            birdY += velocity;

            if (birdY > getHeight() - birdHeight) {
                birdY = getHeight() - birdHeight;
                gameOver = true;
            }

            if (birdY < 0)
                birdY = 0;
        }
    }
    void updatePipe(){
        for(int i = 0; i<pipes.size(); i++){
            Rectangle pipe = pipes.get(i);

            pipe.x = pipe.x - pipeSpeed;
            if(pipe.x + pipe.width < 0 && !gameOver){
                pipes.remove(pipe);
                score ++;
            }


        }
    }
    void collision(){
        for(int i = 0; i<pipes.size(); i++) {
            Rectangle pipe = pipes.get(i);

            if(pipe.intersects(new Rectangle(birdX, birdY, birdWidth, birdHeight))){
                gameOver= true;
            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(gameStarted){
            updateBird();
            updatePipe();
            collision();
        }
        repaint();
    }
}

