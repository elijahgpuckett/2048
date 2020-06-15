package com.codegym.games.game2048;
import com.codegym.engine.cell.*;
import java.util.Arrays;

public class Game2048 extends Game {
    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped = false;
    private int score = 0;
    
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();
    }
    
    private void createGame() {
        gameField = new int[SIDE][SIDE];
        createNewNumber();
        createNewNumber();
    }
    
    
    private void win(){
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "Winner!", Color.BLUE, 100);
    }
    
    private void gameOver() {
    isGameStopped = true;
    showMessageDialog(Color.BLACK, "Loser!", Color.BLUE, 20);
  }
    
    private void drawScene() {
        for (int x = 0; x < SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {
                setCellColoredNumber(x, y, gameField[y][x]);
            }
        }
    }
    
     private int getMaxTileValue(){
        int max = 0;
        for(int i = 0; i < SIDE ; i++){
            int[] row = gameField[i];
            for(int j = 0; j< SIDE ; j++){
                if( max <= row[j] ){
                    max = row[j];
                }
            }
        }
        return max; 
    }
    
    private void createNewNumber() {
        int cellValue = 1;
        int x = 0;
        int y = 0;
        
       
       if (getMaxTileValue() == 2048){
           win();
       }
        
        while (cellValue != 0) {
            x = getRandomNumber(SIDE);
            y = getRandomNumber(SIDE); 
            if (gameField[x][y] == 0) {
                cellValue = 0;
            }
        }   
        int newRandomNum = getRandomNumber(10);
            if (newRandomNum == 9) {
                gameField[x][y] = 4;
            } else {
                gameField[x][y] = 2;
            }
    }
    
    private Color getColorByValue(int value) {
        Color color = null;
        switch (value) {
            case 0:
                color = Color.WHITE;
                break;
            case 2:
                color = Color.BLUE;
                break;
            case 4:
                color = Color.RED;
                break;
            case 8:
                color = Color.CYAN;
                break;
            case 16:
                color = Color.GREEN;
                break;
            case 32:
                color = Color.YELLOW;
                break;
            case 64:
                color = Color.ORANGE;
                break;
            case 128:
                color = Color.PINK;
                break;
            case 256:
                color = Color.MAGENTA;
                break;
            case 512:
                color = Color.BLACK;
                break;
            case 1024:
                color = Color.PURPLE;
                break;
            case 2048:
                color = Color.GRAY;
                break;
        }    
            return color;    
    }
    
    private void setCellColoredNumber(int x, int y, int value) {
        Color color = getColorByValue(value);
        if (value > 0) {
            setCellValueEx(x, y, color, Integer.toString(value));
        } else {
            setCellValueEx(x, y, color, "");
        }
    }
    
    private boolean compressRow(int[] row) {
        int temp = 0;
        int[] rowtemp = row.clone();
        boolean isChanged = false;
        for(int i = 0; i < row.length; i++) {
            for(int j = 0; j < row.length-i-1; j++){
                if(row[j] == 0) {
                    temp = row[j];
                    row[j] = row[j+1];
                    row[j+1] = temp;
                }
            }
        }
        if(!Arrays.equals(row,rowtemp))
            isChanged = true;
        return isChanged;
    }
    
    
    private boolean mergeRow(int[] row){
        boolean moved = false;
        for (int i=0; i< row.length-1;i++)
            if ((row[i] == row[i+1])&&(row[i]!=0)){
                row[i] = 2*row[i];
                row[i+1] = 0;
                score += row[i];
            setScore(score);
                moved = true;
            }
        return moved;
    }
    
      @Override
        public void onKeyPress(Key key) {
        if (!canUserMove()) {
            gameOver();
        } else {
        if (isGameStopped && key == Key.SPACE) {
            isGameStopped = false;
            score = 0;
            setScore(score);
            createGame();
            drawScene();
        } else if (!isGameStopped && key == Key.LEFT) {
            moveLeft();
            drawScene();
        } else if (!isGameStopped && key == Key.RIGHT) {
            moveRight();
            drawScene();
        } else if (!isGameStopped && key == Key.UP) {
            moveUp();
            drawScene();
        } else if (!isGameStopped && key == Key.DOWN) {
            moveDown();
            drawScene();
        }
    }
}
    
    private boolean moveLeft(){
        boolean compress;    
        boolean merge;          
        boolean compresss;     
        int move=0;                  
       for (int i = 0; i < SIDE; i++){
            compress = compressRow(gameField[i]);
            merge = mergeRow(gameField[i]);
            compresss=compressRow(gameField[i]);
            if(compress || merge || compresss)
                  move++;
           }
       if( move!= 0 ){
           createNewNumber();
        }
    return true;
    }
    
    private void rotateClockwise() {
        for (int i = 0; i < SIDE / 2; i++)
        {
            for (int j = i; j < SIDE - i - 1; j++)
            {
                int temp = gameField[i][j];
                gameField[i][j] = gameField[SIDE - 1 - j][i];
                gameField[SIDE - 1 - j][i] = gameField[SIDE - 1 - i][SIDE - 1 - j];
                gameField[SIDE - 1 - i][SIDE - 1 - j] = gameField[j][SIDE - 1 - i];
                gameField[j][SIDE - 1 - i] = temp;
            }
        }
    }
    
    private void moveRight() {
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }
    
    private void moveUp() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }
    
    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();   
    }
    
    private boolean canUserMove() {

    for (int i = 0; i < gameField.length; ++i) {
      for (int j = 0; j < gameField.length; ++j) {
        if (gameField[i][j] == 0) {
          return true;
        }
        if ((i + 1) < SIDE && (gameField[i][j] == gameField[i + 1][j])) {
          return true;
        }
        if ((j + 1) < SIDE && (gameField[i][j] == gameField[i][j + 1])) {
          return true;
        }
      }
    }
    return false;
  }
}