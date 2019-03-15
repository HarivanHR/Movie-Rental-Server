#include "../include/ListenToKeyboardTask.h"
extern volatile bool disconnect_flag;

void ListenToKeyboardTask :: listen_to_keyboard_and_write_to_socket(ConnectionHandler &handler){
    while (!disconnect_flag) {
        const short bufsize = 1024;//bind closure
        char buf[bufsize];
        cin.getline(buf, bufsize);
        string line(buf);
        if (!handler.sendLine(line)) {
            break;
        }
    }
}

