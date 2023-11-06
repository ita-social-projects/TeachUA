package com.example.teachua_android.presentation.toolbar

abstract class DynamicOffsetScrollFlagState(
    heightRange: IntRange,
    scrollValue: Int
) : ScrollFlagState(heightRange, scrollValue) {

    protected abstract var scrollOffset: Float

}