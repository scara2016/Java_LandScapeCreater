import java.awt.Polygon;
import java.util.ArrayList;

class Car extends Actor {
  public Car(Cell inLoc, float inRedness) {
    setLocation(inLoc);
    redness = inRedness;
    turns = 1;
    moves = 3;
    range = 2;
    setPoly();
  }

  protected void setPoly() {
    display = new ArrayList<Polygon>();
    int sides = 20;
    int angle;
    double circleX;
    double circleY;
    Polygon rearWheel = new Polygon();
    Polygon frontWheel = new Polygon();
    angle = 360 / sides;
    for (int s = 0; s <= sides; s++) {
      circleX = (4.0 * Math.sin(Math.toRadians(s * angle)));
      circleY = (4.0 * Math.cos(Math.toRadians(s * angle)));
      rearWheel.addPoint(loc.x + 11 + (int) circleX, loc.y + 25 + (int) circleY);
      frontWheel.addPoint(loc.x + 24 + (int) circleX, loc.y + 25 + (int) circleY);
    }
    Polygon body = new Polygon();
    body.addPoint(loc.x + 6, loc.y + 14);
    body.addPoint(loc.x + 29, loc.y + 14);
    body.addPoint(loc.x + 29, loc.y + 20);
    body.addPoint(loc.x + 6, loc.y + 20);
    Polygon top = new Polygon();
    top.addPoint(loc.x + 11, loc.y + 7);
    top.addPoint(loc.x + 20, loc.y + 7);
    top.addPoint(loc.x + 24, loc.y + 14);
    top.addPoint(loc.x + 11, loc.y + 14);
    display.add(rearWheel);
    display.add(frontWheel);
    display.add(body);
    display.add(top);
  }
}
