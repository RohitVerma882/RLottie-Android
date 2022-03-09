package com.itsrohit.rlottie;

import android.os.SystemClock;
import android.util.SparseIntArray;
import java.util.LinkedList;

import androidx.annotation.UiThread;

public class DispatchQueuePool {

    private LinkedList<DispatchQueue> queues = new LinkedList<>();
    private SparseIntArray busyQueuesMap = new SparseIntArray();
    private LinkedList<DispatchQueue> busyQueues = new LinkedList<>();
    
	private int maxCount;
    private int createdCount;
    private int guid;
    private int totalTasksCount;
    private boolean cleanupScheduled;

    private Runnable cleanupRunnable = new Runnable() {
        @Override
        public void run() {
            if (!queues.isEmpty()) {
                long currentTime = SystemClock.elapsedRealtime();
                for (int a = 0, N = queues.size(); a < N; a++) {
                    DispatchQueue queue = queues.get(a);
                    if (queue.getLastTaskTime() < currentTime - 30000) {
                        queue.recycle();
                        queues.remove(a);
                        createdCount--;
                        a--;
                        N--;
                    }
                }
            }
			
            if (!queues.isEmpty() || !busyQueues.isEmpty()) {
                RLottie.runOnUIThread(this, 30000);
                cleanupScheduled = true;
            } else {
                cleanupScheduled = false;
            }
        }
    };

    public DispatchQueuePool(int count) {
        maxCount = count;
        guid = RLottie.random.nextInt();
    }

    @UiThread
    public void execute(final Runnable runnable) {
        final DispatchQueue queue;
        if (!busyQueues.isEmpty() && (totalTasksCount / 2 <= busyQueues.size() || queues.isEmpty() && createdCount >= maxCount)) {
            queue = busyQueues.remove(0);
        } else if (queues.isEmpty()) {
            queue = new DispatchQueue("DispatchQueuePool" + guid + "_" + RLottie.random.nextInt());
            queue.setPriority(Thread.MAX_PRIORITY);
            createdCount++;
        } else {
            queue = queues.remove(0);
        }
		
        if (!cleanupScheduled) {
            RLottie.runOnUIThread(cleanupRunnable, 30000);
            cleanupScheduled = true;
        }
		
        totalTasksCount++;
        busyQueues.add(queue);
        int count = busyQueuesMap.get(queue.index, 0);
        busyQueuesMap.put(queue.index, count + 1);
        queue.postRunnable(new Runnable() {
				@Override
				public void run() {
					runnable.run();
					
					RLottie.runOnUIThread(new Runnable() {
							@Override
							public void run() {
								totalTasksCount--;
								int remainingTasksCount = busyQueuesMap.get(queue.index) - 1;
								if (remainingTasksCount == 0) {
									busyQueuesMap.delete(queue.index);
									busyQueues.remove(queue);
									queues.add(queue);
								} else {
									busyQueuesMap.put(queue.index, remainingTasksCount);
								}
							}
						});
				}
			});
    }
}

