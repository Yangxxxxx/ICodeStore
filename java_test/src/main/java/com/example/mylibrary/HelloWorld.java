package com.example.mylibrary;

public class HelloWorld{
    private static int[] Data1 = new int[]{1, 5, 3, 2, 6, 7, 3, 9, 10};
    private static int[] Data2 = new int[]{1, 0, 3, 2, 2, 7, 3, 20, 10};

    public static void  main(String argc[]){

        sortMaoPao(Data1);
        printIntArrary(Data1);

        print("search: "+halfSearch(Data1, 9));

        sortMaoPao(Data2);
        printIntArrary(Data2);
        print("search: "+halfSearch(Data2, 5));



        int i = 5;
        while (i-- > 1){
            print(i+"");
        }
    }

    public static void sortMaoPao(int[] org){
        if(org.length < 2) return;
        int endIndex = org.length - 1;
        for(; endIndex > 0; endIndex--) {
            for (int i = 0; i < endIndex; i++) {
                if (org[i] > org[i + 1]) {
                    int tmp = org[i];
                    org[i] = org[i + 1];
                    org[i + 1] = tmp;
                }
            }
        }
    }

    public static int halfSearch(int[] org, int wantValue){
        int startIndex = 0;
        int endIndex = org.length - 1;

        while (endIndex > startIndex){
            int middleIndex = (endIndex + startIndex) / 2;
            if(org[middleIndex] == wantValue) return middleIndex;
            if(org[middleIndex] > wantValue) endIndex = middleIndex - 1;
            if(org[middleIndex] < wantValue) startIndex = middleIndex + 1;
        }
        return -1;
    }


    public static void print(String text){
        System.out.println(text);
    }

    public static void printIntArrary(int[] arr){
        for (Integer item: arr){
            System.out.print(" "+item);
        }
        print("\n");
    }

}