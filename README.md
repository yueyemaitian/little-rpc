# little-rpc Project
>一步一步写个`rpc框架`，从`jdk nio`的使用 ==> 类似netty的`网络框架` ==> 类似dubbo的`rpc框架`

[![JDK Version][common-param-image]]

#Table of Content


##源代码目录说明
src/jdk-nio/java : 用jdk原生nio接口写的socket通信逻辑，旨在了解写一个类似netty的网络通信框架，需要考虑的问题和细节
src/netty/java   : 用netty实现的一些功能、列子
src/rpc/java     : 用netty实现的rpc框架


#tag说明
V0.X是jdk nio的一些代码，涵盖用原生JDK NIO接口开发NIO网络通信框架需要考虑的各种问题：
[v0.1-jdknio_init](./docs/V0.X/v0.1.md) : 简单的jdk nio demo，展示了怎么创建连接，但是客户端关闭后，服务端会报错退出；从这里可以知道如何关闭连接


[common-param-image]: https://img.shields.io/npm/v/datadog-metrics.svg?style=flat-square