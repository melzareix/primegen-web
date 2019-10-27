package models;

public class RequestBody {

  // Algorithms Available
  public static enum Algorithms {
    TrialDivision,
    Sieve,
    LinearSieve,
    MillerRabin,
  };

  Algorithms algorithm;
  int start;
  int end;

  public Algorithms getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(Algorithms algorithm) {
    this.algorithm = algorithm;
  }

  public int getStart() {
    return start;
  }

  public void setStart(int start) {
    this.start = start;
  }

  public int getEnd() {
    return end;
  }

  public void setEnd(int end) {
    this.end = end;
  }

  @Override
  public String toString() {
    return "RequestBody{" +
        "algorithm=" + algorithm +
        ", start=" + start +
        ", end=" + end +
        '}';
  }
}
