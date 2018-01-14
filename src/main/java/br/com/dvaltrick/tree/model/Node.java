package br.com.dvaltrick.tree.model;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Node {  
	@Id 
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="code")
	private String code;
	
	@Column(name="description")
	private String description;
	
	@Column(name="details")
	private String details;
	
	@ManyToOne(fetch=FetchType.LAZY, targetEntity=Node.class)
	@JoinColumn(name="parentId")
	@JsonBackReference
	private Node parent;

	@Transient
	private Integer parentId;
	
	@LazyCollection(LazyCollectionOption.TRUE)
	@OneToMany (mappedBy = "parent")
	@Cascade({CascadeType.ALL})
	@OrderBy("id ASC")
	@JsonManagedReference
	private Set<Node> children = new HashSet<Node>();

	public Node(){}
	
	public Node(Integer id, String code, String description, String details, Integer parentId){
		this.id = id;
		this.code = code;
		this.description = description;
		this.details = details;
		this.parentId = parentId;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	public Node getParent() {
		return this.parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Set<Node> getChildren() {
		return children;
	}

	public void setChildren(Set<Node> children) {
		this.children = children;
	}

	public Integer getParentId() {
		if(parent != null){
			return parent.getId();
		}else{
			return parentId;
		}
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	
}
