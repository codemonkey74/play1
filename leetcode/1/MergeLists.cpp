  struct ListNode {
      int val;
      ListNode *next;
      ListNode(int x) : val(x), next(0) {}
  };
#include <iostream>
class Solution {
    inline ListNode* advance(ListNode* & l) {
        ListNode* x = l;
        if (l) { l = l->next; }
        return x;
    }
    inline ListNode* win(ListNode* & l1, ListNode* & l2) {
        if (l1 == 0) return advance(l2); //also both null;
        if (l2 == 0) return advance(l1); 
        if (l1->val <= l2->val) return advance(l1);
        return advance(l2);
    }
public:
    ListNode* mergeTwoLists(ListNode* l1, ListNode* l2) {
        ListNode* root = win(l1, l2);
        ListNode* c = root;
        while(l1 != 0 || l2 != 0) {
            c->next = win(l1, l2);
            c = c->next;
        }

        if (c) {
            c->next = 0;
        }
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

int main() {
    ListNode n1(1), n2(2), n3(3), n4(4),  *result;
    n1.next = &n2;
    n3.next = &n4;

    Solution s;
    result = s.mergeTwoLists(&n1, &n3);
    s.print(result);
    return 0;      
}
