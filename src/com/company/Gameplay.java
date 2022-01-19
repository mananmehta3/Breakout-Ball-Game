package com.company;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Gameplay extends JPanel implements ActionListener, KeyListener {

    private boolean play = false;
    private int score = 0;

    private int totalBricks = 21;

    private Timer timer;

    private int playerX = 310;

    private int ballPosX = 120;
    private int ballPosY = 350;
    private int ballXDir = -1;
    private int ballYDir = -2;

    private MapGenerator mapG;

    public Gameplay() {
        mapG = new MapGenerator(3, 7);
        super.addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(0, this);
        timer.start();
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLACK); // background
        g.fillRect(1, 1, 692, 592);

        mapG.draw((Graphics2D) g); // bricks

        g.setColor(Color.YELLOW); //  borders for all sides except bottom
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        g.setColor(Color.WHITE); // score
        g.setFont(new Font("Times New Roman", Font.BOLD, 25));
        g.drawString("" + score, 590, 30);

        g.setColor(Color.GREEN); // paddle
        g.fillRect(playerX, 550, 100, 8);

        g.setColor(Color.YELLOW); // ball
        g.fillOval(ballPosX, ballPosY, 20, 20);

        if (totalBricks <= 0) {
            play = false;
            ballXDir = 0;
            ballYDir = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("Algerian", Font.ITALIC, 50));
            g.drawString("YOU WON\n Score= " + score, 260, 350);

            g.setFont(new Font("Algerian", Font.BOLD, 30));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        // Game Over
        if (ballPosY > 570) {
            play = false;
            ballXDir = 0;
            ballYDir = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("Algerian", Font.ITALIC, 40));
            g.drawString("Game Over\n Score= " + score, 190, 300);

            g.setFont(new Font("Algerian", Font.BOLD, 30));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play) {
            if (new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYDir *= -1;
            }
            O:
            for (int i = 0; i < mapG.map.length; i++) {
                for (int j = 0; j < mapG.map[0].length; j++) {
                    if (mapG.map[i][j] == 1) {
                        int brickX = j * mapG.brickWidth + 80;
                        int brickY = i * mapG.brickHeight + 50;
                        int brickW = mapG.brickWidth;
                        int brickH = mapG.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickW, brickH);
                        Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);

                        if (ballRect.intersects(rect)) {
                            mapG.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if (ballPosX + 19 <= rect.x || ballPosX + 1 >= rect.x + rect.width) {
                                ballXDir *= -1;
                            } else {
                                ballYDir *= -1;
                            }
                            break O;
                        }
                    }
                }
            }
            ballPosX += ballXDir;
            ballPosY += ballYDir;
            if (ballPosX < 0) ballXDir *= -1;
            if (ballPosY < 0) ballYDir *= -1;
            if (ballPosX > 670) ballXDir *= -1;
        }
        repaint();
    }

    @Override // not needed
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!play && e.getKeyCode() == KeyEvent.VK_ENTER) {
            play = true;
            ballPosX = 120;
            ballPosY = 350;
            ballXDir = -1;
            ballYDir = -2;
            playerX = 310;
            score = 0;
            totalBricks = 21;
            mapG = new MapGenerator(3, 7);

            repaint();
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }
    }

    private void moveRight() {
        play = true;
        playerX += 20;
    }

    private void moveLeft() {
        play = true;
        playerX -= 20;
    }

    @Override // not needed
    public void keyReleased(KeyEvent e) {
    }
}
