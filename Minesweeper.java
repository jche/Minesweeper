/* Jonathan Che CS-112 12/12/2014
 * 
 * The Minesweeper class holds all of the variables and methods specifically required for a game of Minesweeper. It
 * keeps all of the Minesweeper-related material in one class for easy access.
 * 
 * */

public class Minesweeper{
  
  private Board b;
  private int bombs;
  private int flags;
  private boolean isStarted;
  private boolean isOver;
  
  //Sets up a game of Minesweeper
  public Minesweeper(Board b, int bombs){
    this.b = b;
    this.bombs = bombs;
    flags = 0;
    isStarted = false;
    isOver = false;
    b.randomGen(Minesweeper.BOMB, bombs);
  }
  
  //Getters
  public Board getBoard(){ return b; }
  public int getBombs(){ return bombs; }
  public int getFlags(){ return flags; }
  public boolean getIsStarted(){ return isStarted; }
  public boolean getIsOver(){ return isOver; }
  
  //Mutators
  public void incFlags(){ flags += 1; }
  public void decFlags(){ flags -= 1; }
  public void start(){ isStarted = true; }
  public void end(){ isOver = true; }
  
  //Minesweeper is won when all non-bomb spaces are opened
  public boolean hasWon(){
    return bombs == b.rows() * b.cols() - b.numTotal(Minesweeper.OPEN);
  }
  
  public void reset(){
    this.b.reset(Minesweeper.NUM_PARAMS);
    flags = 0;
    isStarted = false;
    isOver = false;
    b.randomGen(Minesweeper.BOMB, bombs);
  }
  
  //First space opened can never be a bomb
  //Relatively efficient so long as the number of bombs is much less than the total number of Cells in the Board
  public void openFirst(int row, int col){
    while(b.cellAt(row, col).flags[Minesweeper.BOMB]){
      reset();
    }
    openCell(row, col);
  }
  
  //In Minesweeper, Cells with 0 bombs around them are automatically opened if they are adjacent to an opened Cell.
  //Returns false if a bomb is opened
  //Precondition: Cell location is valid
  public boolean openCell(int row, int col){
    if(!b.cellAt(row, col).flags[Minesweeper.FLAG]){
      if(b.cellAt(row, col).flags[Minesweeper.BOMB]){
        b.cellAt(row, col).flags[Minesweeper.OPEN] = true;
        return false;
      }
      if(b.cellAt(row, col).flags[Minesweeper.OPEN]){ return true; }
      b.cellAt(row, col).flags[Minesweeper.OPEN] = true;
      if(b.numAround(row, col, Minesweeper.BOMB) == 0){
        for(int i = row - 1; i < row + 2; i++){
          for(int j = col - 1; j < col + 2; j++){
            if(b.isValid(i, j)){
              openCell(i, j);
            }
          }
        }
      }
    }
    return true;
  }
  
  //A Cell used in a game of Minesweeper holds an array of six (NUM_PARAMS) booleans in the order:
  //[isbomb, isFlag, isOpen, isPressed, isLeftPressed, isRightPressed].
  public static final int NUM_PARAMS = 6;
  public static final int BOMB = 0;
  public static final int FLAG = 1;
  public static final int OPEN = 2;
  public static final int PRESS = 3;
  public static final int LPRESS = 4;
  public static final int RPRESS = 5;
  
}
