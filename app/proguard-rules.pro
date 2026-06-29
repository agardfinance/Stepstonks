-keep class com.stepstonks.app.data.local.database.entity.** { *; }
-keep class com.stepstonks.app.domain.model.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
