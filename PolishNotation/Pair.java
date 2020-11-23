public class Pair<L,R> {

  private L left;
  private R right;

  public Pair(L left, R right) {
    this.left = left;
    this.right = right;
  }

  public L getLeft() { return left; }
  public R getRight() { return right; }

  public void setLeft(L data) { left = data; }
  public void setRight(R data) { right = data; }

  @Override
  public int hashCode() { return left.hashCode() ^ right.hashCode(); }

  public String toString() {
    return "( " + left.toString() + " , " + right.toString() + " )";
  }
}
