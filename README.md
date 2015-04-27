[sailorcast - 水手放映室](http://www.sailorcast.com)
=======================================================

简介
-----------------

[sailorcast - 水手放映室](http://www.sailorcast.com)  是一个面向Android设备的互联网免费视频播放客户端。主要功能是快速的DLNA投射以及无广告的本地播放。

目前支持四个主流的视频网站：

* 搜狐视频
* 优酷视频
* 爱奇艺视频
* 乐视视频

同视频类聚合应用不同，[水手放映室](http://www.sailorcast.com)并不维护聚合后的内容，也没有搭建自有的服务器。各个频道中的剧集列表是利用上述视频网站的接口实时获取剧集数据，等同于上述视频网站自家应用的展示结果。具体剧集的播放链接也是实时从视频网站利用接口解析。

选择好剧集后，无需等待，可直接DLNA投射或者本地播放。

下载试用： http://www.wandoujia.com/apps/com.crixmod.sailorcast

截图
--------------

![home-screen](http://img.wdjimg.com/mms/screenshot/b/02/ed2ecff72358f5a7e27fd4c95b8b902b_320_546.jpeg "首屏截图")
![channel-album-list](http://img.wdjimg.com/mms/screenshot/9/d3/248a4dfae06c20b176faff63ab66cd39_320_544.jpeg "剧集列表")
![album-detail](http://img.wdjimg.com/mms/screenshot/f/10/503cdadee079857bda5440e18c26d10f_320_546.jpeg "剧集详情")
![album-cast](http://img.wdjimg.com/mms/screenshot/0/9b/201f8b0647faeb532de5679c3b4109b0_320_545.jpeg "剧集投射")

编译
--------------

```
git clone https://github.com/fire3/sailorcast.git
```

直接在Android Studio中Open下载的目录中的android目录作为工程即可。


打包
--------------

由于不影响Umeng社会化组件中分享的使用，请使用项目中带的sailorcast.jks文件作为keyStore进行打包，在build文件中也进行了配置：

```
keyAlias 'sailorcast'
keyPassword '123456'
storeFile file('../../sailorcast.jks')
storePassword '123456'
```

后续计划
------------

* 增加下载到本地功能
* 完善播放位置记录

当然，最期待的是来自大家的意见和帮助：）


参考项目
--------------

* [you-get](https://github.com/soimort/you-get) ：提供了大量视频链接解析算法。
* [readtimeapp](https://github.com/eggrollfarm/www.readtimeapp.com) : [水手放映室](http://www.sailorcast.com)的官方网站参考了它的网站代码。


关于水手放映室
-------------

[sailorcast - 水手放映室](http://www.sailorcast.com) 是本人接触android编程后第一个业余时间的练手作品，为了熟悉一下android开发技术，同时又能给自己带来一些方便。

软件能用就好，再加上自己也是“非业内专业人士"，利用业余时间开发，许多代码为了赶工就来不及精雕细琢。人总是懒惰的，等这个东西能转起来以后，就懒得再去整理代码了。

就个人而言，经过这一轮的开发，起码熟悉了构建一个相对完整的android应用前前后后需要掌握的一些关键知识。虽然本人不是从事android开发工作，作为兴趣爱好，也算是一门技能，慢慢培养吧：）


关于我
---------------

这里记录了日常工作中积攒的一些笔记，也是最近才开始告诉自己要养成整理的习惯 : 

[fire3's notes](http://fire3.crixmod.com)
