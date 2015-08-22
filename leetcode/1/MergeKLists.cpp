struct ListNode {
  int val;
  ListNode *next;
  ListNode(int x) : val(x), next(0) {}
};

#include <iostream>
#include <vector>
#include <map>
#include <algorithm>
class Solution {
    typedef std::vector<ListNode*>::iterator vit;
    typedef std::multimap<int, ListNode*> Mln;
    typedef std::pair<int, ListNode*> Mlnpair;
    typedef Mln::iterator mlnit;

    inline ListNode* advance(Mln* & lists, mlnit& l) {
        ListNode* x = (*l).second;
        lists->erase(l);
        if (x) { 
            ListNode* lnp = x->next; 
            if (lnp != 0) {
                lists->insert(Mlnpair(lnp->val, lnp));
            }
        }
        return x;
    }
    inline ListNode* win(Mln* & lists) {
        mlnit list = getMin(lists);
        if (list != lists->end()) {
            return advance(lists, list);
        }
        return 0;
    }
    inline mlnit getMin(Mln* & lists) {
        return lists->begin();
    }
    struct ListNodeSort {
        bool operator() (ListNode* & ln1, ListNode* & ln2) {
            if (ln2 == 0) return true;
            if (ln1 == 0) return false;
            return (ln1->val <= ln2->val);
        }
    };
    
    Mln* bucketSort(std::vector<ListNode*>& lists) {
        Mln* hist = new Mln();
        mlnit mit;
        
        std::sort(lists.begin(), lists.end());
        for(vit t1=lists.begin(); t1 != lists.end(); ++t1) {
            if (*t1 == 0) continue;
            hist->insert(Mlnpair((*t1)->val,  *t1)); 
        }
        return hist;
    }

    inline Mln* prepare(std::vector<ListNode*>& lists) {
        ListNodeSort lns;
        return bucketSort(lists);
    }
public:
    ListNode* mergeKLists(std::vector<ListNode*>& lists) {
        Mln* l = prepare(lists);
        ListNode* root = win(l);
        ListNode* c = root;
        while(! l->empty()) {
            ListNode* next = win(l);
            if (c != 0) { 
                c->next = next;
            }
            c = next; 
        }

        if (c) {
            c->next = 0;
        }

        delete(l);
        return root;
    }

    void print(ListNode* result) {
        while (result != 0) {
            std::cout << result->val << ",";
            result = result->next;
        }
        std::cout << "end" << std::endl;
    }
};

#include <sstream>
#include <string>
#include <fstream>
int readFromFile(std::vector<ListNode*>& lists, const std::string& filename) {
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
        ListNode *c = 0;
        ListNode **head = &c;
        while (std::getline(iss, numberstr, ',')) {
            //std::cout << "reading number" << std::endl;
            ListNode* x = 0;
            try {
                x = new ListNode(std::stoi(numberstr));
                ++countn;
            } catch (std::exception& e) {
                //std::cout << "cant read number " << numberstr << std::endl;
            }
            if (c != 0) { 
                //std::cout << "creating chain" << std::endl; 
                c->next = x;
            }
            c = x;
        }
        //std::cout << "New list added with " << (*head ? (*head)->val : -1) << std::endl;
        lists.push_back(*head);
    }
    std::cout << "Got " << countn << " numbers from " << linen << " lines" << std::endl;
    return countn;
}

int main() {
    std::vector<ListNode*> lists;
    readFromFile(lists, "klist.txt");
    ListNode n1(1), n2(2), n1a(1), n3(3), n4(4), n5(5), n6(6),  *result;
    n1.next = &n3;
    n2.next = &n4;
    n4.next = &n6;
    //lists.push_back(&n1);
    //lists.push_back(&n2);
    //lists.push_back(&n5);

    std::cout << "Calling solution" << std::endl;
    Solution s;
    result = s.mergeKLists(lists);
    s.print(result);
    return 0;
}
