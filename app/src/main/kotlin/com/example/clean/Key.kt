package com.example.clean

import kotlin.reflect.KClass

interface Key

/*fun Key.getLayoutId(): Int {
    return (this as? HasLayout)?.layoutId ?:
        getAnnotation(Layout::class)?.value ?:
        getAnnotation(WithLayout::class).value
}

fun Key.getTransition() = getAnnotation(WithTransition::class)?.value

fun Key.getViewModel(context: Context) =
    getAnnotation(WithViewModel::class)?.value
        ?.run { DaggerComponents.getDependency(context.getComponent()!!, java) }

fun Key.getPresenter(keyComponent: Any) =
    javaClass.kotlin.getAnnotationIncludingSupers(WithPresenter::class)?.value
        ?.run { DaggerComponents.getDependency(keyComponent, java) }

fun Key.getPresenter(context: Context) =
    context.getComponent<Any>()?.run { getPresenter(this) }

fun Key.getLayoutPane() =
    getAnnotation(LayoutPane::class)?.value ?: 1

fun Key.getComponentClass() = javaClass.kotlin.getAnnotationIncludingSupers(WithComponent::class)?.value*/

private fun <T : Annotation> KClass<Key>.getAnnotationIncludingSupers(annotation: KClass<T>): T? =
    java.getAnnotation(annotation.java) ?:
        (java.superclass as? Class<Key>)?.kotlin?.getAnnotationIncludingSupers(annotation)

fun <T : Annotation> Key.getAnnotation(annotation: KClass<T>) =
    javaClass.getAnnotation(annotation.java)
