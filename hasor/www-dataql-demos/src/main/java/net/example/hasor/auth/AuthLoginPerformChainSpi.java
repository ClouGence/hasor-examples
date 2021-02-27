package net.example.hasor.auth;
import net.hasor.dataway.spi.LoginPerformChainSpi;
import net.hasor.utils.CommonCodeUtils;
import net.hasor.utils.StringUtils;
import net.hasor.web.Invoker;

import javax.inject.Singleton;
import javax.servlet.http.Cookie;
import java.security.NoSuchAlgorithmException;

@Singleton
public class AuthLoginPerformChainSpi extends AbstractAuth implements LoginPerformChainSpi {
    public AuthLoginPerformChainSpi() throws NoSuchAlgorithmException {
        super();
    }

    @Override
    public boolean doLogin(Invoker invoker) {
        String username = invoker.getHttpRequest().getParameter("userName");
        String password = invoker.getHttpRequest().getParameter("userPassword");
        return sendCookies(invoker, username, password);
    }

    private boolean sendCookies(Invoker invoker, String username, String password) {
        try {
            // admin
            String userAsAdmin = CommonCodeUtils.MD5.getMD5(username + ":" + password + ":admin");
            if (StringUtils.equalsIgnoreCase(userAsAdmin, ADMIN_TOKENS)) {
                invoker.getHttpRequest().setAttribute(ROLE_ATTR_NAME, ROLE_ADMIN);
                invoker.getHttpResponse().addCookie(new Cookie(COOKIE_NAME, userAsAdmin));
                return true;
            }
            // user
            String userAsUser = CommonCodeUtils.MD5.getMD5(username + ":" + password + ":user");
            if (StringUtils.equalsIgnoreCase(userAsUser, USER_TOKENS)) {
                invoker.getHttpRequest().setAttribute(ROLE_ATTR_NAME, ROLE_USER);
                invoker.getHttpResponse().addCookie(new Cookie(COOKIE_NAME, userAsUser));
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
