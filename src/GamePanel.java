import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 1300;
    static final int SCREEN_HEIGHT = 750;
    static final int UNIT_SIZE = 40; // for setting the size of the objects on the screen

    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE); // for how many units
    // we want on the screen

    static final int DELAY = 50; // Higher the number delay , slower the game is

    final int [] x = new int [GAME_UNITS]; // THIS ARRAY IS GOING TO HOLD ALL THE X- COORDINATES OF THE BODY PARTS
    // INCLUDING THE HEAD OF THE SNAKE
    final int [] y= new int [GAME_UNITS];// all the y coordinates of the body parts
    // and the head

    int bodyParts = 6 ; // initial body parts of the snake;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R'; // R for right, L for left, D for Down , U for Up
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true); // focus ability
        this.addKeyListener(new myKeyAdapter());
        startGame();


    }

    public void startGame(){
        newApple();
        running= true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {
        // turning into matrix , so that it's easier to locate things
        if (running) {
            /*
            // for loop, for the grid lines
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {// using this for loop to draw grid lines
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);// grid lines for x axis
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);// for y axis
            }

             */
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // drawing the body of the snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    //g.setColor(new Color(45, 180, 0));
                    // for giving different colors to the snake
                    g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            // displaying the score at the top of the screen
            g.setColor(Color.RED);
            g.setFont(new Font("Arial",Font.BOLD,40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+applesEaten,(SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2,g.getFont().getSize());
        }
        else{
            gameOver(g);
        }
    }

    public void newApple(){ // to populate the game with the apples--> generate new Coordinates
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;

    }

    public void move(){
        for (int i = bodyParts; i >0 ; i--) {
            x[i] = x[i-1]; // shifting all the coordinates in this array over by one spot
            y[i] = y[i-1];
        }
        switch (direction){
            // U,D,L,R for Up, Bottom, left and right
            case 'U':
                y[0] = y[0]-UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0]+UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0]-UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0]+UNIT_SIZE;
                break;
        }
    }

    public void checkApple(){ // because snake eats apple--> this method allows snake to eat apple
        // if x position of the head of our snake equals x position of the apple
        if((x[0] == appleX) && (y[0]==appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions(){
        // checks if head collides with body
        for (int i = bodyParts; i >0 ; i--) {
            if((x[0] == x[i]) && (y[0] == y[i])) { // here x[0], y[0] are coordinates of head of snake,
                // x[i] and y[i] are the coordinates of the body
                // if head collides with the body
                running = false;
            }
        }
        // checks if head touches left border
        if(x[0]<0){
            running = false;
        }
        // checks if head touches right border
        if(x[0]>SCREEN_WIDTH){
            running = false;
        }
        // checks if head touches top border
        if(y[0]<0){
            running = false;
        }
        // checks if head touches bottom border
        if(y[0]>SCREEN_HEIGHT){
            running = false;
        }

        if(!running) { // if running is false
            timer.stop();
        }
    }

    public void gameOver(Graphics g){
        // Displaying score after the game is over
        g.setColor(Color.RED);
        g.setFont(new Font("Arial",Font.BOLD,40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: "+applesEaten,(SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2,g.getFont().getSize());
        // Setting up Game Over text
        g.setColor(Color.RED);
        g.setFont(new Font("Arial",Font.BOLD,75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over",(SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2,SCREEN_HEIGHT/2);// Displays
        // game over displays in the center of the screen
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){ // if game is running
            move();
            checkApple();
            checkCollisions();
        }
        repaint();// if game no longer running
    }

    public class myKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            // for arrow keys
            switch (e.getKeyCode()){
                // want to limit the user to 90, avoid 180 degrees turn
                case KeyEvent.VK_LEFT :
                    if(direction!='R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT :

                    if(direction!='L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP :
                    if(direction!='D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN :
                    if(direction!='U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
