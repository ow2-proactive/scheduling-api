package org.ow2.proactive.scheduling.api.graphql.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public final class GraphqlContext {

    private final String sessionId;

    private final String login;

}