/*
 * Copyright 2020 Ceridwen Limited.
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
package com.ceridwen.lcf.server.resources.memory.database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;
import org.bic.ns.lcf.v1_0.LcfEntity;

/**
 *
 * @author Matthew.Dovey
 */
public class TransactionalSupplier implements Supplier<String> {

    private final Operation operation;
    private final Collection<LcfEntity> data;

    public TransactionalSupplier(Operation operation, LcfEntity data) {
        this.operation = operation;
        this.data = new ArrayList<>();
        this.data.add(data);

    }
    
    public TransactionalSupplier(Operation operation, Collection<LcfEntity> data) {
        this.operation = operation;
        this.data = data;
    }
    
    @Override
    public String get() {
        StringBuilder msg = new StringBuilder();
        msg.append(operation.name()).append(": ");
        this.data.forEach(e -> msg.append(e.getIdentifier()).append(", "));
        return msg.toString();
    }
    
}
