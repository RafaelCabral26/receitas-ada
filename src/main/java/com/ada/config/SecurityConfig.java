package com.ada.config;

import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.runtime.SecurityIdentityAssociation;
import io.quarkus.vertx.http.runtime.security.HttpAuthenticationMechanism;
import io.quarkus.vertx.http.runtime.security.HttpAuthenticator;
import io.quarkus.vertx.http.runtime.security.HttpSecurityPolicy;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class SecurityConfig {

    @Inject
    SecurityIdentityAssociation identityAssociation;

    public Uni<Optional<HttpSecurityPolicy.CheckResult>> checkPermission(RoutingContext context) {
        if (context.request().path().equals("/receitas")) {
            return Uni.createFrom().item(Optional.of(HttpSecurityPolicy.CheckResult.PERMIT));
        }
        return Uni.createFrom().item(Optional.empty());
    }
}
