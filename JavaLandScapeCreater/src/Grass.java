import java.awt.Color;

class Grass extends Landscape {
  public Grass(char col, int row, int x, int y, int z) {
    super(col, row, x, y, z);
    description = "Grass";
    color = new Color(0.0f, shade, 0.0f);
  }
}
