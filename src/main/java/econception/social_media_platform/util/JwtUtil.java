package econception.social_media_platform.util;

import econception.social_media_platform.security.UserDetailCustom;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtil {
    @Value(value = "${jwt-secret}")
    private String secretKey;
    @Value(value = "${jwt-expiry-in-seconds}")
    public long JWT_TOKEN_VALIDITY;

    public String getUserNameFromToken(String token)  {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetailCustom userDetailCustom) {
        final String userName = getUserNameFromToken(token);
        return (userName.equals(userDetailCustom.getUsername()) && !isTokenExpired(token));
    }
    private Boolean isTokenExpired(String token)  {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, email);
    }

    private Date getExpirationDateFromToken(String token)  {
        return getClaimFromToken(token, Claims::getExpiration);
    }


    private String doGenerateToken(Map<String, Object> claims, String subject) {
        Date currentTime = new Date(System.currentTimeMillis());
        Date expiryTime = new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000);
        String token =  Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(currentTime)
                .setExpiration(expiryTime)
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
        log.info("JWT TOKEN CREATED FOR USER: '{}' WITH EXPIRATION DATE: {}", subject, expiryTime);
        return token;
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver)  {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) throws ExpiredJwtException, IllegalArgumentException {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public String getEmailFromRequest(HttpServletRequest request){
        String token = HttpUtil.getToken(request);
        return getUserNameFromToken(token);
    }
}
