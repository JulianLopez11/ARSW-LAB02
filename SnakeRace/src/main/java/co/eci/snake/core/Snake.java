package co.eci.snake.core;

import java.util.ArrayDeque;
import java.util.Deque;

public final class Snake {
  private final Deque<Position> body = new ArrayDeque<>();
  private volatile Direction direction;
  private int maxLength = 5;
  private long deathTimestamp = -1;

  private Snake(Position start, Direction dir) {
    body.addFirst(start);
    this.direction = dir;
  }

  public static Snake of(int x, int y, Direction dir) {
    return new Snake(new Position(x, y), dir);
  }

  public Direction direction() { return direction; }

  public void turn(Direction dir) {
    if ((direction == Direction.UP && dir == Direction.DOWN) ||
        (direction == Direction.DOWN && dir == Direction.UP) ||
        (direction == Direction.LEFT && dir == Direction.RIGHT) ||
        (direction == Direction.RIGHT && dir == Direction.LEFT)) {
      return;
    }
    this.direction = dir;
  }

  public Position head() { return body.peekFirst(); }

  public synchronized boolean isAlive() {
    return deathTimestamp == -1;
  }

  public synchronized int aliveSize() {
    return isAlive() ? body.size() : -1;
  }


  public synchronized void snakeDie() {
    if (deathTimestamp == -1) {
        deathTimestamp = System.currentTimeMillis();
    }
  }

  public synchronized long getDeathTime() {
      return deathTimestamp;
  }

  public Deque<Position> snapshot() { return new ArrayDeque<>(body); }

  public void advance(Position newHead, boolean grow) {
    body.addFirst(newHead);
    if (grow) maxLength++;
    while (body.size() > maxLength) body.removeLast();
  }
}
