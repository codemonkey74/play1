#include <iostream>


#include "curly.h"
#include "Parser.h"

#include "NodeFetcher.h"

#include <vector>
#include <exception>
#include <stdexcept>
#include <string>

int main() {
    std::string initial = "abs(add(add(157,multiply(46793,136)),add(multiply(-1,abs(29962)),12)))"; 
    std::string myurl = "http://www.crunchyroll.com/tech-challenge/roaming-math/macsf74@gmail.com/";
    Parser p;
    std::string head;
    try {
        Parser::resulttype r = p.getResults(initial);
        std::cout << r.first << std::endl;
        for(const auto& l: r.second) {
            std::cout << l << ", ";
            head = std::to_string(l);
        }
        std::cout << std::endl;
    } catch (const std::exception& e) {
        std::cerr << e.what() << std::endl;
        return 1;
    }
    return 3;
    try {
        Curly curly(myurl);
        std::cout << curly.fetchContent(head) << std::endl;
    } catch ( std::runtime_error& e ) {
        std::cerr << " [" << e.what() << "]" << std::endl;
        return 1;
    }
    return 0;
}
