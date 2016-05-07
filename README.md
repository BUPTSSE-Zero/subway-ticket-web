# subway-ticket-web
地铁自助取票系统——web端

## 依赖

### 构建工具
+ [Gradle](http://gradle.org/)

### 数据库
+ MySQL
+ Redis

### 服务器
+ [Glassfish](https://glassfish.java.net/)

## 构建与部署
### 第一次构建前的准备工作
①安装所有依赖软件，并将`${GLASSFISH_HOME}/bin`和`${GRADLE_HOME}/bin`这两个路径加入到`PATH`环境变量中。其中，`${GLASSFISH_HOME}`为Glassfish的安装路径，`{GRADLE_HOME}`为Gradle的安装路径
	
②创建项目所需的MySQL账号和Glassfish域。将工作目录切换到项目根目录，执行：

```
gradle initWebServer
```

### 开始构建并部署应用到Glassfish上

①启动Glassfish域。

```
gradle startWebServer
```

②启动MySQL和Redis数据库服务器。

②构建项目并部署到Glassfish服务器上。

```
gradle deployWebapp
```
部署完成后，可通过`http://localhost:16080/subway-ticket-web`访问项目首页。

项目中所有的日志文件均存放在`${GLASSFISH_HOME}/glassfish/domains/subway-ticket-server/logs`目录下

## gradle.build常用任务一览
+ build：构建项目并打包成war文件于`build/libs`目录下。
+ initWebServer： 初次构建工程使用，包括创建新的MySQL账号，创建新的Glassfish域。
+ startWebServer：启动Glassfish域。
+ stopWebServer：停止Glassfish域。
+ deployWebapp： 构建并部署（或重新部署）项目到Glassfish上。

## 推荐IDE
+ IntelliJ IDEA

## 开发注意事项
+ 如果在Windows下开发，请将项目的文件编码统一更改为`utf-8`
