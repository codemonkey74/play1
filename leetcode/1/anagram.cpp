#include <unordered_map>
#include <string>
#include <list>
#include <iostream>
#include <sstream>
#include <exception>
template<typename K, typename T, class Hash = std::hash<K>, class Pred = std::equal_to<K> >
class Cache { 
    using prioritylist = typename std::list<K>;
    using plit = typename prioritylist::iterator;
    using maptolist = typename std::pair<T, plit>;
    using mapstorage = typename std::unordered_map<K, maptolist, Hash, Pred>;
    using msit = typename mapstorage::iterator;

    mapstorage mapstore;
    prioritylist prioritystore;

    size_t capacity;
protected:
    void checkCapacity() {
        if (prioritystore.size() >= capacity - 1) {
            K removek = prioritystore.back();
            prioritystore.pop_back();
            mapstore.erase(removek);
        }
    }
    void resetPriority(plit& psi, const K& k) {
        if(psi != prioritystore.end()) {
            prioritystore.erase(psi);
        }
        checkCapacity();
        prioritystore.push_front(k);
        psi = prioritystore.begin();
    }
public:
    Cache(size_t capacity): mapstore(capacity), prioritystore(), capacity(capacity) {}

    
    std::pair<bool, T> getOrElse(const K& k, const T& def = T()) {
        msit el = mapstore.find(k);
        plit psi = prioritystore.end();
        if (el == mapstore.end()) {
            std::cout << "Cache miss: " << k << std::endl;
            std::pair<bool, T> res = std::make_pair(false, def);
            return res;
        }
        std::cout << "Cache hit: " << k << std::endl;
        resetPriority(psi, k);
        el->second.second = psi;
        std::pair<bool, T> ret = std::make_pair(true, el->second.first);
        return ret;
    }

    T set(const K& k, T t) {
        msit el = mapstore.find(k);
        plit psi = prioritystore.end();
        if (el != mapstore.end()) {
            psi = el->second.second;
            el->second.first = t;
        } else {
            maptolist val = std::make_pair(t, psi);
            std::pair<K, maptolist> mappair = std::make_pair(k, val);
            el = mapstore.insert(mappair).first;
        }
        resetPriority(psi, k);
        el->second.second = psi;
        return t;
    }
};

struct TwoStrings {
    std::string a;
    std::string b; 
    TwoStrings(const std::string& one = "", const std::string& two = "") : a(one), b(two) {}
    bool operator==(const TwoStrings& two) const {
        return (this->a == two.a && this->b == two.b);
    }
    friend std::ostream& operator<<(std::ostream& out, const TwoStrings& k) {
        out << "[" << k.a << ", " << k.b << "]";
        return out;
    }
};

struct TwoStringsOps {
    size_t operator() (const TwoStrings& ts) const {
        return std::hash<std::string>()(ts.a) + std::hash<std::string>()(ts.b);
    } 
};
struct StringsOps {
    size_t operator() (const std::string& ts) const {
        return std::hash<std::string>()(ts);
    } 
};

std::unordered_map<char, size_t> buildHistogram(const std::string& a) {
    static std::unordered_map<char, size_t> def(0);
    static auto cache = Cache<const std::string, std::unordered_map<char, size_t>, StringsOps >(6);
    auto res = cache.getOrElse(a, def);
    if (res.first) { return res.second; }

    std::unordered_map<char, size_t> histogram;
    for(const auto& i: a) {
        histogram[i]++;
    }
    return cache.set(a, histogram);
}

bool isanagram(const std::string& a, const std::string& b) {
    if (a.length() != b.length()) { return false; }
    auto histogram = buildHistogram(a);
    for(const auto& i: b) {
        if (histogram[i] == 0) {
            return false;
        }
        histogram[i]--;
    }
    //histogram only contains 0.
    return true;
}

bool isanagramSVC(const std::string& a, const std::string& b) {
    static auto cache = Cache<TwoStrings, bool, TwoStringsOps>(3);

    auto key = TwoStrings(a,b);
    auto res = cache.getOrElse(key, false);
    if (res.first) { return res.second; }
    return cache.set(key, isanagram(a, b));
}

#include <cassert>
inline void test(std::string a, std::string b, bool result = true) {
    assert(result == isanagramSVC(a, b));
}

int main() {
    test("a", "a", true);
    test("a", "a", true); //cached
    test("a", "b", false);
    test("a", "b", false); //cached
    test("ab", "ab", true);
    test("ab", "ab", true); //cached
    test("aa", "aa", true);
    test("aa", "aa", true); //cached
    test("a", "a", true); //uncached
    test("ab", "ba", true);
    test("abc", "ba", false);
    test("abc", "bac", true);
    test("aaabbbccc", "abcabcabc", true);
    test("aaabbbccc", "aaabbccbc", true); //cache
    test("aaaaaaaaa", "aaaaaaaab", false);
    test("", "", true);
    test("", "a", false);
    return 0;
}
