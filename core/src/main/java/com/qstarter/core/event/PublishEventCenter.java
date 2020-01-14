package com.qstarter.core.event;

import com.qstarter.core.model.SmsSendResult;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author peter
 * date: 2019-09-23 17:19
 **/
@Component
public class PublishEventCenter {

    private static ApplicationEventPublisher publisher;


    public PublishEventCenter(ApplicationEventPublisher publisher) {
        PublishEventCenter.publisher = publisher;
    }

    public static ApplicationEventPublisher getPublisher() {
        return publisher;
    }


    public static void publishDeleteFileEvent(String filePath) {
        if (filePath == null) return;

        Path path = Paths.get(filePath);
        publisher.publishEvent(new DeleteFileEvent(path));
    }


    public static void publishSmsSendResultEvent(SmsSendResult result){
        publisher.publishEvent(new SmsSendResultEvent(result));
    }
}
