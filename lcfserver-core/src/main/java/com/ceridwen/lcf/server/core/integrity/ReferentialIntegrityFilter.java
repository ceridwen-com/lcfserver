/*******************************************************************************
 * Copyright (c) 2016, Matthew J. Dovey (www.ceridwen.com).
 *   
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   
 *     http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *    
 *   
 * Contributors:
 *     Matthew J. Dovey (www.ceridwen.com) - initial API and implementation
 *
 *     
 *******************************************************************************/
package com.ceridwen.lcf.server.core.integrity;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

import com.ceridwen.lcf.server.core.EntityTypes;
import com.ceridwen.lcf.server.core.QueryResults;
import com.ceridwen.lcf.server.core.EntityTypes.Type;
import com.ceridwen.lcf.server.core.exceptions.EXC04_UnableToProcessRequest;
import com.ceridwen.lcf.server.core.exceptions.EXC05_InvalidEntityReference;
import com.ceridwen.lcf.server.core.filter.EntitySourcesFilter;
import com.ceridwen.lcf.server.core.persistence.EntitySourceInterface;
import com.ceridwen.lcf.server.core.persistence.EntitySourcesInterface;

/**
 * 
 * Pipeline overlaying referential integrity to EntitySources
 * 
 */

public class ReferentialIntegrityFilter implements EntitySourcesFilter {

	@Override
	public EntitySourcesInterface filters(EntitySourcesInterface entitySources) {
		return new EntitySourcesInterface() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public <T> EntitySourceInterface<T> getEntitySource(Type type, Class<T> clazz) {
				return (EntitySourceInterface<T>) new ReferentialEntitySource(entitySources.getEntitySource(type, clazz), type, this);
			}
		};
	}
	
}

/**
 * 
 * Key methods for enforcing referential integrity
 * 
 */
class ReferentialEntitySource<E> implements EntitySourceInterface<E> {

	private EntitySourceInterface<E> wrapped;
	private EntityTypes.Type entityType;
	private EntitySourcesInterface entityDataSources;

	public ReferentialEntitySource(EntitySourceInterface<E> wrapped, EntityTypes.Type entityType, EntitySourcesInterface entityDataSources) {
		this.wrapped = wrapped;
		this.entityType = entityType;
		this.entityDataSources = entityDataSources;
	}
	
	public EntitySourcesInterface getEntityDataSources() {
		return this.entityDataSources;
	}

	public EntityTypes.Type getEntityType() {
		return this.entityType;
	}

	@Override
	public String Create(Object parent, E entity) {
		synchronized(entity) {
			if (parent == null) {
				// In theory we shouldn't have been able to get here if the parent is unknown!
				throw new EXC05_InvalidEntityReference("Error determining parent for new entity", "Error determining parent for new entity", null, null);
			}
			String childId = wrapped.Create(entity);
			setParent(parent, entity);
			validateChildren(null, entity);
			validateParents(null, entity);
			return childId;
		}
	}

	@Override
	public String Create(E entity) {
		synchronized(entity) {
			String childId = wrapped.Create(entity);
			validateChildren(null, entity);
			validateParents(null, entity);
			return childId;
		}
	}

	@Override
	public E Retrieve(String identifier) {
		return wrapped.Retrieve(identifier);
	}

	@Override
	public E Modify(String identifier, E entity) {
		synchronized(entity) {
			E oldEntity = this.wrapped.Retrieve(identifier);
			this.getEntityType().setIdentifier(entity, 
					this.getEntityType().getIdentifier(oldEntity));
			validateChildren(oldEntity, entity);
			validateParents(oldEntity, entity);
			return wrapped.Modify(identifier, entity);
		}
	}

	@Override
	public void Delete(String identifier) {
		E oldEntity = this.wrapped.Retrieve(identifier);
		synchronized(oldEntity) {	
			validateChildren(oldEntity, null);
			validateParents(oldEntity, null);
			wrapped.Delete(identifier);
		}
	}

	private QueryResults<E> generateResultsFromCandidateRefs(List<String> candidates, int start, int max) {
		QueryResults<E> results = new QueryResults<>();
		
		results.setTotalResults(candidates.size());
		results.setSkippedResults(start);
		results.setResults(new Vector<E>());
		
		for (int i = start; (i < candidates.size()) && (i < (start + max)); i++) {
			results.getResults().add(wrapped.Retrieve(candidates.get(i)));
		}
		
		return results;
	}

