## PageParent 

> 管理 page 的容器,实际为 Activity


设计原理:

 * 应用仅包含一个 Activity ,即 PageParent
 * PageParent 管理所有的 page , page 指代旧有的 Activity , Fragment , ViewGroup , View , Dialog 等一切可见的概念


PageParent 需要实现的功能:

 * 处理添加 page 的逻辑,拥有管理路由的功能,即 PageStackRecord
 * 处理返回按钮等跳转逻辑,重写系统 key-listener
 * page 的显示隐藏等交由具体的模块来完成,即容器类 PageManager
 * 


优势:

 * 模拟 Flutter 实现单 Activity 应用
 * 可见部分为 page ,概念简单,开发方便
 * 修改主题,字体,横竖屏幕等只需一个界面即可完成,界面传值皆在本地,无大小跨进程限制
 * 原生活动启动模式复杂,由 PageStackRecord 管理,可以的方便的移除或者添加某些界面
 * 逻辑和原有activity 等兼容,又适合从新入手
 * 只有 view 存在,不需要创建多余的对象




## 待实现逻辑

 1. 添加子child时动效 : LayoutTransition

























































































>
> if success,i will be mnlin myself
>