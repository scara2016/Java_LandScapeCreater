import java.util.List;

class LeftMostMove implements MoveStrategy {
  @Override
  public Cell chooseNextLoc(List<Cell> possibleLocs) {
    Cell currLeftMost = possibleLocs.get(0);
    for (Cell c : possibleLocs) {
      if (c.leftOfComparison(currLeftMost) < 0) {
        currLeftMost = c;
      }
    }
    return currLeftMost;
  }

  public String toString() {
    return "left-most movement strategy";
  }
}
