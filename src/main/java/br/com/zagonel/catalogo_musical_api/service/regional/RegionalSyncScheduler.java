package br.com.zagonel.catalogo_musical_api.service.regional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegionalSyncScheduler {

    private final RegionalSyncService regionalSyncService;

    @Scheduled(fixedRate = 3600000)
    public void runSync() {
        regionalSyncService.processarSincronizacao();
    }
}
