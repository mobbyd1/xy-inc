#xy-inc

## Aplicação
Cria um backend as a service onde o desenvolvedor pode criar um modelo de dados e posteriormente os serviços básicos de CRUD padrão RESTFUL (POST, PUT, GET, DELETE) estarão disponíveis. 

## Arquitetura
O projeto foi desenvolvido usando as tecnologias:
* Java
* Spring Boot
* Maven
* JUnit
* Mongodb

Foi desenvolvida uma API genérica para fazer as operações de CRUD sobre um modelo. 

```
http://localhost:8080/model/create POST Cria um modelo de domínio
http://localhost:8080/<modelo>/ POST cadastra um novo elemento no modelo
http://localhost:8080/<modelo>/<id> PUT atualiza um elemento no modelo de acordo com o seu id
http://localhost:8080/<modelo>/<id> DELETE um dos elementos cadastrados no modelo de acordo com o id
http://localhost:8080/<modelo>/ GET retorna as configuraçes e os elementos cadastrados em um modelo
http://localhost:8080/<modelo>/<id> GET retorna as configuraçes e o elemento cadastrado em um modelo
http://localhost:8080/<modelo>/ GET retorna as configuraçes e os elementos cadastrados em um modelo
```

Exemplo de um body json para criação de um modelo: 
```
{
	"modelName": "produtos",
	"attributes": {
		"nome": "STRING",
		"preco": "DECIMAL"
	}
}
```

Exemplo de um body json para o cadastro de um novo elemento:
```
{
	"preco": 123.34,
	"nome": "teste"
}
```
Exemplo de retorno dos elementos de um modelo:
```
{
    "settings": {
        "preco": "DECIMAL",
        "nome": "STRING"
    },
    "data": [
        {
            "preco": 123.34,
            "nome": "teste",
            "id": "59ff8e589a028f0871475089"
        }
    ]
}
```

## Persistência
Para a persistência dos dados, foi utilizado o banco NoSQL Mongodb. Uma collection no mongodb representa um modelo de dados que o usuário criou. As configurações e os elementos, são atributos desta collection. Não utilizei o conceito de vários usuários por se tratar apenas de um teste.

## Build
```
mvn clean install
```

## Execução
```
java -jar <nome do jar gerado no Build>
```
