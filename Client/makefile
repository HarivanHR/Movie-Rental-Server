CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_thread -lboost_system

all: DoubleThreadClient
	g++ -o bin/BBclient bin/ConnectionHandler.o bin/DoubleThreadClient.o bin/ListenToSocketTask.o bin/ListenToKeyboardTask.o $(LDFLAGS) 

DoubleThreadClient: bin/ConnectionHandler.o bin/DoubleThreadClient.o bin/ListenToSocketTask.o bin/ListenToKeyboardTask.o
	
bin/DoubleThreadClient.o: src/DoubleThreadClient.cpp
	g++ $(CFLAGS) -o bin/DoubleThreadClient.o src/DoubleThreadClient.cpp


bin/ConnectionHandler.o: src/ConnectionHandler.cpp
	g++ $(CFLAGS) -o bin/ConnectionHandler.o src/ConnectionHandler.cpp


bin/ListenToSocketTask.o: src/ListenToSocketTask.cpp
	g++ $(CFLAGS) -o bin/ListenToSocketTask.o src/ListenToSocketTask.cpp


bin/ListenToKeyboardTask.o: src/ListenToKeyboardTask.cpp
	g++ $(CFLAGS) -o bin/ListenToKeyboardTask.o src/ListenToKeyboardTask.cpp
	
.PHONY: clean
clean:
	rm -f bin/*
