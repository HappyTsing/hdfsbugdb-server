# 简介

项目中需要用到的脚本，共分为三个部分：

- node
- python
- mysql

# node

在不搭建开启 hdfsbugdb-server 的情况下，想运行 hdfsbugdb-ui，可以运行该文件，快速启动一个简单的后端。

注意：这里的数据并不正确。

```shell
# 安装依赖
yarn install
# 开启服务，在8081端口，这也是hdfsbugdb-ui监听的端口
node server-sample.js
```

开启后，可以愉快的浅尝一下前端了！

# python

在项目初期开发过程中，研究的数据都存放在：

- db.json
- Label.json

两个文件中，其中`db.json`中存放的是研究数据，`Label.json`是分类规划。

此外，`fixed-resolved-closed.csv`是从[HDFS JIRA](<https://issues.apache.org/jira/browse/HDFS-16300?jql=project%20%3D%20HDFS%20AND%20issuetype%20%3D%20Bug%20AND%20status%20in%20(Resolved%2C%20Closed)%20AND%20resolution%20%3D%20Fixed%20AND%20created%20%3E%3D%202019-03-30%20AND%20created%20%3C%3D%202022-03-01%20ORDER%20BY%20priority%20DESC%2C%20updated%20DESC>)导出的已经 Resolved,Closed 的，Fixed 的，`2019/03/30 - 2022/03/01`时间段内的所有 477 个 bug。

**我们将该 csv 文件导入数据库 HDFSBUGDB 的 IssueInfo 中。**

提供了一个工具类`utils.py`：

- `utils.Load_Label(r'res/Label.json')`：将`Label.json`中的数据存储到 Mysql 数据库 HDFSBUGDB 的 Label 表中。

- `utils.Load_Research_Classify(file_path=r'res/db.json')`：将`db.json`中的数据存储到 Mysql 数据库 HDFSBUGDB 的 Classify 和 Research 表中。

注意：在运行这两个方法之前，需要保证 IssueInfo 表中已经存储了数据。

# mysql

```shell
# 版本
mysql  Ver 8.0.28-0ubuntu0.20.04.3 for Linux on x86_64 ((Ubuntu))
```

我们需要新创建一个用户，并开启 mysql 的远程服务。

```shell
获取密码
sudo cat /etc/mysql/debian.cnf

创建用户 参考：https://blog.51cto.com/niuben/3027422
sudo mysql

mysql> CREATE USER 'leqing'@'%' IDENTIFIED BY 'Ww0810083142';
mysql> GRANT ALL ON *.* TO 'leqing'@'%';

mysql> use mysql;
mysql> select user,host from user;

# 开启远程服务
sudo vi /etc/mysql/mysql.conf.d/mysqld.cnf
# 注释掉 bind-address = 127.0.0.1

# 重启mysql
sudo service mysql restart

# 腾讯云防火墙打开3306端口
```

同样，在此准备了几个 sql 脚本：

- Create_HDFSBUGDB_Table.sql：创建数据库和表
- HDFSBUGDB_IssueInfo.sql：写 IssueInfo 表数据
- HDFSBUGDB_Label.sql：写 Label 表数据
- HDFSBUGDB_Classify.sql：写 Classify 表数据
- HDFSBUGDB_Research.sql：写 Research 表数据

依次运行上述 sql 脚本，即可快速构建项目所需的数据库 HDFSBUGDB
