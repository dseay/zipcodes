package org.dms.zipcodes.model;

import java.util.Objects;

/**
 * Represents an immutable USPS zipcode
 */
public class Zipcode implements Comparable<Zipcode> {

  private final int value;

  public Zipcode(int value) {
    if (value < 1 || value > 99999) {
      throw new IllegalArgumentException("not in range 1 to 99999");
    }
    this.value = value;
  }

  public int getValue() {
    return value;
  }
 
  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public boolean equals(Object other) {
    if (other == null || !(other instanceof Zipcode))
      return false;
    
    Zipcode otherZip = (Zipcode)other;
    return Objects.equals(value, otherZip.value);
  }

  @Override
  public String toString() {
    return String.format("%05d", value);
  }

  @Override
  public int compareTo(Zipcode other) {
    return this.value - other.value;
  }
}
