#!/usr/bin/env bash
###########################################################################################
##  this is my first shell to start storm cluster
##  author:Mr.zong
##  date:2018-04-10
##  约定：自定义的变量名全部大写
##  songshiyu001:
##          nohup /usr/storm/bin/storm nimbus >/dev/null 2>&1 &
##          nohup /usr/storm/bin/storm ui >/dev/null 2>&1 &
##  songshiyu002:
##          nohup /usr/storm/bin/storm nimbus >/dev/null 2>&1 &
##          nohup /usr/storm/bin/storm supervisor >/dev/null 2>&1 &
##          nohup /usr/storm/bin/storm logviewer >/dev/null 2>&1 &
##  songshiyu003:
##          nohup /usr/storm/bin/storm supervisor >/dev/null 2>&1 &
##          nohup /usr/storm/bin/storm logviewer >/dev/null 2>&1 &
##  就在songshiyu001上启动和停止集群
##  ./和sh 启动sh脚本的区别：
##      第一：要修改文件的启动权限
##          sh可以执行没有执行权限的脚本，而./必须有执行权限
##      第二：修改文件的格式fileformat
##          vim 进入shell文件之后，进入底行编辑模式，执行命令set ff查看文件格式是否为unix，如果是dos，则需要将dos改为unix，修改命令set ff=unix
###########################################################################################

## step 启动自身上面的进程
echo "starting localhost's nimbus & ui"
nohup /usr/storm/bin/storm nimbus >/dev/null 2>&1 &
nohup /usr/storm/bin/storm ui >/dev/null 2>&1 &

## step 启动其他机器上面的进程
## 将启动的从节点的主机名类似于hadoop中的从节点配置的slaves文件一样，将其从节点主机名配置到$STORM_HOME/conf/slaves中
## slaves文件内容：
##      songshiyu002
##      songshiyu003

cat /usr/storm/conf/slaves | while read host
do
    if [ "$host" = "songshiyu002" ]
    then
        echo "starting $host's nimbus"
        ssh root@"$host" nohup /usr/storm/bin/storm nimbus >/dev/null 2>&1 &
    fi
     echo "starting $host's supervisor"
    ssh root@"$host" nohup /usr/storm/bin/storm supervisor >/dev/null 2>&1 &
     echo "starting $host's logviewer"
    ssh root@"$host" nohup /usr/storm/bin/storm logviewer >/dev/null 2>&1 &
done