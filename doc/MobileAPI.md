# 地铁自助取票系统Web端 - Mobile REST API

## 云服务器IP
101.200.144.204

## Model Maven
本项目通过maven的方式使服务端和Androi端共享一些model部分的代码。

使用方式：

① 在Gradle构建脚本中加入maven repository的地址
```
buildscript {
    repositories {
        jcenter()
        maven {
            url 'http://101.200.144.204:16082/nexus/content/repositories/releases'
        }
    }
}
```

```
allprojects {
    repositories {
        jcenter()
        maven {
            url 'http://101.200.144.204:16082/nexus/content/repositories/releases'
        }
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

JSON中时间格式统一为`yyyy-MM-dd HH:mm:ss`，时区为`Asia/Shanghai`。

对于每个API请求，根据对请求的内容的校验情况会返回不同的HTTP状态码。每个API都有可能返回的Http状态码和JSON Result如下表：

| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 404 | 404 | API未找到 |
| 400 | 400 | 请求参数不合法或不完整 |
| 401 | 401 | Token无效或已过期 |
| 500 | 500 | 服务器内部错误 |

对于需要认证后才能使用的API会以 <b>*</b> 来标识，要使用这些API，需要在HTTP Header中加入如下认证字段：

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
| 201 | 0      | 验证码发送成功，30分钟内输入有效 |

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
| 200 | 0      | 重置密码成功|

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
| 200 | 0      | 修改密码成功|

## 注销*

```
PUT account/logout
```

该API不需要任何请求内容，只要Http Header中有`AuthToken`字段就行

### Return:
| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 204 | —/— | —/— |

## 检查登录状态及测试Token是否有效*

```
GET account/check_login
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

获取成功后，返回的结果中会含有一个城市的列表，包括各个城市的ID和名字。

## 获取指定城市的地铁号线列表
```
GET subway/line/{cityId}
```
其中`cityId`为参数，为要查询的城市的ID。

### Model Class：
Return: SubwayLineListResult

### Return:
| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 404 | 100301 | 结果未找到 |
| 200 | 0      | —/— |

获取成功后，返回的结果中会含有一个地铁号线的列表，包括各个地铁号线的ID和名字。

## 获取指定地铁号线的地铁站列表
```
GET subway/station/{subwayLineId}
```
其中`subwayLineId`为参数，为要查询的地铁号线的ID。

### Model Class：
Return: SubwayStationListResult

### Return:
| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 404 | 100301 | 结果未找到 |
| 200 | 0      | —/— |

获取成功后，返回的结果中会含有一个地铁站的列表，包括各个地铁站的ID和名字。

## 获取票价
```
GET subway/ticket_price/{startStationId}/{endStationId}
```
其中`startStationId`为起始站ID，`endStationId`为终点站ID。

### Model Class：
Return: TicketPriceResult

### Return:
| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 404 | 100301 | 结果未找到 |
| 200 | 0      | —/— |

获取成功后，返回的结果中会含有票价信息。

# 订单相关API
## 提交订单*
```
POST ticket_order/submit
```
目前每次最多可订10张票。

### Model Class：
Request: SubmitOrderRequest
Return: SubmitOrderResult

### Request JSON Key:
+ start_station_id：起始站ID
+ end_station_id：终点站ID
+ amount：订票数

### Return:
| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 422 | 100401 | 提交订单时发生错误 |
| 422 | 100402 | 线路不存在 |
| 422 | 100403 | 订票数需至少为1张 |
| 422 | 100404 | 订票数超出限制 |
| 201 | 0      | 订单已提交 |

订票提交成功后，返回结果中会含有详细的订单信息，包括订单号，下单时间等。

## 取消订单*
```
DELETE ticket_order/cancel/{orderId}
```
`orderId`为要取消的订单的订单号。

### Model Class：
Return: Result

### Return:
| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 404 | 100410 | 订单不存在 |
| 422 | 100411 | 此订单不可取消 |
| 200 | 0      | 订单已取消 |

## 支付订单*
```
PUT ticket_order/pay
```

### Model Class：
Request: PayOrderRequest
Return: PayOrderResult

### Request JSON Key:
+ order_id：订单号ID

### Return:
| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 404 | 100410 | 订单不存在 |
| 422 | 100420 | 此订单不可支付 |
| 200 | 0      | 订单支付成功 |

支付成功后，返回的结果中会含有提取码。

## 订单退款*
```
PUT ticket_order/refund
```

### Model Class：
Request: RefundOrderRequest
Return: RefundOrderResult

### Request JSON Key:
+ order_id：订单号ID

