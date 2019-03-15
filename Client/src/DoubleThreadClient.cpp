#include <cstdlib>
#include <boost/thread.hpp>
#include "../include/ConnectionHandler.h"
#include "../include/ListenToKeyboardTask.h"
#include "../include/ListenToSocketTask.h"
bool disconnect_flag;
bool _is_logged_in;


    using namespace std;

    /**
    * This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
    */
    int main (int argc, char *argv[]) {
        disconnect_flag = false;
        if (argc < 3) {
            cerr << "Usage: " << argv[0] << " host port" << endl << endl;
            return -1;
        }
        string host = argv[1];
        short port = atoi(argv[2]);
        ConnectionHandler connectionHandler(host, port);
        if (!connectionHandler.connect()) {
            cerr << "Cannot connect to " << host << ":" << port << endl;
            return 1;
        }
        ListenToSocketTask socket_listen_task = ListenToSocketTask(connectionHandler);
        boost::thread socket_listen(&ListenToSocketTask::Listen_to_socket_and_print, &socket_listen_task);
        ListenToKeyboardTask::listen_to_keyboard_and_write_to_socket(connectionHandler);
        return 0;
    }
