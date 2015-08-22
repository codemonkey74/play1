/*
 * Given n light bulbs, write two methods. 
 *
 * isOn(i) to determine if a light bulb is on 
 * toggle(start, end) to toggle all the light bulbs in the range 
 *
 * One caveat, write toggle so that it is less than O(n) complexity
 */

#include <vector>
#include <iostream>
#include <sstream>
#include <bitset>
class LightBulbs {
    private:
        using ull = unsigned long long;
        using vecull = std::vector<ull>;
        
        vecull mybulbs;
        size_t mybulbssize;
        constexpr static size_t ullsize = sizeof(ull) * 8;
    public:
        LightBulbs(size_t size) : mybulbs((size / ullsize) + 1, 0), mybulbssize(size) {}
        virtual bool isOn(size_t i) const {
            if (i > mybulbssize) { return false; } //could also throw
            ull el = mybulbs[i / ullsize]; 
            ull mask = (1LL << (i % ullsize));
            return (el & mask) != 0LL;
        }
        virtual void toggle(int start, int end) {
            if (start > end) return; 
            if (start < 0) return;
            if (end > mybulbssize) return;
            size_t startidx = start / ullsize;
            size_t endidx = end / ullsize;
            
            for (size_t idx = startidx; idx <= endidx; ++idx) {
                size_t startbit = start % ullsize;
                size_t endbit = end % ullsize;
                ull mask = (ull) -1LL; 
                if (idx == startidx) {
                    ull startmask = (1LL << (startbit)) - 1LL;
                    mask ^= startmask;
                }
                if (idx == endidx) {
                    ull endmask = ((((ull)1LL) << (endbit + 1)) - 1LL);
                    if (endbit + 1 == ullsize) { endmask = -1LL; }
                    mask &= endmask;
                }
                mybulbs[idx] ^= mask;
            }
        }
        friend std::ostream& operator<<(std::ostream& out, const LightBulbs& it) {
            out << "sz(" << it.mybulbssize  << ") -> "; 
            for(const auto& i : it.mybulbs) {
                out << std::bitset<ullsize>(i) << ",";
            }
            out  << std::endl;
            return out;
        }
        virtual std::string x() const { return "one"; }
};

class LightBulbsNZB : public LightBulbs {
    public:
        LightBulbsNZB(size_t size): LightBulbs(size) {}
        bool isOn(size_t i) const {
            if (i == 0) { return false; }
            return LightBulbs::isOn(i - 1);
        }

        void toggle(int start, int end) {
            if (start >= 1 && end >= 1) 
                LightBulbs::toggle(start - 1, end - 1);
        }
        virtual std::string x() const { return "two"; }
};

#ifndef NDEBUG
#   define ASSERT(condition, message) \
        do { \
                    if (! (condition)) { \
                                    std::cerr << "Assertion `" #condition "` failed in " << __FILE__ \
                                              << " line " << __LINE__ << ": " << message << std::endl; \
                                    std::exit(EXIT_FAILURE); \
                                } \
                } while (false)
#else
#   define ASSERT(condition, message) do { } while (false)
#endif

#include <sstream>
template<typename T>
inline void test(const T & lb, const size_t& from = 0, const size_t& to = 0, bool shouldbe = true) {
    for(size_t i = from; i <= to; ++i) {
        std::ostringstream s("");
        s << "f: " << from << ", t: " << to << "," << (shouldbe ? "true" : "false" ) << ", idx: " << i <<  ", me: " << lb.x();
        ASSERT(shouldbe == lb.isOn(i), s.str());
    }
}

template<typename T>
void createTest(T & lb, const size_t& from = 0, const size_t& to = 0, const size_t size = 0) {
    static size_t testnumber = 0;
    std::cout << " =============== " << " Test " << lb.x() <<  " # " << ++testnumber << " ============ " << std::endl;
    test(lb, 0, size, false);
    lb.toggle(from, to);
    std::cout << "toggle (" << from << "," << to << ") ";
    //std::cout << lb;
    std::cout << std::endl;
    if (from > 0) { test(lb, 0, from - 1, false); }
    test(lb, from, to, true);

    const size_t avg = (from + to) / 2;
    lb.toggle(avg, to);
    std::cout << "toggle (" << avg << "," << to << ") ";
    //std::cout <<  lb;
    std::cout << std::endl;
    if (from > 0) { test(lb, 0, from - 1, false); }
    test(lb, from, avg - 1, true);
    test(lb, avg, to, false);
}

int main() {
    {
        const size_t size = 20;
        LightBulbs lb(size);
        createTest(lb, (size_t) 0, (size_t) 8, size);
    }
    {
        const size_t size = 20;
        LightBulbsNZB lb(size);
        createTest(lb, (size_t) 1, (size_t) 8, size);
    }

    {
        const size_t size = 100;
        LightBulbs lb(size);
        createTest(lb, (size_t) 63, (size_t) 67, size);
    }

    {
        const size_t size = 1000;
        LightBulbs lb(size);
        createTest(lb, (size_t) 191, (size_t) 512, size);
    }
    {
        const size_t size = 1023;
        LightBulbs lb(size);
        createTest(lb, (size_t) 191, (size_t) 1023, size);
    }
    {
        const size_t size = 1024;
        LightBulbsNZB lb(size);
        createTest(lb, (size_t) 191, (size_t) 1023, size);
    }
    return 0;
}
