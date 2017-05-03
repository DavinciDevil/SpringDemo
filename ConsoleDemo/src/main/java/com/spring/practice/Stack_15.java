package com.spring.practice;


import java.util.*;

/**
 * Created by Administrator on 17/3/19.
 */
public class Stack_15 {
    public static void main(String[] args) {
        for(Map.Entry entry : System.getenv().entrySet()){
            System.out.println(entry.getKey() + ":" +  entry.getValue());
        }
        String str = "education should eschew obfuscation";
        List<String> list = Arrays.asList(str.split(""));
        PriorityQueue<String> stringpq = new PriorityQueue<String>(list);
        printPQ(stringpq);
        stringpq = new PriorityQueue<String>(list.size(),Collections.<String>reverseOrder());
        stringpq.addAll(list);
        printPQ(stringpq);

    }
    public static void printPQ(Queue queue){
        while (queue.peek() != null){
            System.out.print(queue.remove() + " ");
        }
        System.out.println();
    }
}
