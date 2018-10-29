#include <iostream>
#include <stack>
#include <unordered_map>

int main(int argc, const char** argv) {
  std::unordered_map<char, char> closes = {{')','('},{']', '['},{'}', '{'}};
  while(argc-- > 0) {
    std::stack<char> counter;
    if (argv[argc][0] == '\0') continue;

    counter.emplace(argv[argc][0]);
    for(size_t idx=1; argv[argc][idx] != '\0'; ++idx) {
      char current = argv[argc][idx];
      auto close = closes.find(current);
      if (close != closes.end()) {
        counter.push(current);
      } else {
        if (close->second != counter.top()) {
          return 1;
        }
        counter.pop();
      }
      std::cout << counter.size() << std::endl;
    }
    if (counter.size() != 0) return 2;
  }
  return 0;
}
