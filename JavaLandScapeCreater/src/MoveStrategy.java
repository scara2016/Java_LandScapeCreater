import java.util.List;

interface MoveStrategy {
  public Cell chooseNextLoc(List<Cell> possibleLocs);
}
