# gdb简明教程


+ layout src/split(<C-l>刷新布局)
+ i f(short for `info frame`) 显示当前进程stack信息
  + i r eip ebp esp: 打印寄存器信息
+ x/nxw addr 打印自addr开始的n个16进制word
  + x &var: 默认打印var对应地址开始4字节的值，以16进制显示，注意x86是小端法，显示时末位字节存在低地址处
  + x $ebp: 打印`$ebp 内值(指向前一个ebp在栈中位置的指针)：$ebp值指向的值即前一个ebp的值`
  + x/i: 以指令形式打印
+ 输入重定向：r < foo.txt
+ 在gdb中执行shell命令，例`shell ls`
