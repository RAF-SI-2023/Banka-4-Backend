package rs.edu.raf.security;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;

public class CustomPermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || !authentication.isAuthenticated() || !(permission instanceof String)) {
            return false;
        }
                                            //authentication objekat je nastao nakon provere jwt-a iz jwtFiltera
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();


        var a = userDetails.getAuthorities();

                //zahtevana bitovna permisija rute
        long requiredPermission = Long.parseLong((String)permission);

            //dostupne permisije usera iz baze
        for(var b: a) {
            String c= b.getAuthority();
            long d = Long.parseLong(c);
                    //bitovna operacija & koja vraca isto samo ako su oba poredjenja ista

            return (d & requiredPermission) == requiredPermission;
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
