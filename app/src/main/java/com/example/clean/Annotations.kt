package com.example.clean

object Annotations {

    private val ANNOTATION_METHOD = "annotationData"
    private val ANNOTATION_FIELDS = "declaredAnnotations"
    private val ANNOTATIONS = "annotations"

    /*
    public static void main(String ...args) {
        Greeter greetings = Greetings.class.getAnnotation(Greeter.class);
        System.err.println("Hello there, " + greetings.greet() + " !!");

        Greeter targetValue = new DynamicGreeter("Good evening");
        //alterAnnotationValueJDK8(Greetings.class, Greeter.class, targetValue);
        alterAnnotationValueJDK7(Greetings.class, Greeter.class, targetValue);

        greetings = Greetings.class.getAnnotation(Greeter.class);
        System.err.println("Hello there, " + greetings.greet() + " !!");
    }
*/

    fun alterAnnotationValueJDK7(targetClass: Class<*>, targetAnnotation: Class<out Annotation>, targetValue: Annotation) {
        try {

            val ann = targetClass::class.java.fields
            println("----------------")
            ann.forEach {
                println(it.name)
            }


            val annotations = Class::class.java.getDeclaredField(ANNOTATIONS)
            annotations.isAccessible = true

            val map = annotations.get(targetClass) as MutableMap<Class<out Annotation>, Annotation>
            println(map)
            map.put(targetAnnotation, targetValue)
            println(map)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
