package net.example.hasor.auth;
import net.hasor.dataway.spi.LoginTokenChainSpi;
import net.hasor.web.Invoker;

import javax.inject.Singleton;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

@Singleton
public class AuthLoginTokenChainSpi extends AbstractAuth implements LoginTokenChainSpi {
    public AuthLoginTokenChainSpi() throws NoSuchAlgorithmException {
        super();
    }

    @Override
    public boolean doCheckToken(Invoker invoker) {
        String cookieDwAuthData = cookieValue(invoker.getHttpRequest());
        if (cookieDwAuthData != null && cookieDwAuthData.equalsIgnoreCase(USER_TOKENS)) {
            invoker.getHttpRequest().setAttribute(ROLE_ATTR_NAME, ROLE_USER);
            return true;
        }
        if (cookieDwAuthData != null && cookieDwAuthData.equalsIgnoreCase(ADMIN_TOKENS)) {
            invoker.getHttpRequest().setAttribute(ROLE_ATTR_NAME, ROLE_ADMIN);
            return true;
        }
        return false;
    }

    protected String cookieValue(HttpServletRequest httpRequest) {
        Cookie[] cookies = httpRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase(COOKIE_NAME)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
