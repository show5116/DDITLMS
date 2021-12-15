package com.example.dditlms.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;

public class SsoAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    public SsoAuthenticationToken(Object principal) {
        super(principal, null);
    }
    public String getName() {
        if (this.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) this.getPrincipal()).getUsername();
        }

        if (getPrincipal() instanceof Principal) {
            return ((Principal) getPrincipal()).getName();
        }
        return (this.getPrincipal() == null) ? "" : this.getPrincipal().toString();
    }
}
