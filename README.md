# Zipcodes Service

> Given arbitrary set of possibly overlapping zipcode ranges computes the minimum set of ranges.

Features:
- Ranges may be provided in arbitrary order
- Ranges may or may not overlap

Examples:

Input = [94133,94133] [94200,94299] [94600,94699]
Output = [94133,94133] [94200,94299] [94600,94699]


Input [94133,94133] [94200,94299] [94226,94399] 
Output [94133,94133] [94200,94399]

