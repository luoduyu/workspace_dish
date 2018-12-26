import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-26 14:31
 * @version 1.0
 */
public class Test {
    public static void main(String[] args) {
        System.out.println(TimeUnit.DAYS.ordinal());
        System.out.println(TimeUnit.DAYS.name());
        System.out.println(TimeUnit.valueOf(TimeUnit.DAYS.name()));
    }
}