	private QueryResults<E> generateResultsFromCandidates(List<E> candidates, int start, int max) {
		QueryResults<E> results = new QueryResults<>();
		
		results.setTotalResults(candidates.size());
		results.setSkippedResults(start);
		results.setResults(new Vector<E>());
		
		for (int i = start; (i < candidates.size()) && (i < (start + max)); i++) {
			results.getResults().add(candidates.get(i));
		}
		
		return results;
	}
	
	
	// FIXME Implement selection criteria filtering
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public QueryResults<E> Query(Object parent, int start, int max, String query) {
		Relationship<?,?> rel = RelationshipFactory.getRelationship(EntityTypes.lookUpByClass(parent.getClass()), this.getEntityType());
	
		if (rel != null){
			if (rel.getChildRefInParent() instanceof NullListRef) {
				String parentId = EntityTypes.lookUpByClass(parent.getClass()).getIdentifier(parent);
				List<E> candidates = new ArrayList<>();
				this.Query(query, 0, 0).getResults().forEach(
					e -> {
						switch (rel.getRelationshipType()) {
							case OneToOne:
							case OneToMany:
								String id = ((SingletonRef)rel.getParentRefInChild()).getGetter().get(e);
								if (StringUtils.equals(id, parentId)) {
									candidates.add(e);
								}
								break;
							case ManyToMany:
								if (((ListRef)rel.getParentRefInChild()).getContains().contains(e, parentId)) {
									candidates.add(e);
								}
								break;
						}
					}
				);
				return this.generateResultsFromCandidates(candidates, start, max);				
			} else {
				List<String> candidates = new ArrayList<>();
				switch (rel.getRelationshipType()) {
					case OneToOne:
						candidates.add(((SingletonRef)rel.getChildRefInParent()).getGetter().get(parent));
						break;
					case OneToMany:
					case ManyToMany:
						candidates.addAll(((ListRef)rel.getChildRefInParent()).getLister().list(parent));
						break;
				}
				return this.generateResultsFromCandidateRefs(candidates, start, max);
			}
		}
		
		Relationship<?,?> rel1 = RelationshipFactory.getRelationship(this.getEntityType(), EntityTypes.lookUpByClass(parent.getClass()));
		
		if (rel1 != null){
			if (rel1.getParentRefInChild() instanceof NullListRef) {
				String parentId = EntityTypes.lookUpByClass(parent.getClass()).getIdentifier(parent);
				List<E> candidates = new ArrayList<>();
				this.Query(query, 0, 0).getResults().forEach(
					e -> {
						switch (rel1.getRelationshipType()) {
							case OneToOne:
							case OneToMany:
								String id = ((SingletonRef)rel1.getParentRefInChild()).getGetter().get(e);
								if (StringUtils.equals(id, parentId)) {
									candidates.add(e);
								}
								break;
							case ManyToMany:
								if (((ListRef)rel1.getParentRefInChild()).getContains().contains(e, parentId)) {
									candidates.add(e);
								}
								break;
						}
					}
				);
				return this.generateResultsFromCandidates(candidates, start, max);				
			} else {
				List<String> candidates = new ArrayList<>();
				switch (rel1.getRelationshipType()) {
					case OneToOne:
					case OneToMany:
						candidates.add(((SingletonRef)rel1.getParentRefInChild()).getGetter().get(parent));
						break;
					case ManyToMany:
						candidates.addAll(((ListRef)rel1.getParentRefInChild()).getLister().list(parent));
						break;
				}
				return this.generateResultsFromCandidateRefs(candidates, start, max);
			}
		}
		
		QueryResults<E> results = new QueryResults<>();
		results.setSkippedResults(0);
		results.setTotalResults(0);
		results.setResults(new Vector<E>());
		return results;
	}

