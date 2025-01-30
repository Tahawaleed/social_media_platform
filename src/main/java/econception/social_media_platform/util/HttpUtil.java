package econception.social_media_platform.util;

import jakarta.servlet.http.HttpServletRequest;

public class HttpUtil {
    private static final String BEARER = "Bearer ";


    public static String getToken(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        if(null != authorization && authorization.startsWith(BEARER)) {
            return authorization.substring(BEARER.length());
        }
        return null;
    }
}
