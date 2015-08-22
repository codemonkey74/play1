#include <vector>
class Solution {
public:
    void merge(std::vector<int>& nums1, int m, std::vector<int>& nums2, int n) {
        if ( n == 0 ) {
            return;
        }
        if (nums1.capacity() < m + n) {
            nums1.reserve(m+n);
        }
        size_t pos = n + m;
        for(;pos > 0; --pos) {
            if (m == 0) {
                nums1[pos-1] = nums2[--n];
                continue;
            }
            if (n == 0) {
                break;
            }
            bool awins = nums1[m-1] > nums2[n-1];
            if (awins) {
                nums1[pos-1] = nums1[--m];
            } else {
                nums1[pos-1] = nums2[--n];
            }
        }
        
    }
};
