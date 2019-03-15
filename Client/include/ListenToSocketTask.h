#include "ConnectionHandler.h"

using boost::asio::ip::tcp;
using namespace std;

#ifndef HW3_SPL_LISTENTOSOCKETTASK_H
#define HW3_SPL_LISTENTOSOCKETTASK_H

class ListenToSocketTask {
private:
    ConnectionHandler& _handler;
public:
    ListenToSocketTask(ConnectionHandler& refrence_to_handler);
void Listen_to_socket_and_print();

};
#endif //HW3_SPL_LISTENTOSOCKETTASK_H
