#include <list>
struct range { int start; int end; range(int s, int e): start(s), end(e){} };

inline bool matches(range r, int n) {
    return n >= r.start && n <= r.end; 
}
bool matches(range r1, range r2) {
    return matches(r1, r2.start) || matches(r1, r2.end) || matches(r2, r1.start) || matches(r2, r1.end);
}

typedef std::list<range> Slr;
void merge(std::list<range>& lor, range r) {
    Slr::iterator it1=lor.begin();
    Slr::iterator it2, aux;
    if (lor.empty()) {
        lor.push_back(r);
        return;
    }
    if (r.end < (lor.front()).start) {
        lor.push_front(r);
        return;
    }
    while(it1 != lor.end() && !(matches((*it1), r))) {
        ++it1;
    }
    it2 = it1;
    aux = it2; if (aux != lor.end()) { aux++; }
    while((aux) != lor.end() && (matches((*(aux)), r))) {
        ++it2;
        ++aux;
    }
    r.start = std::min(r.start, (it1 != lor.end() ? (*it1).start : r.start));
    r.end = std::max(r.end, (it2 != lor.end()) ? (*it2).end : r.end); 
    if (it1 == lor.end()) {
        lor.push_back(r);
    } else {
        (*it1) = r;
        lor.erase(++it1, ++it2); //remove range.
    }
}

#include <iostream>
int main() {
    std::list<range> lr;
    lr.push_back(range(2,3));
    lr.push_back(range(4,5));
    lr.push_back(range(6,11));
    merge(lr, range(1,10));
    for(Slr::iterator it=lr.begin(); it != lr.end(); ++it) {
        std::cout << "(" << (*it).start << ":" << (*it).end << "),";
    }
    std::cout << std::endl;
    return 0;
}
