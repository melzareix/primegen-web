package models;

import java.time.LocalDateTime;
import org.bson.types.ObjectId;

public class Prime {

  private ObjectId id;
  private String algorithm;
  private int startRange;
  private int endRange;
  private int numberOfPrimes;
  private long timeOfExecution;
  private LocalDateTime createdAt;

  public Prime() {
  }

  public Prime(String algorithm, int startRange, int endRange, int numberOfPrimes,
      long timeOfExecution, LocalDateTime createdAt) {
    this.algorithm = algorithm;
    this.startRange = startRange;
    this.endRange = endRange;
    this.numberOfPrimes = numberOfPrimes;
    this.timeOfExecution = timeOfExecution;
    this.createdAt = createdAt;
  }

  public ObjectId getId() {
    return id;
  }

  public void setId(final ObjectId id) {
    this.id = id;
  }

  public String getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(final String algorithm) {
    this.algorithm = algorithm;
  }

  public int getStartRange() {
    return startRange;
  }

  public void setStartRange(final int startRange) {
    this.startRange = startRange;
  }

  public int getEndRange() {
    return endRange;
  }

  public void setEndRange(final int endRange) {
    this.endRange = endRange;
  }

  public int getNumberOfPrimes() {
    return numberOfPrimes;
  }

  public void setNumberOfPrimes(final int numberOfPrimes) {
    this.numberOfPrimes = numberOfPrimes;
  }

  public long getTimeOfExecution() {
    return timeOfExecution;
  }

  public void setTimeOfExecution(final long timeOfExecution) {
    this.timeOfExecution = timeOfExecution;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(final LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
