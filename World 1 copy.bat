@echo off
title Arrav - World 1
set lib=%lib%bin;
set lib=%lib%libs/xstream-1.4.2.jar;
set lib=%lib%libs/kxml2-2.3.0.jar;
set lib=%lib%libs/kxml2-min-2.3.0.jar;
set lib=%lib%libs/netty-3.5.10.Final.jar;
set lib=%lib%libs/mysql-connector-java-5.1.18-bin.jar;
java -Xmx540m -cp %lib% com.server2.Server dev1.acfg

pause