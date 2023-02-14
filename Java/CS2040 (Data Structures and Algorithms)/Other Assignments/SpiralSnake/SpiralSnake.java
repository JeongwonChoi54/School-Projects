/**
 * Name : Jeongwon Choi
 * Matric. No : A0219728U
 */

import java.util.*;

class Snake{
    public int x;
    public int y;
    public String [][] grid;
    public int j;
    public int m;
    public int n;

    public Snake(int m, int n, String[][] grid, int j){
        this.x = 0;
        this.y = 0;
        this.grid = grid;
        this.j = j;
        this.m = m;
        this.n = n;
    }

    public void check(){
        if (grid[y][x].equals("X")){
            System.out.println("Apple at (" + x + ", " + y + ") eaten at step " + j);
        }
    }

    public void right(){
        while(x < n && !(grid[y][x].equals("o"))){
            check();
            grid[y][x] = "o";
            x++;
            j++;
        }
        x--;
    }

    public void down(){
        y++;
        while(y < m && !(grid[y][x].equals("o"))){
            check();
            grid[y][x] = "o";
            y++;
            j++;
        }
        y--;
    }

    public void left(){
        x--;
        while(x > -1 && !(grid[y][x].equals("o"))){
            check();
            grid[y][x] = "o";
            x--;
            j++;
        }
        x++;
    }

    public void up(){
        y--;
        while(y > -1 && !(grid[y][x].equals("o"))){
            check();
            grid[y][x] = "o";
            y--;
            j++;
        }
        y++;
        x++;
    }

    public int getJ(){
        return j;
    }
}

public class SpiralSnake {
  public static void main(String args[]) {
      Kattio io = new Kattio(System.in);
      int m = io.getInt();
      int n = io.getInt();
      String [][] grid = new String[m][n];

      for (int i = 0; i<m; i++){
          grid[i] = io.getWord().split("");
      }

      int j = 1;

      Snake a = new Snake(m, n, grid, j);

      while (a.getJ() < m*n){
          a.right();
          if (a.getJ() >= m*n) break;
          a.down();
          if (a.getJ() >= m*n) break;
          a.left();
          if (a.getJ() >= m*n) break;
          a.up();
          if (a.getJ() >= m*n) break;
      }
      io.close();
  }
}
