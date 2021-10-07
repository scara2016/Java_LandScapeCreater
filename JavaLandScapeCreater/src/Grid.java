import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Grid implements Iterable<Cell>{
  Cell[][] cells = new Cell[20][20];
  private static Random rand = new Random();

  public Grid() {
    int elevation;
    // The grid is 20x20=400 cells
    // Create a list of integers numbered 0 through 399
    List<Integer> distribution = new ArrayList<Integer>();
    distribution = IntStream.rangeClosed(0, 20 * 20 - 1).boxed().collect(Collectors.toList());
    int current;
    int index;
    for (int i = 0; i < cells.length; i++) {
      for (int j = 0; j < cells[i].length; j++) {
        // altitude vary over the following range:
        // -500 <= elevation <= 6000
        // this means that there are 6501 possible
        // values including zero
        elevation = rand.nextInt(6501) - 500;
        // grab a random element from our list
        // use the element's value to determine
        // which cell type to instantiate
        // and then remove that element from the list
        // so that there are no repeats
        index = rand.nextInt(distribution.size());
        current = distribution.get(index);
        distribution.remove(index);
        char c = colToLabel(i);
        int x = 10 + 35 * i;
        int y = 10 + 35 * j;
        //  Road: 10% of 400 = 40
        if (current < 40) {
          cells[i][j] = new Road(c, j, x, y, elevation);
        }
        // Water: 20% of 400 = 80
        if (current >= 40 && current < 120) {
          cells[i][j] = new Water(c, j, x, y, elevation);
        }
        // Grass: 40% of 400 = 160
        if (current >= 120 && current < 280) {
          cells[i][j] = new Grass(c, j, x, y, elevation);
        }
        // Mountain: 25% of 400 = 100
        if (current >= 280 && current < 380) {
          cells[i][j] = new Mountain(c, j, x, y, elevation);
        }
        // Buildings: 5% of 400 = 20
        if (current >= 380 && current < 400) {
          cells[i][j] = new Building(c, j, x, y);
        }
      }
    }
  }

  private char colToLabel(int col) {
    return (char) (col + 65);
  }

  private int labelToCol(char col) {
    return (int) col - 65;
  }

  public void paint(Graphics g, Point mousePos) {
    doToEachCell((Cell c) -> c.paint(g, mousePos));
  }

  private Optional<Cell> cellAtColRow(int c, int r) {
    if (c >= 0 && c < cells.length && r >= 0 && r < cells[c].length) {
      return Optional.of(cells[c][r]);
    } else {
      return Optional.empty();
    }
  }

  public Optional<Cell> cellAtColRow(char c, int r) {
    return cellAtColRow(labelToCol(c), r);
  }

  public Optional<Cell> cellAtPoint(Point p) {
    for(Cell c: this){
        if (c.contains(p)) {
          return Optional.of(c);
        }
      }
    
    return Optional.empty();
  }

  public List<Cell> cellsInRange(char c1, int r1, char c2, int r2) {
    int c1i = labelToCol(c1);
    int c2i = labelToCol(c2);
    List<Cell> output = new ArrayList<Cell>();
    for (int i = c1i; i <= c2i; i++) {
      for (int j = r1; j <= r2; j++) {
        cellAtColRow(colToLabel(i), j).ifPresent(output::add);
      }
    }
    return output;
  }

  public void replaceCell(Cell old, Cell replacement) {
    cells[labelToCol(old.col)][old.row] = replacement;
  }

  /**
   * Takes a cell consumer (i.e. a function that has a single `Cell` argument and
   * returns `void`) and applies that consumer to each cell in the grid.
   *
   * @param func The `Cell` to `void` function to apply at each spot.
   */
  public void doToEachCell(Consumer<Cell> func) {
    for (Cell c: this) {
      
      func.accept(c);
      
    }
  }

  public void paintOverlay(Graphics g, List<Cell> cells, Color color) {
    g.setColor(color);
    for (Cell c : cells) {
      g.fillRect(c.x + 2, c.y + 2, c.width - 4, c.height - 4);
    }
  }

  public List<Cell> getRadius(Cell from, int size, boolean considerElevation) {
    int i = labelToCol(from.col);
    int j = from.row;
    Set<Cell> inRadius = new HashSet<Cell>();
    if (size > 0) {
      cellAtColRow(colToLabel(i), j - 1).ifPresent(inRadius::add);
      cellAtColRow(colToLabel(i), j + 1).ifPresent(inRadius::add);
      cellAtColRow(colToLabel(i - 1), j).ifPresent(inRadius::add);
      cellAtColRow(colToLabel(i + 1), j).ifPresent(inRadius::add);
    }

    for (Cell c : inRadius.toArray(new Cell[0])) {
      if (considerElevation) {
        if (c instanceof Landscape) {
          Landscape here = (Landscape) from;
          Landscape there = (Landscape) c;
          if (here.elevation() > there.elevation()) {
            inRadius.addAll(getRadius(c, size, true));
          } else {
            inRadius.addAll(getRadius(c, size - 1, true));
          }
        } else {
          inRadius.remove(c);
        }
      } else {
        inRadius.addAll(getRadius(c, size - 1, false));
      }
    }
    return new ArrayList<Cell>(inRadius);
  }

  public String toString() {
    String retval = new String();
    for (Cell c: this) {
      
        retval = retval + c;
      
    }
    return retval;
  }
  public CellIterator iterator(){
    return new CellIterator(cells);
  }
}