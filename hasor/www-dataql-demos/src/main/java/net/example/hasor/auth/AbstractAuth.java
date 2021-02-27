package net.example.hasor.auth;
import net.hasor.utils.CommonCodeUtils;

import java.security.NoSuchAlgorithmException;

public class AbstractAuth {
    protected final String ADMIN_TOKENS;
    protected final String USER_TOKENS;
    protected final String COOKIE_NAME    = "DW_TOKEN";
    //
    public final    String ROLE_ATTR_NAME = "ROLE";
    public final    String ROLE_USER      = "USER";
    public final    String ROLE_ADMIN     = "ADMIN";

    public AbstractAuth() throws NoSuchAlgorithmException {
        this.ADMIN_TOKENS = CommonCodeUtils.MD5.getMD5("yongchun.zyc:yongchun.zyc:admin");
        this.USER_TOKENS = CommonCodeUtils.MD5.getMD5("admin:admin:user");
    }
}
