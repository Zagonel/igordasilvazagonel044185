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