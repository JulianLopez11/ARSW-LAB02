/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;

/**
 *
 */
public class Control extends Thread {
    
    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 30000000;
    private final static int TMILISECONDS = 5000;

    private final int NDATA = MAXVALUE / NTHREADS;

    private PrimeFinderThread pft[];
    private final Object pauseLock = new Object();
    
    private Control() {
        super();
        this.pft = new  PrimeFinderThread[NTHREADS];

        int i;
        for(i = 0;i < NTHREADS - 1; i++) {
            PrimeFinderThread elem = new PrimeFinderThread(i*NDATA, (i+1)*NDATA);
            pft[i] = elem;
        }
        pft[i] = new PrimeFinderThread(i*NDATA, MAXVALUE + 1);
    }
    
    public static Control newControl() {
        return new Control();
    }

    @Override
    public void run() {
        for(int i = 0;i < NTHREADS;i++ ) {
            pft[i].start();
        }

        Scanner sc = new Scanner(System.in);
        while (true){
            try{
                
                Thread.sleep(TMILISECONDS);
                for(PrimeFinderThread t: pft){
                    t.pauseThread();
                }

                int totalPrimes = 0;

                for(PrimeFinderThread t: pft){
                    totalPrimes += t.getPrimes().size();
                }

                System.out.println("Total primes found: " + totalPrimes);
                System.out.println("Press Enter to continue");
                sc.nextLine();
                for(PrimeFinderThread t: pft){
                    t.resumeThread();
                }
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
    }
    
}
