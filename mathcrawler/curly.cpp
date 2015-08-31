#include "curly.h"

CURLcode Curly::Fetch(std::string url){

    // clear things ready for our 'fetch'
    mHttpStatus = 0;
    mContent.clear();
    mHeaders.clear();

    // set our callbacks
    curl_easy_setopt(pCurlHandle , CURLOPT_WRITEFUNCTION, HttpContent);
    curl_easy_setopt(pCurlHandle, CURLOPT_HEADERFUNCTION, HttpHeader);
    curl_easy_setopt(pCurlHandle, CURLOPT_WRITEDATA, this);
    curl_easy_setopt(pCurlHandle, CURLOPT_WRITEHEADER, this);

    // set the URL we want
    curl_easy_setopt(pCurlHandle, CURLOPT_URL, url.c_str());

    //  go get 'em, tiger
    CURLcode curlErr = curl_easy_perform(pCurlHandle);
    if (curlErr == CURLE_OK){

        // assuming everything is ok, get the content type and status code
        char* content_type = NULL;
        if ((curl_easy_getinfo(pCurlHandle, CURLINFO_CONTENT_TYPE,
              &content_type)) == CURLE_OK)
            mType = std::string(content_type);            

        unsigned int http_code = 0;
        if((curl_easy_getinfo (pCurlHandle, CURLINFO_RESPONSE_CODE,
              &http_code)) == CURLE_OK)
            mHttpStatus = http_code;

    }
    return curlErr;
}

size_t Curly::HttpContent(void* ptr, size_t size,
                            size_t nmemb, void* stream) {

    Curly* handle = (Curly*)stream;
    size_t data_size = size*nmemb;
    if (handle != NULL){
        handle->mContent.append((char *)ptr,data_size);
    }
    return data_size;
}

size_t Curly::HttpHeader(void* ptr, size_t size,
                            size_t nmemb, void* stream) {

    Curly* handle = (Curly*)stream;
    size_t data_size = size*nmemb;
    if (handle != NULL){
        std::string header_line((char *)ptr,data_size);
        handle->mHeaders.push_back(header_line);
    }
    return data_size;
}
