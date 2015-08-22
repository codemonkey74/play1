#define NULL 0
 struct TreeNode {
      int val;
      TreeNode *left;
      TreeNode *right;
      TreeNode(int x) : val(x), left(NULL), right(NULL) {}
  };
 
class Solution {
private:
    int sumBranch(TreeNode* root, int accum = 0) {
        if (root == NULL) {
            return 0;
        }
        accum = accum * 10;
        if (NULL == root->left && NULL == root->right) {
           return accum + root->val;
        }
        int childsum = 0;
        if (root->left != NULL) {
           childsum = sumBranch(root->left, accum + root->val); 
        }
        if (root->right != NULL) {
           childsum = sumBranch(root->right, accum  + root->val);
        }
        return left + right;
    }
public:
    int sumNumbers(TreeNode* root) {
        return sumBranch(root);
    }
};

#include <iostream>
int main() {
    TreeNode t0(0), t1(1), t2(2), t3(3), t4(4);
    t0.left = &t1; 
    t1.left = &t2;
    t1.right = &t3;
    t2.left = &t4;

    Solution s;
    std::cout << s.sumNumbers(&t0) << std::endl; 
    return 0;
}
