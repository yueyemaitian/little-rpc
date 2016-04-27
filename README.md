little-rpc Project
========================
>一步一步写个`rpc框架`，从`jdk nio`的使用 ==> 类似netty的`网络框架` ==> 类似dubbo的`rpc框架`


#Table of Content
* [Requirements](#requirements)
* [源代码目录说明](#source-code)
* [Tag说明](#tag)

Requirements
------------
* JDK 1.7
* Maven


source code
------------
`src/jdk-nio/java` : 用jdk原生nio接口写的socket通信逻辑，旨在了解写一个类似netty的网络通信框架，需要考虑的问题和细节  
`src/netty/java`   : 用netty实现的一些功能、列子  
`src/rpc/java`     : 用netty实现的rpc框架  


tag
------------
* `V0.X`是基于jdk nio的一些代码，涵盖用原生JDK NIO接口开发NIO网络通信框架需要考虑的各种问题：  
	* [v0.1-jdknio_single](./docs/V0.X/v0.1.md) : `简单单连接通信` 构建一个能简单的读写通信的cs程序，服务端只支持一个连接  
	* [v0.2-jdknio_common](./docs/V0.X/v0.2.md) : `简单多连接通信` 服务端和客户端都支持多连接通信
	* [v0.3-jdknio_xxxx](./docs/V0.X/v0.3.md) : `XXXXX通信` 服务端和客户端都支持多连接通信
	* [v0.4-jdknio_xxxx](./docs/V0.X/v0.4.md) : `XXXXX通信` 服务端和客户端都支持多连接通信
	
* `V1.X`是基于Netty写的一些代码
* `V2.X`是基于Netty写的RPC框架代码


