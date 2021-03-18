/**
 * @author ykm
 * @date 2021/1/22 3:53 下午
 */
public class DeadLock implements Runnable {

    public DeadLock(Object a, Object b) {
        this.a = a;
        this.b = b;
    }

    Object a;
    Object b;

    @Override
    public void run() {
        synchronized (a) {
            System.out.println("我不想死锁");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (b) {
                System.out.println("我竟然没有发生死锁！");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Object a = new Object();
        Object b = new Object();
        Thread t1 = new Thread(new DeadLock(a, b));
        Thread t2 = new Thread(new DeadLock(b, a));
        t1.start();
        t2.start();
    }
}
