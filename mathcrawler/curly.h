#ifndef curly_h
#define curly_h

#include <string>
#include <vector>
#include <curl/curl.h>
#include <memory>

#include "Retriever.h"

class Curly: public Retriever {
private:
    std::string        mContent;
    std::string        mType;
    unsigned int       mHttpStatus;
    std::vector<std::string>        mHeaders;
    CURL*              pCurlHandle;
    static size_t      HttpContent(void* ptr, size_t size,
                        size_t nmemb, void* stream);
    static size_t      HttpHeader(void* ptr, size_t size,
                        size_t nmemb, void* stream);

    const std::string        baseuri;

public:
    Curly(const std::string& baseURI):pCurlHandle(curl_easy_init()), baseuri(baseURI){};  // constructor
    ~Curly(){};
    CURLcode    Fetch (std::string);

    inline std::string   Content()    const { return mContent; }
    inline std::string   Type()       const { return mType; }
    inline unsigned int  HttpStatus() const { return mHttpStatus; }
    inline std::vector<std::string>   Headers()    const { return mHeaders; }

    std::string fetchContent(const std::string& urileaf) {
        std::string uri = baseuri + urileaf;
        std::string result;
        if (Fetch(uri) == CURLE_OK) {
            if (HttpStatus() != 200) {
                throw std::runtime_error("http return code " + std::to_string(HttpStatus()) + " for " + uri);
            }
            result = Content();
        } else {
            throw std::runtime_error("could not fetch " + uri);
        }
        return std::move(result);
    } 
};

#endif
