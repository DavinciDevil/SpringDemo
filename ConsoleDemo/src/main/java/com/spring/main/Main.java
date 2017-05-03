package com.spring.main;


import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Administrator on 17/3/10.
 */
public class Main {

    public static void  main(String[] args){
        List<Integer> integerList = new ArrayList<Integer>();
        List<Integer> reverseList = new ArrayList<Integer>();
        for (int i = 0; i < 10; i++){
            integerList.add(i);
        }
        for (int i: integerList) {
            System.out.print(i);
        }
        System.out.println();
        ListIterator<Integer> it = integerList.listIterator();

        while (it.hasNext()){
            Integer a = it.next();
            //reverseList.add(a);
            if (a == 5){
                it.add(123);
            }
            System.out.print(a + " ");

        }

        ListIterator<Integer> it2 = integerList.listIterator();
        System.out.println();
        while (it2.hasNext()){

            System.out.print(it2.next() + " ");

        }
        System.out.println();

        it = integerList.listIterator(integerList.size());

        while(it.hasPrevious()){
            Integer a = it.previous();
            if (a == 5 ){
                it.add(567);
            }

            System.out.print(a + " ");
        }
        return ;

    }

}
