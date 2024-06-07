package com.gildas.springBaseProjet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TypePermission {
    ADMIN_CREATE,
    ADMIN_READ,
    ADMIN_UPDATE,
    ADMIN_DELETE,
    ADMIN_SAVE,
    ADMIN_SAVE_ALL,
    ADMIN_SAVE_READ,
    ADMIN_SAVE_UPDATE,
    ADMIN_SAVE_DELETE,
    ADMIN_SAVE_READ_ALL,
    ADMIN_SAVE_READ_READ,
    ADMIN_SAVE_READ_UPDATE,
    ADMIN_SAVE_READ_DELETE,
    ADMIN_SAVE_READ_READ_ALL;

    @Getter
    private String libelle;

    ///########################################################################
}
