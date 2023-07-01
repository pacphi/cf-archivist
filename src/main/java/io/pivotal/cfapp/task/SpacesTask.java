package io.pivotal.cfapp.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import io.pivotal.cfapp.client.ArchivistClient;
import io.pivotal.cfapp.event.SpacesRetrievedEvent;
import io.pivotal.cfapp.event.TimeKeepersRetrievedEvent;
import io.pivotal.cfapp.service.SpaceService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Component
public class SpacesTask implements ApplicationListener<TimeKeepersRetrievedEvent> {

    private final ArchivistClient client;
    private final SpaceService service;
    private ApplicationEventPublisher publisher;

    @Autowired
    public SpacesTask(
            ArchivistClient client,
            SpaceService service,
            ApplicationEventPublisher publisher) {
        this.client = client;
        this.service = service;
        this.publisher = publisher;
    }

    public void collect() {
        log.info("SpaceTask started");
        client.getSpaces()
            .flatMapMany(Flux::fromIterable)
            .flatMap(service::save)
            .thenMany(service.findAll())
            .collectList()
            .subscribe(
                result -> {
                    publisher.publishEvent(new SpacesRetrievedEvent(this).spaces(result));
                    log.info("SpaceTask completed");
                    log.trace("Retrieved {} organizations", result.size());
                },
                error -> {
                    log.error("SpaceTask terminated with error", error);
                }
            );
    }

    @Override
    public void onApplicationEvent(TimeKeepersRetrievedEvent event) {
        collect();
    }

}