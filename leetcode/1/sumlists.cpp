  struct ListNode {
      int val;
      ListNode *next;
      ListNode(int x) : val(x), next(0) {}
  };
 
#include <iostream>
class Solution {
public:
    inline void attach(ListNode*& result, ListNode* n) {
        if (result == 0) {
            result = n;
        } else {
            result->next = n;
        }
    }
    void add (ListNode* l1, ListNode* l2, ListNode*& result, int carry = 0) {
        int sum = carry;
        bool hasdigit = false;
        if (l1 != 0) { sum += l1->val; l1 = l1->next; }
        if (l2 != 0) { sum += l2->val; l2 = l2->next; }
        attach(result, new ListNode(sum % 10));
        print(result);
        if (l1 != 0 || l2 != 0 || (sum / 10 > 0)) {
            add(l1, l2, result->next, sum / 10);
        }
    }
    ListNode* addTwoNumbers(ListNode* l1, ListNode* l2) {
        ListNode* r = 0;
        add(l1, l2, r);
        return r;
    }
    void print (ListNode* result) {
        while (result != 0) { std::cout << result->val; result = result->next; }
        std::cout << std::endl;
        
    }
};

ListNode*& a(ListNode*& result, int n) {
    static Solution s;
    s.attach(result, new ListNode(n));
    return result;
}
int main() {
    Solution s;
    ListNode* one = 0;  a(one,1);    
    ListNode* nn = 0;   a(a(nn, 9), 9);
    s.print(one);
    s.print(nn);
    ListNode* result = s.addTwoNumbers(one, nn);
    s.print(result);
    return 0;
}
