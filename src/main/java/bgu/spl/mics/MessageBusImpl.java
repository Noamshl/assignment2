package bgu.spl.mics;

import java.util.concurrent.*;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
    /**
     * We decided to use {@link ConcurrentHashMap} because we want that the data structures for this class will be thread-safe,
     * and we want more concurrency by using the methods.{@link ConcurrentHashMap} has Atomic-method putIfAbsent(), for adding key and value,
     * and the map divide to parts, that each part has different lock, what make it more concurrent than object lock.
     * we also decided to use {@link ConcurrentLinkedQueue}  using its thread-safe method "add", instead of using a scope of "synchronize",
     * for more readable code.
     */
    private ConcurrentHashMap<MicroService, BlockingQueue<Message>> queueList;
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<MicroService>> eventList;
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<MicroService>> broadcastList;
    private ConcurrentHashMap<Event, Future> futureList;


    private static class SafeMessageBus {
        private static MessageBusImpl instance = new MessageBusImpl();
    }

    private MessageBusImpl() {
        queueList = new ConcurrentHashMap<>();
        eventList = new ConcurrentHashMap<>();
        broadcastList = new ConcurrentHashMap<>();
        futureList = new ConcurrentHashMap<>();
    }

    public static MessageBusImpl getInstance() {
        return SafeMessageBus.instance;
    }

    /**
     * this method is called by {@link MicroService} to add itself into an event list.
     * by using {@link ConcurrentHashMap} , this method uses an Atomic method (putIfAbsent) to add new event type key.
     * by using {@link ConcurrentLinkedQueue}, this method adds the input {@link MicroService} to the list of the event type
     * the add method of {@link ConcurrentLinkedQueue} is blocking and thus thread-safe.
     *
     * @param type The type to subscribe to,
     * @param m    The subscribing micro-service.
     * @param <T>
     */
    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
        eventList.putIfAbsent(type.getSimpleName(), new ConcurrentLinkedQueue<>());
        ConcurrentLinkedQueue<MicroService> roundRobinQueue = eventList.get(type.getSimpleName());
        roundRobinQueue.add(m);
    }

    /**
     * this method is called by {@link MicroService} to add itself into a broadcast list.
     * by using {@link ConcurrentHashMap} , this method uses an Atomic method (putIfAbsent) to add new broadcast type key.
     * by using {@link ConcurrentLinkedQueue}, this method adds the input {@link MicroService} to the list of the broadcast type
     * the add method of {@link ConcurrentLinkedQueue} is blocking and thus thread-safe.
     *
     * @param type The type to subscribe to.
     * @param m    The subscribing micro-service.
     */
    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
        broadcastList.putIfAbsent(type.getSimpleName(), new ConcurrentLinkedQueue<>());
        broadcastList.get(type.getSimpleName()).add(m);
    }

    /**
     * this method is called by {@link MicroService} to complete an event.
     *
     * @param e      The completed event.
     * @param result The resolved result of the completed event.
     * @param <T>
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> void complete(Event<T> e, T result) {
        futureList.get(e).resolve(result);
    }

    /**
     * this method is called by {@link MicroService} to send a broadcast for every {@link MicroService} that is subscribed
     * to that type of broadcast.
     *
     * @param b The message to added to the queues.
     */
    @Override
    public void sendBroadcast(Broadcast b) {
        for (MicroService registered : broadcastList.get(b.getClass().getSimpleName())) {
            queueList.get(registered).add(b);
        }
    }

    /**
     * this method is called by {@link MicroService} to send an event for one {@link MicroService} that is subscribed
     * to that type of event. also, return the future of that event.
     * this method prevents multi-threading usage on the same event queue(round-robin queue).
     *
     * @param e   The event to add to the queue.
     * @param <T>
     * @return
     */
    @Override
    public <T> Future<T> sendEvent(Event<T> e) {

        ConcurrentLinkedQueue<MicroService> roundRobinQueue = eventList.get(e.getClass().getSimpleName());
        Future<T> future;
        MicroService handled;
        //Added to handle at least 1 event-sender thread
        synchronized (roundRobinQueue) {
            handled = roundRobinQueue.poll();
            if (handled == null)
                return null;
            future =new Future<>();
            futureList.putIfAbsent(e, future);
            queueList.get(handled).add(e);
            roundRobinQueue.add(handled);
        }
        return future;
    }

    /**
     * this method is called by {@link MicroService} to create for it a message queue.
     *
     * @param m the micro-service to create a queue for.
     */
    @Override
    public void register(MicroService m) {
        queueList.putIfAbsent(m, new LinkedBlockingQueue<Message>());
    }

    /**
     * this method is called by {@link MicroService} to remove itself from every list/queue that it appears in.
     *
     * @param m the micro-service to unregister.
     */
    @Override
    public void unregister(MicroService m) {
        queueList.remove(m);
        eventList.forEach((key, value) -> {
            if (value.contains(m))
                value.remove(m);
        });
        broadcastList.forEach((key, value) -> {
            if (value.contains(m))
                value.remove(m);
        });

    }

    /**
     * this method is called by {@link MicroService} to get a new message from its queue if exist
     * if not this method will block the {@link MicroService} until it gets a new message
     *
     * @param m The micro-service requesting to take a message from its message
     *          queue.
     * @return
     * @throws InterruptedException
     */
    @Override
    public Message awaitMessage(MicroService m) throws InterruptedException {
        //take is a special method of blocking queue
        return queueList.get(m).take();
    }
}