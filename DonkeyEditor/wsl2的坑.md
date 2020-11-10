# WSL2以及linux与windows之间差异的坑

+ 客户机连主机的时候，不能使用127.0.0.1。
    + 可以通过`cat /etc/resolv.conf`查看dns配置
+ 从github上clone下来的etcd中某些go文件只有一个路径，在windows下因此无法编译。但是linux下可以成功编译。很有可能因为路径文件在linux下是链接文件。
