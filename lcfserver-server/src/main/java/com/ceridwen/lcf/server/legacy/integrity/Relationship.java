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
package com.ceridwen.lcf.server.legacy.integrity;

import com.ceridwen.lcf.model.enumerations.EntityTypes;

/**
 *
 * @author Ceridwen Limited
 * @param <ParentType>
 * @param <ChildType>
 */
public class Relationship<ParentType, ChildType> {

    /**
     *
     */
    public enum Type {

            /**
             *
             */
            OneToOne,

            /**
             *
             */
            OneToMany,

            /**
             *
             */
            ManyToMany
	}
	
    /**
     *
     */
    public enum Integrity {

        /**
         *
         */
        ReferencesValid,

        /**
         *
         */
        CascadeParentDelete,

        /**
         *
         */
        ParentRequired
	}

	private Type relationshipType;
	private EntityTypes.Type parentType;
	private EntityTypes.Type childType;
	private Ref<ParentType> childRefInParent;
	private Ref<ChildType> parentRefInChild;
	private Integrity integrity;
	
    /**
     *
     * @param parentType
     * @param parentRefInChild
     * @param childType
     * @param childRefInParent
     * @param integrity
     */
    public Relationship(EntityTypes.Type parentType, SingletonRef<ChildType> parentRefInChild, EntityTypes.Type childType, SingletonRef<ParentType> childRefInParent, Integrity integrity) {
		this.relationshipType = Type.OneToOne;
		this.parentType = parentType;
		this.childRefInParent = childRefInParent;
		this.childType = childType;
		this.parentRefInChild = parentRefInChild;
		this.integrity = integrity;
	}

    /**
     *
     * @param parentType
     * @param parentRefInchild
     * @param childType
     * @param childRefInParent
     * @param integrity
     */
    public Relationship(EntityTypes.Type parentType, SingletonRef<ChildType> parentRefInchild, EntityTypes.Type childType, ListRef<ParentType> childRefInParent, Integrity integrity) {
		this.relationshipType = Type.OneToMany;
		this.parentType = parentType;
		this.childRefInParent = childRefInParent;
		this.childType = childType;
		this.parentRefInChild = parentRefInchild;
		this.integrity = integrity;
	}

    /**
     *
     * @param parentType
     * @param parentRefInChild
     * @param childType
     * @param childRefInParent
     */
    public Relationship(EntityTypes.Type parentType, ListRef<ChildType> parentRefInChild, EntityTypes.Type childType, ListRef<ParentType> childRefInParent) {
		this.relationshipType = Type.ManyToMany;
		this.parentType = parentType;
		this.childRefInParent = childRefInParent;
		this.childType = childType;
		this.parentRefInChild = parentRefInChild;
		this.integrity = Integrity.ReferencesValid;
	}

    /**
     *
     * @return
     */
    public Type getRelationshipType() {
		return relationshipType;
	}

    /**
     *
     * @return
     */
    public EntityTypes.Type getParentType() {
		return parentType;
	}

    /**
     *
     * @return
     */
    public EntityTypes.Type getChildType() {
		return childType;
	}

    /**
     *
     * @return
     */
    public Ref<ParentType> getChildRefInParent() {
		return childRefInParent;
	}

    /**
     *
     * @return
     */
    public Ref<ChildType> getParentRefInChild() {
		return parentRefInChild;
	}

    /**
     *
     * @return
     */
    public boolean isDeleteChildrenOnDeleteParent() {
		switch (this.integrity) {
			case ParentRequired:
			case CascadeParentDelete:
				return true;
			case ReferencesValid:
			default:
				return false;
		}
	}

    /**
     *
     * @return
     */
    public boolean isParentRequired() {
		switch (this.integrity) {
			case ParentRequired:
				return true;
			case CascadeParentDelete:
			case ReferencesValid:
			default:
				return false;
		}
	}
	
    /**
     *
     * @return
     */
    public Integrity getIntegrity() {
		return this.integrity;
	}
}
