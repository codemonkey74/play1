
  struct TreeNode {
      int val;
      TreeNode *left;
      TreeNode *right;
      TreeNode(int x) : val(x), left(0), right(0) {}
  };
class Solution {
public:
    inline int minf(int a, int b) { return (a == 0 ? b : (a < b ? a : b)); }
    int minDepth(TreeNode* root, int depth, int min) {
        if (min == minf(min, depth)) { return min; }
        TreeNode* left = root->left;
        TreeNode* right = root->right;
        int minL = 0, minR = 0;
        if (!left && !right) {
            min = minf(min, depth);
        } else {
            if (left) {
                min = minf(min, minDepth(root->left, depth + 1, min));
            }
            if (right) {
                min = minf(min, minDepth(root->right, depth + 1, min));
            }
        }
        return min;
    }
    
    int minDepth(TreeNode* root) {
        if (!root) return 0;
        return minDepth(root, 1, 0);
    }
};
