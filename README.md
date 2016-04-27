# little-rpc 总体介绍
一步一步写个rpc框架，从jdk nio ==> 类似netty的网络框架 ==> 类似dubbo的rpc框架

#源代码目录说明
src/jdk-nio/java : 用jdk原生nio接口写的socket通信逻辑，旨在了解写一个类似netty的网络通信框架，需要考虑的问题和细节
src/netty/java   : 用netty实现的一些功能、列子
src/rpc/java     : 用netty实现的rpc框架


#tag说明
