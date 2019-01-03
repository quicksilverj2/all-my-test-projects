package com.edel.messagebroker.util;

public class MBAppConstants {

    public static final String PLATFORM_NAME_ANDROID = "ANDROID";
    public static final String PLATFORM_NAME_IOS = "IOS";
    public static final String TARGET_AUDIENCE_USER = "User";

    public static final String NO_PRESENT = "NO";

    public static final String EXCEPTION = "EXCEPTION";

    public static final String SEPERATOR = ":";

    public static final String NAME_PLACEHOLDER="<NAME>";
    public static final String ORDER_TYPE_PLACEHOLDER = "<ORDER>";
    public static final String SYMBOL_PLACEHOLDER = "<SYMBOL>";
    public static final String RATIO_PLACEHOLDER = "<RATIO>";
    public static final String PRICE_PLACEHOLDER = "<RUPEE>";
    public static final String TIME_PLACEHOLDER = "<TIME>";

    public class TaskTypes{
        public static final String INITIATE = "task";
        public static final String DONE = "done";
        public static final String CLOSE = "close";
    }

    public class OrderStatus{
        public static final String OPEN = "open";
        public static final String TRIGGER_PENDING = "trigger pending";
        public static final String COMPLETE = "complete";
        public static final String REJECTED = "rejected";
        public static final String CANCELLED = "cancelled";
    }

    public class  PendingTasks{
        public static final String STARTED = "STARTED";
        public static final String COMPLETED = "COMPLETED";
        public static final String EXPIRY_TIME = "23:59:59";
    }

}
