# PROJETO PRÁTICO - IMPLEMENTAÇÃO BACK END JAVA SÊNIOR

## Informações do Candidato
* **Nome:** Igor Da Silva Zagonel
* **Vaga:** Analista de Tecnologia da Informação
* **Perfil:** Engenheiro da Computação / SÊNIOR
* **Edital:** https://seletivo.seplag.mt.gov.br/ver-edital/397

---

## Sobre a Solução
Esta aplicação consiste em uma API REST robusta para a gestão de um catálogo musical (Artistas e Álbuns) e um 
serviço inteligente de sincronização de dados de Regionais. A solução foi projetada com foco em 
**alta disponibilidade, segurança e eficiência algorítmica**, atendendo aos requisitos técnicos exigidos pelo projeto prático descrito no Edital.

### Principais Pilares Técnicos:
* **Segurança:** Autenticação JWT com renovação de token e proteção de perímetro (CORS/Domain Lock).
* **Escalabilidade:** Controle de fluxo através de Rate Limiting (10 req/min) e processamento assíncrono.
* **Integração:** Sincronização de dados externos com complexidade otimizada $O(n)$ e armazenamento de objetos (S3/MinIO).
* **Observabilidade:** Implementação de Health Checks (Liveness/Readiness) e documentação interativa via Swagger.

---

### Tecnologias e Ferramentas
* **Linguagem:** Java 25
* **Framework:** Spring Boot 4.01
* **Banco de Dados:** PostgreSQL (Gerenciado via Flyway)
* **Storage:** MinIO
* **Mensageria/Notificação:** WebSockets para atualizações em tempo real
* **Containerização:** Docker e Docker Compose

---

### Arquitetura e Decisões de Projeto
A solução foi estruturada seguindo padrões modernos de engenharia de software com objetivo de facilitar a manutenção e isolamento dos processos de negocio a medida do possivel.

* ***Domínio Rico e DDD (Domain-Driven Design)*** 
  * **Encapsulamento de Regras:** Entidades como Album e Artista são responsáveis por sua própria consistência interna (ex: validações de data de lançamento e regras de vinculação) o que resulta na proteção da consistencia da classe no seu ciclo de vida.
  * **Modelos Anêmicos:** Para evitar o antipadrão de Modelos Anêmicos, a lógica de negócio foi centralizada nas entidades de Domínio.
  * **Desacoplamento de Infraestrutura:** Foi aplicada uma separação entre as entidades de domínio e as entidades de persistência JPA utilizando Mappers customizados para converter objetos de domínio em entidades JPA garantindo que as regras de negocio não influemciem no banco de dados.


* ***Aplicação de um dos Princípios SOLID***
  * **Single Responsibility Principle (SRP):** O sistema foi construído sobre um dos pilares do SOLID que é o padrão SRP que consiste que cada serviço (ex: UpdateAlbumService, VincularCapaAlbumService) possui uma única responsabilidade clara o que facilita a manutenção e reduz o acoplamento.


* ***Resiliência e Controle de Fluxo (Rate Limiting)***
  * **Bucket4j + Caffeine:** Para proteger a infraestrutura contra ataques DDoS e garantir a disponibilidade do serviço para todos os usuários uma camada de Rate Limiting foi implementada utilizando a biblioteca Bucket4j que utiliza o algoritmo Token Bucket e além dela foi escolhido o Caffeine para cache em memoria com intuito de garantir baixa latência na verificação das quotas de requisições.
    * **Configuração:** O sistema está parametrizado para permitir até 10 requisições por minuto por usuário/IP, respondendo com o status 429 Too Many Requests caso o limite seja excedido.
    * **Monitoramento do Rate Limit via Headers:** Para cada resposta bem-sucedida da API é incluido nos cabeçalhos de controle a tag ***X-Rate-Limit-Remaining*** que torna possivel o cliente monitorar sua cota de requisições em tempo real
      * **X-Rate-Limit-Remaining:** Indica o número de requisições restantes dentro da janela atual de 1 minuto.


* ***Gerenciamento de Mídia e Storage***
  * **MinIO:** O armazenamento de capas de álbuns é realizado de forma desacoplada através do serviço de Storage do MinIo.
    * **Links Assinados (On-Demand):** Para garantir a segurança dos ativos, a API gera URLs pré-assinadas com tempo de expiração de 30 minutos, evitando a exposição pública do bucket e economizando largura de banda do servidor de aplicação.


* ***Tratamento de Erros e Padronização (RFC 7807)***
  * A api desenvolvida utiliza esse padrão para fornecer as respostas a erros internos de maneira consistente e estruturada para facilitar no entendimento para quem for consumir a Api.
    * **Anatomia do Erro**
      * **type:** Uma URI que identifica o tipo do problema e pode fornecer documentação adicional (No caso dessa Api não foi implementado esse endpoint e deixado somente um placeholder). 
      * **title:** Uma descrição curta do erro.
      * **status:** O código de status HTTP original.
      * **detail:** Uma explicação detalhada sobre o erro em especifico.
      * **instance:** A URI do endpoint onde o erro ocorreu.

### Sincronização de Regionais
O módulo de integração com a API de Regionais foi construido levando em conta a consistência de dados com a **menor complexidade possível**. 
Em vez de utilizar abordagens com múltiplas consultas ao banco de dados foi utilizado uma estratégia de **Comparação em Memória com Complexidade Linear $O(N)$**.

