import Interfase.Panel;

public class PlayThread extends Thread {
    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            Panel panel = new Panel();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            while (true) {
                Panel.movement();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    });

    Thread thread1 = new Thread(new Runnable() {
        @Override
        public void run() {
            Panel.panelStatistics();
            for (int i = 0; i < 100; i++) {
                Panel.statistics();
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    });
    Thread thread2 = new Thread(new Runnable() {
        @Override
        public void run() {
            Panel.grassGrowth();
        }
    });

    public void run() {
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        thread1.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        thread2.start();
    }
}
