package com.qstarter.core.event;

import java.nio.file.Path;

/**
 * @author peter
 * date: 2019-09-23 17:17
 **/
public class DeleteFileEvent extends BaseEvent<Path> {
    /**
     * Create a new ApplicationEvent.
     *
     * @param data the object on which the event initially occurred (never {@code null})
     */
    public DeleteFileEvent(Path data) {
        super(data);
    }
}
