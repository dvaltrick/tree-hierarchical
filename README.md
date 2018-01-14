# tree-hierarchical

## Ambiente
### Maven
- Para importar o projeto deve ser importado como `MavenProject` e utilizado o arquivo `pom.xml` da raiz do repositório;

### SpringBoot
- A aplicação foi desenvolvida utilizando o framework Spring, por isso foi utilizada a ferramenta SpringBoot;

### MySQL
- O banco de dados utilizado para a aplicação é o MySQL; 
- Por padrão o nome utilizado para o banco é `dbtree`;
- As configurações de acesso do banco se encontram no arquivo `application.properties` no diretório **main/resources**;
- Abaixo segue a configuração padrão usada para o desenvolvimento:  
`spring.jpa.hibernate.ddl-auto=update`  
`spring.datasource.url=jdbc:mysql://localhost/dbtree?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC`  
`spring.datasource.username=root`  

## Testes
- Para executar a rotina de testes, deve ser executada a classe `NodeControllerTest`;
- Para os testes a aplicação cria um banco de dados H2 próprio, as configurações desse banco se encontram no arquivo `application-test.properties` em `test/resources`;
