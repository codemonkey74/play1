#include <iostream>
#include <vector>
#include <exception>
struct badcode: public std::exception {
    const std::string mm;
    badcode(const char* m) : mm(m) {} 
    virtual const char* what() const _NOEXCEPT {
        return mm.c_str();
    }
};
class Solution {
public:
    size_t cwl,  cred, cblue;
    bool ir, iw, ib;
    bool swap(std::vector<int>& nums, size_t idx1, size_t idx2) {
        if (idx1 == idx2) return false;
        std::cout << "Changing pos " << idx1 << " and " << idx2 << std::endl;
        int aux = nums[idx1];
        nums[idx1] = nums[idx2];
        nums[idx2] = aux;
        print(nums, 0, "swap");
        return false;
    }

    inline size_t ini(bool i) { return i ? 0 : 1; }
    size_t nextcw(std::vector<int>& nums, size_t i) {
        print(nums, i, "white");
        if (!iw) {
            print(nums, i, "iw");
            iw = true;
            if (!ib) { cwl = nums.size() - 1; }
            else { cwl = cblue - 1; }
            return cwl;
        }
        //if (cwl == 0) throw new badcode("white underun");
        return --cwl;
    }

    size_t nextblue(std::vector<int>& nums, size_t i) {
        print(nums, i, "blue");
        if (!ib) { 
            print(nums, i, "nb");
            ib = true;
            cblue = nums.size() - 1;
            if (!iw) { return cblue; }
        } else {
          //  if (cblue == 0) throw badcode("blue underun");
            --cblue;
        }
        if (iw && cwl == cblue) { 
            cwl = nextcw(nums, cwl);
        }
        return cblue;
    }

    size_t nextred(std::vector<int>& nums, size_t i) {
        print(nums, i, "red");
        if (!ir) {
            print(nums, i, "nr");
            ir = true;
            return cred;
        }
        //if (cred == nums.size() - 1) throw badcode("red overrun");
        return ++cred;
    }
    
    void sortColors(std::vector<int>& nums) {
       ir=ib=iw=false;
       cred = 0;
       cblue = nums.size() - 1;
       cwl = cblue;
       for (size_t i = 0; i < nums.size();) {
           print(nums, i, "i");
           if (nums[i] == 0) { if (ir && i <= cred) { ++i; continue; } else { swap(nums, i, nextred(nums, i)); }}
           print(nums, i, "r");
           if (nums[i] == 1) { if (iw && i >= cwl && (!ib || i < cblue)) { ++i; continue; } else { swap(nums, i, nextcw(nums, i)); }}
           print(nums, i, "w");
           if (nums[i] == 2) { if (ib && i >= cblue) { ++i; continue; } else { swap(nums, i, nextblue(nums, i)); }}
           print(nums, i, "b");
       }
       std::cout << "sort done" << std::endl;
    }
    void print(std::vector<int>& nums, size_t i, std::string s = "" ) {
        return;
        std::cout << s <<  i << "(" << cred << "," << cwl << "," <<  cblue << ") "; 
        for (size_t j=0; j<nums.size(); j++) {
            std::cout << nums[j] << ",";
        }
        std::cout << std::endl;
    }
};

#include <sstream>
#include <string>
#include <fstream>
#include <list>
#include <vector>
#include <stdexcept>
typedef std::list<std::vector<int> > Lvi;

int readFromFile(Lvi& lists, const std::string& filename) {
    std::string line;
    std::ifstream infile(filename);
    std::cout << "reading file " << filename << std::endl;
    int linen = 0;
    int countn = 0;
    while (std::getline(infile, line))
    {
        ++linen;
        //std::cout << "reading line" << linen << std::endl;
        std::istringstream iss(line);
        std::string numberstr;
        std::vector<int> v;
        while (std::getline(iss, numberstr, ',')) {
            //std::cout << "reading number" << std::endl;
            int x = 0;
            try {
                x = std::stoi(numberstr);
                if (x < 0 || x > 2) { throw new std::out_of_range("Invalid number"); }
                ++countn;
            } catch (std::exception& e) {
                continue;
            }
            v.push_back(x);
        }
        //std::cout << "New list size " << v.size() << std::endl;
        lists.push_back(v);
    }
    std::cout << "Got " << countn << " numbers from " << linen << " lines" << std::endl;
    return countn;
}
int main() {
    //int myints[] = {1};
    //std::vector<int> v (myints, myints + sizeof(myints) / sizeof(int) );
    Lvi lists;
    readFromFile(lists, "rwb.txt");

    int x=1;
    for(Lvi::iterator it=lists.begin(); it != lists.end(); ++it, ++x) {
        Solution* s = new Solution();
        std::cout << "Running line " << x << " size " << (*it).size() << std::endl;
        try {
            s->sortColors(*it);
        } catch (badcode& e) {
            std::cout << "error " << e.what() << std::endl;
        }
        delete s;

        std::cout << "Checking ";
        int last=0;
        for (std::vector<int>::iterator vit=(*it).begin(); vit != (*it).end(); ++vit) {
            if(*vit == last) { continue; }
            if(*vit > last) {last = *vit; continue;}
            std::cout << "error " << (*vit) << " found after " << last << std::endl; 
            exit(0);
        }
        std::cout << " ------ END RUN ---- " << std::endl;

    }
    return 0;
}

