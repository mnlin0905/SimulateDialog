# versions

# V 1.0.1

> 时间 : 2019.04.09 11:30:00

 1. 修复在根部局修改为IncludeDialogViewGroup 后,Preview 无法正常工作的问题
 2. 修复之前在IncludeDialogViewGroup 伴生类中,将变量设置为后端的问题

# V 1.0.0
    
> 时间 : 2019.04.03 18:00:00

 1. 添加基本库,实现 `view` 模拟 `dialog` 进行弹出
 2. 提供简便工具类:`DefaultSimulateDialogImpl` ; 通过该类通过一行代码就可生成 `dialog` 布局
 3. 提供默认的动画实现:**透明度动画**
 4. `dialog` 中关闭等模拟事件已进行防 `抖动` 处理