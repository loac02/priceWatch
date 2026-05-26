# 📉 PriceWatch API

A **PriceWatch API** é uma solução robusta e automatizada para monitorização de preços de produtos em e-commerces reais. Desenvolvida em **Java** com o ecossistema **Spring Boot**, a aplicação combina técnicas de *Web Scraping* dinâmico com uma arquitetura de microsserviços distribuída, utilizando persistência poliglota e mensageria assíncrona para notificar utilizadores assim que o preço-alvo de um produto for atingido.

---

## 🛠️ Arquitetura e Tecnologias

A aplicação foi desenhada seguindo as melhores práticas de desenvolvimento, padrões de projeto corporativos (como **Fachada/Facade**) e isolamento de responsabilidades:

* **Core:** Java 17 & Spring Boot 3
* **Web Scraping Real:** [Jsoup](https://jsoup.org/) para extração de dados diretamente do HTML de e-commerces (Mercado Livre, KaBuM!, Amazon, etc.).
* **Persistência Relacional (SQL):** [PostgreSQL](https://www.postgresql.org/) (alojado no **Supabase**) para a gestão de estados dos alertas e utilizadores.
* **Persistência Não-Relacional (NoSQL):** [MongoDB](https://www.mongodb.com/) (alojado no **Mongo Atlas**) focado em performance para gravação do histórico de flutuação de preços.
* **Mensageria e Filas:** [RabbitMQ](https://www.rabbitmq.com/) (alojado no **CloudAMQP**) para o desacoplamento do envio de notificações.
* **Comunicação:** [Spring Mail](https://spring.io/projects/spring-framework) integrado diretamente com o servidor SMTP do Gmail para disparos reais de e-mail.
* **Containerização & Deploy:** Dockerfile multi-stage baseado em **Eclipse Temurin 17** pronto para produção no **Render**.

---

## 🔄 Fluxo de Funcionamento

```
[ PriceScheduler (Agendador) ]
             │
             ▼ (Executa a cada X minutos)
[ ScraperService (Jsoup) ] ───► Captura Preço Real (E-commerce)
             │
             ├─► [ MongoDB Atlas ] ──► Grava histórico apenas se o preço flutuar
             │
             ▼ (Preço <= Alvo? e Não Notificado?)
[ RabbitMQ (CloudAMQP) ] ───► [ NotificationConsumer ]
                                         │
                                         ▼ (Disparo Real)
                            [ JavaMailSender (Gmail SMTP) ] ──► 📧 Utilizador
```

1.  **Monitorização Agendada:** O `PriceScheduler` acorda periodicamente e consulta os alertas ativos no PostgreSQL.
2.  **Scraping Inteligente:** O `ScraperService` consome a URL do produto, emula um navegador real através de *headers* customizados e percorre uma lista de seletores CSS dinâmicos (como `.andes-money-amount__fraction` ou `h4.text-secondary-500`).
3.  **Auditoria Histórica:** A Fachada valida o último registo no MongoDB Atlas. Se houver variação de preço, um novo documento é persistido.
4.  **Disparo Único:** Caso o valor atinja a meta do utilizador, a flag `notificado` é validada no PostgreSQL. Se estiver como `false`, o evento é postado na fila do RabbitMQ e o estado é alterado para `true`, evitando *spam*.
5.  **Consumo e Envio:** O `NotificationConsumer` intercetará a mensagem de forma assíncrona e invocará o `EmailService` para a entrega final na caixa de entrada do utilizador.

---

## ⚙️ Configuração do Ambiente

A aplicação está totalmente configurada para ser executada localmente ou na nuvem através de variáveis de ambiente seguras no `application.properties`.

### Variáveis Necessárias:

| Variável | Descrição | Valor Padrão (Local) |
| :--- | :--- | :--- |
| `PORT` | Porta do servidor web | `8080` |
| `DB_URL` | URL de Conexão do PostgreSQL | `jdbc:postgresql://localhost:5432/db_pricewatch` |
| `DB_USERNAME` | Utilizador do PostgreSQL | `admin` |
| `DB_PASSWORD` | Senha do PostgreSQL | `password123` |
| `MONGO_URI` | String de conexão do MongoDB | `mongodb://admin:password123@localhost:27017/...` |
| `RABBITMQ_HOST` | Host do servidor RabbitMQ | `localhost` |
| `RABBITMQ_PORT` | Porta do RabbitMQ | `5672` |
| `RABBITMQ_USERNAME`| Utilizador do RabbitMQ | `admin` |
| `RABBITMQ_PASSWORD`| Senha do RabbitMQ | `password123` |
| `EMAIL_USER` | Conta de Gmail Remetente | `seu_email@gmail.com` |
| `EMAIL_PASS` | Senha de App gerada na Conta Google| `sua_senha_de_app` |

---

## 🚀 Como Executar o Projeto

### Pré-requisitos:
* Java 17 instalado
* Maven instalado
* Docker e Docker Compose (para execução dos serviços locais)

### Passo a Passo:

1.  **Clonar o repositório:**
    ```bash
    git clone https://github.com/loac02/priceWatch.git
    cd priceWatch
    ```

2.  **Iniciar infraestrutura local (Opcional):**
    Caso não utilize os serviços na nuvem (Supabase, Atlas, CloudAMQP) para testes locais, inicialize os containers:
    ```bash
    docker-compose up -d
    ```

3.  **Compilar e Buildar a aplicação:**
    ```bash
    mvn clean package -DskipTests
    ```

4.  **Executar a API:**
    ```bash
    java -jar target/pricewatch-api-0.0.1-SNAPSHOT.jar
    ```

---

## 📦 Deploy em Produção (Render)

A aplicação possui um pipeline de deploy contínuo via Docker estruturado no arquivo `Dockerfile`. 

Para realizar o deploy no **Render**:
1. Conecte o repositório GitHub ao Render como um **Web Service**.
2. Altere o *Runtime* para **Docker**.
3. No menu **Environment**, preencha as variáveis descritas na tabela de configuração com as suas credenciais do *Supabase*, *Mongo Atlas*, *CloudAMQP* e *Gmail*.
4. Conclua o deploy. O Render compilará e rodará a aplicação automaticamente 24/7.
