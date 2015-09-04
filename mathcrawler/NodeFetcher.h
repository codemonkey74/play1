#ifndef NodeFetcher__
#define NodeFetcher__

#include "Parser.h"
#include "Retriever.h"

#include <memory>
#include <unordered_map>
#include <string>

template <typename T>
class Node {
public:
    T v; 
    int visited;
    int references;
    std::vector<std::shared_ptr<Node<T>>> adjacents;
    Node(const T& value) : v(value), visited(0), references(0), adjacents(0) {}
    void addChild(std::shared_ptr<Node<T>> n) {
        adjacents.push_back(n);
    }
};

template <typename T>
class NodeFetcher {
    using allNodesT = std::unordered_set<T>;
    allNodesT allNodes; 
    std::shared_ptr<Retriever> retriever;
    Parser parser;
    std::vector<unsigned long> largestPath;
public:
    NodeFetcher(std::shared_ptr<Retriever> r, Parser& p) : retriever(r), parser(p), allNodes(0), largestPath(0) {}
    void buildGraph(const T& headvalue) {
        if (allNodes.count(headvalue) == 1) {
            //this node was seen already.
            //  increment cycles.
            //  return 
        } else {
            //new node.
            //  increment counter
            //  increment level
            //  if (level >= smaller) drop queue.
            allNodes.insert(headvalue);
        } 
        std::string childrenraw = retriever->fetchContent(headvalue);
        if (childrenraw == "GOAL") {
            //found the goal.
            //  Save as goal for the answer (headvalue)
            //  Calculate smaller path
        }
        if (childrenraw == "DEADEND") {
            //found dead end.
            //  noop.  
        }
        //recurse for all children.
        std::vector<unsigned long> children = parser.getResults(childrenraw);
        
    }
};

#endif
