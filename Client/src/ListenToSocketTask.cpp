#include "../include/ListenToSocketTask.h"
extern volatile bool disconnect_flag;
ListenToSocketTask::ListenToSocketTask(ConnectionHandler &refrence_to_handler): _handler(refrence_to_handler) {
}

void ListenToSocketTask::Listen_to_socket_and_print() {
    string message;
    while (!disconnect_flag) {
        if (!_handler.getLine(message)){
            cout << "We have been disconnected!" << endl;
            disconnect_flag = true;
            break;
        }
        int len=message.length();
        message.resize(len-1); //To remove the \n
        cout << message << endl;
        if (message == "ACK signout succeeded") {
            cout << "Ready to exit. Press enter" << endl;
            disconnect_flag = true;
            _handler.close();
            break;
        }
        message = "";
    }
}




