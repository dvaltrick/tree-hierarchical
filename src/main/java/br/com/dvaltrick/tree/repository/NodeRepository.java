package br.com.dvaltrick.tree.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.dvaltrick.tree.model.Node;

@Repository
public interface NodeRepository extends JpaRepository<Node,Integer> {
	@Query("SELECT A FROM Node A " +
           " WHERE A.parent is null ")
	Node getTreeByRoot(); 
}
