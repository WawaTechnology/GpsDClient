package com.example.unsan.gpsdclient;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void print() {
        int x = 7 / 2;
        System.out.println(x);
        List<String> charList = new ArrayList();
        for (int i = 65; i < 91; i++) {
            char c = (char) i;
            System.out.println(String.valueOf(c));
        }
    }

    @Test
    public void divisibleSumPairs() {
        // Complete this function
        int n = 6;
        int ar[] = {1, 3, 2, 6, 1, 2};
        int k = 3;

        int cnt = 0;


        for (int i = 0; i <= n - 2; i++) {
            for (int j = i + 1; j <= n - 1; j++) {

                System.out.println("values at index"+i+" "+j+" " +ar[i]+" "+ar[j]);


                int sum=ar[i]+ar[j];


                if (sum % k == 0) {
                    System.out.println("sum"+sum);


                    cnt=cnt+1;

                }

            }




        }
        System.out.print(cnt);
    }
}