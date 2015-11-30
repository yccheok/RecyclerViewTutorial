package org.yccheok.recyclerviewtutorial;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by yccheok on 1/12/2015.
 */
public abstract class UniqueIdGenerator {
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private final long id = NEXT_ID.getAndIncrement();

    public long getId() {
        return id;
    }
}
