package br.com.dvaltrick.tree.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value="children")
public class NodeFromParent extends Node implements Serializable { 
	private Boolean hasChildren;	

	public Boolean getHasChildren() {
		return hasChildren;    
	}

	public void setHasChildren(Boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

}
