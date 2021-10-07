import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class StageReader {
  public static Stage readStage(String path) {
    Stage stage = Stage.getInstance();
    try {
      Properties props = (new Properties());
      props.load(new FileInputStream(path));
      for (String key : props.stringPropertyNames()) {
        String value = props.getProperty(key);
        Pattern range = Pattern.compile("(.)(\\d+)->(.)(\\d+)");
        Pattern cell = Pattern.compile("(.)(\\d+)");
        List<Cell> cellsInQuestion = new ArrayList<Cell>();
        Matcher rangeMatcher = range.matcher(key);
        Matcher cellMatcher = cell.matcher(key);
        if (rangeMatcher.matches()) {
          cellsInQuestion = stage.grid.cellsInRange(rangeMatcher.group(1).charAt(0),
            Integer.parseInt(rangeMatcher.group(2)),
            rangeMatcher.group(3).charAt(0),
            Integer.parseInt(rangeMatcher.group(4))
          );
        } else if (cellMatcher.matches()) {
          char col = cellMatcher.group(1).charAt(0);
          int row = Integer.parseInt(cellMatcher.group(2));
          stage.grid.cellAtColRow(col, row).ifPresent(cellsInQuestion::add);
        } else {
          System.out.println("no match");
        }
        for (Cell c : cellsInQuestion) {
          Pattern itemPattern = Pattern.compile("(\\w+)(([,\\s])([-\\w]+))?");
          Matcher itemMatcher = itemPattern.matcher(value);
          if (itemMatcher.matches()) {
            String item = itemMatcher.group(1);
            float redness;
            int elevation;
            if (itemMatcher.group(2) == null) {
              if (item.equals("building")) {
                stage.grid.replaceCell(c, new Building(c.col, c.row, c.x, c.y));
              } else {
                System.out.println("error: no such cell type " + item);
              }
            } else {
              if (itemMatcher.group(3).equals(",")) {
                elevation = Integer.parseInt(itemMatcher.group(4));
                if (item.equals("road")) {
                  stage.grid.replaceCell(c, new Road(c.col, c.row, c.x, c.y, elevation));
                } else if (item.equals("water")) {
                  stage.grid.replaceCell(c, new Water(c.col, c.row, c.x, c.y, elevation));
                } else if (item.equals("grass")) {
                  stage.grid.replaceCell(c, new Grass(c.col, c.row, c.x, c.y, elevation));
                } else if (item.equals("mountain")) {
                  stage.grid.replaceCell(c, new Mountain(c.col, c.row, c.x, c.y, elevation));
                } else {
                  System.out.println("error: " + value + ", no such cell type " + item);
                  break;
                }
              } else if (itemMatcher.group(3).equals(" ")) {
                if (itemMatcher.group(4).equals("blue")) {
                  redness = 0.0f;
                } else if (itemMatcher.group(4).equals("red")) {
                  redness = 1.0f;
                } else {
                  System.out.println("error: " + value + ", no such color " + itemMatcher.group(4));
                  break;
                }
                if (item.equals("train")) {
                  stage.actors.add(new Train(c, redness));
                } else if (item.equals("car")) {
                  stage.actors.add(new Car(c, redness));
                } else if (item.equals("boat")) {
                  stage.actors.add(new Boat(c, redness));
                } else {
                  System.out.println("error: " + value + ", no such actor  type" + item);
                  break;
                }
              }
            }
          } else {
            System.out.println("error: unrecognised value " + value);
          }
        }
      }
    } catch (IOException e) {
      System.out.println("catastrophic");
      // Start with an empty stage if we cannot open the stage file.
    }
    return stage;
  }
}