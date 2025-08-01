package com.fitness.idm.service;

import com.fitness.idm.dto.AuthDTO;
import io.jsonwebtoken.*;

import java.util.Date;

public class Token {
    private static final long duration = 60 * 60 * 1000;
    private String SECRET_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJJc3N1ZXIiLCJVc2VybmFtZSI6InByb2llY3QiLCJleHAiOjE3MDE2OTY4MDUsImlhdCI6MTcwMTY5NjgwNX0.R6-tANgT4OtxeRMw9PtCy42LKSCYtmlY3YV_I8dY-3k";

    public String createToken(Long id, String role) {
        return Jwts.builder()
                .setId(id.toString())
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + duration))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }


    public AuthDTO decodeToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);

            Claims claims = claimsJws.getBody();
            int userId = Integer.parseInt(claims.getId());
            String role = (String) claims.get("role");

            return new AuthDTO(userId, role);
        } catch (SignatureException e) {
            System.out.println("Invalid token or signature");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Token validation exception");
            return null;
        }
    }

}
