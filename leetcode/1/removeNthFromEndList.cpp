  struct ListNode {
      int val;
      ListNode *next;
      ListNode(int x) : val(x), next(0) {}
  };
class Solution {
public:
    ListNode* removeNthFromEnd(ListNode* head, int n) {
        ListNode* back = head;
        ListNode* front = head;
        if (!head || n < 1) return head;
        for (; n > 0 && front; --n) {
            front = front->next;
        }
        if (n > 0) return 0;
        if (front == 0) {
            return head->next;
        }
        while (front->next != 0) {
            front = front->next;
            back = back->next;
        }
        if (back->next) {
            back->next = back->next->next;
        }
        
        return head;
    }
};
