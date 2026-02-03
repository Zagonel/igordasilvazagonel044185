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


* ***Gerenciamento de Mídia e Storage***
  * **MinIO:** O armazenamento de capas de álbuns é realizado de forma desacoplada através do serviço de Storage do MinIo.
    * **Links Assinados (On-Demand):** Para garantir a segurança dos ativos, a API gera URLs pré-assinadas com tempo de expiração de 30 minutos, evitando a exposição pública do bucket e economizando largura de banda do servidor de aplicação.