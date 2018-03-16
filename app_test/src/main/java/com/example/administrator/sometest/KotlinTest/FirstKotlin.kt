package com.example.administrator.sometest.KotlinTest

/**
 * Created by Administrator on 2018/1/17 0017.
 */
class FirstKotlin(var org1 :Int,var org2: Int){
    var time = 9


    fun add(a: Int, b: Int):Int{
        return a + b + org1 + org2
    }

    var bigNum = 9

    fun getNum():Int{
        return bigNum
    }

    fun setNum(num: Int){
        bigNum = num
    }

}
