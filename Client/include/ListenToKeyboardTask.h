#include "ConnectionHandler.h"

using boost::asio::ip::tcp;
using namespace std;
#ifndef HW3_SPL_LISTENTOKEYBOARDTASK_H
#define HW3_SPL_LISTENTOKEYBOARDTASK_H

class ListenToKeyboardTask{
private:
public:
    static void listen_to_keyboard_and_write_to_socket(ConnectionHandler &handler);
};
#endif //HW3_SPL_LISTENTOKEYBOARDTASK_H