/* 
 * Copyright 2019 Ceridwen Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ceridwen.lcf.server.codegen;

/**
 *
 * @author Ceridwen Limited
 */
public class StringTemplateSubEntity {
    private String entity;
    private String entityPath;

    /**
     *
     * @return
     */
    public String getEntity() {
        return entity;
    }

    /**
     *
     * @param Entity
     */
    public void setEntity(String Entity) {
        this.entity = Entity;
    }

    /**
     *
     * @return
     */
    public String getEntityPath() {
        return entityPath;
    }

    /**
     *
     * @param EntityPath
     */
    public void setEntityPath(String EntityPath) {
        this.entityPath = EntityPath;
    }
    
    
    
}