	@Override
	public QueryResults<E> Query(String query, int start, int max) {
		return wrapped.Query(query, start, max);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setParent(Object parent, E child) {
		
		
		EntityTypes.Type childType = EntityTypes.lookUpByClass(child.getClass());
		EntityTypes.Type parentType = EntityTypes.lookUpByClass(parent.getClass());
		
		String childId = childType.getIdentifier(child);
		String parentId = parentType.getIdentifier(parent);
		

		Relationship r = RelationshipFactory.getRelationship(
				parentType, 
				EntityTypes.lookUpByClass(child.getClass()));
		
		if (r != null) {
			switch (r.getRelationshipType()) {
			case OneToOne:
				((SingletonRef)r.getChildRefInParent()).getSetter().set(parent, childId);
				((SingletonRef)r.getParentRefInChild()).getSetter().set(child, parentId);
				break;
			case OneToMany:
				((SingletonRef)r.getChildRefInParent()).getSetter().set(parent, childId);
				((ListRef)r.getParentRefInChild()).getAdder().add(child, parentId);
				break;
			case ManyToMany:
				((ListRef)r.getChildRefInParent()).getAdder().add(parent, childId);
				((ListRef)r.getParentRefInChild()).getAdder().add(child, parentId);
				break;
			}
			
			this.getEntityDataSources().getEntitySource(parentType, parentType.getTypeClass()).Modify(parentId, parent);
		} else {
			throw new EXC04_UnableToProcessRequest(childType.getEntityTypeCodeValue() + " cannot have parent " + parentType.getEntityTypeCodeValue(), childType.getEntityTypeCodeValue() + " cannot have parent " + parentType.getEntityTypeCodeValue(), null, null );
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void validateParents(E oldEntity, E entity) {
		List<Relationship<?,?>> rels = RelationshipFactory.getRelationshipsAsChild(this.getEntityType());
		
		for (Relationship<?,?> rel: rels) {
			switch (rel.getRelationshipType()) {
				case OneToOne:
					if (entity != null) {
						String childId = this.entityType.getIdentifier(entity);
						String parentId = ((SingletonRef)rel.getParentRefInChild()).getGetter().get(entity);
						if (StringUtils.isEmpty(parentId)) {
							if (rel.isParentRequired()) {
								throw new EXC04_UnableToProcessRequest(rel.getChildType().getEntityTypeCodeValue() + " requires parent " + rel.getParentType().getEntityTypeCodeValue(), rel.getChildType().getEntityTypeCodeValue() + " requires parent " + rel.getParentType().getEntityTypeCodeValue(), null, null);								
							}							
						} else {
							Object parent = this.getEntityDataSources().getEntitySource(rel.getParentType(), rel.getParentType().getTypeClass()).Retrieve(parentId);
							if (parent == null) {
								throw new EXC05_InvalidEntityReference(rel.getParentType().getEntityTypeCodeValue() + "/" + parentId + " not found.", rel.getParentType().getEntityTypeCodeValue() + "/" + parentId + " not found.", null, null);
							}
							String backReference = ((SingletonRef)rel.getChildRefInParent()).getGetter().get(parent);
							if (StringUtils.isEmpty(backReference) || StringUtils.equals(backReference, childId)) {
								((SingletonRef)rel.getChildRefInParent()).getSetter().set(parent, childId);
							} else {
								throw new EXC04_UnableToProcessRequest(rel.getParentType().getEntityTypeCodeValue() + "/" + parentId + " already allocated", rel.getParentType().getEntityTypeCodeValue() + "/" + parentId + " already allocated.", null, null);								
							}							
							this.getEntityDataSources().getEntitySource(rel.getParentType(), rel.getParentType().getTypeClass()).Modify(parentId, parent);
						}
					}
					if (oldEntity != null) {
						String childId = this.entityType.getIdentifier(oldEntity);
						if (entity == null || !StringUtils.equals(this.entityType.getIdentifier(entity), childId)) {
							String parentId = ((SingletonRef)rel.getParentRefInChild()).getGetter().get(oldEntity);
							Object parent = this.getEntityDataSources().getEntitySource(rel.getParentType(), rel.getParentType().getTypeClass()).Retrieve(parentId);
							if (parent != null) {
								((SingletonRef)rel.getChildRefInParent()).getSetter().set(parent, "");;
								this.getEntityDataSources().getEntitySource(rel.getParentType(), rel.getParentType().getTypeClass()).Modify(parentId, parent);						
							}
						}
					}
					break;
				case OneToMany:
					if (entity != null) {
						String childId = this.entityType.getIdentifier(entity);
						String parentId = ((SingletonRef)rel.getParentRefInChild()).getGetter().get(entity);
						if (StringUtils.isEmpty(parentId)) {
							if (rel.isParentRequired()) {
								throw new EXC04_UnableToProcessRequest(rel.getChildType().getEntityTypeCodeValue() + " requires parent " + rel.getParentType().getEntityTypeCodeValue(), rel.getChildType().getEntityTypeCodeValue() + " requires parent " + rel.getParentType().getEntityTypeCodeValue(), null, null);								
							}							
						} else {
							Object parent = this.getEntityDataSources().getEntitySource(rel.getParentType(), rel.getParentType().getTypeClass()).Retrieve(parentId);
							if (parent == null) {
								throw new EXC05_InvalidEntityReference(rel.getParentType().getEntityTypeCodeValue() + "/" + parentId + " not found.", rel.getParentType().getEntityTypeCodeValue() + "/" + parentId + " not found.", null, null);
							}
							ListRef refs = (ListRef)rel.getChildRefInParent();
							if (!refs.getContains().contains(parent, childId)) {
								refs.getAdder().add(parent, childId);
							}
							this.getEntityDataSources().getEntitySource(rel.getParentType(), rel.getParentType().getTypeClass()).Modify(parentId, parent);
						}
					}
					if (oldEntity != null) {
						String childId = this.entityType.getIdentifier(oldEntity);
						if (entity == null || !StringUtils.equals(this.entityType.getIdentifier(entity), childId)) {
							String parentId = ((SingletonRef)rel.getParentRefInChild()).getGetter().get(oldEntity);
							Object parent = this.getEntityDataSources().getEntitySource(rel.getParentType(), rel.getParentType().getTypeClass()).Retrieve(parentId);
							if (parent != null) {
								ListRef refs = (ListRef)rel.getChildRefInParent();
								if (refs.getContains().contains(parent, childId)) {
									refs.getRemover().remove(parent, childId);
								}
								this.getEntityDataSources().getEntitySource(rel.getParentType(), rel.getParentType().getTypeClass()).Modify(parentId, parent);						
							}
						}
					}
					break;
				case ManyToMany:
					System.out.println("Many to many relationship not implemented.");
					// throw new NotImplementedException("Many to many relationship not implemented.");
					// FIXME Implement many to many relational integrity
			}
			
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void validateChildren(E oldEntity, E entity) {
		List<Relationship<?,?>> rels = RelationshipFactory.getRelationshipsAsParent(this.getEntityType());
		
		for (Relationship<?,?> rel: rels) {
			switch (rel.getRelationshipType()) {
				case OneToOne:
					if (entity != null && oldEntity != null) { 
						String oldChildId = ((SingletonRef)rel.getChildRefInParent()).getGetter().get(oldEntity);
						String newChildId = ((SingletonRef)rel.getChildRefInParent()).getGetter().get(entity);
						if (!StringUtils.equals(oldChildId, newChildId)) {
							if (rel.isDeleteChildrenOnDeleteParent()) {
								this.getEntityDataSources().getEntitySource(rel.getChildType(), rel.getChildType().getTypeClass()).Delete(oldChildId);
							} else if (rel.isParentRequired()) {
								throw new EXC04_UnableToProcessRequest(rel.getChildType().getEntityTypeCodeValue() + " requires parent " + rel.getParentType().getEntityTypeCodeValue(), rel.getChildType().getEntityTypeCodeValue() + " requires parent " + rel.getParentType().getEntityTypeCodeValue(), null, null);
							} else {
								Object child = this.getEntityDataSources().getEntitySource(rel.getChildType(), rel.getChildType().getTypeClass()).Retrieve(oldChildId);
								((SingletonRef)rel.getParentRefInChild()).getSetter().set(child, "");
								this.getEntityDataSources().getEntitySource(rel.getChildType(), rel.getChildType().getTypeClass()).Modify(oldChildId, child);
							}
						}						
					} else if (oldEntity != null) {
						if (rel.isDeleteChildrenOnDeleteParent()) {
							String childId = ((SingletonRef)rel.getChildRefInParent()).getGetter().get(oldEntity);
							this.getEntityDataSources().getEntitySource(rel.getChildType(), rel.getChildType().getTypeClass()).Delete(childId);							
						} 
					}
					if (entity != null) {
						
						// FIXME validate child refs
						// FIXME can parent add new child refs or must child add parent ref?
					}
					break;
				case OneToMany:
					if (entity != null && oldEntity != null) {
						if (rel.isDeleteChildrenOnDeleteParent()) {
							List<String> oldChildIds = ((ListRef)rel.getChildRefInParent()).getLister().list(oldEntity);
							List<String> newChildIds = Arrays.asList((String[])((ListRef)rel.getChildRefInParent()).getLister().list(entity).toArray(new String[]{})); // force copy of list
							for (String childId: oldChildIds.toArray(new String[]{})) { // force copy of list
								if (!newChildIds.contains(childId)) {
									this.getEntityDataSources().getEntitySource(rel.getChildType(), rel.getChildType().getTypeClass()).Delete(childId);
								}
							}
						} 
					} else if (oldEntity != null) {
						if (rel.isDeleteChildrenOnDeleteParent()) {
							List<String> childIds = ((ListRef)rel.getChildRefInParent()).getLister().list(oldEntity);
							for (String childId: childIds.toArray(new String[]{})) { // force copy of list
								this.getEntityDataSources().getEntitySource(rel.getChildType(), rel.getChildType().getTypeClass()).Delete(childId);	
							}
						} 
					}
					if (entity != null) {
						// FIXME validate child refs
						// FIXME can parent add new child refs or must child add parent ref?
					}
					break;
				case ManyToMany:
					System.out.println("Many to many relationship not implemented.");
					// throw new NotImplementedException("Many to many relationship not implemented.");
					// FIXME Implement many to many relational integrity
			}
			
		}
	}
}
