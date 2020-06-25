package com.greycells.dateone.server.atmosphere;

import org.atmosphere.config.service.Disconnect;
import org.atmosphere.config.service.ManagedService;
import org.atmosphere.config.service.Post;
import org.atmosphere.config.service.Ready;
import org.atmosphere.cpr.AtmosphereRequest;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereResourceFactory;
import org.atmosphere.cpr.HeaderConfig;

import com.google.inject.Singleton;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import javax.inject.Inject;

/** Super simple managed echo application that use two broadcaster for pushing data back to the client.
 */
@Singleton
@ManagedService(path = "/atmosphere/dateone/search")
public class SearcherManagedService extends Searcher {

	static final Logger logger = Logger.getLogger("SearcherManagedService");
    private final ConcurrentLinkedQueue<String> uuids = new ConcurrentLinkedQueue<String>();

    @Inject
    private AtmosphereResourceFactory factory;
    
    @Ready
    public void onReady(final AtmosphereResource r) {
    	logger.info("SearcherManagedService::onReady");
        if (!uuids.contains(r.uuid())) {
            super.onOpen(r);
            uuids.add(r.uuid());
        }
    }

    @Disconnect
    public void onDisconnect(AtmosphereResourceEvent event) {
    	logger.info("SearcherManagedService::onDisconnect");

        AtmosphereRequest request = event.getResource().getRequest();
        String s = request.getHeader(HeaderConfig.X_ATMOSPHERE_TRANSPORT);
        if (s != null && s.equalsIgnoreCase(HeaderConfig.DISCONNECT_TRANSPORT_MESSAGE)) {
        	SearcherManagedService.super.onClose(event.getResource());
            uuids.remove(event.getResource().uuid());
        }
    }

    @Post
    public void onMessage(AtmosphereResource resource) {
        try {
        	logger.info("SearcherManagedService::onMessage");

        	AtmosphereResource res = factory.find(resource.uuid());
            // Here we need to find the suspended AtmosphereResource
            super.onMessage(resource, resource.getRequest().getReader().readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
