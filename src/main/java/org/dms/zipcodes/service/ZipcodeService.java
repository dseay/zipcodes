package org.dms.zipcodes.service;

import java.util.List;

import org.dms.zipcodes.model.ZipcodeRange;

/**
 * Provides zipcode related services
 */
public interface ZipcodeService {

  /**
   * Will take any list of zipcode ranges and produce the minimal set of ranges
   * 
   * @param ranges list zipcode ranges
   * @return minimal set of zipcode ranges
   */
  public List<ZipcodeRange> computeMinimumRanges(List<ZipcodeRange> ranges);
  
  /**
   * Convenience method
   * 
   * @see ZipcodeService#computeMinimumRanges(List)
   */
  public List<ZipcodeRange> computeMinimumRanges(ZipcodeRange... ranges);
  
}
