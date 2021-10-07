import java.awt.Color;

class Water extends Landscape {
  public Water(char col, int row, int x, int y, int z) {
    super(col, row, x, y, z);
    description = "Water";
    color = new Color(0.0f, 0.0f, shade);
  }
}
