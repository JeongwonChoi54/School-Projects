/**
 * Name: Jeongwon Choi
 * Matric. No: A0219728U
 */

//For each factorials, stores the number of primes composing the factorial
//where the index of the numPrimes and the index of corresponding primes is the same
class Primes{
    int[] primes;
    long[] numPrimes;

    public Primes(){
        this.primes = new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47};
        this.numPrimes = new long[15];

    }

    //Pre-Cond: k >= 3 and Primes x refers to the composition of (k-1)!
    //Post-Cond: returns the 'Primes' of k!
    public Primes setPrimes(Primes x, int k){
        Primes output = new Primes();
        int index = 0;

        for (int i = 0; i < 15; i++){
            output.numPrimes[i] = x.numPrimes[i];
        }

        while (k > 1){
            while (k % output.primes[index] == 0){
                k = k / output.primes[index];
                output.numPrimes[index] += 1;
            }
            index += 1;
        }

        return output;
    }
}

//Stores array of 'Primes' upto 50 to reduce redundant calculation:
//x! as 'Primes' at factorial[x]
class Factorials{
    Primes[] factorials;
    int cur;

    public Factorials(){
        this.factorials = new Primes[51];
        this.cur = 1;
        factorials[0] = new Primes();
        factorials[1] = new Primes();
    }


    //As cur keeps track of the max number of 'Primes' stored in factorial
    //if the wanted value x is not available, add 'Primes' of factorials to
    //'factorials' upto x
    //Pre-Cond: x >= 0
    //Post-Cond: Returns the 'Primes' of value x
    public Primes get(int x){
        if (x > cur){
            for (int i = (cur+1); i <= x; i++){
                Primes num = new Primes();
                factorials[i] = num.setPrimes(factorials[i-1], i);
            }
        }

        cur = x;
        return factorials[x];
    }
}

public class Constellations {
  public static void main(String args[]) {
    Kattio io = new Kattio(System.in);
    int stars = io.getInt();
    int min_group = io.getInt();
    int max_group = io.getInt();
    Factorials fac = new Factorials();

    System.out.println(Configs(stars, min_group, max_group, fac));

  }

  //Calculates x/(y*z) by subtracting the sum of 'numPrimes' of y and z from 'numPrimes' of x
  //Pre-Cond: x, y, z are 'Primes' and y+z = x
  //Post-Cond: Returns (x choose y) modulo 1_000_000_007
  public static long Combination(Primes x, Primes y, Primes z){

    int mod = 1_000_000_007;
    long[] output_numPrimes = new long[15];
    long output = 1;

    for (int i = 0; i < 15; i++){
        output_numPrimes[i] = x.numPrimes[i] - y.numPrimes[i] - z.numPrimes[i];
        
        while (output_numPrimes[i] != 0){
            output = (output * x.primes[i])%mod;
            output_numPrimes[i] -= 1;
        }
    }

    return output;

  }

  //Pre-Cond: 1<= min <= max <= stars <= 50
  //Post-Cond: Returns the (total number of valid configurations of stars between min and max) % 1_000_000_007
  public static long Configs(int stars, int min, int max, Factorials fac){

    int mod = 1_000_000_007;

    //Base-Cases for termination of recursion
    if (stars <= min || max < 0){
        return 1;
    }

    //Loop through possible configurations and simplify till base-case
    else{
        long output = 0;
        for (int i = min; i <= max; i++){
            output = (output + Combination(fac.get(stars), fac.get(i), fac.get(stars-i)) * (Configs(stars-i, min, Math.min(max, stars-i), fac))%mod)%mod;
        }
        return output;

    }
  }

}
