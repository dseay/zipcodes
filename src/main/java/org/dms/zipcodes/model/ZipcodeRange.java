package org.dms.zipcodes.model;

import java.util.Objects;

/**
 * Represents a USPS zipcode range, natural ordering will be based on from zipcode then to zipcode
 * 
 */
public class ZipcodeRange implements Comparable<ZipcodeRange> {

  private Zipcode from;
  private Zipcode to;
  
  public ZipcodeRange(int from, int to) {
    this(new Zipcode(from), new Zipcode(to));
  }
  
  /**
   * @throws NullPointerException on null from or to
   * @throws IllegalArgumentException on invalid zipcode range
   */
  public ZipcodeRange(Zipcode from, Zipcode to) {
    if (from == null || to == null) {
      throw new NullPointerException("from and to must not be null");
    }
    if (from.getValue() > to.getValue()) {
      throw new IllegalArgumentException("invalid range " + from.getValue() + " ... " + to.getValue());
    }
    this.from = from;
    this.to = to;
  }
  
  public Zipcode getFrom() {
    return from;
  }
  
  public Zipcode getTo() {
    return to;
  }
  
  /**
   * @throws IllegalArgumentException if from produces an invalid range
   */
  public void setFrom(Zipcode from) {
    if (from == null) {
      throw new NullPointerException();
    } else if (from.getValue() > to.getValue()) {
      throw new IllegalArgumentException("invalid range");
    }
    this.from = from;
  }
  
  public void setFrom(int from) {
    setFrom(new Zipcode(from));
  }

  /**
   * @throws IllegalArgumentException if to produces an invalid range
   */
  public void setTo(Zipcode to) {
    if (to == null) {
      throw new NullPointerException();
    } else if (from.getValue() > to.getValue()) {
      throw new IllegalArgumentException("invalid range");
    }
    this.to = to;
  }
  
  public void setTo(int to) {
    setTo(new Zipcode(to));
  }

  /**
   * @return true if candidate range is fully contained within this range
   */
  public boolean isConsumedBy(ZipcodeRange range) {
    return range.getFrom().getValue() >= this.from.getValue() &&
           range.getTo().getValue() <= this.to.getValue();
  }

  /**
   * @return true if candidate range over laps on the lower range
   */
  public boolean isOverlapOnLowerRange(ZipcodeRange range) {
    return range.getFrom().getValue() < this.from.getValue() &&
           range.getTo().getValue() >= this.from.getValue();
  }

  /**
   * @return true if candidate range over laps on the lower range
   */
  public boolean isOverlapOnUpperRange(ZipcodeRange range) {
    return range.getTo().getValue() > this.to.getValue() &&
           range.getFrom().getValue() <= this.to.getValue();
  }  
  
  /**
   * @return true if candidate range adjacent on lower range
   */
  public boolean isAdjacentOnLowerRange(ZipcodeRange range) {
    return range.getTo().getValue() == (this.from.getValue() - 1);
  }
  
  /**
   * @return true if candidate range adjacent on upper range
   */
  public boolean isAdjacentOnUpperRange(ZipcodeRange range) {
    return range.getFrom().getValue() == (this.to.getValue() + 1);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(from, to);
  }

  @Override
  public boolean equals(Object other) {
    if (other == null || !(other instanceof ZipcodeRange))
      return false;
    
    ZipcodeRange otherRange = (ZipcodeRange)other;
    return Objects.equals(from, otherRange.from) &&
           Objects.equals(to, otherRange.to);
  }

  @Override
  public String toString() {
    return from.toString() + " ... " + to.toString();
  }

  @Override
  public int compareTo(ZipcodeRange other) {    
    if (this.from.getValue() == other.from.getValue()) {
      return this.to.compareTo(other.to);      
    } else {
      return this.from.compareTo(other.from);
    }
  }
  
}
