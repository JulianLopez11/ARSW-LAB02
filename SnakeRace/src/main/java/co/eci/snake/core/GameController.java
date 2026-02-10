package co.eci.snake.core;

public class GameController {

    private volatile boolean paused = false;
    private int pausedSnakes = 0;
    private final int totalSnakes;
    private final Object lock = new Object();

    public GameController(int totalSnakes) {
        this.totalSnakes = totalSnakes;
    }

    public void awaitGamePaused() {
        synchronized (lock) {
            if (!paused) return;

            pausedSnakes++;
            lock.notifyAll(); 

            while (paused) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void waitAllSnakesPaused() {
        synchronized (lock) {
            while (pausedSnakes < totalSnakes) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void pauseGame() {
        synchronized (lock) {
            paused = true;
            pausedSnakes = 0;
        }
    }

    public void resumeGame() {
        synchronized (lock) {
            paused = false;
            pausedSnakes = 0;
            lock.notifyAll();
        }
    }
}
