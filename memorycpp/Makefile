SHELL=/bin/bash

all: build test

TOTALTESTS=$(shell grep -E '^[0-9].*\.test:' Makefile |wc -l|sed -e 's/[ \t]*//g')
TESTS=$(shell for i in {1..$(TOTALTESTS)};do echo "$$i.runtest ";done)

build:
	g++ -std=c++11 -stdlib=libc++  -Wno-c++98-compat -o memory memory.cpp 

test: ${TESTS}

%.headtest:
	@echo -n "Test $* :"

%.runtest: %.headtest
	@${MAKE} $*.test > /tmp/test && echo "PASS" || (cat /tmp/test && echo "FAIL")

1.test:
	@echo "Hi parser" | ./memory |grep -E '^Invalid command$$' 

2.test:
	@echo -e "set a 10\nget a" | ./memory |grep -E '^10$$' 

3.test:
	@echo -e "set a 10\nbegin\nset a 20\nrollback\nget a" | ./memory |grep -E '^10$$'

4.test:
	@echo -e "set a 10\nbegin\nset a 20\ncommit\nget a" | ./memory |grep -E '^20$$'

5.test:
	@echo -e "set a 10\nbegin\nset a 20\nbegin\nset a 30\ncommit\nget a" | ./memory |grep -E '^30$$'

6.test:
	@echo -e "set a 10\nbegin\nset a 20\nbegin\nset a 30\nrollback\ncommit\nget a" | ./memory |grep -E '^20$$'

7.test:
	@echo -e "set a 10\nbegin\nset a 20\nrollback\nbegin\nset a 30\ncommit\nget a" | ./memory |grep -E '^30$$'

8.test:
	@echo -e "set a 10\nset b 20\nget a" | ./memory |grep -E '^10$$'

9.test:
	@echo -e "end" | ./memory 
