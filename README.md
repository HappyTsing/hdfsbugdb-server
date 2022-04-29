# Redis

sudo  apt-get install redis-server 

修改配置
```shell
# 从源码安装
wget https://download.redis.io/redis-stable.tar.gz
tar -xzvf redis-stable.tar.gz
cd redis-stable
make

# 修改redis.conf 

src/redis-server redis.conf
```

# 项目经验

## pom依赖
非常非常重要！比如 `spring-data-redis 2.6.0` ，它依赖于 `spring-context 5.3.13`，但之前我用的确是其他版本的，因此二者冲突，无法启动。

因此当出现错误时，首先要检查导入的新包是否有问题，是否与之前的包的依赖冲突！

## xx.properties

注意，spring只能使用一次：
```xml
<context:property-placeholder location="classpath:properties/*.properties"/>
```
如果在多个地方使用，会找不到文件。

## 移动文件

当你移动文件到某个地方时，Idea会自动修改一些路径。
但是！当你用 `/**/*` 等匹配符匹配了3个文件，但你修改了其中某个文件的位置。
idea会自动将通配符改成具体文件的名字，此时你其他文件就没有引用了！因此一定要注意！