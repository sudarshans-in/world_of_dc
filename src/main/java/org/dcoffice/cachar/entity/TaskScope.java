package org.dcoffice.cachar.entity;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum TaskScope {
    PERSONAL,
    SELF,
    @JsonEnumDefaultValue
    DEPARTMENT,
    DISTRICT,
    TEAM,
    ORGANIZATION,
    CROSS_DEPARTMENT,
    DISTRICT_WIDE,
    ALL
}
