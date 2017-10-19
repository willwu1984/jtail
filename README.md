# jtail
java websocket 实现文件实时日志展示（类似linux tail）

## Getting Started
启动服务
```
git clone https://github.com/willwu1984/jtail.git
cd jtail && mvn clean package
java -jar target/jtail-0.1.jar
```

查看日志1.log（默认路径/var/log）
```
# 可以使用chrome的websocket插件：Simple WebSocket Client
ws://127.0.0.1:8080/log?id=1.log
```
## 配置
默认日志路径为/var/log，如果需要修改配置文件(src/main/resources/application.properties)后重新构建
```
logs.dir=/var/log/
```
