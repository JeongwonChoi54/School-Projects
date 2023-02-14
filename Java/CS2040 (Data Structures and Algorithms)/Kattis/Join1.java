// Name: Jeongwon Choi
// Student No.: A0219728U

// "Join Strings" question from Kattis; https://nus.kattis.com/problems/joinstrings

import java.util.*;
import java.io.*;

public class Join1 {
    // Fast I/) @source https://www.geeksforgeeks.org/fast-io-in-java-in-competitive-programming/
    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        public FastReader()
        {
            br = new BufferedReader(
                new InputStreamReader(System.in));
        }

        String next()
        {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() { return Integer.parseInt(next()); }

        long nextLong() { return Long.parseLong(next()); }

        double nextDouble()
        {
            return Double.parseDouble(next());
        }

        String nextLine()
        {
            String str = "";
            try {
                str = br.readLine();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return str;
        }
    }

    public static String recursion(LinkedList <Integer>[] l, StringBuilder[] words, int i){
            if (l[i] == null){
                return "";
            }

            else{
                while (l[i].size() != 0){
                    int j = l[i].poll();
                    System.out.print(words[j].toString());

                    if(l[j]!= null){
                        l[j].poll();
                    }

                    recursion(l, words, j);

                }
            }
            return "";
        }


    public static void main(String[] args){
        FastReader s = new FastReader();
        int n = s.nextInt();

        StringBuilder[] words = new StringBuilder[n];
        boolean[] bool = new boolean[n];

        for (int i = 0; i <n; i++){

            words[i] = new StringBuilder(s.nextLine());
        }

        if (n == 1){
            System.out.print(words[0].toString());
        }

        else{

            LinkedList<Integer> [] l = new LinkedList [n];
            int last = 0;

            for (int i = 0; i < n-1; i++){

                int a = s.nextInt()-1;
                int b = s.nextInt()-1;

                if (i == n-2){
                    last = a;
                }

                if (l[a] == null){
                    l[a] = new LinkedList<Integer>();
                    l[a].add(a);

                }

                l[a].add(b);

            }

            System.out.print(words[l[last].poll()].toString());
            recursion(l, words, last);


        }


    }
}
