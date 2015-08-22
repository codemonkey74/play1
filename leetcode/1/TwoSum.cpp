#include <algorithm>
#include <vector>
class Solution {
    typedef std::vector<int>::iterator vit;
public:
    std::vector<int> twoSum(std::vector<int>& nums, int target) {
        std::vector<int> a;
        std::vector<int> b = nums;
        sort(b.begin(),b.end());
        for (vit i = b.begin(); i != b.end(); i++) {
            if ((target >= 0 && *i > target) || (target < 0 && *i < target)) {
                break;
            }
            for (vit j = i + 1; j != b.end(); j++) {
                if (*i + *j == target) {
                    vit iori = find(nums.begin(), nums.end(), *i);
                    vit jori = find(nums.begin(), nums.end(), *j);
                    if (jori == iori) {
                        jori = find(jori + 1, nums.end(), *j);
                    }
                    a.push_back(iori - nums.begin() + 1);
                    a.push_back(jori - nums.begin() + 1);
                    sort(a.begin(), a.end());
                    return a;
                }
                if (*i + *j > target) {
                    break;
                }
            }
        }
        sort(a.begin(), a.end());
        return a;
    }
};
