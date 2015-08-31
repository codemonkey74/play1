#include <iostream>
#include <array>
template <size_t B>
int maxSubSum(const std::array<int, B>& a) {
    if (a.size() < 0) { throw std::invalid_argument("can not get a max of empty list"); }
    int maxsum = a[0];
    int sum = maxsum;
    for (int i = 1; i < a.size(); ++i) {
        sum = (maxsum < 0) ? a[i] : sum + a[i];
        if (maxsum < sum) {
            maxsum = sum;
        } else if (sum < 0 && maxsum >= 0) {
            sum = 0;
        }
    }
    return maxsum;
} 

int main() {
    std::array<int, 7> a {{-10, -1 , -5, 14, -3, 4, -7}}; 
    std::cout << maxSubSum(a) << std::endl;
    return 0;
}
