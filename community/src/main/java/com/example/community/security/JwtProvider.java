package com.example.community.security;

import com.example.community.exceptions.SpringRedditException;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

import static io.jsonwebtoken.Jwts.parser;

@Service
public class JwtProvider {

    private KeyStore keyStore;

    @PostConstruct
    public void init(){
        try{
            keyStore = keyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
            keyStore.load(resourceAsStream, "secret".toCharArray());

        } catch (KeyStoreException | NoSuchAlgorithmException | IOException | CertificateException e) {
            throw new SpringRedditException("Exception occurred while Loadiong Key Store");
        }
    }
    public String generateToken(Authentication authentication) {
        User principal =(org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        return Jwts.builder().setSubject(principal.getUsername()).signWith(getPrivateKey()).compact();
    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("springblog", "secret".toCharArray());
        }
        catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
            throw new SpringRedditException("Exception occurred while trying to get private key from key store");
        }
    }

    public boolean validateToken(String jwt){
        parser().setSigningKey(getpublicKey()).parseClaimsJws(jwt);
        return true;
    }

    private PublicKey getpublicKey() {
        try{
            return keyStore.getCertificate("springblog").getPublicKey();
        } catch (KeyStoreException e) {
            throw new SpringRedditException("Exception occurred while trying to get public key from key store");
        }
    }

    public String getusername(String token){
        Claims claims = parser().setSigningKey(getpublicKey()).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
