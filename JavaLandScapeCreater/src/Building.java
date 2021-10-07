import java.awt.Color;

class Building extends Cell {
  public Building(char col, int row, int x, int y) {
    super(col, row, x, y);
    description = "Building";
    color = new Color(96, 64, 32);
  }
}
