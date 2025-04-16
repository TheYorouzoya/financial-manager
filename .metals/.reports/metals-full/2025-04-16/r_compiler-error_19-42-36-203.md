file://<WORKSPACE>/src/main/java/com/ratnesh/financialmanager/config/SecurityConfig.java
### java.lang.AssertionError: assertion failed: position not set for <error> # -1 of class dotty.tools.dotc.ast.Trees$Ident in <WORKSPACE>/src/main/java/com/ratnesh/financialmanager/config/SecurityConfig.java

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
uri: file://<WORKSPACE>/src/main/java/com/ratnesh/financialmanager/config/SecurityConfig.java
text:
```scala
package com.ratnesh.financialmanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.ratnesh.financialmanager.security.jwt.JwtBlacklistFilter;
import com.ratnesh.financialmanager.security.jwt.RSAKeyConfig;
import com.ratnesh.financialmanager.security.oauth2.CustomOAuth2UserService;
import com.ratnesh.financialmanager.security.oauth2.OAuth2AutheticationSuccessHandler;
import com.ratnesh.financialmanager.security.userdetails.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    private final RSAKeyConfig keys;

    private static final String[] PUBLIC_ENDPOINTS = {
        "/api/v1/auth/login",
        "/api/v1/auth/refresh",
        "/oauth2/**"
    };

    @Bean
    public SecurityFilterChain protectedEndpoints(
        HttpSecurity http, 
        CustomOAuth2UserService customOAuth2UserService,
        OAuth2AutheticationSuccessHandler successHandler,
        JwtBlacklistFilter jwtBlacklistFilter
    ) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                .requestMatchers("/actuator/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .decoder(jwtDecoder())
                    .jwtAuthenticationConverter(jwtAuthenticationConverter()))
            )
            .addFilterBefore(jwtBlacklistFilter, BearerTokenAuthenticationFilter.class)
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(info -> info
                    .userService(customOAuth2UserService))
                .successHandler(successHandler)
            )
            .formLogin(form -> form.disable())
            .httpBasic(Customizer.withDefaults())
            .userDetailsService(customUserDetailsService);

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(keys.getPublicKey()).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(keys.getPublicKey())
                    .privateKey(keys.getPrivateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));

        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    static RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
            .role("ADMIN").implies("USER")
            .build();
    }

    @Bean
    static MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }

    // Password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @
}
```



#### Error stacktrace:

