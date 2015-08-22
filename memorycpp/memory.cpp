#include <iostream>
#include <sstream>
#include <vector>
#include <list>
#include <map>
#include <stdexcept>

namespace memorizer {
using strings = std::vector<std::string>;

class CommandExceptions: public std::runtime_error {
    public:
        CommandExceptions(const std::string& msg): std::runtime_error(msg) {}
        CommandExceptions(): std::runtime_error("") {}

};
class InvalidCommand:public CommandExceptions {};
class MissingParameters:public CommandExceptions {};
class NothingToRollback:public CommandExceptions {};
class ShouldExit: public CommandExceptions {};

enum class Commander {
    unknown, 
    get,
    set,
    begin,
    commit,
    rollback,
    end
};

std::map<std::string, Commander> commandmapper = {
    {"unknown", Commander::unknown},
    {"get", Commander::get},
    {"set", Commander::set},
    {"begin", Commander::begin},
    {"commit", Commander::commit},
    {"rollback", Commander::rollback},
    {"end", Commander::end}
};

std::ostream& operator<<(std::ostream& out, const Commander& c) {
    for (auto it = commandmapper.begin(); it != commandmapper.end(); ++it )
        if (it->second == c)
            out << it->first;
    return out;
}
class Command {
    private:
        std::string command;
        strings params;

    public:
        
       Commander getCommand() const { 
            return commandmapper[this->command];
        }
       std::string getParams(const size_t pos) const { 
            if (this->params.size() <= pos) {
                throw MissingParameters();
            }
            return this->params[pos]; 
        }

        Command (const std::string& line) {
            std::stringstream linestream(line);

            linestream >> this->command;
              
            std::string token;
            while (linestream >> token) {
                this->params.push_back(token);
            }
        }
};

using memorystruct = std::list<std::map<std::string, std::string>>;
class Memory {
    private:
        memorystruct M;
        
    public:
        Memory() : M() {
            M.emplace_front(); 
        }
        void set(const std::string& key, const std::string& value) {
            (M.back())[key] = value;
        }
        std::string get(const std::string& key) {
            try {
                return M.front().at(key);
            } catch (const std::out_of_range&) {
                return "NULL";
            }
        }
        void begin() {
            M.emplace_back();
        }
        void rollback() {
            if (M.size() > 1) {
                M.pop_back();
            } else {
                throw NothingToRollback();
            }
        }
        void commit() {
            size_t sz = M.size();
            if (sz <= 1) {
                return;
            }
            auto i = M.begin(); ++i;
            for(; i != M.end(); ++i) {
                for (auto& kv: *i) {
                    (M.front())[kv.first] = kv.second;
                }
            }
            while(--sz > 1) {
                M.pop_back();
            }
        }
        void end() {
            throw ShouldExit();
        }
};

class Executor {
    private:
        Memory m;
    public:
        Executor(Memory& memory): m(memory) {}
        std::string execute(const Command& c) {
            switch (c.getCommand()) {
                case Commander::unknown:
                    throw InvalidCommand(); 
                case Commander::get:
                    return m.get(c.getParams(0));
                case Commander::set:
                    m.set(c.getParams(0), c.getParams(1));
                    break;
                case Commander::begin:
                    m.begin();
                    break;
                case Commander::rollback:
                    m.rollback();
                    break;
                case Commander::commit:
                    m.commit();
                    break;
                case Commander::end:
                    m.end();
                    break;
            }
            return "";
        }
};

} //end of namespace
//int main (int argc, char* argv[]) {
int main () {
    using namespace memorizer;
    std::string line;
    Memory memory;
    Executor ex(memory);
    while (std::getline(std::cin,line)) {
        if (line.length() < 1) {
            continue;
        }
        try {
            std::cout << ex.execute(Command(line)) << std::endl;
        } catch (InvalidCommand& e) {
            std::cerr << "Invalid command" << std::endl;
        } catch (const MissingParameters& m) {
            std::cerr << "Missign parameters" << std::endl;
        } catch (const NothingToRollback& m) {
            std::cerr << "Nothing to rollback" << std::endl;
        } catch (const ShouldExit& m) {
            break;
        } catch (std::exception& e) {
            std::cerr << e.what() << std::endl;
            return 1;
        }

    }
    return 0;
}
