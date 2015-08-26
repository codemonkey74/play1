//*
// * Definition for a binary tree node.
struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode(int x) : val(x), left(0), right(0) {}
};
// */


#include <vector>
using namespace std;
class Solution {
public:
    int _appendRight(TreeNode* node, int ch, int mh, std::vector<int>* v) {
        if (!node) return mh;
        if (ch > mh) {
            v->push_back(node->val);
            ++mh;
        }
        mh = _appendRight(node->right, ch + 1, mh, v);
        mh = _appendRight(node->left, ch + 1, mh, v);
        return mh;
    }
    vector<int> rightSideView(TreeNode* root) {
        std::vector<int> result;
        int size = this->_appendRight(root, 0, -1, &result);
        return result;
    }
};
