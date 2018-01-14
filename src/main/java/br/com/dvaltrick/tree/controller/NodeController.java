package br.com.dvaltrick.tree.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.dvaltrick.tree.model.Node;
import br.com.dvaltrick.tree.model.NodeFromParent;
import br.com.dvaltrick.tree.service.NodeService;

@RestController
public class NodeController {
	@Autowired
	NodeService service;
	
	@RequestMapping(method={RequestMethod.POST,RequestMethod.PUT},
			value="/node", 
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> saveNode(@RequestBody Node toSaveNode){
		Node saved = new Node();
		Map<String, Object> result = new HashMap<String, Object>(); 
		
		try{
			saved = service.save(toSaveNode);
			result.put("id", saved.getId());
		}catch(Exception e){
			result.put("error", e.getMessage());	
		}
		
		return result; 
	}
	
	@RequestMapping(method=RequestMethod.GET, 
			value="/node",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public Node getTreeByRoot(){
		return service.getTreeByRoot();
	}
	 
	@RequestMapping(method=RequestMethod.GET, 
			value="/node/{parentId}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public List<NodeFromParent> getTreeByParent(@PathVariable("parentId") Integer parentId){
		return service.getTreeByParent(parentId);
	}
	
	@RequestMapping(method=RequestMethod.DELETE,
		value="/node/{id}",
		produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String,String> deleteNode(@PathVariable("id") Integer id){
		Map<String,String> result = new HashMap<String,String>();
		String key = "status";
		
		try{
			service.deleteNode(id);
			
			result.put(key, "success");	
		}catch(Exception e){
			result.put(key, "error: " + e.getMessage());
		}
		
		return result;
	}
	
}
