package net.example.hasor.auth;
import net.hasor.dataway.DatawayApi;
import net.hasor.dataway.authorization.PermissionType;
import net.hasor.dataway.spi.AuthorizationChainSpi;
import net.hasor.web.invoker.HttpParameters;

import javax.inject.Singleton;
import java.security.NoSuchAlgorithmException;

import static net.hasor.dataway.authorization.PermissionGroup.Group_Execute;
import static net.hasor.dataway.authorization.PermissionGroup.Group_ReadOnly;

@Singleton
public class AuthAuthorizationChainSpi extends AbstractAuth implements AuthorizationChainSpi {
    public AuthAuthorizationChainSpi() throws NoSuchAlgorithmException {
        super();
    }

    @Override
    public boolean doCheck(PermissionType permission, DatawayApi apiInfo, boolean defaultCheck) {
        Object attribute = HttpParameters.localInvoker().getHttpRequest().getAttribute(ROLE_ATTR_NAME);
        if (ROLE_ADMIN.equals(attribute)) {
            return true;
        }
        return Group_ReadOnly.testPermission(permission) || Group_Execute.testPermission(permission);
    }
}
