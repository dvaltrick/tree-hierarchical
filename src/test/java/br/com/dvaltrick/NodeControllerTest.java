package br.com.dvaltrick;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.dvaltrick.tree.TreeApplication;
import br.com.dvaltrick.tree.model.Node;

@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT,
		classes=TreeApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application-test.properties")
public class NodeControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void testNodeController() throws Exception{		
		//Try to add the root into the tree
		testPost(new Node(null,"root","Root description","Root details",null),"$.id", Matchers.is(1));
		
		//Try to add one more root into the tree
		testPost(new Node(null,"root","Root description","Root details",null),"$.error", Matchers.any(String.class));
		
		//Try to add a child into a root
		testPost(new Node(null,"first child","first child description","first child details",1),"$.id", Matchers.is(2));
		
		//Try to add a child into an inexistent parent
		testPost(new Node(null,"second child","second child description","second child details",10),"$.error", Matchers.any(String.class));
		
		//Try to add a child as parent to itself
		testPut(new Node(2,"second child","second child description","second child details",2),"$.error", Matchers.any(String.class));
		
		//Try to add a new child into a root
		testPost(new Node(null,"third child","third child description","third child details",1),"$.id", Matchers.is(3));
		
		//Try to add a new child into a child
		testPost(new Node(null,"forth child","forth child description","forth child details",3),"$.id", Matchers.is(4));
		
		//Try to add a new child into a grandchild
		testPost(new Node(null,"fifth child","fifth child description","fifth child details",4),"$.id", Matchers.is(5));
		
		//Try to add a new child into a grandchild
		testPost(new Node(null,"sixth child","sixth child description","sixth child details",5),"$.id", Matchers.is(6));
		
		//Try to change a hierarchical dependent node
		testPut(new Node(4,"forth child","forth child description","forth child details",5),"$.error", Matchers.any(String.class));
		
		//Try to change a hierarchical dependent node of second level
		testPut(new Node(4,"forth child","forth child description","forth child details",6),"$.error", Matchers.any(String.class));

		//Try to change a node to a non dependent node
		testPut(new Node(4,"forth child","forth child description","forth child details",2),"$.id", Matchers.is(4));

		//Try to get the root
		testGetRoot();
		
		//Try to get by parent 1
		testGetByParent(1,2);
		
		//Try to get by parent 2
		testGetByParent(2,1);
		
		//Try to get by parent 2
		testGetByParent(3,0);
		
		//Try to delete a node
		testDelete(4);
		
		//Try to get by parent 2 after deleted child
		testGetByParent(2,0);
				
		//Try to delete a root
		testDelete(1);
	}
		
	private void testPost(Node toTestNode, String jsonKey, Matcher matcher) throws Exception{
		mockMvc.perform(post("/node")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(asJsonString(toTestNode)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath(jsonKey, matcher));
		
	}
	
	private void testPut(Node toTestNode,String jsonKey, Matcher matcher) throws Exception{
		mockMvc.perform(put("/node")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(asJsonString(toTestNode)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath(jsonKey, matcher));
		
	}
	
	private void testGetRoot() throws Exception{
		mockMvc.perform(get("/node")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", Matchers.is(1)))
			.andExpect(jsonPath("$.code", Matchers.is("root")))
			.andExpect(jsonPath("$.children.*", Matchers.hasSize(2)));
	}
	
	private void testGetByParent(Integer parentId, Integer size) throws Exception{
		mockMvc.perform(get("/node/"+parentId.toString())
				.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.*", Matchers.hasSize(size)));
	}
	
	private void testDelete(Integer id) throws Exception{
		mockMvc.perform(delete("/node/"+id.toString())
				.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("status",Matchers.is("success")));
	}
	
	public static String asJsonString(final Object obj) {
	    try {
	        final ObjectMapper mapper = new ObjectMapper();
	        final String jsonContent = mapper.writeValueAsString(obj);
	        return jsonContent;
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}  
}
