# 地铁自助取票系统Web端 - Mobile API

## 云服务器IP
101.200.144.204

## 格式说明

url统一格式：`http://${HOST_IP}:16080/subway-ticket-web/mobileapi/${SERVLET_NAME}` 

`${HOST_IP}`为目标主机的IP或域名，`16080`为开发时使用的Http端口，Https端口为`16081`，`${SERVLET_NAME}`为以下提供的API名。

字符串一律使用`UTF-8`编码。

所有API除非作出特别说明，一律使用`POST`方法发出请求，且必须包含如下Parameters：

+ data：请求的数据字段，除非作出特别说明，一律为JSON格式的字符串。
+ timestamp：时间戳，长整数，精确到毫秒，表示请求发出的时间（预留）。
+ sign：对url的签名，字符串（预留）

所有API除非作出特别说明，一律返回JSON字符串，且至少包含如下属性：

+ result_code：结果码，整数，所有API共享如下结果码，后面不再说明：
	+ 0：API调用成功返回。
	+ 1：POST请求中data字段为空。
	+ 2：请求的JSON字符串中有语法错误、数据格式错误或数据损坏。
+ result_description：结果描述，字符串。

若API调用过程中服务器发生内部错误，将返回`500`状态码

## 注册 - RegisterServlet
使用该API无需登录。

### 需要的JSON Key：
+ phone_number：手机号，字符串
+ password：密码，字符串，6-20个字符
+ captcha：验证码，字符串，可通过`PhoneCaptchaServlet`获得

### 返回的JSON Key:
+ result_code：
	+ 100：手机号为空
	+ 101：手机号已被注册过
	+ 200：密码过短或过长
	+ 201：密码格式不正确（预留）
	+ 300：验证码不正确

## 获取验证码 - PhoneCaptchaServlet

验证码有效时间为30分钟，两次获取验证码的时间需间隔至少1分钟。验证码目前模拟固定为`123456`。

使用该API无需登录。

### 需要的JSON Key：
+ phone_number：手机号，字符串

### 返回的JSON Key:
+ result_code：
	+ 100：发送失败
	+ 101：两次获取验证码时间间隔太短

## 登录 - LoginServlet

### 需要的JSON Key：
+ phone_number：手机号，字符串
+ password：密码，字符串

### 返回的JSON Key:
+ result_code：
	+ 100：登录成功，但该账号同时也在别处处于登录状态，前次登录将被强制注销
	+ 200：用户不存在
	+ 300：密码不正确

若登录成功（result_code为0或100），则多出如下key：

+ token：生成的token，字符串，用于之后的自动登录和数据校验。

## 忘记密码（重置密码）/修改密码 - ResetPasswordServlet

### 需要的JSON Key：
+ 重置密码（无需登录）
	+ phone_number：手机号，字符串
	+ new_password：新密码，字符串，6-20位
	+ captcha：验证码，字符串，可通过`PhoneCaptchaServlet`获得
+ 修改密码（需要登录）
	+ old_password: 原密码
	+ new_password：新密码

### 返回的JSON Key:
+ result_code：
	+ 200：用户不存在
	+ 300：原密码不正确
	+ 301：新密码过短或过长
	+ 302：新密码格式不正确（预留）
	+ 400：验证码不正确

