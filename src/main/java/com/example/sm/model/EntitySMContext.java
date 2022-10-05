package com.example.sm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntitySMContext {

    private String contextId;
    private String entityId;
    private String state;
    private String context;

}
