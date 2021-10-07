import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.List;

abstract class Actor {
  Cell loc;
  List<Polygon> display;
  float redness;
  int turns;
  int moves;
  int range;
  MoveStrategy strat;

  void paint(Graphics g) {
    for (Polygon p : display) {
      g.setColor(new Color(redness, 0f, 1f - redness));
      g.fillPolygon(p);
      g.setColor(Color.GRAY);
      g.drawPolygon(p);
    }
  }

  void makeRedder(float amt) {
    redness = redness + amt;
    if (redness > 1.0f) {
      redness = 1.0f;
    }
  }

  protected abstract void setPoly();

  boolean isTeamRed() {
    return redness >= 0.5;
  }

  void setLocation(Cell inLoc) {
    loc = inLoc;
    if (loc.row % 2 == 0) {
      strat = new RandomMove();
    } else {
      strat = new LeftMostMove();
    }
    setPoly();
  }
}
