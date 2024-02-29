import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Graphics2D;



import static java.awt.geom.Path2D.intersects;

public class Gameplay extends JPanel implements KeyListener, ActionListener {

private boolean play = false;
private int score = 0;
private int totalBricks = 21;
private Timer timer;
private int delay = 8;
private  int playerX = 310;
private int ballposX = 120;
private int ballposY = 350;
private int ballXdir = -3;
private int ballYdir = -4;

private MapGenerator map;


public Gameplay()
{
    map = new MapGenerator(3,7);

    addKeyListener(this);
    setFocusable(true);
    setFocusTraversalKeysEnabled(false);
    timer = new Timer(delay, this);
    timer.start();


}
public void paint(Graphics g)
{
    //background
    g.setColor(Color.black);
    g.fillRect(1,1,692,592);

    //drawing map

    map.draw((Graphics2D) g);

    // add score
     g.setColor(Color.white);
     g.setFont( new Font("serif", Font.BOLD, 25));
     g.drawString( "Score : " + score, 490, 30);

    //borders
    g.setColor(Color.yellow);;
    g.fillRect(0,0,3,592); // left border
    g.fillRect(0,0,692,3); // top border
    g.fillRect(683,0,3,592); // right border

    //the paddle
    g.setColor(Color.green);
    g.fillRect(playerX,550,100,8);

    //ball
    g.setColor(Color.red);
    g.fillOval(ballposX, ballposY,20,20);


    // if the game is won
    if ( totalBricks <=0)
    {
        play = false;
        ballXdir = 0;
        ballYdir = 0;
        g.setColor(Color.red);;
        g.setFont( new Font("serif", Font.BOLD, 30));
        g.drawString( " YOU WON :).  Your Score : " + score, 260, 300);

        g.setFont( new Font("serif", Font.BOLD, 30));
        g.drawString( " Press enter to restart " , 230, 350);
    }

    // if the game is lost
    if(ballposY > 570)
    {
        play = false;
        ballXdir = 0;
        ballYdir = 0;
        g.setColor(Color.red);;
        g.setFont( new Font("serif", Font.BOLD, 30));
        g.drawString( " GAME OVER . Your Score : " + score, 190, 300);

        g.setFont( new Font("serif", Font.BOLD, 30));
        g.drawString( " Press enter to restart " , 230, 350);


    }

    g.dispose(); // clear the frame

}



    @Override
    public void actionPerformed(ActionEvent e) {
    timer.start();
    if (play)
    {
        // check for intersection with the paddle
      if(new Rectangle(ballposX,ballposY,20,20).intersects(new Rectangle(playerX,550,100,8)))
      {
          ballYdir = -ballYdir; //change direction
      }

       A: for (int i = 0; i<map.map.length;i++) {
         for (int j = 0; j < map.map[0].length; j++) {
            if(map.map[i][j] > 0)
            {
                int brickX = j*map.brickWidth + 80;
                int brickY = i*map.brickHeight + 50;
                int brickWidth = map.brickWidth;
                int brickHeight = map.brickHeight;

                Rectangle rect = new Rectangle(brickX,brickY,brickWidth,brickHeight);
                Rectangle ballRect = new Rectangle(ballposX,ballposY,20,20);
                Rectangle brickRect = rect;

                if(ballRect.intersects(brickRect))
                {
                    map.setBrickValue(0,i,j); // make the brick disappear ( change it s value to 0)
                    totalBricks --;
                    score += 5;

                    // for intersection with the tiles => it changes direction
                    if(ballposX + 19 <= brickRect.x || ballposX+1 >= brickRect.x + brickRect.width)
                    {
                        ballXdir = -ballXdir;
                    }
                    else
                    {
                        ballYdir = -ballYdir;
                    }

                    break A;
                }
            }
         }
     }


        ballposX += ballXdir;
        ballposY += ballYdir;

        if (ballposX < 0) // the ball is hitting the left frame
        {
            ballXdir = -ballXdir; // change direction
        }
        if (ballposY < 0) // the ball is hitting the top
        {
            ballYdir = -ballYdir; // change direction
        }

        if (ballposX > 670) // the ball is hitting the right frame
        {
            ballXdir = -ballXdir;
        }
    }

    repaint(); // call the function so that the paddle is redrawn when it's moving to the left or to the right
    }

    // for moving the ball with the keys => keyListeners
    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
    //for moving the paddle to the right
    if (e.getKeyCode()== KeyEvent.VK_RIGHT)
    {
        // check if it doesn't go outside the panel
        if( playerX >=600)
        {
            playerX = 600;
        }
        else {
            moveRight();
        }
    }
    // for moving the paddle to the left
      if (e.getKeyCode()== KeyEvent.VK_LEFT)
      {
          // check if it doesn't go outside the panel
          if( playerX < 10)
          {
              playerX = 10;
          }
          else {
              moveLeft();
          }
      }


      // pressing the Enter key
        if(e.getKeyCode() == KeyEvent.VK_ENTER)
        { if(!play)
            play = true;
            ballposX = 120;
            ballposY = 350;
            ballXdir = -1;
            ballYdir = -2;
            playerX = 310;
            score = 0;
            totalBricks = 21;
            map = new MapGenerator(3,7);

            repaint();

        }
    }
     public void moveRight()
     {
         play = true;
         playerX += 20; // move 20px to the right side
     }

        public void moveLeft()
        {
            play = true;
            playerX -= 20; // move 20px to the left side
        }


    public void keyReleased(KeyEvent e) {}
}
