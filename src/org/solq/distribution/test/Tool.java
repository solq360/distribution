package org.solq.distribution.test;

public abstract class Tool {
    public final static int count = 1000000;

    public static void printlnTime(String info, long start) {
 	long end = System.currentTimeMillis();
 	System.out.println(info + (end - start));
     }
}
