-keep class com.subwayticket.model.** {
    void set*(***);
    *** get*();
    <init>(...);
}

-keep class com.subwayticket.database.model.** {
    void set*(***);
    *** get*();
    <init>(...);
}

-keepclassmembers class * {
    public static *;
    private *;
}
