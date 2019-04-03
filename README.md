# SDialog (Android)

## 简介

SDialog是一个使用View来模拟dialog，可在界面中同时进行多个dialog弹出与关闭的第三方库；

[详细使用方法及原理解释](https://blog.csdn.net/lovingning/article/details/89001984)

## 设计思路

通过在根部局中添加一个容器：Container，控制某些 ‘自由’ 状态的 View 消失与隐藏，同时伴随着一些动画的使用，来达到模拟 Dialog 弹出与关闭 的目的；

同时定义了开放了动画接口，可自主实现其他动画效果；

## 主要功能

 1. 可由 layout 布局生成被模拟的 `dialog` 对象，用于实际操作
 2. 可同时保持多个 `dialog` 的显示或隐藏
 3. 弹出框显示或关闭时，可使用动画完成过渡效果
 
## 使用步骤（简单使用）

### 引入本库



## 版本记录

[版本文件](VERSIONS.md)