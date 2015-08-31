#ifndef Parser__
#define Parser__

#include <vector>
#include <stdexcept>
#include <memory>
#include <sstream>
class Parser {
    using ulong = long;

    enum Op {add, subtract, multiply, abs, value};
    const std::vector<std::string> Ops = {"add", "subtract", "multiply", "abs"};
    const std::vector<int> OpsArity = {2,2,2,1};
    
    static long fadd(long a, long b) { return a + b; }
    static long fsubtract(long a, long b) { return a - b; }
    static long fmultiply(long a, long b) { return a * b; }
    static long fabs(long a, long b = 0) { return (a >= 0) ? a : -a; }
    static long fno(long a = 0, long b = 0) { return 0; }

    const std::vector<long (*)(long, long)> funcs = { fadd, fsubtract, fmultiply, fabs, fno };

    struct OpNum {
        Op operation;
        int arity;
        long value;
        long (*func)(long, long);
        OpNum(Op o, int a, long(*f)(long, long)) : operation(o), arity(a), value(0), func(f) {}
        OpNum(ulong v) : operation(Op::value), arity(0), value(v), func(fno) {} 
    };

    inline const std::string getOpName(const Op& op) { return Ops[op]; }

    inline bool match(const std::string& source, const std::string::const_iterator& bg, const std::string& what) {
        return ((source.end() - bg) >= what.size() &&
                std::equal(bg, bg + what.size() - 1, what.begin())
               );
    }

    std::unique_ptr<OpNum>  matchOps(const std::string& str, std::string::const_iterator& p) {
        for (int i = 0; i < Ops.size(); ++i) {
            if (match(str, p, Ops[i])) {
                p += Ops[i].size();
                return std::unique_ptr<OpNum>(new OpNum(static_cast<Op>(i), OpsArity[i], funcs[i]));
            }
        }
        return std::unique_ptr<OpNum>(nullptr); 
    }

    bool isMatchText(const std::string& str, std::string::const_iterator& p, std::string what) {
        skip(str, p);
        return match(str, p, what);
    }

    bool matchNumber(const std::string& str, std::string::const_iterator& p, long& n) {
        n = 0L;
        bool neg = false;
        bool found = false;
        skip(str, p);
        if (p != str.end() && *p == '-') { 
            neg = true;
            ++p; 
            skip(str, p);
        }
        for (;p != str.end() && *p >= '0' && *p <= '9'; ++p) {
            found = true;
            n = 10L * n + (*p - '0');
        }
        if (neg) n = -n;
        return found;
    }

    std::invalid_argument createException(const std::string& str, std::string::const_iterator& p, std::string st = "") {
        std::ostringstream result(str);
        if (p == str.end()) {
            result << " [at the end]";
        } else if (p - str.begin() > str.end() - str.begin()) {
            result << " [after the end]";
        } else {
            result << "[" << *p << "]"; 
        }
        result <<  " at position " << p - str.begin();
        result << " --> " << st;
        return std::invalid_argument(result.str()); 
    }

    inline void matchSep(const std::string& str, std::string::const_iterator& p, char sep) {
        skip(str, p);
        if (p == str.end() || *p != sep) {
            throw createException(str, p);
        }
        ++p;
    }

    inline bool trySep(const std::string& str, std::string::const_iterator& p, char sep) {
        skip(str, p);
        if (p != str.end() && *p == sep) {
            ++p;
            return true;
        }
        return false;
    }

    inline bool isAtEnd(const std::string& str, std::string::const_iterator& p) {
        return p == str.end();
    }

    inline bool isEndOfRec(const std::string& str, std::string::const_iterator& p) {
        return (isAtEnd(str, p) || trySep(str, p, '\n') || trySep(str, p, '\r'));
    }

    inline void skip(const std::string& str, std::string::const_iterator& p) {
        while (p != str.end() && (*p == ' ' || *p == '\t' || (*p == '\r' && *(p+1) == '\n'))) {
                ++p;
        }
    }

    long calculate(const std::string& str, std::string::const_iterator& p) {
        //std::cout << "evaluating " << *p << std::endl;
        long result = 0;
        skip(str, p);
        std::unique_ptr<OpNum> op;
        if ((op = matchOps(str, p)) != 0) {
            matchSep(str, p, '(');
            std::cout << "found command " << getOpName(op->operation) << std::endl;
            if (op->arity == 1) { 
                long r1 = calculate(str, p);
                matchSep(str, p, ')');
                return op->func(r1, 0);
            } else {
                if (op->arity == 2) {
                    long r1 = calculate(str, p);
                    matchSep(str, p, ',');
                    long r2 = calculate(str, p);
                    matchSep(str, p, ')');
                    return op->func(r1, r2);
                }
            }
        } else {
            if (matchNumber(str, p, result)) {
                std::cout << "found number " << result << std::endl;
                return result;
            }
        }
        throw createException(str, p);
    }

    public:
        enum typt {DEADEND, GOAL, VALUES};
        using resulttype = std::pair<Parser::typt, std::vector<unsigned long>>;
        resulttype getResults(const std::string& str) {
            std::vector<unsigned long> results;
            std::string::const_iterator p = str.begin();
            Parser::typt typ = DEADEND; 
            while (p != str.end()) {
                if (isEndOfRec(str, p)) {
                    continue; //blank lines.
                }
                if (isMatchText(str, p, "DEADEND")) {
                    typ = DEADEND;
                    break;
                }
                if (isMatchText(str, p, "GOAL")) {
                    typ = GOAL;
                    break;
                }
                unsigned long res = calculate(str, p); 
                if (isEndOfRec(str, p)) {
                    typ = VALUES; 
                    results.push_back(res);
                } else {
                    throw createException(str, p, "record did not ended properly");
                }
            }
            return std::make_pair(typ, std::move(results));
        }
};

#endif
