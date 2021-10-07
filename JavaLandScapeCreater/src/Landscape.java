abstract class Landscape extends Cell {
  protected int height;
  protected float shade;

  public Landscape(char col, int row, int x, int y, int z) {
    super(col, row, x, y);
    // we could have used an exception here, however
    // as they were not a requirement of the assignment
    // we'll just force the floor and ceiling for now
    if (z < -500) {
      z = -500;
    }
    if (z > 6000) {
      z = 6000;
    }
    height = z;
    // in order to avoid shades that are almost black or white
    // we will user shades between 35% and 85%
    shade = ((float) height + 500) / 6500 * 0.5f + 0.35f;
  }

  public int elevation() {
    return height;
  }
}
