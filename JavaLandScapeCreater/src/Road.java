import java.awt.Color;

class Road extends Landscape {
  public Road(char col, int row, int x, int y, int z) {
    super(col, row, x, y, z);
    description = "Road";
    color = new Color(shade, shade, shade);
  }
}
