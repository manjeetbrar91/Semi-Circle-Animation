# Semi-Circle-Animation
Animate View Around Semi Circle on corner of fragment/activity

Usage

In Layout XML File

    <com.mj.semicircleanimation.SemiCircleAnimationView
          android:id="@+id/customView"
          android:layout_width="150dp"
          android:layout_height="150dp"
          android:background="@android:color/transparent"
          app:anim_duration="4000"
          app:animated_circle_color="@color/cardview_dark_background"
          app:animated_circle_width="16dp"
          app:inner_circle_color="@color/colorPrimary"
          app:layout_constraintStart_toStartOf="parent"
          app:layout_constraintTop_toTopOf="parent"
          app:position="top_left"
          app:primary_circle_border_color="@color/colorPrimaryDark" />

Attributes
1. inner_circle_color : fill color of inner circle
2. primary_circle_border_color : outer circle border color
3. primary_circle_border_width: outer circle border stroke width
4. anim_duration : Animation duration in seconds
5. animated_circle_width : Width of smallest circle which will animate.
6. position = top_left, top_right, bottom_left, bottom_right







