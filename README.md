# DragProgressView
## 1.DragProgressView是什么？
DragProgressView是一个可以拖拽含有进度条View。
## 2.DragProgressView演示:
![image](https://github.com/XZwjw/DragProgressView/blob/master/dragProgress.gif) 

## 3.使用方法：
        ```
        <com.xingzhe.test.DrawProgressCircleView
        android:id="@+id/drawProgressCircleView"
        android:layout_width="95dp"
        android:layout_height="95dp"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:imageWidth="20"
        app:imageHeight="20"
        app:betweenWidth="15"
        app:progressWidth="10"
        app:outEdgeWidth="5"
        app:image="@drawable/qaq"
        />
        ```
        
## 4.使用说明：

属性|类型|功能
--|:--:|--:
image|reference|图片
imageHeight|integer|图片高度
imageWidth|integer|图片宽度
betweenWidth|integer|图片与进度条内边缘之间的间距
betweenColor|color|图片与进度条内边缘之间的颜色
progressLoadColor|color|进度条进度颜色
progressUnLoaderColor|color|进度条背景颜色
progressWidth|integer|进度条宽度
outEdgeWidth|integer|外边缘宽度
startAngle|float|进度条开始角度
sweepAngle|float|进度条走过的角度




