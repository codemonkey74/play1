// Min Heap implementation

#include <iostream>
#include <sstream>
#include <array>
template<typename T, const size_t B>
class MinHeap {
        std::array<T, B> heap;
        const size_t heap_size;
        inline void swapit(size_t idx1, size_t idx2) {
            if (idx2 < heap_size) {
                if (heap[idx2] < heap[idx1]) {
                    std::swap(heap[idx1], heap[idx2]);
                    Heapfy(idx2);
                }
            }
        }

        void Heapfy(size_t idx) {
            size_t left  = getLeft(idx);
            size_t right = getRight(idx);
            swapit(idx, left);
            //std::cout << "s" << left << "," << right << " : " << idx << "   " << (*this) << std::endl;
            swapit(idx, right);
            //std::cout << "s" << left << "," << right << " : " << idx << "   " << (*this) << std::endl;
        }
    public:
        MinHeap(): heap(), heap_size(B) {} 

        void doHeapfy() {
            if (heap_size <= 1) return;
            for(size_t i=(heap_size - 1)/2; i > 0; --i) {
                Heapfy(i);
            }
            Heapfy(0);
        }

        inline size_t getParent(size_t idx) { if (idx == 0) return 0; return (idx - 1) / 2; }
        inline size_t getLeft(size_t idx) { return (idx * 2) + 1; }
        inline size_t getRight(size_t idx) { return (idx * 2) + 2; }

        friend std::ostream& operator<<(std::ostream& out, const MinHeap& mh) {
            out << "[";
            for (const auto& i: mh.heap) {
                out << i << ",";
            }
            out << "]";
            return out;
        }

        inline void ini(const size_t idx, const T& value) {
            heap[idx] = value;
        }

        void set(const size_t idx, const T& value) {
            ini(idx, value);
            Heapfy(idx);
        }
};

int main() {
    MinHeap<int, 5> a;
    for (auto i=0; i< 5;++i){ a.ini(i,4-i); }

    std::cout << a << std::endl;
    a.doHeapfy();
    std::cout << a << std::endl;
    a.set(0, 5);
    std::cout << a << std::endl;
    a.set(0, 3);
    std::cout << a << std::endl;
    a.set(1, 7);
    std::cout << a << std::endl;


    return 0;
}
