/* Jonathan Che CS-112 12/12/2014
 * 
 * This class graphically draws the grid, bombs, and flags in a game of Minesweeper.
 * 
 */

import javax.swing.JComponent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class GameComponent extends JComponent{
  
  //For ease of access/change
  public static final int BOX_SIZE = 40;
  public static final Font FONT = new Font(Font.SERIF, Font.PLAIN, BOX_SIZE / 2);
  
  private Minesweeper game;
  
  public GameComponent(Minesweeper game){
    this.game = game;
  }
  
  //Draws the grid based on the given game
  private class Grid{
    
    public void draw(Graphics2D g2){
      Rectangle box = new Rectangle(0, 0, BOX_SIZE, BOX_SIZE);
      for(int row = 0; row < game.getBoard().rows(); row++){
        box.setLocation(0, row * BOX_SIZE);
        for(int col = 0; col < game.getBoard().cols(); col++){
          //Darken spaces that are pressed
          if(game.getBoard().cellAt(row, col).flags[Minesweeper.PRESS]){
            g2.setColor(Color.DARK_GRAY);
            g2.fill(box);
            g2.setColor(Color.BLACK);
          }
          if(game.getBoard().cellAt(row, col).flags[Minesweeper.OPEN]){
            //If bombs is opened, color the space red
            if(game.getBoard().cellAt(row, col).flags[Minesweeper.BOMB]){
              g2.setColor(Color.RED);
              Rectangle2D.Double rect = new Rectangle2D.Double(col * BOX_SIZE, row * BOX_SIZE, BOX_SIZE, BOX_SIZE);
              g2.fill(rect);
              g2.setColor(Color.BLACK);
            }
            else{
              g2.setColor(Color.LIGHT_GRAY);
              g2.fill(box);
              g2.setColor(Color.BLACK);
              int x = game.getBoard().numAround(row, col, Minesweeper.BOMB);
              //The color of the number depends on the number
              if(x != 0){
                switch(x){
                  case 0: break;
                  case 1: g2.setColor(Color.BLUE); break;
                  case 2: g2.setColor(Color.GREEN); break;
                  case 3: g2.setColor(Color.RED); break;
                  case 4: g2.setColor(Color.MAGENTA); break;
                  case 5: g2.setColor(Color.PINK); break;
                  case 6: g2.setColor(Color.CYAN); break;
                  case 7: g2.setColor(Color.BLACK); break;
                  case 8: g2.setColor(Color.GRAY); break;
                }
                g2.drawString("" + x, col * BOX_SIZE + BOX_SIZE / 3, row * BOX_SIZE + 2 * BOX_SIZE / 3);
                g2.setColor(Color.BLACK);
              }
            }
          }
          g2.draw(box);
          box.translate(BOX_SIZE, 0);
        }
      }
    }
  }
  
  //Draws the bombs based on the given game
  private class Bombs{
    
    public void draw(Graphics2D g2){
      for(int row = 0; row < game.getBoard().rows(); row++){
        for(int col = 0; col < game.getBoard().cols(); col++){
          //If flagged incorrectly, draw an x through the flag
          if(!game.getBoard().cellAt(row, col).flags[Minesweeper.BOMB] && 
             game.getBoard().cellAt(row, col).flags[Minesweeper.FLAG]){
            g2.draw(new Line2D.Double(col * BOX_SIZE, row * BOX_SIZE, col * BOX_SIZE + BOX_SIZE, 
                                      row * BOX_SIZE + BOX_SIZE));
            g2.draw(new Line2D.Double(col * BOX_SIZE + BOX_SIZE, row * BOX_SIZE, col * BOX_SIZE, 
                                      row * BOX_SIZE + BOX_SIZE));
          }
          //if the location is a bomb
          if(game.getBoard().cellAt(row, col).flags[Minesweeper.BOMB]){
            if(game.getBoard().cellAt(row, col).flags[Minesweeper.FLAG]){
              ; //do nothing
            } 
            else{ 
              Ellipse2D.Double bomb = new Ellipse2D.Double(col * BOX_SIZE + 1 * BOX_SIZE / 5, 
                                                           row * BOX_SIZE + 1 * BOX_SIZE / 5,
                                                           3 * BOX_SIZE / 5, 3 * BOX_SIZE / 5);
              g2.fill(bomb);
            }
          }
        }
      }
    }
  }
  
  //Draws the flags based on the given game
  public class Flags{
    
    //This method is called as the user creates flags.
    public void draw(Graphics2D g2){
      for(int row = 0; row < game.getBoard().rows(); row++){
        for(int col = 0; col < game.getBoard().cols(); col++){
          if(game.getBoard().cellAt(row, col).flags[Minesweeper.FLAG]){
            Rectangle2D.Double rect = new Rectangle2D.Double(col * BOX_SIZE + BOX_SIZE / 3, row * BOX_SIZE + BOX_SIZE / 6,
                                                             BOX_SIZE / 3, BOX_SIZE / 4);
            Line2D.Double line = new Line2D.Double(col * BOX_SIZE + BOX_SIZE / 3, row * BOX_SIZE + BOX_SIZE / 6,
                                                   col * BOX_SIZE + BOX_SIZE / 3, row * BOX_SIZE + 5 * BOX_SIZE / 6);
            g2.setColor(Color.RED);
            g2.fill(rect);
            g2.setColor(Color.BLACK);
            g2.draw(line);
          }
        }
      }
    }
    
    //This method is only called when the game is won. All bombs are displayed as flags (regardless of whether the
    //user actually flagged them or not.
    public void draw2(Graphics2D g2){
      for(int row = 0; row < game.getBoard().rows(); row++){
        for(int col = 0; col < game.getBoard().cols(); col++){
          if(game.getBoard().cellAt(row, col).flags[Minesweeper.BOMB]){
            Rectangle2D.Double rect = new Rectangle2D.Double(col * BOX_SIZE + BOX_SIZE / 3, row * BOX_SIZE + BOX_SIZE / 6,
                                                             BOX_SIZE / 3, BOX_SIZE / 4);
            Line2D.Double line = new Line2D.Double(col * BOX_SIZE + BOX_SIZE / 3, row * BOX_SIZE + BOX_SIZE / 6,
                                                   col * BOX_SIZE + BOX_SIZE / 3, row * BOX_SIZE + 5 * BOX_SIZE / 6);
            g2.setColor(Color.RED);
            g2.fill(rect);
            g2.setColor(Color.BLACK);
            g2.draw(line);
          }
        }
      }
    }

  }
  
  //Paints the grid, bombs, and flags
  public void paintComponent(Graphics g){
    
    Graphics2D g2 = (Graphics2D) g;
    g2.setFont(FONT);
    
    Grid grid = new Grid();
    Bombs bombs = new Bombs();
    Flags flags = new Flags();
    
    flags.draw(g2);
    grid.draw(g2);
    if(game.getIsOver()){
      if(!game.hasWon())
        bombs.draw(g2);
      else //if game has been won
        flags.draw2(g2);
    }
  }
}