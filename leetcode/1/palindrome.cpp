#include <iostream>
#include <cassert>
#include <algorithm>
#include <vector>
#include <set>
#include <map>

using namespace std;

bool isPalindrome(const std::string& phrase);
struct Pair {
  const std::string str1;
  const std::string str2;
  
  Pair(const std::string& s1, const std::string& s2): str1(s1), str2(s2) {}
};

std::vector<Pair> findPalindromes(std::vector<std::string> words);
std::vector<Pair> findPalindromes2(std::vector<std::string> words);

struct Test {
  int test = 0;

  void doTest(std::vector<string>& word, int expected) {
    test++;
    std::vector<Pair> result = findPalindromes2(word);
    if (result.size() != expected) {
      std::cout << "test " << test << " s:" << result.size() << "[";
      std::string sep = "";
      for (auto& r: result) {
        std::cout << sep << r.str1 << ":" << r.str2;
        sep = ",";
      }
      std::cout << "]" << std::endl;
      std::cout.flush();
      std::cerr << "test " << test << " failed." << std::endl;
    }
  }
};

// To execute C++, please define "int main()"
int main() {
 
  assert(isPalindrome("") == true);
  assert(isPalindrome("a") == true);
  assert(isPalindrome("aa") == true);

  Test t;

  { 
    std::vector<std::string> test(0); 
    t.doTest(test, 0); // test 1 s:0[]
  }
  {
    std::vector<std::string> test{"a"};
    t.doTest(test, 0); // test 2 s:0[]
  }
  { 
    std::vector<std::string> test{"a", "b"}; 
    t.doTest(test, 0); // test 3 s:0[]
  }
  { 
    std::vector<std::string> test{"ab", "ba"}; 
    t.doTest(test, 2); // test 4 s:2[ba:ab,ab:ba]
  }
  { 
    std::vector<std::string> test{"a", "ba"}; 
    t.doTest(test, 1); // test 5 s:1[a:ba]
  }
  { 
    std::vector<std::string> test{"bag", "gab"};
    t.doTest(test, 2); // test 6 s:2[gab:bag,bag:gab]
  }
  { 
    std::vector<std::string> test{"bag", "gab", "ga"}; 
    t.doTest(test, 3); // test 7 s:3[ga:bag,gab:bag,bag:gab]
  }
  { 
    std::vector<std::string> test{"bag", "gab", "ga", "g"}; 
    t.doTest(test, 4); // test 8 s:4[ga:g,ga:bag,gab:bag,bag:gab]
  }
  { 
    std::vector<std::string> test{"a", "ab", "abb", "abbb", "bbba", "bba", "ba", "b", "bb", "bbb"};  
    t.doTest(test, 30); // test 9 s:30[ab:a,b:ab,bb:b,b:bb,ba:ab,ba:b,a:ba,ab:ba,bba:bb,a:bba,ab:bba,abb:bba,abb:ba,abb:a,bb:abb,bba:abb,bbb:bb,b:bbb,bbb:b,bb:bbb,bbba:bbb,a:bbba,ab:bbba,abb:bbba,abbb:bbba,abbb:bba,abbb:ba,abbb:a,bbb:abbb,bbba:abbb]
  }
  {
    std::vector<std::string> test {"a", "a"};
    t.doTest(test, 2); // test 10 s:2[a:a,a:a]
  }
  {
    std::vector<std::string> test {"a", "ab", "b", "ba"};
    t.doTest(test, 6); // test 11 s:6[ab:a,b:ab,ba:ab,ba:b,a:ba,ab:ba]
  }

  std::cout << "done" << std::endl;
  return 0;
}


bool isPalindrome(const std::string& phrase) {
  int sz = phrase.length();
  for(int i = 0; i < sz / 2; i++) {
    if (phrase[i] != phrase[sz - i - 1]) {
      return false;
    }
  }
  return true;
}

// Write a function that takes an array of strings, and figure out all pairs of exactly two strings such taht if you concatenate them, it becomes a palindrome.
// Example: ["gab", "cat", "bag", "alpha"] => [["bag", "gab"], ["gab", "bag"]]
// its baggab vs gabbag two different output words.

/*reverse word
# for (i=1; sz)
#   if (remaining is palindrome)
#      if (suffix in hashtable)
#         add pair
# add to hash
# */

bool compStr(const std::string& str1, const std::string& str2) {
  return str1.length() < str2.length();
}

std::vector<Pair> findPalindromes(std::vector<std::string> words) {
  std::set<std::string> visited{};
  std::vector<Pair> result{};

  std::sort(words.begin(), words.end(), compStr);
  for (const std::string& theword: words) {
    std::string word(theword);

    std::reverse(word.begin(), word.end());

    std::string::iterator itr = word.begin();
    for (int i=0; i <= word.size(); i++) {
      itr = word.begin() + i;
      std::string revprefix(word.begin(), itr);
      std::string revsuffix(itr, word.end());
      if (revsuffix.length() > 0 && isPalindrome(revprefix)) {
        std::set<std::string>::iterator vit = visited.find(revsuffix);
        if (vit != visited.end()) {                        
          result.push_back(Pair(theword, *vit));
        }
      }
      if (revprefix.length() > 0 && isPalindrome(revsuffix)) {
        std::set<std::string>::iterator vit = visited.find(revprefix);
        if (vit != visited.end()) {                        
          result.push_back(Pair(*vit, theword));
        }
      }
    }
    visited.insert(theword);
  }
  return result;
}

std::vector<Pair> findPalindromes2(std::vector<std::string> words) {
  typedef std::map<std::string, std::set<string>> visitable;
  typedef std::map<std::string, std::set<string>>::iterator visitableit;
  typedef std::set<string>::iterator setit;

  visitable visited{};
  std::vector<Pair> result{};

  std::sort(words.begin(), words.end(), compStr);
  for (const std::string& theword: words) {
    std::string word(theword);

    std::reverse(word.begin(), word.end());

    std::string::iterator itr = word.begin();
    for (int i=0; i <= word.size(); i++) {
      visitableit vit;
      itr = word.begin() + i;
      std::string revprefix(word.begin(), itr);
      std::string revsuffix(itr, word.end());
      if (revsuffix.length() > 0 && isPalindrome(revprefix)) {
        vit = visited.find(revsuffix);
        if (vit != visited.end()) {
          for (setit vsit = vit->second.begin(); vsit != vit->second.end(); ++vsit) { 
            if (*vsit != theword) {
              result.push_back(Pair(*vsit, theword));
            }
          }
          vit->second.insert(theword);
        } else {
          visited.emplace(revsuffix, (std::set<string>){theword});
        }
      }
      if (revprefix.length() > 0 && isPalindrome(revsuffix)) {
        vit = visited.find(revprefix);
        if (vit != visited.end()) {
          for (setit vsit = vit->second.begin(); vsit != vit->second.end(); ++vsit) { 
            if (*vsit != theword) {
              result.push_back(Pair(theword, *vsit));
            }
          }
          vit->second.insert(theword);
        } else {
          visited.emplace(revprefix, (std::set<string>){theword});
        }
      }
    }
  }
  return result;
}

