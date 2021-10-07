import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

class MenuItem extends Rectangle {
  Runnable action;
  String display;
  static int height = 30;
  static int width = 200;
  static Font displayFont = new Font("Full Pack 2025", Font.PLAIN, 20);

  MenuItem(String display, int x, int y, Runnable action) {
    super(x,y,width,height);
    this.display = display;
    this.action = action;
  }

  void paint(Graphics g) {
    g.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
    g.fillRect(x, y, width, height);
    g.setColor(Color.BLACK);
    g.drawRect(x, y, width, height);
    g.drawRect(x - 1, y - 1, width + 2, height + 2);
    g.setFont(displayFont);
    g.drawString(display, x + 8, y + 23);
  }
}