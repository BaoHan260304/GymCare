package base.api.security;

import base.api.model.Account;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Account account;

    public CustomUserDetails(Account account) {
        this.account = account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // The "ROLE_" prefix is a standard convention in Spring Security.
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + account.getRole().toUpperCase()));
    }

    @Override
    public String getPassword() {
        // Delegate directly to the account entity
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        // The username for authentication is the email
        return account.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // in updating
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // in updating
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // in updating
    }

    @Override
    public boolean isEnabled() {
        // Delegate directly to the account's isActive status
        return account.isActive();
    }
}