### Return:
| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 404 | 100410 | 订单不存在 |
| 422 | 100430 | 此订单不可退款 |
| 200 | 0      | 订单退款成功 |

退款完成后，返回的结果中会含有退款总额。

## 查询某个订单的详细信息*
```
GET ticket_order/order_info/{orderId}
```
`orderId`为要查询的订单号ID。

### Model Class：
Return: OrderInfoResult

### Return:
| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 404 | 100410 | 订单不存在 |
| 200 | 0      | --/-- |

查询成功后，返回的结果中会含有订单的详细信息，订单信息包括如下属性：

+ ticketOrderId：订单号ID
+ ticketOrderTime：下单时间。
+ startStation：起始站
+ endStation：终点站
+ ticketPrice：每张票的价格
+ amount：总票数
+ extractAmount：已提取的票数
+ status：订单状态，为以下几个值之一：
	+ TicketOrder.ORDER_STATUS_NOT_PAY：订单未支付
	+ TicketOrder.ORDER_STATUS_NOT_DRAW_TICKET：未取票，包括未取完所有票
	+ TicketOrder.ORDER_STATUS_FINISHED：已完成，即已取完所有票
	+ TicketOrder.ORDER_STATUS_REFUNDED：已退款
+ extractCode：提取码
+ comment：订单备注信息

## 查询某个时间段内的某个状态的订单列表*
```
GET ticket_order/order_list/{status}/{startTimestamp}/{endTimestamp}
```
+ `status`：订单状态，为以上所述的几个值之一
+ `startTimestamp`：开始时间戳，时间戳为类似`System.currentTimeMillis()`或`Date.getTime()`方法所取得的时间戳
+ `endTimestamp`：结束时间戳。
注：时间戳中只要有准确的年月日信息就行，时分秒信息不重要，startTimestamp在服务器端会被转化成当日的0时0分0秒，endTimestamp会被转化成当日的23时59分59秒。

### Model Class：
Return: OrderListResult

### Return:
| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 404 | 100410 | 订单不存在 |
| 200 | 0      | --/-- |

订单列表将自动按时间由新到旧排序。

## 查询某个时间段内的所有状态的订单列表*
```
GET ticket_order/order_list/{startTimestamp}/{endTimestamp}
```

### Model Class：
Return: OrderListResult

### Return:
| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 404 | 100410 | 订单不存在 |
| 200 | 0      | --/-- |

# 常用设置相关API

## 获取历史路线记录*
```
GET preference/history_route
```
### Model Class：
Return: HistoryRouteListResult

### Return:
| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 404 | 100503 | 暂无历史路线记录 |
| 200 | 0      | --/-- |

## 获取常用地铁站*
```
GET preference/prefer_station
```
### Model Class：
Return: PreferStationListResult

### Return:
| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 404 | 100503 | 暂无常用地铁站 |
| 200 | 0      | --/-- |

## 添加常用地铁站*
```
POST preference/prefer_station/add
```
### Model Class：
Request: AddPreferStationRequest
Return: Result

### Request JSON Key:
+ station_id：地铁站ID

### Return:
| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 404 | 100502 | 该常用地铁站已存在 |
| 201 | 0      | 添加常用地铁站成功 |


## 移除常用地铁站*
```
DELETE preference/prefer_station/remove/{stationId}
```
`stationId`为要移除的常用地铁站的ID

### Model Class：
Return: Result

### Return:
| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 404 | 100501 | 该常用地铁站不存在 |
| 200 | 0      | 已从常用地铁站中移除 |

## 获取常用路线*
```
GET preference/prefer_route
```
### Model Class：
Return: PreferRouteListResult

### Return:
| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 404 | 100503 | 暂无常用路线 |
| 200 | 0      | --/-- |

## 添加常用路线*
```
POST preference/prefer_route/add
```
### Model Class：
Request: AddPreferRouteRequest
Return: Result

### Request JSON Key:
+ start_station_id：起始站ID
+ end_station_id：终点站ID

### Return:
| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 404 | 100502 | 该常用路线已存在 |
| 422 | 100510 | 起始站与终点站不能一致 |
| 201 | 0      | 添加常用路线成功 |

## 移除常用路线*
```
DELETE preference/prefer_route/remove/{startStationId}/{endStationId}
```
`startStationId`为起始站ID，`endStationId`为终点站ID

### Model Class：
Return: Result

### Return:
| Http Status Code | result_code | result_description |
|------------------|-------------|--------------------|
| 404 | 100501 | 该常用路线不存在 |
| 200 | 0      | 已移除常用路线 |

