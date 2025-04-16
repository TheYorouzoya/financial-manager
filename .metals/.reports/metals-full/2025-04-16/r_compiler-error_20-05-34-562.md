file://<WORKSPACE>/src/main/java/com/ratnesh/financialmanager/controller/AuthController.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 3272
uri: file://<WORKSPACE>/src/main/java/com/ratnesh/financialmanager/controller/AuthController.java
text:
```scala
package com.ratnesh.financialmanager.controller;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ratnesh.financialmanager.dto.LoginRequest;
import com.ratnesh.financialmanager.dto.TokenRefreshRequest;
import com.ratnesh.financialmanager.dto.TokenResponse;
import com.ratnesh.financialmanager.exceptions.RefreshTokenException;
import com.ratnesh.financialmanager.model.RefreshToken;
import com.ratnesh.financialmanager.model.User;
import com.ratnesh.financialmanager.security.jwt.JwtService;
import com.ratnesh.financialmanager.security.jwt.TokenBlacklistService;
import com.ratnesh.financialmanager.security.userdetails.CustomUserDetails;
import com.ratnesh.financialmanager.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final TokenBlacklistService blacklistService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), 
                    loginRequest.getPassword()
                )
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String accessToken = jwtService.generateToken(userDetails.getUsername(), userDetails.getId(), userDetails.getAuthorities());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

            TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken.getId().toString());

            return ResponseEntity.ok(tokenResponse);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody TokenRefreshRequest request) {
        RefreshToken refreshToken = refreshTokenService.getTokenById(request.id());
        if (refreshToken == null) {
            throw new RefreshTokenException("Refresh Token not found");
        }

        refreshToken = refreshTokenService.@@verifyExpiration(refreshToken);
        User user = refreshToken.getUser();
        String accessToken = jwtService.generateToken(user.getUsername(), user.getId(), user.getAuthorities());

        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken.getId().toString());

        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal Jwt jwt) {
        String jti = jwt.getId();
        Instant expiration = jwt.getExpiresAt();
        Instant now = Instant.now();

        long remainingTTL = Duration.between(now, expiration).getSeconds();

        if (remainingTTL > 0) {
            blacklistService.blacklistToken(jti, remainingTTL);
        }

        String userId = jwt.getClaim("id");
        refreshTokenService.deleteAllUserRefreshTokens(UUID.fromString(userId));

        return ResponseEntity.noContent().build();
    }
}

```



#### Error stacktrace:

```
scala.collection.Iterator$$anon$19.next(Iterator.scala:973)
	scala.collection.Iterator$$anon$19.next(Iterator.scala:971)
	scala.collection.mutable.MutationTracker$CheckedIterator.next(MutationTracker.scala:76)
	scala.collection.IterableOps.head(Iterable.scala:222)
	scala.collection.IterableOps.head$(Iterable.scala:222)
	scala.collection.AbstractIterable.head(Iterable.scala:935)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:164)
	dotty.tools.pc.CachingDriver.run(CachingDriver.scala:45)
	dotty.tools.pc.HoverProvider$.hover(HoverProvider.scala:40)
	dotty.tools.pc.ScalaPresentationCompiler.hover$$anonfun$1(ScalaPresentationCompiler.scala:389)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator