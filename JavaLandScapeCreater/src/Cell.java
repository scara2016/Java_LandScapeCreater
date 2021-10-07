import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

class Cell extends Rectangle {
  private static int size = 35;
  protected String description;
  protected Color color;
  protected char col;
  protected int row;

  public Cell(char inCol, int inRow, int inX, int inY) {
    super(inX, inY, size, size);
    col = inCol;
    row = inRow;
  }

  void paint(Graphics g, Point mousePos) {
    if (contains(mousePos)) {
      g.setColor(Color.GRAY);
    } else {
      g.setColor(color);
    }
    g.fillRect(x, y, size, size);
    g.setColor(Color.BLACK);
    g.drawRect(x, y, size, size);
  }

  @Override
  public boolean contains(Point p) {
    if (p != null) {
      return (super.contains(p));
    } else {
      return false;
    }
  }

  public int leftOfComparison(Cell c) {
    return Character.compare(col, c.col);
  }

  public int aboveComparison(Cell c) {
    return Integer.compare(row, c.row);
  }

  public String toString() {
    return Character.toString(col) + Integer.toString(row) + ":" + description;
  }
}