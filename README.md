# SDialog (Android)

[![License](https://img.shields.io/aur/license/yaourt.svg)](http://www.gnu.org/licenses/gpl-3.0.html)
[![Download](https://api.bintray.com/packages/lovingning/maven/sdialog/images/download.svg)](https://bintray.com/lovingning/maven/sdialog/_latestVersion)

## 简介

SDialog是一个使用View来模拟dialog，可在界面中同时进行多个dialog弹出与关闭的第三方库；

[详细使用方法及原理解释](https://blog.csdn.net/lovingning/article/details/89001984) 

## demo 效果

[demo](/files/demo.gif)

## 设计思路

通过在根部局中添加一个容器：Container，控制某些 ‘自由’ 状态的 View 消失与隐藏，同时伴随着一些动画的使用，来达到模拟 Dialog 弹出与关闭 的目的；

同时定义了开放了动画接口，可自主实现其他动画效果；

## 主要功能

 1. 可由 layout 布局生成被模拟的 `dialog` 对象，用于实际操作
 2. 可同时保持多个 `dialog` 的显示或隐藏
 3. 弹出框显示或关闭时，可使用动画完成过渡效果
 
## 使用步骤（简单使用）

### 第一步：引入本库

可以通过git将项目下载到本地，然后引用其中的module：`sdialog`;

或者通过 gradle 进行引用：

```groovy
compile 'com.knowledge.mnlin:sdialog:1.0.0'
```

### 第二步：修改 `Activity` 类继承关系

让需要弹窗操作的 Activity 类继承 [SDActivity](/sdialog/src/main/java/com/knowledge/mnlin/sdialog/base/SDActivity.java)，像这样：

```kotlin
class MainActivity : SDActivity(){
    //...
}
```

然后继续其他操作；

或者：

如果不想每个Activity都继承，或者已有了基类Activity，则可以增加或修改基类Activity的代码，只需将[SDActivity](/sdialog/src/main/java/com/knowledge/mnlin/sdialog/base/SDActivity.java)中内容拷贝到自己的基类Activity中即可（不同方法中代码要拷贝到对应的方法中）；记得让基类实现 [ProvideIncludeDialogInterface](/sdialog/src/main/java/com/knowledge/mnlin/sdialog/interfaces/ProvideIncludeDialogInterface.java)接口；

想这样就可以 ：(代码位置位于：[TestBaseActivity](/app/src/main/java/com/knowledge/mnlin/simulatedialog/TestBaseActivity.java))

```java
public class TestBaseActivity extends AppCompatActivity implements ProvideIncludeDialogInterface {
    // ... 其他代码
    
    private IncludeDialogViewGroup dialogContentView;

    // ... 其他代码

    @Override
    public void onBackPressed() {
        if (getIncludeDialog() != null && getIncludeDialog().existDialogOpened()) {
            getIncludeDialog().closeDialogsOpened(null, null, true, true);
        } else {
            super.onBackPressed();
        }
    }
    
    // ... 其他代码

    @Nullable
    @Override
    public IncludeDialogViewGroup getIncludeDialog() {
        if(dialogContentView == null){
            dialogContentView = ((IncludeDialogViewGroup) findViewById(android.R.id.content).getTag(com.knowledge.mnlin.sdialog.R.id.id_include_dialog_view_group));
        }
        return dialogContentView;
    }
    
    // ... 其他代码
}

```

### 第三步：修改Activity根部局控件类型为 [IncludeDialogViewGroup](/sdialog/src/main/java/com/knowledge/mnlin/sdialog/widgets/IncludeDialogViewGroup.kt)

对于原生的 dialog 来说，需要借助 `window` 进行显示，对于我们模拟的 dialog ，则需要有一个布局容器来控制视图的显示和隐藏；

针对Activity的布局文件：如 [TestBaseActivity](/app/src/main/java/com/knowledge/mnlin/simulatedialog/TestBaseActivity.java) 的布局文件：[activity_main.xml](/app/src/main/res/layout/activity_main.xml)，根布局需要替换为：[IncludeDialogViewGroup](/sdialog/src/main/java/com/knowledge/mnlin/sdialog/widgets/IncludeDialogViewGroup.kt)

```xml
<?xml version="1.0" encoding="utf-8"?>
<!--库容器布局-->
<com.knowledge.mnlin.sdialog.widgets.IncludeDialogViewGroup
        xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

    <!--实际界面显示的根布局-->
    <LinearLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center"
            android:gravity="center"
            android:orientation="vertical">

            <!--其他内容部分-->
            <!-- ... .... .....-->
            <!--其他内容部分-->

    </LinearLayout>
</com.knowledge.mnlin.sdialog.widgets.IncludeDialogViewGroup>
```

此时准备工作就完成了，接下来只需要 编写 dialog 对应的布局文件，然后默认提供的`弹出框生成器`，生成 dialog（实际为[SimulateDialogInterface 类型](/sdialog/src/main/java/com/knowledge/mnlin/sdialog/interfaces/SimulateDialogInterface.java)） 就可以了；

### 第四步：生成 dialog

最后，通过默认的生成器(即：[DefaultSimulateDialogImpl](/sdialog/src/main/java/com/knowledge/mnlin/sdialog/utils/DefaultSimulateDialogImpl.java))，可以很方便的生成 dialog

假设当前已有 dialog 布局文件：[dialog_first.xml](/app/src/main/res/layout/dialog_first.xml) 与 [dialog_second.xml](/app/src/main/res/layout/dialog_second.xml)

*注：为了达到真实的效果，dialog对应布局的根视图，尽量为 CardView ，这样可很好的达到模拟的效果*

则通过如下方式可生成 两个对应的：dialog

```kotlin
includeDialog?.also { container ->
            //初始化弹出框控件
            firstDialog =
                    DefaultSimulateDialogImpl<CardView, FrameLayout.LayoutParams>(
                        container,
                        R.layout.dialog_first
                    ).also {
                        //对 dialog 中的控件,进行逻辑处理,或者添加点击等事件
                        it.generateView().apply {
                            //确认按钮
                            bt_submit.dOnClick {
                                toast("不会关闭弹窗")
                            }
                        }
                    }

            secondDialog =
                    DefaultSimulateDialogImpl<FrameLayout, FrameLayout.LayoutParams>(
                        container,
                        R.layout.dialog_second
                    ).also {
                        //对 dialog 中的控件,进行逻辑处理,或者添加点击等事件
                        it.generateView().apply {
                            //关闭按钮
                            bt_close.dOnClick {
                                // <b> 关闭某个弹出框 <b/>
                                secondDialog.close(true)
                                toast("将关闭弹窗")
                            }
                        }
                    }
}
```

> 注： DefaultSimulateDialogImpl 类中泛型对应的含义为：根部局类型；填充到容器时提供的 LayoutParams类型（一般固定为FrameLayout.LayoutParams）。 

> 注： 详细内容可参考源码注释信息，如有不明，请提 `issues`

---

此时，工作都已完成了，我们可以通过如下方式来控制dialog

```kotlin
// 不使用动画
secondDialog.show()

// 使用库提供的默认动画
firstDialog.show(AlphaIDVGAnimatorImpl(0f, 1f, 1000L, 500L))
```

---

当然可以用户觉得弹窗时主动添加的遮罩颜色不好看，或者在用户点击 dialog 以外的区域时，不想让弹窗自动关闭，则可以通过以下两种方式 进行修改：

**第一种：应用启动前全局修改遮罩颜色以及设置是否自动关闭弹窗**（当前未开放，请等待后续升级）

修改  [IncludeDialogViewGroup](/sdialog/src/main/java/com/knowledge/mnlin/sdialog/widgets/IncludeDialogViewGroup.kt) 类的 `defaultMaskColor` 以及 `defaultCloseOnClickOut` 属性。

**第二种：修改单个 container 的逻辑**

```kotlin
//修改遮罩颜色以及自动关闭逻辑; container 为  IncludeDialogViewGroup 实例对象
container.closeOnClickOut = true
container.maskColor  = Color.parseColor("#40000000")
```

## 高级模式

### 1.自定义动画

如果想要自定义弹出动画，可参照 [AlphaIDVGAnimatorImpl](/sdialog/src/main/java/com/knowledge/mnlin/sdialog/animators/AlphaIDVGAnimatorImpl.java) ，继承 [ProvideIncludeDialogVGAnimator](/sdialog/src/main/java/com/knowledge/mnlin/sdialog/interfaces/ProvideIncludeDialogVGAnimator.java) 接口，完成动画逻辑

动画由两部分组成：打开弹出框动画，关闭弹出框动画

### 2.同时弹出或关闭多个弹出框口

有两种方式：

**通过关闭多个dialog**

```kotlin
fun closeAll(){
    firstDialog?.close()
    secondDialog?.close()
}
```

**通过容器布局关闭所有**

```kotlin
fun closeAll(){
    container.closeDialogsOpened(closeAll = true)
}
```

-----
-----

一般只需要按照简单方式使用即可，如果功能无法实现，请详细参照 demo 源码 

使用前可先下载 [demo.apk](/files/demo.apk) 体验使用效果 

## 常见问题

1. 为什么dialog设置了在顶部显示，却距离上部还有一个statusBar或者toolbar？

> 模拟的 dialog 核心还是使用  view 和 ViewGroup 来操作的，因此需要保证  container[IncludeDialogViewGroup] (/sdialog/src/main/java/com/knowledge/mnlin/sdialog/widgets/IncludeDialogViewGroup.kt) 宽高占满全屏，所以最好将container放在布局最上层，同时让应用  style 为全屏模式

2. dialog 在不显示时，会致使界面复杂度变高，绘制变慢么？

> 目前dialog 显示和隐藏采用的不是控制 Visibility 属性，因此只有在 dialog 显示时，界面复杂度才会上升

3. 屏幕旋转或其他方式导致界面重绘，会导致应用崩溃么？

> 模拟出的 dialog 和普通原生 dialog 不同，不存在界面重启，应用崩溃的情况。但 因为界面重启跟重新打开 Activity 类似，因此 弹出框dialog 会和普通的布局一样消失

## 联系方式

邮箱 `mnlin0905@gmail.com`
QQ `357638154`

## 版本记录

[版本文件](VERSIONS.md)

## License

Copyright lovingning
