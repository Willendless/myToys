# WSL2以及linux与windows之间差异的坑

+ 客户机连主机的时候，不能使用127.0.0.1。
    + 可以通过`cat /etc/resolv.conf`查看dns配置
+ 从github上clone下来的etcd中某些go文件只有一个路径，在windows下因此无法编译。但是linux下可以成功编译。很有可能因为路径文件在linux下是链接文件。
+ wsl2连接usb：参考：https://snowstar.org/2020/06/14/wsl2-usb-via-usbip/，一些过程中奇怪的地方
    1. 我用的是华硕飞行堡垒，bios貌似没法直接禁用安全模式，但是有一个选项是删除所有选项，删了之后，usbip也可以正常安装了
    2. `.wslconfig`文件请放在win下面，当前用户那一级！(内核镜像也是)
    2. 两边用usbip的时候都要开管理员模式！！
