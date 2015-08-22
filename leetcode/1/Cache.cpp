#include <unordered_map>

class LRUCache{
private:
    struct Node {
        Node(): next(0), previous(0) {}
        Node(int k, int v): key(k), value(v), next(0), previous(0) {}
        int value;
        int key;
        Node* next;
        Node* previous;
    };
    typedef std::unordered_map<int, Node*> mymap;
    mymap m;
    int maxsize;
    Node* head;
    Node* tail;
    
    inline void createNewHead(Node* n) {
        head = n;
        tail = head; 
    }
    
    void insertathead(Node* n) {
        n->previous = 0;
        n->next = head;
        if (head == 0) {
            createNewHead(n);
        } else {
            head->previous = n;
            head = n;
        }
    }
    void setused(Node* n) {
        if (n == 0 || n == head) {
            return;
        }
        if (head == 0) {
            createNewHead(n);
        } else {
            removefromlist(n);
            insertathead(n);
        }
    }

    Node* removefromlist(Node* n) {
        //remove from list
        Node* previous = n->previous;
        Node* next = n->next;
        if (previous != 0) {
            previous->next = next;
        }
        if (next != 0) {
            next->previous = previous;
        }
        if (n == head) {
            head = head->next;
        }
        if (n == tail) {
            tail = tail->previous;
        }
        return n;
    }    
    void invalidate() {
        Node* n = removefromlist(tail);
        if (n != 0) {
            m.erase(n->key);
            delete n;
        }
    }
    void insertelement(int key, int value) {
        mymap::iterator it = m.find(key);
        if (it == m.end()) {
            Node* n = new Node(key, value);
            insertathead(n);
            m.insert(std::pair<int, Node*>(key, n));
            if (m.size() > maxsize) {
                invalidate();
            }
        } else {
            it->second->value = value;
            setused(it->second);
        }
    }
public:
    LRUCache(int capacity): m(), maxsize(capacity), head(0), tail(0) {
        
    }
    
    int get(int key) {
        mymap::iterator it = m.find(key);
        if (it == m.end()) { 
            return -1;
        }
        setused(it->second);
        return it->second->value;
     }
    
    void set(int key, int value) {
        insertelement(key, value);
    }
};

#include <cassert>
int main() {
    LRUCache l(2);
    assert(-1 == l.get(2));
    l.set(2,6);
    assert(-1 == l.get(1));
    l.set(1,5);
    l.set(1,2);
    assert(2 == l.get(1));
    assert(6 == l.get(2));

    LRUCache m(1);
    m.set(2,1);
    assert(1 == m.get(2));
    m.set(3,2);
    assert(-1 == m.get(2));
    assert(2 == m.get(3));
    
    return 0;
}
