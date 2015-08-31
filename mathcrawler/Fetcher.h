#ifndef Fetcher__
#define Fetcher__

class Fetcher {
public:
    std::string getContent(const std::string& URI) = 0;
};

#endif