```
scala.runtime.Scala3RunTime$.assertFailed(Scala3RunTime.scala:8)
	dotty.tools.dotc.typer.Typer$.assertPositioned(Typer.scala:72)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3272)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3281)
	dotty.tools.dotc.typer.Typer.typedType(Typer.scala:3395)
	dotty.tools.dotc.typer.Namer.typedAheadType$$anonfun$1(Namer.scala:1701)
	dotty.tools.dotc.typer.Namer.typedAhead(Namer.scala:1694)
	dotty.tools.dotc.typer.Namer.typedAheadType(Namer.scala:1701)
	dotty.tools.dotc.typer.Namer.typedAheadAnnotationClass(Namer.scala:1710)
	dotty.tools.dotc.typer.Namer.typedAheadAnnotationClass(Namer.scala:1709)
	dotty.tools.dotc.typer.Namer.typedAheadAnnotationClass(Namer.scala:1707)
	dotty.tools.dotc.typer.Namer$Completer.addAnnotations$$anonfun$1(Namer.scala:850)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.List.foreach(List.scala:334)
	dotty.tools.dotc.typer.Namer$Completer.addAnnotations(Namer.scala:849)
	dotty.tools.dotc.typer.Namer$Completer.completeInCreationContext(Namer.scala:952)
	dotty.tools.dotc.typer.Namer$Completer.complete(Namer.scala:828)
	dotty.tools.dotc.core.SymDenotations$SymDenotation.completeFrom(SymDenotations.scala:174)
	dotty.tools.dotc.core.Denotations$Denotation.completeInfo$1(Denotations.scala:188)
	dotty.tools.dotc.core.Denotations$Denotation.info(Denotations.scala:190)
	dotty.tools.dotc.core.SymDenotations$SymDenotation.ensureCompleted(SymDenotations.scala:392)
	dotty.tools.dotc.typer.Typer.retrieveSym(Typer.scala:3067)
	dotty.tools.dotc.typer.Typer.typedNamed$1(Typer.scala:3092)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:3206)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3277)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3281)
	dotty.tools.dotc.typer.Typer.traverse$1(Typer.scala:3303)
	dotty.tools.dotc.typer.Typer.typedStats(Typer.scala:3349)
	dotty.tools.dotc.typer.Typer.typedClassDef(Typer.scala:2746)
	dotty.tools.dotc.typer.Typer.typedTypeOrClassDef$1(Typer.scala:3114)
	dotty.tools.dotc.typer.Typer.typedNamed$1(Typer.scala:3118)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:3206)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3277)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3281)
	dotty.tools.dotc.typer.Typer.traverse$1(Typer.scala:3303)
	dotty.tools.dotc.typer.Typer.typedStats(Typer.scala:3349)
	dotty.tools.dotc.typer.Typer.typedPackageDef(Typer.scala:2889)
	dotty.tools.dotc.typer.Typer.typedUnnamed$1(Typer.scala:3159)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:3207)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3277)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3281)
	dotty.tools.dotc.typer.Typer.typedExpr(Typer.scala:3392)
	dotty.tools.dotc.typer.TyperPhase.typeCheck$$anonfun$1(TyperPhase.scala:45)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	dotty.tools.dotc.core.Phases$Phase.monitor(Phases.scala:463)
	dotty.tools.dotc.typer.TyperPhase.typeCheck(TyperPhase.scala:51)
	dotty.tools.dotc.typer.TyperPhase.$anonfun$4(TyperPhase.scala:97)
	scala.collection.Iterator$$anon$6.hasNext(Iterator.scala:479)
	scala.collection.Iterator$$anon$9.hasNext(Iterator.scala:583)
	scala.collection.immutable.List.prependedAll(List.scala:152)
	scala.collection.immutable.List$.from(List.scala:685)
	scala.collection.immutable.List$.from(List.scala:682)
	scala.collection.IterableOps$WithFilter.map(Iterable.scala:900)
	dotty.tools.dotc.typer.TyperPhase.runOn(TyperPhase.scala:96)
	dotty.tools.dotc.Run.runPhases$1$$anonfun$1(Run.scala:315)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.ArrayOps$.foreach$extension(ArrayOps.scala:1323)
	dotty.tools.dotc.Run.runPhases$1(Run.scala:308)
	dotty.tools.dotc.Run.compileUnits$$anonfun$1(Run.scala:349)
	dotty.tools.dotc.Run.compileUnits$$anonfun$adapted$1(Run.scala:358)
	dotty.tools.dotc.util.Stats$.maybeMonitored(Stats.scala:69)
	dotty.tools.dotc.Run.compileUnits(Run.scala:358)
	dotty.tools.dotc.Run.compileSources(Run.scala:261)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:161)
	dotty.tools.pc.CachingDriver.run(CachingDriver.scala:45)
	dotty.tools.pc.WithCompilationUnit.<init>(WithCompilationUnit.scala:31)
	dotty.tools.pc.SimpleCollector.<init>(PcCollector.scala:351)
	dotty.tools.pc.PcSemanticTokensProvider$Collector$.<init>(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.Collector$lzyINIT1(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.Collector(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.provide(PcSemanticTokensProvider.scala:88)
	dotty.tools.pc.ScalaPresentationCompiler.semanticTokens$$anonfun$1(ScalaPresentationCompiler.scala:111)
```
#### Short summary: 

java.lang.AssertionError: assertion failed: position not set for <error> # -1 of class dotty.tools.dotc.ast.Trees$Ident in <WORKSPACE>/src/main/java/com/ratnesh/financialmanager/config/SecurityConfig.java