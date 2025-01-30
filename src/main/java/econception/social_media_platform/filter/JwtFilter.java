package econception.social_media_platform.filter;

import econception.social_media_platform.security.UserDetailCustom;
import econception.social_media_platform.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

//import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private static final String BEARER = "Bearer ";
    private static final String AUTHORIZATION = "Authorization";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(AUTHORIZATION);
        String username= null ;
        String token = null;
        if(null != authorization && authorization.startsWith(BEARER)) {
            token = authorization.substring(BEARER.length());
        }


        try {
            if(token != null) {
                username =  jwtUtil.getUserNameFromToken(token);
            }

            boolean tokenContainsUserName = username != null && !username.isEmpty();

            if (tokenContainsUserName && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetailCustom userDetailCustom = new UserDetailCustom(username);
                boolean isTokenAuthenticated = jwtUtil.validateToken(token, userDetailCustom);

                if (isTokenAuthenticated){
                    Authentication authentication = getAuthorizedAuthentication(userDetailCustom);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

        }catch (ExpiredJwtException ex){
            log.error("JWT TOKEN HAS EXPIRED. TOKEN: {}", token);
            setUnAuthorizedResponseWithMessage(response, "Token is Expired.");
            return;
        } catch (IllegalArgumentException ex){
            log.error("JWT TOKEN IS INVALID. TOKEN: {}", token);
            setUnAuthorizedResponseWithMessage(response, "Invalid Token.");
            return;
        }


        filterChain.doFilter(request, response);
    }

    private static void setUnAuthorizedResponseWithMessage( HttpServletResponse httpServletResponse, String message) throws IOException
    {
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setContentType("application/json");
        httpServletResponse.getWriter().write(message);
    }

    private static  Authentication getAuthorizedAuthentication(UserDetailCustom userDetailCustom){
        return new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return userDetailCustom;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return null;
            }
        };
    }
}

