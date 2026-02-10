package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;

public class PrimeFinderThread extends Thread{

	
	int a,b;
	private List<Integer> primes;
    private final Object pauseLock = new Object();
    private volatile boolean isPaused = true;

	
	public PrimeFinderThread(int a, int b) {
		super();
        this.primes = new LinkedList<>();
		this.a = a;
		this.b = b;
	}

    @Override
	public void run(){
        try {
            for (int i= a;i < b;i++){	
                checkPaused();					
                if (isPrime(i)){
                    primes.add(i);
                    System.out.println(i);
                }
            }
        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
	}

    //Lock para pausar el hilo
    public void pauseThread(){
        synchronized (pauseLock) {
            isPaused = true;
        }
    }
    
    //Lock para reanudar el hilo
    public void resumeThread(){
        synchronized (pauseLock) {
            isPaused = false;
            pauseLock.notifyAll();
        }
    }

    private void checkPaused() throws InterruptedException {
        synchronized (pauseLock) {
            while (isPaused) {
                pauseLock.wait();
            }
        }
    }
	
	boolean isPrime(int n) {
	    boolean ans;
            if (n > 2) { 
                ans = n%2 != 0;
                for(int i = 3;ans && i*i <= n; i+=2 ) {
                    ans = n % i != 0;
                }
            } else {
                ans = n == 2;
            }
	    return ans;
	}

	public List<Integer> getPrimes() {
		return primes;
	}

    public boolean isPaused() {
        return isPaused;
    }
	
}
