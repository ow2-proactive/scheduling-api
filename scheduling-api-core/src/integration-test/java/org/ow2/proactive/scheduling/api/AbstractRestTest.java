package org.ow2.proactive.scheduling.api;

import org.springframework.beans.factory.annotation.Value;


/**
 * @author ActiveEon Team on 5/2/2016.
 */
public class AbstractRestTest {

    @Value("${local.server.port}")
    protected int serverPort;

}
