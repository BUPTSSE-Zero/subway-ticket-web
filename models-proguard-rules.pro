-keep class com.subwayticket.model.** {
    void set*(***);
    *** get*();
    *** is*();
    <init>(...);
}

-keep class com.subwayticket.database.model.** {
    void set*(***);
    *** get*();
    *** is*();
    <init>(...);
}

-keepclassmembers class * {
    public static *;
    private *;
}

-keepattributes LocalVariableTable, LocalVariableTypeTable, Signature
