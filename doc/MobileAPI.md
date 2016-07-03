# 地铁自助取票系统Web端 - Mobile REST API

## 云服务器IP
101.200.144.204

## Model Maven
本项目通过maven的方式使服务端和Androi端共享一些model部分的代码。

使用方式：

① 在Gradle构建脚本中加入maven repository的地址
```
repositories {
	maven{ 
		...
		url 'http://101.200.144.204:16082/nexus/content/repositories/releases'
	}
}
```

② Dependency中加入
```
dependencies {
    ...
    compile 'com.subwayticket:subway-ticket-models:0.+'
}
```


## API格式说明

url统一格式：`http(s)://${HOST_IP}:16080/subway-ticket-web/mobileapi/${API_VERSION}/${API_NAME}` 

`${HOST_IP}`为目标主机的IP或域名，`16080`为开发时使用的Http端口，Https端口为`16081`，`${API_NAME}`为以下提供的API名。`${API_VERSION}`为API的版本号，目前可用的版本号为`v1`。

示例：注册API的URL
```html
http://101.200.144.204:16080/subway-ticket-web/mobileapi/v1/account/register
```

字符串一律使用`UTF-8`编码。

所有API除非作出特别说明，请求的内容格式均为`JSON`格式（MIME类型为`application/json`），返回的内容格式也均为`JSON`格式，并且至少有以下的JSON Key：

+ result_code：业务结果码
+ result_description：对结果的文字描述。

对于每个API请求，根据对请求的内容的校验情况会返回不同的HTTP状态码。每个API都有可能返回的Http状态码和JSON Result如下表：

| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 404 | 404 | API未找到 |
| 400 | 400 | 请求参数不合法或不完整 |
| 401 | 401 | Token无效或已过期 |
| 500 | 500 | 服务器内部错误 |

对于需要认证后才能使用的API会以** * **来标识，要使用这些API，需要在HTTP Header中加入如下认证字段：

```
AuthToken：${TOKEN}
```

`${TOKEN}`为登录时获得的Token。

# 账号相关API

## 用户注册
```
POST account/register
```

### Model Class：
Request: RegisterRequest
Return: Result

### Request JSON Key：

+ phone_number：手机号，字符串
+ password：密码，字符串，6-20个字符
+ captcha：手机验证码，字符串

### Return:

| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 422 | 100001 | 手机号为空 |
| 422 | 100002 | 该手机号已被注册 |
| 422 | 100003 | 密码过短或过长 |
| 422 | 100004 | 密码格式不符合要求 |
| 422 | 100103 | 验证码不正确 |
| 422 | 100104 | 验证码错误次数已达到3次，该验证码已失效 |
| 201 | 0      | 注册成功 |

## 获取验证码

验证码有效时间为30分钟，两次获取验证码的时间需间隔至少1分钟。验证码目前模拟固定为`123456`。

```
PUT account/phone_captcha
```

### Model Class：
Request: PhoneCaptchaRequest
Return: Result

### Request JSON Key：
+ phone_number：手机号

### Return:
| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 422 | 100101 | 验证码发送失败 |
| 422 | 100102 | 再次获取验证码需间隔至少60秒 |

## 登录

```
PUT account/login
```

### Model Class：
Request: LoginRequest
Return: MobileLoginResult

### Request JSON Key：
+ phone_number：手机号
+ password：密码

### Return:

| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 401 | 100202 | 用户不存在 |
| 401 | 100203 | 密码不正确 |
| 201 | 100201 | 登录成功，但该账号同时也在别处处于登录状态，前次登录将被强制注销 |
| 201 | 0      | 登录成功|

若登录成功（Http Status Code为201），则返回的JSON内容中会多出`token`字段，即服务器分配的token。

## 忘记密码（重置密码）

```
PUT account/reset_password
```

### Model Class：
Request: ResetPasswordRequest
Return: Result

### Request JSON Key：
+ phone_number：手机号
+ new_password：新密码
+ captcha：手机验证码

### Return：
| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 401 | 100202 | 用户不存在 |
| 422 | 100003 | 密码过短或过长 |
| 422 | 100004 | 密码格式不符合要求 |
| 422 | 100103 | 验证码不正确 |
| 422 | 100104 | 验证码错误次数已达到3次，该验证码已失效 |
| 201 | 0      | 重置密码成功|

## 修改密码*

```
PUT account/modify_password
```

### Model Class：
Request: ModifyPasswordRequest
Return: Result

### Request JSON Key:
+ old_password：原密码
+ new_password：新密码

### Return:
| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 401 | 100203 | 原密码不正确 |
| 422 | 100003 | 密码过短或过长 |
| 422 | 100004 | 密码格式不符合要求 |
| 201 | 0      | 修改密码成功|

## 注销*

```
PUT account/logout
```

该API不需要任何请求内容，只要Http Header中有`AuthToken`字段就行

### Return:
| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 204 | —/— | —/— |


# 获取地铁信息API

## 获取城市列表
```
GET subway/city
```

### Model Class：
Return: CityListResult

### Return:
| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 404 | 100301 | 结果未找到 |
| 200 | 0      | —/— |


## 获取指定城市的地铁号线列表
```
GET subway/line/{cityID}
```
其中cityID为参数，为要查询的城市的ID。

### Model Class：
Return: SubwayLineListResult

### Return:
| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 404 | 100301 | 结果未找到 |
| 200 | 0      | —/— |

## 获取指定地铁号线的地铁站列表
```
GET subway/station/{subwayLineID}
```
其中subwayLineID为参数，为要查询的地铁号线的ID。

### Model Class：
Return: SubwayLineListResult

### Return:
| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 404 | 100301 | 结果未找到 |
| 200 | 0      | —/— |

## 获取票价
```
GET subway/ticket_price/{startStationID}/{endStationID}
```
其中startStationID为起始站ID，endStation为终点站ID。

### Model Class：
Return: TicketPriceResult

### Return:
| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 404 | 100301 | 结果未找到 |
| 200 | 0      | —/— |
