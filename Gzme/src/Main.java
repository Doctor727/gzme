import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Main extends JPanel {

    final int BF_WIDTH = 576;
    final int BF_HEIGHT = 576;

    final int QUADRANT_SIZE = 64;

    final int UP = 1;
    final int DOWN = 2;
    final int LEFT = 3;
    final int RIGHT = 4;

    final int TOP_Y = BF_HEIGHT - QUADRANT_SIZE;
    final int TOP_X = BF_WIDTH - QUADRANT_SIZE;


    String[][] objects = {
            // B-wall, G-field, W-gold
            {"W", "G", "G", "G", "G", "G", "G", "B", "W"},
            {"B", "B", "G", "B", "B", "B", "G", "B", "B"},
            {"B", "B", "G", "G", "G", "G", "G", "W", "W"},
            {"G", "G", "B", "B", "G", "B", "B", "G", "G"},
            {"B", "G", "B", "G", "G", "G", "W", "G", "B"},
            {"G", "W", "B", "B", "G", "B", "B", "G", "G"},
            {"B", "G", "B", "G", "B", "G", "B", "G", "B"},
            {"B", "W", "G", "B", "G", "B", "G", "B", "B"},
            {"B", "G", "G", "G", "B", "G", "G", "W", "B"},
    };

    // 1 - Up, 2 - Down, 3 - Left, 4 - Right
    int direction = 3;

    int bulletX = -100;
    int bulletY = -100;

    int playerX = 256;
    int playerY = 256;

    void move(int direction) throws Exception {
        this.direction = direction;

        if (dontCanMove()) {
            System.out.println("Can't move!");
            shoot();
            return;
        }

        for (int i = 0; i < QUADRANT_SIZE; i++) {

            if (direction == 1) {
                playerY--;
            } else if (direction == 2) {
                playerY++;
            } else if (direction == 3) {
                playerX--;
            } else if (direction == 4) {
                playerX++;
            }
            Thread.sleep(8);
            repaint();
        }

    }

    void moveToQuadrant(int y, int x) {

    }

    void moveRandom() throws Exception {
        Random random = new Random();
        int direction = random.nextInt(4) + 1;
        move(direction);
    }


    boolean dontCanMove() {
        return (direction == UP && playerY == 0) || (direction == DOWN && playerY == TOP_Y)
                || (direction == LEFT && playerX == 0) || (direction == RIGHT && playerX == TOP_X)
                || nextObject(direction).equals("W");

    }

    String nextObject(int direction) {
        int y = playerY;
        int x = playerX;

        switch (direction) {
            case UP:
                y -= 64;
                break;
            case DOWN:
                y += 64;
                break;
            case LEFT:
                x -= 64;
                break;
            case RIGHT:
                x += 64;
                break;
        }

        return objects[y / QUADRANT_SIZE][x / QUADRANT_SIZE];
    }

    boolean processInterception() {
        int y = bulletY / 64;
        int x = bulletX / 64;

        if (objects[y][x].equals("B") && y >= 0 && y <= 8 && x >= 0 && x <= 8) {
            objects[y][x] = "G";
            return true;
        }

        return false;
    }

    void shoot() throws Exception {
        bulletX = playerX + 25;
        bulletY = playerY + 25;

        while (bulletX > 0 && bulletX < BF_WIDTH && bulletY > 0 && bulletY < BF_HEIGHT) {

            switch (direction) {
                case 1:
                    bulletY -= 1;
                    break;
                case 2:
                    bulletY += 1;
                    break;
                case 3:
                    --bulletX;
                    break;
                case 4:
                    bulletX++;
                    break;
            }

            if (processInterception()) {
                destoyBullet();
            }

//            Thread.sleep(0);
            repaint();
        }

        destoyBullet();
    }

    void destoyBullet() {
        bulletX = -100;
        bulletY = -100;
        repaint();
    }

    void runTheGame() throws Exception {

        while (true) {
            shoot();
        }
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.runTheGame();
    }


    Main() {
        JFrame frame = new JFrame("Destroy Yellow");
        frame.setMinimumSize(new Dimension(BF_WIDTH, BF_HEIGHT + 35));
        frame.getContentPane().add(this);
        frame.setLocation(0, 0);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);


        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                switch (objects[y][x]) {
                    case "B":
                        g.setColor(new Color(255, 199, 0));
                        break;
                    case "W":
                        g.setColor(new Color(22, 78, 1));
                        break;
                    case "G":
                        g.setColor(new Color(235, 235, 235));
                        break;
                }
                g.fillRect(x * QUADRANT_SIZE, y * QUADRANT_SIZE, QUADRANT_SIZE, QUADRANT_SIZE);
            }
        }


        //draw player
        g.setColor(new Color(1, 9, 57));
        g.fillRect(playerX, playerY, 64, 64);
        g.setColor(new Color(59, 229, 233));

        if (direction == 1) {
            g.fillRect(playerX + 20, playerY, 24, 34);
        } else if (direction == 2) {
            g.fillRect(playerX + 20, playerY + 30, 24, 34);
        } else if (direction == 3) {
            g.fillRect(playerX, playerY + 20, 34, 24);
        } else if (direction == 4) {
            g.fillRect(playerX + 30, playerY + 20, 34, 24);
        }
        g.setColor(new Color(255, 255, 255));
        g.fillRect(bulletX, bulletY, 14, 14);

    }
}