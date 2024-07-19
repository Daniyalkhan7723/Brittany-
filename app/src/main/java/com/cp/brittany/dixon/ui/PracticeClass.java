package com.cp.brittany.dixon.ui;


public class PracticeClass {

    int[] arr = {5, 2, 8, 9, 1, 7};

    public void getSecondLargestValueWithSortingInDescendingOrder() {
        int temp = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                if (arr[i] < arr[j]) {
                    temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
        System.out.println("Second largest number " + arr[1]);
    }

    public void getSecondLargestValueWithMinMaxValue() {
        int largest_value = Integer.MAX_VALUE;
        int second_largest_value = Integer.MAX_VALUE;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > largest_value) {
                second_largest_value = largest_value;
                largest_value = arr[i];
            } else if (arr[i] > second_largest_value && arr[i] != largest_value) {
                second_largest_value = arr[i];
            }
        }
        System.out.println("Second largest number " + second_largest_value);

    }

    public void reverseString() {
        String originalString = "Hello";
        int length = originalString.length();

        StringBuilder revString = new StringBuilder();
        for (int i = length - 1; i > 0; i--) {
            revString.append(originalString.charAt(i));
        }
        System.out.println(revString);

    }

    public void reverseStringWithStringBuilder() {
        String originalString = "Hello, World!";
        String reversedString = new StringBuilder(originalString).reverse().toString();
        System.out.println(reversedString);
    }

    private void reverseArray() {
        int[] list = {5, 2, 8, 9, 1, 7};
        int temp = 0;
        for (int i = 0; i < list.length / 2; i++) {
            temp = list[i];
            list[i] = list[list.length - i - 1];
            list[list.length - i - 1] = temp;
        }
    }

    private void reverseList() {
        int[] list = {5, 2, 8, 9, 1, 7};
        int[] reversedArray = {5, 2, 8, 9, 1, 7};

        for (int i = list.length - 1; i > 0; i--) {

        }

    }

//    public class MySingletonClass{
//        public static MySingletonClass Instance;
//
//        public static MySingletonClass getInstance(){
//            if (Instance==null){
//                Instance=new MySingletonClass();
//            }
//            return Instance;
//        }
//    }

}
