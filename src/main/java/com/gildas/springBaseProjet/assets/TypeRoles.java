package com.gildas.springBaseProjet.assets;

import com.gildas.springBaseProjet.TypePermission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum TypeRoles {
    USERS(
        Set.of(
                TypePermission.ADMIN_CREATE
        )
    ),
    ADMIN(
            Set.of(
                    TypePermission.ADMIN_SAVE,
                    TypePermission.ADMIN_UPDATE
            )
    ),
    MANAGER(
            Set.of(
                    TypePermission.ADMIN_SAVE_READ,
                    TypePermission.ADMIN_SAVE_UPDATE
            )
    );

    Set<TypePermission> permissions;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        final List<SimpleGrantedAuthority> grantedAuthorities = new java.util.ArrayList<>(this.getPermissions().stream().map(
                permission -> new SimpleGrantedAuthority(permission.name())
        ).toList());
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return grantedAuthorities;
    }


}