* ***Estratégia Utilizada*** 
  * **Carga Única (Batch Fetch):** É realizado a carga de  todas as regionais ativas do banco de dados em uma única consulta e as converte para um `HashMap` o que acesso aos registros em tempo constante **$O(1)$** em memoria.
  * **Lógica de construção dos requisitos do edital:**
    * **Iteração Principal:** Ao consultar e iterar sobre a lista de retorno da API Externa é feito a verificação se existencia no hashMap do banco local.
    * **Novo Registro:** Se não existir no Map do banco de dados local o objeto é marcado para inserção e no final é removido do Map.
    * **Mudança no resgistro:** Se houver alguma mudança de dados encontrada o registro antigo é marcado como inativo e o novo é preparado para inserção e ao final o item é então **removido do Map** pois já foi processado.
    * **Exclusões:** Ao final do loop os itens que sobraram no Map do banco local representam regionais que não existem mais na API externa e por isso são inativados.
    * **Persistência em Lote:** Todas as operações de banco são consolidadas em uma única transação (`saveAll`) reduzindo a quantidade de transações e consultas ao banco.


* ***Ciclo de Vida e Agendamento***
  * **Sincronização Agendada (Scheduler):** Foi criado job agendado (`@Scheduled`) que executa o serviço de sincronização periodicamente para manter a base atualizada e ao inicializar a aplicação é disparado a sincronização imediatamente o que garante que a tabela interna seja populada de acordo com o requisito do edital.


### Observabilidade e Health Checks
Para facilitar a operação em ambientes de container a aplicação foi configurada para expor endpoints de monitoramento via Spring Boot Actuator o que permite acompanhar a saúde do sistema e a execução de tarefas em segundo plano em tempo real.


* ***Endpoints Principais*** 
  * **/api/v1/actuator/health :** Indica o estado e todos os subsistemas da aplicação.
  * **/api/v1/actuator/health/liveness :** Indica se a aplicação está rodando.
  * **/api/v1/actuator/health/readiness :** Indica se a aplicação está pronta para receber requisições.
  * **/api/v1/actuator/scheduledtasks :** Lista todas as tarefas agendadas (Cron Jobs).
  * **/api/v1/jacoco/index.html :** Relatorio da cobertura de Testes do Jacoco.


### WebSocket
Para atender ao requisito do edital sem a necessidade de polling foi implementado a comunicação assíncrona via WebSockets para avisar quando um novo album é cadastrado.

* **Implementação Técnica**
  * **Protocolo:** Foi utilizado o protocolo STOMP (Simple Text Oriented Messaging Protocol) facilitar o roteamento do Pub/Sub do websocket.
  * **Fluxo:**
    * O cliente (Frontend) se conecta ao endpoint: `http://localhost:8080/api/v1/ws`
    * O cliente se inscreve no tópico: /topic/albuns
    * Sempre que é criado um album o serviço `CreateAlbumService` publica o payload do novo álbum neste tópico o que gera a notificação para todos os inscritos ao topico.
  * **Decisão de Segurança (CORS e WebSocket)**
    * Para facilitar os testes funcionais do websocket o endpoint `/ws` foi configurado intencionalmente como Permissão de Origem Abrangente (*), isso é feito somente para facilitar os testes em um ambiente produtivo seria acrescentado estritamente os dominios confiaveis que podem utilizar o websocket.
    * Para visualizar o funcionamento do WebSocket foi feito um monitor simples com Html e Js para facilitar na vizualização, esse monitor pode ser acessado na url: `http://localhost:8080/api/v1/monitorWs.html`


### Execução via Docker
O projeto foi containerizado para garantir uma execução consistente utilizando o Multi-stage Build do Docker, nele é executado os testes unitários e gerado o relatório do Jacoco durante o processo de build.

* **Pré-requisitos**
  * Docker e Docker Compose instalados.     
* **Passo a Passo**
  * Na raiz do projeto execute o comando `docker-compose up --build` para compilar a aplicação, rodar os testes e subir a infraestrutura
  * Aguarde até que os logs indiquem que a aplicação inicializou (mensagem: Started CatalogoMusicalApiApplication).


### Endpoints e Ferramentas Disponíveis
* **Documentação (Swagger)** `http://localhost:8080/api/v1/swagger-ui.html` Interface para testar os endpoints REST da API.

* **Monitor WebSocket**	`http://localhost:8080/api/v1/monitorWs.html`	Cliente HTML simples para visualizar as notificações de novos álbuns em tempo real.

* **Relatório de Testes (JaCoCo)** `http://localhost:8080/api/v1/jacoco/index.html`	Relatório da cobertura de testes da aplicação.

* **MinIO Console**	`http://localhost:9001`	Painel administrativo do MinIo para visualizar as capas de álbuns enviadas.

* **Health Check**	`http://localhost:8080/api/v1/actuator/health`	Endpoint de monitoramento e metricas do sistema e do seus dependentes como o Banco de Dados e Conexão com Storage.

### Credenciais de Acesso

* **Banco de Dados (PostgreSQL)**
  * **Host:** localhost
  * **Porta:** 5432
  * **Database:** db_catalogo_musical
  * **Usuário:** admin
  * **Senha:** admin  
* **Object Storage (MinIO)**
  * **Console Web:** `http://localhost:9001`
  * **Usuário (Access Key):** adminuser
  * **Senha (Secret Key):** adminpassword
  * **Bucket Público:** catalogo-musical

### Configuração de Segurança (JWT)
Para fins de cumprimento dos requisitos do edital a aplicação inicializa com as seguintes definições de segurança injetadas via variáveis de ambiente no Docker
* **`SECURITY_JWT_EXPIRATION_MINUTES`:** Tempo de vida (TTL) do Access Token com valor de 5
* **`SECURITY_JWT_REFRESH_EXPIRATION_MINUTES`:** Tempo de vida do Refresh Token com valor de 60
* **`JWT_SECRET_KEY`:** Chave privada para assinatura digital dos tokens
































