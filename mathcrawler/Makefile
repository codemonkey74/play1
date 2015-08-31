CPPFLAGS  += -std=c++11
OBJS = curly.o
MAIN = matchcrawler.cpp
INCS += -I . -I /usr/include 
LIBINC += -L/usr/lib
LDLIBS += -lcurl

ALL: matchcrawler

clean:
	rm -f matchcrawler

matchcrawler: ${OBJS}
	g++ ${CPPFLAGS} ${INCS} ${LIBINC} ${LDLIBS} -o $@ ${MAIN} ${OBJS}

%.o: %.cpp
	g++ ${CPPFLAGS} -c $< -o $@

