package com.aps.yinghai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BaseTest {

    public static void main(String[] args) {
        test2();
    }

    public static void test2(){
        ArrayList<Integer> objects = new ArrayList<>();
        objects.add(1);
        objects.add(2);
        objects.add(3);
        objects.add(4);
        List<Integer> collect = objects.stream().sorted((t1, t2) -> t2-t1).collect(Collectors.toList());
        System.out.println(collect);

    }

    public static void test1(){
        int a = -2;
        a++;
        a = 0-a;
        System.out.println(a);

    }

    public static void testCalc(){
        int a = 0 << Integer.SIZE-3;
        System.out.println(a);
        System.out.println("------------");
        int b = 1 << Integer.SIZE-3;
        System.out.println(b);
        System.out.println("------------");
    }

    public static void testSort(){
        List<int[]> ints = Arrays.asList(new int[]{100, 10}, new int[]{400, 10}, new int[]{300, 7}, new int[]{600, 10}, new int[]{300, 3}, new int[]{300, 10});
        List<int[]> collect = ints.stream().sorted((i1, i2) -> {
            int i = i1[0] - i2[0];
            if (i==0){
                return i1[1] - i2[1];
            }else{
                return i;
            }
        }).collect(Collectors.toList());
        for (int[] ints1 : collect) {
            System.out.println(ints1[0] + "--" + ints1[1]);
        }
    }

}
