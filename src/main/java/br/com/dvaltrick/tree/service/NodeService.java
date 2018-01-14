package br.com.dvaltrick.tree.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.dvaltrick.tree.model.Node;
import br.com.dvaltrick.tree.model.NodeFromParent;
import br.com.dvaltrick.tree.repository.NodeRepository;

@Service
public class NodeService {
	@Autowired
	NodeRepository repository;
	
	public Node save(Node toSaveNode) throws Exception{
		try{
			testRootUnique(toSaveNode);
			testParentWithItself(toSaveNode);
			testIfParentExist(toSaveNode);
			
			if(toSaveNode.getParentId() != null){
				if(toSaveNode.getParentId() > 0){
					hierarchicalValidation(toSaveNode.getId(), toSaveNode.getParentId());
					
					toSaveNode.setParent(repository.findOne(toSaveNode.getParentId()));
				}
			}
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
		
		return repository.save(toSaveNode);
	}
	
	public Node getTreeByRoot(){
		return repository.getTreeByRoot();
	}
	
	public List<NodeFromParent> getTreeByParent(Integer parentId){
		Set<Node> childrenList = repository.findOne(parentId).getChildren();
		List<NodeFromParent> allNodesFromParent = new ArrayList<NodeFromParent>();
		
		if(!childrenList.isEmpty()){
			for(Node childrenNode:childrenList){
				NodeFromParent nodeFromParent = new NodeFromParent();
				nodeFromParent.setId(childrenNode.getId());
				nodeFromParent.setCode(childrenNode.getCode()); 
				nodeFromParent.setDescription(childrenNode.getDescription());
				nodeFromParent.setParentId(childrenNode.getParentId());
				nodeFromParent.setDetails(childrenNode.getDetails());
				nodeFromParent.setHasChildren(!childrenNode.getChildren().isEmpty());
				allNodesFromParent.add(nodeFromParent);
			}
		}
		
		return allNodesFromParent;
	}
	
	public void deleteNode(Integer id) throws Exception{
		try{ 
			repository.delete(id);
		}catch(Exception e){
			throw new Exception("Delete not completed - " + e.getMessage());
		}
	}
	
	private void testRootUnique(Node toTestNode) throws Exception {
		if(toTestNode.getParentId() == null){
			if(repository.getTreeByRoot() != null &&
					repository.getTreeByRoot().getId() != toTestNode.getId()){
				throw new Exception("The tree already has a root");
			}
		}
	}
	
	private void testParentWithItself(Node toTestNode) throws Exception{
		if(toTestNode.getParentId() != null &&
				toTestNode.getParentId() == toTestNode.getId()){
			throw new Exception("A node can't be a parent to itself.");
		}
	}
	
	private void testIfParentExist(Node toTestNode) throws Exception{
		if(toTestNode.getParentId() != null && 
				repository.findOne(toTestNode.getParentId()) == null){
			throw new Exception("The parent node doesn't exist.");
		}
	}
	
	private void hierarchicalValidation(Integer toTestParentId, Integer toCompareParentId) throws Exception{
		if(toTestParentId != null){ 
			List<NodeFromParent> childrenNode = getTreeByParent(toTestParentId);
			if(!childrenNode.isEmpty()){
				for(NodeFromParent childNode:childrenNode){
					if(toCompareParentId == childNode.getId()){
						throw new Exception("A node can't be a child of one of your dependent hierarchical nodes.");
					}else{
						if(childNode.getHasChildren()){
							hierarchicalValidation(childNode.getId(), toCompareParentId);
						}
					}
				}
			}
		}
	}
}
