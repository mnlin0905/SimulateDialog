## PageParent 

> 管理 page 的容器,实际为 Activity


设计原理:

 * 应用仅包含一个 Activity ,即 PageParent
 * PageParent 管理所有的 page , page 指代旧有的 Activity , Fragment , ViewGroup , View , Dialog 等一切可见的概念


PageParent 需要实现的功能:

 * 处理添加 page 的逻辑,拥有管理路由的功能,即 PageStackRecord : OK
 * 处理返回按钮等跳转逻辑,重写系统 key-listener : OK
 * page 的显示隐藏等交由具体的模块来完成,即容器类 PageManager : OK
 * 界面参数功能(伴随路由跳转框架来传值) : 

优势:

 * 模拟 Flutter 实现单 Activity 应用
 * 可见部分为 page ,概念简单,开发方便
 * 修改主题,字体,横竖屏幕等只需一个界面即可完成,界面传值皆在本地,无大小跨进程限制
 * 原生活动启动模式复杂,由 PageStackRecord 管理,可以的方便的移除或者添加某些界面
 * 逻辑和原有activity 等兼容,又适合从新入手
 * 只有 view 存在,不需要创建多余的对象


## 待实现逻辑

 1. 添加子child时动效 : LayoutTransition : OK ,支持扩展
 2. 界面插入,a->b,在两个page中插入c,变成 a->c->b,支持part-page前插入page : ok
 3. 首次启动界面如何配置
 4. 切换屏幕时动画处理 ( 同 1),以及共享动画如何配置 
 5. 编译时注解,避免反射调用 : 
 6. mask-view 可设置背景(dialog的遮罩效果) : OK
 7. Page 支持 adapter 


## 页面方法调用流程

#### 启动 FirstPage

```
2019-10-21 17:28:48.821 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.FirstPage : onPageCreate
2019-10-21 17:28:48.830 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.FirstPage : onPageViewInject
2019-10-21 17:28:48.830 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.FirstPage : onPageAttachToParent
2019-10-21 17:28:48.831 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.FirstPage : onPageActive
```

#### 在FirstPage已启动成功后,启动 SecondPage

```
2019-10-21 17:28:59.414 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.SecondPage : onPageCreate
2019-10-21 17:28:59.432 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.SecondPage : onPageViewInject
2019-10-21 17:28:59.440 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.SecondPage : onPageAttachToParent
2019-10-21 17:28:59.441 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.FirstPage : onPageDeactive
2019-10-21 17:28:59.443 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.FirstPage : onPageDetachFromParent
2019-10-21 17:28:59.443 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.SecondPage : onPageActive
```

在第二个界面 `onPageAttachToParent` 与 `onPageActive` 之间,第一个界面会依次调用 `onPageDeactive` 与 `onPageDetachFromParent`

#### 启动第三个part类型的page : ThirdPage 

```
2019-10-21 17:29:33.634 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.ThirdPage : onPageCreate
2019-10-21 17:29:33.653 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.ThirdPage : onPageViewInject
2019-10-21 17:29:33.654 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.SecondPage : onPageDeactive
2019-10-21 17:29:33.664 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.ThirdPage : onPageAttachToParent
2019-10-21 17:29:33.664 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.ThirdPage : onPageActive
```

启动 part 类型 page 时,前一个界面不会被移除或者关闭,但仍然会调用 `onPageDeactive`

#### 在之前part-page 基础上再次启动一个 page-part

```
2019-10-21 17:32:44.412 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.FourthPage : onPageCreate
2019-10-21 17:32:44.431 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.FourthPage : onPageViewInject
2019-10-21 17:32:44.431 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.ThirdPage : onPageDeactive
2019-10-21 17:32:44.441 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.FourthPage : onPageAttachToParent
2019-10-21 17:32:44.442 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.FourthPage : onPageActive
```

逻辑直接在 full-page 上启动 part-page 回调相同

#### 连续返回四次来退出界面

```
2019-10-21 17:37:00.113 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.FourthPage : onPageDeactive
2019-10-21 17:37:00.115 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.FourthPage : onPageDetachFromParent
2019-10-21 17:37:00.116 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.ThirdPage : onPageActive
```

```
2019-10-21 17:38:11.685 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.ThirdPage : onPageDeactive
2019-10-21 17:38:11.687 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.ThirdPage : onPageDetachFromParent
2019-10-21 17:38:11.688 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.SecondPage : onPageActive
```

```
2019-10-21 17:38:39.573 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.SecondPage : onPageDeactive
2019-10-21 17:38:39.575 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.FirstPage : onPageReResume
2019-10-21 17:38:39.575 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.FirstPage : onPageAttachToParent
2019-10-21 17:38:39.575 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.FirstPage : onPageActive
2019-10-21 17:38:39.576 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.SecondPage : onPageDetachFromParent
```

```
2019-10-21 17:38:57.422 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.FirstPage : onPageDeactive
2019-10-21 17:38:57.425 12724-12724/com.knowledge.mnlin.simulatedialog E/PageApp===: com.knowledge.mnlin.simulatedialog.FirstPage : onPageDetachFromParent
```



















































































>
> if success,i will be mnlin myself
>