import java.awt.Color;

class Mountain extends Landscape {
  public Mountain(char col, int row, int x, int y, int z) {
    super(col, row, x, y, z);
    description = "Mountain";
    color = new Color(shade, shade, 0.0f);
  }
}
