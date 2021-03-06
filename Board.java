/* Jonathan Che CS-112 12/12/2014
 *
 * The purpose of the Board class is to hold variables and methods that would be useful for a general rectangular board.
 * It consists of a 2D array of Cells, which each hold a boolean array of flags. I use the Board for Minesweeper in this
 * case, and as such I only wrote class methods that would be useful for Minesweeper. One could imagine using this
 * same Board class for other games as well (e.g. Connect 4, Checkers, Go) if some more methods were written, or for
 * other functions that would require a 2D array of objects.
 * 
 */

public class Board{
  
  private Cell[][] board;
  
  /* Instances of the Cell class hold boolean arrays of flags that say whether they are bombs, open, clicked, etc. in 
   * Minesweeper. Other uses of the Board class could care about different flags.
   */
  class Cell{
    boolean[] flags;
    
    public Cell(int n){
      flags = new boolean[n];
    }
  }
  
  public Board(int rows, int cols, int flags){
    board = new Cell[rows][cols];
    reset(flags);
  }
  
  //Resets all of the flags of the Cells in the Board
  public void reset(int flags){
    for(int i = 0; i < board.length; i++){
      for(int j = 0; j < board[0].length; j++){
        board[i][j] = new Cell(flags);
      }
    }
  }
  
  //Getters
  public Cell[][] getBoard(){ return board; }
  public int rows(){ return board.length; }
  public int cols(){ return board[0].length; }
  
  public Cell cellAt(int row, int col){
    return board[row][col];
  }
  
  //Returns true if the given coordinates exist on the Board.
  public boolean isValid(int row, int col){
    return 0 <= row && row < rows() && 0 <= col && col < cols();
  }
  
  //Returns number of "param" around Cell at row, col
  public int numAround(int row, int col, int param){
    int count = 0;
    for(int i = row - 1; i < row + 2; i++){
      for(int j = col - 1; j < col + 2; j++){
        if(isValid(i, j)){
          if(board[i][j].flags[param]) count++;
        }
      }
    }
    return count;
  }
  
  //Sets "param" for all Cells around Cell at row, col to true
  public void actAround(int row, int col, int param, boolean b){
    for(int i = row - 1; i < row + 2; i++){
      for(int j = col - 1; j < col + 2; j++){
        if(isValid(i, j)){
          board[i][j].flags[param] = b;
        }
      }
    }
  }
  
  //Returns "true" if "param" is around Cell at row, col
  public boolean isAround(int row, int col, int param){
    for(int i = row - 1; i < row + 2; i++){
      for(int j = col - 1; j < col + 2; j++){
        if(isValid(i, j)){
          if(board[i][j].flags[param]){
            return true;
          }
        }
      }
    }
    return false;
  }
  
  //Returns int total number of "param" in the Board
  public int numTotal(int param){
    int count = 0;
    for(int i = 0; i < rows(); i++){
      for(int j = 0; j < cols(); j++){
        if(board[i][j].flags[param]) count++;
      }
    }
    return count;
  }
  
  //Randomly sets "param" true for a "num" of Cells in the Board
  //Method is relatively efficient so long as "num" is much less than the total number of Cells in the Board
  public void randomGen(int param, int num){
    for(int i = 0; i < num; i++){
      int col = (int)(Math.random() * cols());
      int row = (int)(Math.random() * rows());
      if(board[row][col].flags[param])
        i--;
      else
        board[row][col].flags[param] = true;
    }
  }
    
}
