package org.dms.zipcodes.model;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

public class ZipcodeRangeTest {
  
  /**
   * test from zipcode greater than to zipcode
   */
  @Test(expected=IllegalArgumentException.class)
  public void testFromGreaterThanTo() {
    new ZipcodeRange(100, 99);
  }

  /**
   * test from zipcode greater than to zipcode
   */
  @Test(expected=NullPointerException.class)
  public void testNullFromZipcode() {
    new ZipcodeRange(null, new Zipcode(90210));
  }

  /**
   * test from zipcode greater than to zipcode
   */
  @Test(expected=NullPointerException.class)
  public void testNullToZipcode() {
    new ZipcodeRange(new Zipcode(90210), null);
  }
  
  /**
   * test setting from to null
   */
  @Test(expected=NullPointerException.class)
  public void testSetFromZipcodeToNull() {
    new ZipcodeRange(90210, 90210).setFrom(null);
  }
  
  /**
   * test setting from to null
   */
  @Test(expected=NullPointerException.class)
  public void testSetToZipcodeToNull() {
    new ZipcodeRange(90210, 90210).setTo(null);
  }
  
  /**
   * test setting from zipcode to invalid range
   */
  @Test(expected=IllegalArgumentException.class)
  public void testSetFromZipcodeToInvalidRange() {
    new ZipcodeRange(90000, 91000).setFrom(92000);
  }
  
  /**
   * test setting to zipcode to invalid range
   */
  @Test(expected=IllegalArgumentException.class)
  public void testSetToZipcodeToInvalidRange() {
    new ZipcodeRange(90000, 91000).setTo(8000);
  }
  
  /**
   * test is consumed by
   */
  @Test
  public void testIsConsumedBy() {
    ZipcodeRange range = range(90000, 91000);
    assertThat(range.isConsumedBy(range(90000, 91000))).isTrue();
    assertThat(range.isConsumedBy(range(90001, 90999))).isTrue();
    
    assertThat(range.isConsumedBy(range(80000, 89999))).isFalse();
    assertThat(range.isConsumedBy(range(91001, 91002))).isFalse();
    assertThat(range.isConsumedBy(range(1, 99999))).isFalse();
  }

  /**
   * test is overlap on lower range
   */
  @Test
  public void testIsOverlapOnLowerRange() {
    ZipcodeRange range = range(90000, 91000);
    assertThat(range.isOverlapOnLowerRange(range(80000, 91500))).isTrue();
    assertThat(range.isOverlapOnLowerRange(range(89999, 90000))).isTrue();
    
    assertThat(range.isOverlapOnLowerRange(range(80000, 89999))).isFalse();
    assertThat(range.isOverlapOnLowerRange(range(90000, 99999))).isFalse();
  }
  
  /**
   * test is overlap on lower range
   */
  @Test
  public void testIsOverlapOnUpperRange() {
    ZipcodeRange range = range(90000, 91000);
    assertThat(range.isOverlapOnUpperRange(range(80000, 91500))).isTrue();
    assertThat(range.isOverlapOnUpperRange(range(91000, 92000))).isTrue();
    
    assertThat(range.isOverlapOnUpperRange(range(91001, 91001))).isFalse();
    assertThat(range.isOverlapOnUpperRange(range(80000, 80000))).isFalse();
    assertThat(range.isOverlapOnUpperRange(range(89999, 89999))).isFalse();
  }
  
  /**
   * test is adjacent on lower range
   */
  @Test
  public void testIsAdjacentOnLowerRange() {
    ZipcodeRange range = range(90000, 91000);
    assertThat(range.isAdjacentOnLowerRange(range(80000, 89999))).isTrue();
    assertThat(range.isAdjacentOnLowerRange(range(80000, 89998))).isFalse();
    assertThat(range.isAdjacentOnLowerRange(range(90000, 91000))).isFalse();
  }
  
  /**
   * test is adjacent on upper range
   */
  @Test
  public void testIsAdjacentOnUpperRange() {
    ZipcodeRange range = range(90000, 91000);
    assertThat(range.isAdjacentOnUpperRange(range(91001, 91001))).isTrue();
    assertThat(range.isAdjacentOnUpperRange(range(91002, 91003))).isFalse();
    assertThat(range.isAdjacentOnUpperRange(range(80000, 90000))).isFalse();
  }
  
  // builder 
  private ZipcodeRange range(int from, int to) {
    return new ZipcodeRange(from, to);
  }
  
}
