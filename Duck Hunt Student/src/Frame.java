import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Frame extends JPanel implements ActionListener, MouseListener, KeyListener {

    // Create objects and fonts
    Font bigFont = new Font("Serif", Font.BOLD, 100);
    Font medFont = new Font("Serif", Font.BOLD, 50);
    Monkey monkey = new Monkey();
    Banana banana1 = new Banana();
    Background background = new Background();
    Ground ground = new Ground();

    // Score related vars and timer
    int roundTimer;
    int score;
    long time;
    int currRound = 1;

    // Banana state
    boolean bananaHit = false;
    boolean bananaScored = false; // Flag to track if the banana has already been scored
    boolean resetBanana = false;  // Flag to track if the banana has been reset

    public void init() {
        // Initialize the round timer and score
        roundTimer = 30;
        score = 0;
        time = 0;

        // Initialize banana object
        banana1.setWidthHeight(58, 50);
        banana1.setScale(.8, .8);
        banana1.setVx((int)(Math.random() * (4 - 2 + 1)) + 2 + currRound);
        banana1.setVy((int)(Math.random() * (4 - 2 + 1)) + 2 + currRound);

        // Initialize monkey object
        monkey.setWidthHeight(135, 125);
        monkey.setScale(0.65, 0.65);
        monkey.setVx(0);
        monkey.setXY(330, 435);

        // Ground state
        background.setScale(1.0, 1.0);
        background.setXY(-100, 500);
        
        StdAudio.loopInBackground("/audio/background.wav/");
    }

    public void nextRound() {
        // Reset round counter
        roundTimer = 30;
        currRound++;
        banana1.setXY(300, 475);
        monkey.setXY(330, 435);
        monkey.setVy(0);
        score = 0;
        bananaHit = false;
        bananaScored = false; 
        resetBanana = false;  

        // Randomize banana's initial velocity
        int randVx = (int)(Math.random() * (4)) + 1;
        banana1.setVx(randVx + currRound);
        banana1.setVy((int)(Math.random() * (4 - 2 + 1)) + 2 + currRound);
    }

    public void paint(Graphics g) {
        super.paintComponent(g);
        time += 20;

        if (time % 1000 == 0) {
            roundTimer -= 1;
            if (roundTimer == 0) {
                nextRound();
                t.stop();
            }
        }

        // Layer objects
        background.paint(g);
        monkey.paint(g);
        banana1.paint(g);
        ground.paint(g);

        // Handle banana movement only if it's not paused
        if (!bananaHit) {
            banana1.setX(banana1.getX() + banana1.getVx());
            banana1.setY(banana1.getY() + banana1.getVy());

            // Bounce off walls
            if (banana1.getX() <= 0 || banana1.getX() >= 725) {
                banana1.setVx(-banana1.getVx());
            }

            if (banana1.getY() <= 0) {
                banana1.setVy(-banana1.getVy());
            }

            // Bounce off the bottom
            if (banana1.getY() >= 445) {
                banana1.setY(445); 
                banana1.setVy(-banana1.getVy()); 
            }
        } else {
            // When banana is hit and monkey moves up
            if (banana1.getVx() == 0 && banana1.getVy() > 0 && banana1.getY() > 395) {
                monkey.setVy(-3);  
                banana1.setVy(0);   
            }

            // Monkey should go back down once it hits an imaginary ceiling
            if (monkey.getY() < 325) {
                monkey.setVy(3);  // Monkey comes back down

                // Reset banana's position and velocity once after the monkey goes up
                if (!resetBanana) {
                    resetBanana = true;

                    // random set banana position and velocity
                    banana1.setX((int)(Math.random() * (724 - 1 + 1)) + 1);
                    banana1.setY((int)(Math.random() * (445 - 375 + 1)) + 375);
                    banana1.setVx((int)(Math.random() * (4 - 2 + 1)) + 2 + currRound);
                    banana1.setVy((int)(Math.random() * (4 - 2 + 1)) + 2 + currRound);

                    // Reset flags
                    bananaHit = false;
                    resetBanana = false;  // Allow reset again for the next hit
                    bananaScored = false; // Reset the scored flag for the next hit
                }
            }
        }

        // Draw time-related strings
        g.setFont(bigFont);
        g.drawString("" + this.roundTimer, 340, 100);
        g.setFont(medFont);
        g.drawString("Round " + this.currRound, 200, 530);
        g.drawString("Score " + this.score, 400, 530);

        if (roundTimer == 30 && currRound > 1) {
            Font messageFont = new Font("Serif", Font.BOLD, 30);
            g.setFont(messageFont);
            g.drawString("Press the space bar for the next round", 160, 225);
        }
    }

    public static void main(String[] arg) {
        Frame f = new Frame();
    }

    public Frame() {
        JFrame f = new JFrame("Duck Hunt");
        f.setSize(new Dimension(800, 600));
        f.setBackground(Color.blue);
        f.add(this);
        f.setResizable(false);
        f.setLayout(new GridLayout(1, 2));
        f.addMouseListener(this);
        f.addKeyListener(this);

        init();
        t.start();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    Timer t = new Timer(16, this);

    @Override
    public void mouseClicked(MouseEvent arg0) {}

    @Override
    public void mouseEntered(MouseEvent arg0) {}

    @Override
    public void mouseExited(MouseEvent arg0) {}

    @Override
    public void mousePressed(MouseEvent mouse) {
        Rectangle rMouse = new Rectangle(mouse.getX(), mouse.getY(), 10, 10);
        Rectangle rMain = new Rectangle(banana1.getX(), banana1.getY() + 30, banana1.getWidth(), banana1.getHeight());

        if (rMouse.intersects(rMain) && !bananaScored) {
            banana1.setVy(10);
            banana1.setVx(0);
            bananaHit = true;   // set the banana to paused state
            bananaScored = true; // prevent scoring again for this banana
            monkey.setX(banana1.getX());
            monkey.setY(435);
            score++;           
            StdAudio.playInBackground("/audio/gunshot.wav");
        }
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {}

    @Override
    public void actionPerformed(ActionEvent arg0) {
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
        if (arg0.getKeyCode() == 32) {
            if (!t.isRunning()) {
                t.start();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {}

    @Override
    public void keyTyped(KeyEvent arg0) {}
}
