package org.dms.zipcodes.service.impl;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.dms.zipcodes.model.ZipcodeRange;
import org.junit.Before;
import org.junit.Test;

public class ZipcodeSerivceImplTest {

  private ZipcodeSerivceImpl service;
  private Random random;
  
  @Before
  public void setup() {
    service = new ZipcodeSerivceImpl();
    random = new Random(1); // seed with fixed number to both get same set of numbers and avoid overhead of seed generation on first use
  }
  
  /**
   * test first example from code challenge doc
   */
  @Test
  public void testExample1() {
    assertThat(service.computeMinimumRanges(range(94133,94133), range(94200,94299), range(94600,94699)))
      .hasSize(3)
      .contains(range(94133,94133))
      .contains(range(94200,94299))
      .contains(range(94600,94699));
  }
  
  /**
   * test second example from code challenge doc
   */
  @Test
  public void testExample2() {
    assertThat(service.computeMinimumRanges(range(94133,94133), range(94200,94299), range(94226,94399)))
      .hasSize(2)
      .contains(range(94133,94133))
      .contains(range(94200,94399));
  }

  /**
   * test range consumed by another
   */
  @Test
  public void testConsumedBy() {
    assertThat(service.computeMinimumRanges(range(90000,91000), range(90100,90900)))
      .hasSize(1)
      .contains(range(90000,91000));
  }
  
  /**
   * test range consumed by another
   */
  @Test
  public void testDuplicateRange() {
    assertThat(service.computeMinimumRanges(range(90000,91000), range(90000,91000)))
      .hasSize(1)
      .contains(range(90000,91000));
  }
  
  /**
   * test consumed by with same lower range
   */
  @Test
  public void testConsumedSameLowerRange() {
    assertThat(service.computeMinimumRanges(range(90000,91000), range(90000,90500)))
      .hasSize(1)
      .contains(range(90000,91000));
  }
  
  /**
   * test consumed by with same upper range
   */
  @Test
  public void testConsumedSameUpperRange() {
    assertThat(service.computeMinimumRanges(range(90000,91000), range(90500,91000)))
      .hasSize(1)
      .contains(range(90000,91000));
  }

  @Test
  public void testAdjacentAscending() {
    assertThat(service.computeMinimumRanges(range(1,1), range(2,2), range(3,3), range(4,4)))
      .hasSize(1)
      .contains(range(1,4));
  }
  
  @Test
  public void testAdjacentDescending() {
    assertThat(service.computeMinimumRanges(range(1,1), range(2,2), range(3,3), range(4,4)))
      .hasSize(1)
      .contains(range(1,4));
  }
  
  @Test
  public void testAdjacentRandomOrder() {
    assertThat(service.computeMinimumRanges(range(3,3), range(2,2), range(1,1), range(4,4)))
      .hasSize(1)
      .contains(range(1,4));
  }
  
  /**
   * test multiple minimum sized adjacent ranges with duplicates
   */
  @Test
  public void testMultipleMinSizeRanges() {
    assertThat(service.computeMinimumRanges(range(3,3), range(3,3), range(1,1), range(4,4), range(2,2), range(1,1), range(2,2)))
      .hasSize(1)
      .contains(range(1,4));
  }
  
  @Test
  public void testNullListOfRanges() {
    List<ZipcodeRange> ranges = null;
    assertThat(service.computeMinimumRanges(ranges)).isEmpty();
  }  
  
  @Test
  public void testEmptyListOfRanges() {
    ZipcodeRange range = range(9000, 9900);
    assertThat(service.computeMinimumRanges(range))
      .hasSize(1)
      .contains(range);
  }  

  @Test
  public void testSingleRange() {
    List<ZipcodeRange> ranges = Collections.emptyList();
    assertThat(service.computeMinimumRanges(ranges)).isEmpty();
  }  
  
  @Test
  public void testRandomListsOfRanges() {
    int iterations = 20;
    for (int nbrOfRanges = 10; nbrOfRanges <= 100; nbrOfRanges+=5) {
      for (int maxSizeOfRange = 2; maxSizeOfRange <= 100; maxSizeOfRange+=5) {
        testRanges(iterations, nbrOfRanges, maxSizeOfRange);
      }      
    }
  }  
  
  private void testRanges(int iterations, int nbrOfRanges, int maxSizeOfRange) {
    List<ZipcodeRange> orig, minimal;
    for (int i = 0; i < iterations; i++) {
      orig = buildRandomRanges(nbrOfRanges, maxSizeOfRange);
      minimal = service.computeMinimumRanges(orig);
      assertThat(isOverlap(minimal)).isFalse();
      assertThat(burtFoceCheckSameCoverage(orig, minimal)).isTrue();
    }    
  }

  /* 
   * Will build a hashset of all the covered zipcodes in the minimal set then
   * check that each of the zipcodes in each range of the orig is covered
   */
  private boolean burtFoceCheckSameCoverage(List<ZipcodeRange> orig, List<ZipcodeRange> minimal) {
    Set<Integer> origCoverage = buildCoverageSet(orig);
    Set<Integer> minCoverage = buildCoverageSet(orig);
    if (origCoverage.size() != minCoverage.size()) {    
      return false;
    }
    
    for (int zip : origCoverage) {
      if (!minCoverage.contains(zip)) {
        return false;
      }
    }
    return true;
  }

  private Set<Integer> buildCoverageSet(List<ZipcodeRange> ranges) {
    Set<Integer> coverage = new HashSet<>();
    for (ZipcodeRange range : ranges) {
      for (int zip = range.getFrom().getValue(); zip <= range.getTo().getValue(); zip++) {
        coverage.add(zip);
      }
    }
    return coverage;
  }
  
  /**
   * @return true if there is no over lap in the set of ranges
   */
  private boolean isOverlap(List<ZipcodeRange> ranges) {
    Collections.sort(ranges);
    // now that ranges are sorted we should iterate over each range and the from and to should always be increasing
    int start = ranges.get(0).getTo().getValue();
    for (int i = 1; i < ranges.size(); i++) {
      if (ranges.get(i).getFrom().getValue() <= start) {
        return true;
      }
      start = ranges.get(i).getTo().getValue();
    }
    return false;
  }

  /**
   * @param size the number of ranges to build
   * @param maxRange the max zipcodes in each range, i.e. to - from <= maxRange
   * @return random list of ranges
   */
  private List<ZipcodeRange> buildRandomRanges(int size, int maxRange) {
    List<ZipcodeRange> ranges = new ArrayList<>();
    int from, to;
    for (int i = 0; i < size; i++) {
      from = random.nextInt(99998) + 1;
      to = from == 99999 ? 99999 : from + random.nextInt(99999 - from);
      to = Math.min(to, from + maxRange);
      ranges.add(new ZipcodeRange(from, to));
    }
    return ranges;
  }
  
  // builder 
  private ZipcodeRange range(int from, int to) {
    return new ZipcodeRange(from, to);
  }
  
}
