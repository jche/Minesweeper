/* Jonathan Che CS-112 12/12/2014
 * 
 * The Display class contains the all of the listeners and JFrame/etc. setup as well as the main method for the 
 * program. When run, a ready-to-play game of Minesweeper should appear on the screen.
 * 
 */

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class Display {
  
  private Board b;
  private Minesweeper g;
  private GameComponent gComp; 
  private JLabel timerLabel;
  private JLabel flagLabel;
  private JFrame frame;
  private Timer timer;
  private int time;
  private boolean hasWon;
  
  //For ease of access/ease of change
  private int numBombs = 9;
  private int numRows = 10;
  private int numCols = 10;    
  
  //Called when reset button is pressed
  private class ResetListener implements ActionListener{
    public void actionPerformed(ActionEvent event){
      timer.stop();
      time = 0;
      timerLabel.setText("Time: " + time);
      flagLabel.setText("Flags Left: " + numBombs);
      g.reset();
      gComp.repaint();
    }
  }
  
  //Called when timer events go off (every second
  private class TimerListener implements ActionListener{
    public void actionPerformed(ActionEvent evt) {
      time++;
      timerLabel.setText("Time: " + time);
    }
  }
  
  //Called when mouse is pressed and moved (dragged)
  private class MouseMotionListener extends MouseMotionAdapter{
    public void mouseDragged(MouseEvent event){
      if(g.getIsOver()){ return; }
      if(event.getX() < 0 || event.getY() < 0){ return; }
      int col = event.getX() / GameComponent.BOX_SIZE;
      int row = event.getY() / GameComponent.BOX_SIZE;
      if(SwingUtilities.isLeftMouseButton(event)){ //only care if left mouse button is dragged
        if(b.isValid(row, col)){
          b.actAround(row, col, Minesweeper.PRESS, false); //To avoid multiple cells being pressed at a time
          if(!b.cellAt(row, col).flags[Minesweeper.FLAG]){
            b.cellAt(row, col).flags[Minesweeper.PRESS] = true;
            gComp.repaint();
          }
        }
      }
    }
  }
  
  //Listens for when the mouse is pressed and released
  private class MouseListener extends MouseAdapter{
    
    //Triggers when mouse is pressed
    public void mousePressed(MouseEvent event){
      if(g.getIsOver()){ return; }
      if(event.getX() < 0 || event.getY() < 0){ return; }
      int col = event.getX() / GameComponent.BOX_SIZE;
      int row = event.getY() / GameComponent.BOX_SIZE;
      if(b.isValid(row, col)){
        if(!b.cellAt(row, col).flags[Minesweeper.OPEN]){ //if cell is unopened
          if(event.getButton() == MouseEvent.BUTTON1){ //if cell is left-pressed
            if(!b.cellAt(row, col).flags[Minesweeper.FLAG]){ //if cell is not a flag
              b.cellAt(row, col).flags[Minesweeper.PRESS] = true;
              gComp.repaint();
            }
          }
          if(event.getButton() == MouseEvent.BUTTON3){ //if cell is right-pressed
            if(!b.cellAt(row, col).flags[Minesweeper.FLAG]){ //if cell is not flag
              b.cellAt(row, col).flags[Minesweeper.FLAG] = true;
              g.incFlags();
            }
            else{ //if cell is flag
              b.cellAt(row, col).flags[Minesweeper.FLAG] = false;
              g.decFlags();
            }
            flagLabel.setText("Flags Left: " + (numBombs - g.getFlags()));
            gComp.repaint();
          }
        }
        else{ //if cell is opened
          if(event.getButton() == MouseEvent.BUTTON1) //if cell is left-pressed
            b.cellAt(row, col).flags[Minesweeper.LPRESS] = true;
          if(event.getButton() == MouseEvent.BUTTON3) //if cell is right-pressed
            b.cellAt(row, col).flags[Minesweeper.RPRESS] = true;
        }
      }
    }
    
    //Triggers when mouse is released
    public void mouseReleased(MouseEvent event){
      if(g.getIsOver()){ return; }
      if(event.getX() < 0 || event.getY() < 0){ return; }
      int col = event.getX() / GameComponent.BOX_SIZE;
      int row = event.getY() / GameComponent.BOX_SIZE;
      if(b.isValid(row, col)){
        /* In Minesweeper, a dual press (left and right mouse button click and release) on an open cell with the same 
         * number of flags as bombs around it opens all non-flagged cells around it. It is used to quickly open
         * multiple cells known not to be bombs (hopefully) at the same time.
         */
        if(b.cellAt(row, col).flags[Minesweeper.LPRESS] && b.cellAt(row, col).flags[Minesweeper.RPRESS] &&
           b.cellAt(row, col).flags[Minesweeper.OPEN] && b.numAround(row, col, Minesweeper.FLAG) == 
           b.numAround(row, col, Minesweeper.BOMB)){ //If conditions for dual-press are met and either button is released
          for(int i = row - 1; i < row + 2; i++){
            for(int j = col - 1; j < col + 2; j++){
              if(b.isValid(i, j)){
                if(!b.cellAt(row, col).flags[Minesweeper.FLAG]){
                  if(!g.openCell(i, j)){ //open the cell regardless, but jump into loop if it's a bomb
                    hasWon = false; //for clarity
                    g.end();
                  }
                }
              }
            }
          }
          gComp.repaint();
        }
        if(event.getButton() == MouseEvent.BUTTON3){ //if right-release
          b.cellAt(row, col).flags[Minesweeper.RPRESS] = false;
        }
        if(event.getButton() == MouseEvent.BUTTON1){ //if left-release
          b.cellAt(row, col).flags[Minesweeper.LPRESS] = false;
          b.cellAt(row, col).flags[Minesweeper.PRESS] = false;
          if(!b.cellAt(row, col).flags[Minesweeper.OPEN]){ //if cell is unopened
            if(!g.getIsStarted()){ //If the game hasn't started yet
              g.openFirst(row, col);
              g.start();
              timer.start();
            }
            if(!g.openCell(row, col)){ //open the cell regardless, but jump into body if it's a bomb
              hasWon = false;
              g.end();
            }
            gComp.repaint();
          }
        }
        if(g.hasWon()){
          hasWon = true;
          g.end();
        }
        if(g.getIsOver()){
          timer.stop();
          gComp.repaint();
          if(hasWon && !b.cellAt(row, col).flags[Minesweeper.BOMB]){ //if last opened space is a bomb
            JOptionPane.showMessageDialog(gComp, "You Win!", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
          }
          else{
            JOptionPane.showMessageDialog(gComp, "BOOM!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
          }
        }
      }
    }
  }
  
  //Initializes the private data and formats the display for the game of Minesweeper
  public Display(){
    b = new Board(numRows, numCols, Minesweeper.NUM_PARAMS);
    g = new Minesweeper(b, numBombs);
    gComp = new GameComponent(g);
    timerLabel = new JLabel("Time: 0");
    flagLabel = new JLabel("Flags Left: " + numBombs);
    frame = new JFrame();
    timer = new Timer(1000 /* one second */, new TimerListener());
    
    //Constructs and adds the mouse listener to the gridComponent
    gComp.addMouseListener(new MouseListener());
    gComp.addMouseMotionListener(new MouseMotionListener());
    
    //Constructs and formats the reset button
    JButton resetButton = new JButton("New Game");
    ActionListener resetListener = new ResetListener();
    resetButton.addActionListener(resetListener);
    resetButton.setToolTipText("Start a new game");
    
    //Constructs and formats the JPanels
    JPanel labelPanel = new JPanel(new FlowLayout());
    labelPanel.add(timerLabel);
    labelPanel.add(flagLabel);
    JPanel buttonPanel = new JPanel(new FlowLayout());
    buttonPanel.add(resetButton);
    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.add(labelPanel, BorderLayout.PAGE_START);
    mainPanel.add(gComp, BorderLayout.CENTER);
    mainPanel.add(resetButton, BorderLayout.PAGE_END);
    
    //Formats the JFrame
    frame.add(mainPanel);
    frame.setTitle("Minesweeper");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    frame.setSize(GameComponent.BOX_SIZE * numCols + frame.getInsets().left + frame.getInsets().right,
                  GameComponent.BOX_SIZE * numRows + frame.getInsets().top + frame.getInsets().bottom 
                  + 52/* JPanel and JButton are 52 pixels tall combined */ );
    frame.setResizable(false);
  }
  
  public static void main(String[] args){ 
    new Display();
  }
  
}