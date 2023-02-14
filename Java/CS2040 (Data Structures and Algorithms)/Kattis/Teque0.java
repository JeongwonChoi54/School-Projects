// Name: Jeongwon Choi
// Student No.: A0219728U

// "Problem A Teque" question from Kattis; https://nus.kattis.com/contests/xvef8u/problems/teque

import java.util.*;
import java.io.*;

class Circular{
	public int[] farr;
	public int[] barr;
	public int ffirst, fback, bfirst, bback;
	public int capacity;
	public final int INITSIZE = 1000000;
	public int fnum_int, bnum_int;


	public Circular(){
		capacity = INITSIZE;
		farr = new int[INITSIZE];
		ffirst = 0;
		fback = 0;
		fnum_int = 0;

		barr = new int[INITSIZE];
		bfirst = 0;
		bback = 0;
		bnum_int = 0;

	}

	public void push(char a, int value){
		if (fnum_int == 0){
			farr[ffirst] = value;
			fnum_int++;
		}

		else if (a == 'f'){
			if (ffirst == 0){
				ffirst = capacity -1;
			}

			else{
				ffirst --;
			}

			farr[ffirst] = value;
			fnum_int++;

			if (fnum_int > bnum_int +1){
				push('m', farr[fback]);
				if (fback == 0){
					fback = capacity -1;
				}
				else{
					fback --;
				}
				fnum_int --;
			}

		}

		else if (a == 'm'){
			if (fnum_int > bnum_int){
				if (bnum_int == 0){
					barr[bfirst] = value;
					bnum_int++;
				}
				else{
					if (bfirst == 0){
						bfirst = capacity -1;
					}
					else{
						bfirst --;
					}
					barr[bfirst] = value;
					bnum_int ++;
				}

			}

			else{

				if (fback == capacity -1){
					fback = 0;
				}
				else{
					fback ++;
				}
				farr[fback] = value;
				fnum_int ++;
			}
		}

		else{
			if (bnum_int == 0){
				barr[bback] = value;
				bnum_int ++;
			}
			else{

				if (bback == capacity -1){
					bback = 0;
				}
				else{
					bback ++;
				}
				barr[bback] = value;
				bnum_int ++;

				if (bnum_int > fnum_int){
					push('m', barr[bfirst]);
					bnum_int--;
					if (bfirst == capacity -1){
						bfirst = 0;
					}
					else{
						bfirst ++;
					}
				}
			}
		}
	}

	public void get(int index){
		if (index < fnum_int){
			int i = (ffirst + index) % capacity;
			System.out.println(farr[i]);
		}

		else{
			int i = (bfirst + index - fnum_int) % capacity;
			System.out.println(barr[i]);
		}

	}
}

public class Teque0 {
    // Fast I/) @source https://www.geeksforgeeks.org/fast-io-in-java-in-competitive-programming/

	static class FastReader {
		final private int BUFFER_SIZE = 1 << 16;
		private DataInputStream din;
		private byte[] buffer;
		private int bufferPointer, bytesRead;

		public FastReader()
		{
			din = new DataInputStream(System.in);
			buffer = new byte[BUFFER_SIZE];
			bufferPointer = bytesRead = 0;
		}

		public char next() throws IOException
		{
			char command = ' ';
			byte c = read();
			if (c == 'g'){
				command = (char) c;
			}
			while ((c = read()) != -1) {
				if (c == ' '){
					break;
				}
				if (c == 'f' || c == 'b' || c == 'm'){
					command = (char) c;
				}
			}
			return command;
			
		}

		public int nextInt() throws IOException
		{
			int ret = 0;
			byte c = read();
			while (c <= ' ') {
				c = read();
			}
			boolean neg = (c == '-');
			if (neg)
				c = read();
			do {
				ret = ret * 10 + c - '0';
			} while ((c = read()) >= '0' && c <= '9');

			if (neg)
				return -ret;
			return ret;
		}


		private void fillBuffer() throws IOException
		{
			bytesRead = din.read(buffer, bufferPointer = 0,
								BUFFER_SIZE);
			if (bytesRead == -1)
				buffer[0] = -1;
		}

		private byte read() throws IOException
		{
			if (bufferPointer == bytesRead)
				fillBuffer();
			return buffer[bufferPointer++];
		}

		public void close() throws IOException
		{
			if (din == null)
				return;
			din.close();
		}
	}


    public static void main(String[] args) throws IOException{

        FastReader s = new FastReader();
        int n = s.nextInt();
        Circular list = new Circular();

        for (int i = 0; i < n; i++){
            char command = s.next();
            int value = s.nextInt();

            if (command == 'g'){
            	list.get(value);
            }
            else{
            	list.push(command, value);
            }
        }


    }
}