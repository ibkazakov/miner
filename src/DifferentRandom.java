// Generate sequence of random numbers. All numbers in the sequence are DIFFERENT

import java.util.Random;

public class DifferentRandom {
    private int n; // 0...n-1
    private Random random;
    private boolean[] exceptFlags; // int[i] == true, then next random will be not i
    private int excepted;

    public DifferentRandom(int n) {
        this.n = n;
        exceptFlags = new boolean[n];
        random = new Random();
    }

    public void exceptValue(int value) {
        if (!exceptFlags[value]) {
            excepted++;
        }
        exceptFlags[value] = true;
        random.setSeed(System.nanoTime()); //update random's
    }

    public int nextValue() {
        int value = n;
        int randomNumber = random.nextInt(n - excepted);
        int counterNoExcepted = -1;
        for(int i = 0; i < n; i++) {
            if (!exceptFlags[i]) {
                counterNoExcepted++;
            }
            if (randomNumber == counterNoExcepted) {
                value = i;
                break;
            }
        }
        exceptValue(value); //if value == n after all, there are no element.Illegal argument exception
        return value;
    }

    public void initRefresh() {
        random.setSeed(System.nanoTime());
        for(int i = 0; i < n; i++) {
            exceptFlags[i] = false;
        }
        excepted = 0;
    }

    public void resetValue(int value) {
        if (exceptFlags[value]) {
                excepted--;
        }
        exceptFlags[value] = false;
        random.setSeed(System.nanoTime());
    }

    public int remain() {
        return (n - excepted);
    }
}
