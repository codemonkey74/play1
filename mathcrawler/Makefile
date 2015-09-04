CPPFLAGS  += -std=c++11
OBJS = curly.o
MAIN = matchcrawler.cpp
INCS += -I . -I /usr/include 
LIBINC += -L/usr/lib
LDLIBS += -lcurl
DEPS += $(shell ls -1 *.cpp *.h)

ALL: matchcrawler

clean:
	rm -f matchcrawler
	rm -f *.o

matchcrawler: ${MAIN} ${DEPS} ${OBJS}
	g++ ${CPPFLAGS} ${INCS} ${LIBINC} ${LDLIBS} -o $@ ${MAIN} ${OBJS}

%.o: %.cpp
	g++ ${CPPFLAGS} -c $< -o $@

