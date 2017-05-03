package com.spring.Calculator;

/**
 * Created by Administrator on 17/4/05.
 */
public class Calculator {
    public static void main(String[] args) {
        //double[] airplane = {2100,1260};
        double[] airplane = {88,158,158,158,300,300,138,45,200,158,99.8,158,158,158,18.5,105.5};
        double[] hotel = {2592,889.92,325,5525,1300};
        double[] trainOrBus = {105.5,18.5,158.5,43,26,30,45,30};
        double[] traffic = {13.5,21.9,20.5,24.4,23.6,24.7,24,17,19.5,36.6,59,19.8,24.7,45,34.85,24.7,20,20,20,50,50,50,100};
        double[] othersfee = {10,10,12};
        double sum = 0;
        double fee = 0;
        for (double d:airplane) {
            fee += d;
        }
        sum += fee;
        System.out.println("机票费发票一共"+airplane.length+"张，共"+fee+"元");
        fee = 0;
        for (double d:hotel) {
            fee += d;
        }
        sum += fee;
        System.out.println("住宿费发票一共"+hotel.length+"张，共"+fee+"元");
        fee = 0;
        for (double d:trainOrBus) {
            fee += d;
        }
        sum += fee;
        System.out.println("火车与大巴费发票一共"+trainOrBus.length+"张，共"+fee+"元");
        fee = 0;
        for (double d:traffic) {
            fee += d;
        }
        sum += fee;
        System.out.println("交通费发票一共"+traffic.length+"张，共"+fee+"元");
        fee = 0;
        for (double d:othersfee) {
            fee += d;
        }
        sum += fee;
        System.out.println("其他费发票一共"+othersfee.length+"张，共"+fee+"元");
        System.out.println("总计报销："+sum+"元");
    }
}
