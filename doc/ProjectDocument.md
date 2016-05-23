# 地铁自助取票系统

## 总体设计风格
+ 简洁设计：一个界面上文字内容应尽量避免过多，在达到功能的前提下，界面设计应尽量简洁大气。
+ 图文结合：界面上多使用图文结合的方式来作交互（如菜单项文字旁边附加图标），以免界面显得太枯燥。
+ 扁平化设计：图标采用简洁的扁平化图标，总体色彩鲜艳明亮。
+ 尽量少的界面跳转次数。

## 模块说明
### 账号系统
#### 注册
用户注册需要提供以下信息：

+ 手机号：用于登录
+ 密码：6-20位
+ 验证码：发送到手机上的验证码，用户每获取一次验证码后需隔至少60秒后才能重新获取，每次获取的验证码有效期为30分钟。若连续输错3次，该次验证码将直接失效，需要重新获取。

#### 登录
用户只需输入手机号和密码即可快速登录。

#### 忘记密码
如果忘记密码，则用户可以通过手机验证码重置密码后直接登录。
用户需要提供以下信息：

+ 验证码：验证码：发送到手机上的验证码，用户每获取一次验证码后需隔至少60秒后才能重新获取，每次获取的验证码有效期为30分钟。若连续输错3次，该次验证码将直接失效，需要重新获取。
+ 新密码：6-20位

重置密码后直接进入登录状态。

### 购票系统

用户交互总体流程：

#### 1.选择起始站和终点站

注：前期暂时只做文字选站，后期考虑做图上选点，提供更好的用户交互方式。
选择流程如下：

+ 选择城市
+ 选择起始站所在的地铁号线
+ 选择起始地铁站
+ 选择终点站所在的地铁号线
+ 选择终点地铁站

对于暂时封闭的地铁站，界面上依然会显示该地铁站，但不可选。同时，如果某个地铁站有公告信息，也需要显示出来。

#### 2.根据起始站和终点站列出当前最新票价

#### 3.买票
用户只能购买当天的票，并且也只能在当天提票。

+ 选择购买票数
+ 生成订单：自订单生成的时间起，用户需要在60分钟内并且于当日23:59前完成付款，否则订单自动失效。
+ 选择付款方式
+ 用户付款

#### 4.完成购票
完成购票后，用户可立即得到该订单对应的文字提取码和二维提取码，可选择其中一种方式提票。订单仅限当天有效，若用户当天没有取完订单内的所有票，则系统将于次日0点对未取的票作自动退票处理。

### 个人中心

#### 安全相关
##### 修改密码
对用户提供两种方式修改密码：

+ 通过登录密码，用户需提供以下信息：
	+ 原密码
	+ 新密码：6-20位
+ 与“忘记密码”一样，通过手机验证码直接设置新密码。

#### 购票常用设置

##### 管理常用地铁站
在这里，用户可以添加或删除常用城市，在常用城市中可以添加或删除常用地铁站，如果用户在登录状态下选择起始站和终点站，则优先显示常用城市和常用地铁站。同时，常用城市和常用地铁站都有一个优先级，优先级越高，显示越靠前。

#### 订单记录
##### 未完成订单
这里显示的是已经生成的但并未完成付款的订单，用户可以在此继续对订单付款。

##### 未提票订单
这里显示的是已经完成的但并未提完订单内所有票的订单，用户可在此处得到订单对应的文字提取码和二维码。同时也可以对订单中未取的票作退票处理，退票流程为：

+ 选择退票数
+ 退票

##### 历史订单
这里显示的是过去的以下类型的订单：

+ 已经取完票的订单
+ 已经失效的订单
+ 已经退票的订单

## Web端页面及其功能

所有页面都要有的功能：

+ 登录（含忘记密码）
+ 注册
+ App下载（指向App下载页面）
+ 登录状态下能够进入到个人中心页面

### 主页
+ 显示公告信息

### 购票页面
+ 完成整个购票流程

### 个人中心
+ 对应个人中心的所有功能

### App下载页面
+ 提供App下载链接
+ 提供扫码下载
+ 显示App版本号、更新日期、更新日志等信息