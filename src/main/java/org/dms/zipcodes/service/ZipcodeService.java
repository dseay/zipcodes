package org.dms.zipcodes.service;

import java.util.List;

import org.dms.zipcodes.model.ZipcodeRange;

public interface ZipcodeService {

  public List<ZipcodeRange> computeMinimumRanges(List<ZipcodeRange> ranges);
  
  public List<ZipcodeRange> computeMinimumRanges(ZipcodeRange... ranges);
  
}
