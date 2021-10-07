import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class Stage {
  private static Stage uniqueStage;
  Grid grid;
  List<Actor> actors;
  List<Cell> cellOverlay;
  List<MenuItem> menuOverlay;
  Optional<Actor> actorInAction;

  enum State {
    ChoosingActor, SelectingNewLocation, CPUMoving, SelectingMenuItem, SelectingTarget
  }

  State currentState;

  private Stage() {
    grid = new Grid();
    actors = new ArrayList<Actor>();
    cellOverlay = new ArrayList<Cell>();
    menuOverlay = new ArrayList<MenuItem>();
    actorInAction = Optional.empty();
    currentState = State.ChoosingActor;
  }

  public static Stage getInstance(){
    if(uniqueStage==null){
        uniqueStage = new Stage();
    }
    return uniqueStage;
}

  void paint(Graphics g, Point mouseLoc) {
    // do we have AI moves to make
    if (currentState == State.CPUMoving) {
      for (Actor a : actors) {
        if (!a.isTeamRed()) {
          List<Cell> possibleLocs = getClearRadius(a.loc, a.moves, true);
          Cell nextLoc = a.strat.chooseNextLoc(possibleLocs);
          a.setLocation(nextLoc);
        }
      }
      currentState = State.ChoosingActor;
      for (Actor a : actors) {
        a.turns = 1;
      }
    }

    grid.paint(g, mouseLoc);
    grid.paintOverlay(g, cellOverlay, new Color(0f, 0f, 1f, 0.5f));

    for (Actor a : actors) {
      a.paint(g);
    }

    // where to draw text in the information area
    final int hTab = 10;
    final int vTab = 15;
    final int blockVtab = 35;
    final int margin = 21 * blockVtab;
    int yloc = 20;

    // state display
    g.setColor(Color.DARK_GRAY);
    g.drawString(currentState.toString(), margin, yloc);
    yloc = yloc + blockVtab;
    Optional<Cell> underMouse = grid.cellAtPoint(mouseLoc);
    if (underMouse.isPresent()) {
      Cell hoverCell = underMouse.get();
      g.setColor(Color.DARK_GRAY);
      String coord = String.valueOf(hoverCell.col) + String.valueOf(hoverCell.row);
      g.drawString(coord, margin, yloc);
      g.drawString(hoverCell.description, margin + 3 * blockVtab, yloc);
      if (hoverCell instanceof Landscape) {
        Landscape land = (Landscape) hoverCell;
        g.drawString("elevation", margin, yloc + vTab);
        g.drawString(String.valueOf(land.elevation()), margin + 3 * blockVtab, yloc + vTab);
      }
    }

    // actor display
    final int labelIndent = margin + hTab;
    final int valueIndent = margin + 3 * blockVtab;
    yloc = yloc + 2 * blockVtab;
    for (int i = 0; i < actors.size(); i++) {
      Actor a = actors.get(i);
      yloc = yloc + 2 * blockVtab;
      g.drawString(a.getClass().toString(), margin, yloc);
      g.drawString("location:", labelIndent, yloc + vTab);
      String coordinate = Character.toString(a.loc.col) + Integer.toString(a.loc.row);
      g.drawString(coordinate, valueIndent, yloc + vTab);
      g.drawString("redness:", labelIndent, yloc + 2 * vTab);
      g.drawString(Float.toString(a.redness), valueIndent, yloc + 2 * vTab);
    }

    // menu overlay (on top of everything else)
    for (MenuItem mi: menuOverlay) {
      mi.paint(g);
    }
  }

  List<Cell> getClearRadius(Cell from, int size, boolean considerElevation) {
    List<Cell> init = grid.getRadius(from, size, considerElevation);
    for (Actor a : actors) {
      init.remove(a.loc);
    }
    return init;
  }

  void mouseClicked(int x, int y) {
    switch (currentState) {
      case ChoosingActor:
        actorInAction = Optional.empty();
        for (Actor a : actors) {
          if (a.loc.contains(x, y) && a.isTeamRed() && a.turns > 0) {
            cellOverlay = grid.getRadius(a.loc, a.moves, true);
            actorInAction = Optional.of(a);
            currentState = State.SelectingNewLocation;
          }
        }
        if (actorInAction.isEmpty()) {
          currentState = State.SelectingMenuItem;
          currentState = State.SelectingMenuItem;
          menuOverlay.add(new MenuItem("Oops", x, y,
              () -> currentState = State.ChoosingActor));
          menuOverlay.add(new MenuItem("End Turn", x, y + MenuItem.height,
              () -> currentState = State.CPUMoving));
          menuOverlay.add(new MenuItem("End Game", x, y + MenuItem.height * 2,
              () -> System.exit(0)));
        }
        break;
      case SelectingNewLocation:
        Optional<Cell> clicked = Optional.empty();
        for (Cell c : cellOverlay) {
          if (c.contains(x, y)) {
            clicked = Optional.of(c);
          }
        }
        if (clicked.isPresent() && actorInAction.isPresent()) {
          actorInAction.get().setLocation(clicked.get());
          actorInAction.get().turns--;
          menuOverlay.add(new MenuItem("Fire", x, y, () -> {
            cellOverlay = grid.getRadius(actorInAction.get().loc, actorInAction.get().range, false);
            cellOverlay.remove(actorInAction.get().loc);
            currentState = State.SelectingTarget;
          }));
          currentState = State.SelectingMenuItem;
        }
        break;
      case SelectingMenuItem:
        for (MenuItem mi : menuOverlay) {
          if (mi.contains(x, y)) {
            mi.action.run();
            menuOverlay = new ArrayList<MenuItem>();
          }
        }
        break;
      case SelectingTarget:
        for (Cell c : cellOverlay) {
          if (c.contains(x, y)) {
            Optional<Actor> oa = actorAt(c);
            if (oa.isPresent()) {
              oa.get().makeRedder(0.1f);
            }
          }
        }
        cellOverlay = new ArrayList<Cell>();
        currentState = State.ChoosingActor;
        break;
      default:
        System.out.println(currentState);
        break;
    }
  }

  Optional<Actor> actorAt(Cell c) {
    for (Actor a : actors) {
      if (a.loc == c) {
        return Optional.of(a);
      }
    }
    return Optional.empty();
  }
}
