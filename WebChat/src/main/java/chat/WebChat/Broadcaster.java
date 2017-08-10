package chat.WebChat;


import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Broadcaster implements Serializable {
    static ExecutorService executorService =
        Executors.newSingleThreadExecutor();

    public interface BroadcastListener {
        void receiveBroadcast(StringBuilder message);
    }

    private static LinkedList<BroadcastListener> listeners =
        new LinkedList<BroadcastListener>();

    public static synchronized void register(
            BroadcastListener listener) {
        listeners.add(listener);
        System.out.println(" listeners.size() = "+listeners.size());
    }

    public static synchronized void unregister(
            BroadcastListener listener) {
        listeners.remove(listener);
 if(listeners.size()>100) {
	 listeners.clear();
 }

    }

    public static synchronized void broadcast(
            final StringBuilder message) {
        for (final BroadcastListener listener: listeners)
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    listener.receiveBroadcast(message);
                }
            });
    }
}