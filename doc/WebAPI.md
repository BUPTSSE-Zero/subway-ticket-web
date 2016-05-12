# 地铁自助取票系统Web端 - WebAPI

## 日志记录工具 - LoggerUtil
### `com.subwayticket.util`主要方法一览：
```java
static public Logger getLogger(String name, String fileName)
```
获取一个Logger，获取后即可使用`log4j`提供的API来记录日志

+ name：日志记录的主体名称，具体见示例
+ fileName：日志文件名，日志文件会统一存放在`${GLASSFISH_HOME}/glassfish/domains/subway-ticket-server/applications/subway-ticket-web/logs/`目录下，`${GLASSFISH_HOME}`为Glassfish的安装目录

示例：当name为`Register Servlet`，fileName为`RegisterServlet.log`时，`RegisterServlet.log`文件内容如下：

```
2016-05-12 11:01:36,700 - Register Servlet - INFO: JSON Result:{"result_code":101,"result_description":"该手机号已被注册"} 
```

## Redis for Java 工具 - JedisUtil
`com.subwayticket.util.JedisUtil`方法一览：

```java
public static Jedis getJedis()
```
从默认主机（localhost:6379）的Redis连接池中获取一个Redis连接，一般用此方法来获取Redis连接即可。获取Jedis对象后，即可使用Jedis提供的API来往Redis Server中存取键值（key-value）对

## I18N 字符串资源 - BundleUtil
字符串资源文件路径：`src/main/resources/bundle/string.properties`

**请注意：所有的中文字符串均不允许在Java源代码以及Web网页代码中直接硬编码，必须存放到资源文件中**

`com.subwayticket.util.BundleUtil方法一览`

```
public static String getString(ServletRequest request, String key)
```
返回key所映射的字符串

+ request：客户端发送的Http请求对象，关于`HttpServletRequest`（ServletRequest的子类）对象实例获取方法如下：
	+ 在JSF的ManagedBean中：

	```java
(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	```
	+ 在Servlet中：直接使用自带的`HttpServletRequest`对象即可。

	强烈建议了解一下JSP中的五大对象作用：page（JSF中称作view），request、response、session、application，以及对应的Java代码的写法。

+ key：键值

示例：string.properties资源文件中有这么一行：

```
IndexPageTitle = 地铁自助取票系统
```
获取`IndexPageTitle`所对应的字符串：

+ Java代码写法：

```java
BundleUtil.getString(request, "IndexPageTitle")；
```
+ xhtml页面中可通过EL表达式直接获取：

```
#{bundle.IndexPageTitle}
```

## 持久化数据库操纵技术 - EJB、JPA与实体类

所有由数据库表映射到的实体类均存放在`com.subwayticket.database.model`包下，一个实体类对应数据库中的一张表，一个实体类对象实例对应数据库表中的一条记录。

所有与数据库操作相关的Helper类均存放在`com.subwayticket.database.control`包下，其中，`EntityManagerHelper`类为抽象类，其它的Bean均继承了该抽象类，同时这些Bean也都是EJB。

### `com.subwayticket.database.control.EntityManagerHelper` 方法一览：

+ `public void create(Object entity)`

将一个实体类对象实例所对应的记录插入到数据库中。

显然，要往数据库表中插入一条新的记录，只需要new一个实体类，并设置好对象实例中的各个属性，然后调用该方法即可。

+ `public void merge(Object entity)`

将对一个实体类对象实例所作的修改同步到数据库中。

要更新一条数据库表中的记录，只需要对相应的实体类对象的属性使用setter进行修改，然后调用该方法即可。

+ `public void refresh(Object entity)`

同步实体类对象实例在数据库表中的最新记录。

+ `public void remove(Object entity)`

删除实体类对象实例在数据库表中的对应记录。

+ `public Object find(Class<?> entityClass, Object id)`

根据主键查询实体类所对应的数据库表中的记录。entityClass为要查询的实体类，id为该实体类对应的主键对象，返回值为查询到的实体类对象实例。

+ `public List findAll(Class<?> entityClass)`

查询实体类所对应的数据库表中的所有记录。

+ `public int count(Class<?> entityClass)`

统计实体类所对应的数据库表中的记录数目。

+ `public List findRange(Class<?> entityClass, int[] range)`

目前作用不明……

针对特定实体类的操作方法会陆续在其它的DBHelperBean中提供。

实体类的作用非常强大，通过一个实体类对象实例还可直接得到与其相关联的其它实体类对象实例。（也就是查询的过程中EJB还自动完成了关联表的联合查询。）

**请注意**：EJB的类不可直接new来使用，必须要使用`@EJB`标记，通过EJB注入的方式获取EJB类的对象实例，且EJB类的对象实例的可见域只能为`private`，如下示例：

```java
@EJB
private SubwayTicketDBHelperBean dbBean;
```
**注意**：`@EJB`标记只能在一些特殊的Java类中使用，如Servlet，EJB、ManagedBean等。

## 数据合法性检查 - CheckUtil
`com.subwayticket.util.CheckUtil`方法一览：

```java
public static Result checkRegisterInfo(ServletRequest req, RegisterRequest regReq, SubwayTicketDBHelperBean dbBean, Jedis jedis)
```
对用户的注册请求进行检查，并返回检查结果。

+ req：客户端发送的Http请求
+ regReq：用户的注册请求，有如下属性：
	+ phoneNumber：手机号
	+ password：密码
	+ captcha：手机验证码
+ dbBean：`SubwayTicketDBHelperBean`的EJB对象实例。
+ jedis：Jedis对象实例，可通过`JedisUtil.getJedis()`获得

返回的Result中包含如下属性：

+ resultCode：结果码，值可能为0（`PublicResultCode.SUCCESS_CODE`）或者CheckUtil中的`REGISTER_XXX`这些静态常量。值为0表示验证通过。
+ resultDescription：结果描述。

示例：一个简单的用户注册流程：

```java
@EJB
private SubwayTicketDBHelperBean dbBean;

public void register(){
	RegisterRequest regReq = new RegisterRequest("123456", "123456", "123456");
	if(CheckUtil.checkRegisterInfo(request, regReq, dbBean, JedisUtil.getJedis()).getResultCode() == PublicResultCode.SUCCESS_CODE){
		//将新的用户信息写入到数据库中
		Account newUser = new Account(regReq.getPhoneNumber(), regReq.getPassword());
        dbBean.create(newUser);
	}

```

```java
public static boolean checkPhoneCaptcha(String phoneNumber, String captcha, Jedis jedis)
```
检查用户输入的验证码是否正确，如果验证通过，验证码将会从Redis数据库中删除。

+ phoneNumber: 手机号
+ captcha：用户输入的验证码
+ jedis：Jedis对象

## 安全相关 - SecurityUtil
`com.subwayticket.util.SecurityUtil`方法一览：

```java
public static Result sendPhoneCaptcha(ServletRequest request, String phoneNumber, Jedis jedis)
```
向用户的手机发送验证码，目前只是模拟发送，并不会真正发送到手机上，且验证码固定为123456

+ request：用户的Http请求
+ phoneNumber：用户的手机号
+ jedis：Jedis对象

返回一个Result对象，含有两个属性：

+ resultCode：结果码，值可能为0（`PublicResultCode.SUCCESS_CODE`）或者SecurityUtil中的`PHONE_CAPTCHA_XXX`这些静态常量。值为0表示发送成功。
+ resultDescription：结果描述。
