#ifndef Retriever__
#define Retriever__

class Retriever {
public:
    virtual std::string fetchContent(const std::string& identifier) = 0;
};

#endif
