package com.geeker.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Lubin.Xuan on 2017-09-25.
 * {desc}
 */
@Slf4j
public class JwtTokenUtil {

    public final Jws<Claims> parse(String authToken,String secret) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(authToken);
    }

    public final String getUsernameFromToken(Jws<Claims> jws) {
        return jws.getBody().getSubject();
    }
}
