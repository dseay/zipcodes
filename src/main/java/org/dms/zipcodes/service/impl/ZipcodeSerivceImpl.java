package org.dms.zipcodes.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.dms.zipcodes.model.ZipcodeRange;
import org.dms.zipcodes.service.ZipcodeService;

public class ZipcodeSerivceImpl implements ZipcodeService {
  
  @Override
  public List<ZipcodeRange> computeMinimumRanges(ZipcodeRange... ranges) {
    return computeMinimumRanges(Arrays.asList(ranges));
  }

  /**
   * Algorithm: 
   *   1.) Sort provided range list PRL by from zipcode
   *   2.) Create empty results range list RRL
   *   3.) For each PR in PRL
   *          if RRL is empty add PR
   *          otherwise
   *            for each RR in RRL
   *               if PR is contained within RR 
   *                 skip
   *               if PR overlaps or is adjacent to RR
   *                 adjust RR to include PR range
   */
  @Override
  public List<ZipcodeRange> computeMinimumRanges(List<ZipcodeRange> ranges) {
    // first check for edge cases
    if (ranges == null || ranges.size() == 0) {
      return Collections.emptyList();
    } else if (ranges.size() == 1) {
      return ranges;
    }
    // make copy of ranges so as not to change order on caller and in case provided an immutable list
    ranges = new ArrayList<>(ranges); 
    // sort ranges will produce listing of from zipcode in ascending order with smaller ranges first
    Collections.sort(ranges);
    // implement algorithm
    List<ZipcodeRange> results = new ArrayList<>();
    for (ZipcodeRange range : ranges) {
      if (!extentExistingRangeIfPossible(range, results)) {
        results.add(range);  // if we could not extend and existing range then it the start of a new range
      }
    }
    return results;
  }
  
  /**
   * Attempt to extend an existing range if possible
   * 
   * @param range the range to be considered
   * @param ranges list of ranges to extend
   * 
   * @return true if an existing range was extended or candidate range was consumed by an existing range
   */
  private boolean extentExistingRangeIfPossible(ZipcodeRange candidate, List<ZipcodeRange> ranges) {
    boolean result = false;
    for (ZipcodeRange existing : ranges) {
      
      if (existing.isConsumedBy(candidate)) {  // check to see if candidate is consumed by existing range
        result = true;
      } else {
        if (existing.isOverlapOnLowerRange(candidate) ||
            existing.isAdjacentOnLowerRange(candidate)) {
          existing.setFrom(candidate.getFrom());  // extend existing range to include candidates range
          result = true;
        }
        
        if (existing.isOverlapOnUpperRange(candidate) ||
            existing.isAdjacentOnUpperRange(candidate)) {
          existing.setTo(candidate.getTo()); // extend existing range to include candidates range
          result = true;
        }
      }
      
      if (result) {  // we have extended, no need to check further
        break;
      }
    }
    return result;
  }

}
