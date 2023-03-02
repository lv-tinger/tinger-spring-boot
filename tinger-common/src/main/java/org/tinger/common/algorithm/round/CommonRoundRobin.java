package org.tinger.common.algorithm.round;

import org.tinger.common.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class CommonRoundRobin<T> implements RoundRobin<T> {
    private final List<T> nodes;
    private int index = 0;
    private final int size;
    private final ReentrantLock lock = new ReentrantLock();

    public CommonRoundRobin(List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            throw new RuntimeException();
        }
        this.size = list.size();
        this.nodes = new ArrayList<>(list);
    }

    @Override
    public final T get() {
        lock.lock();
        try {
            T current = nodes.get(index);
            index += 1;
            if (index >= size) {
                index = 0;
            }
            return current;
        } finally {
            lock.unlock();
        }
    }
}
