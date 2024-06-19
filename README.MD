# Email Delivery Microservice
This microservice is designed to handle asynchronous email delivery using Java, Spring Boot, and various supporting technologies. It communicates via a Message Broker (Kafka), stores message details in ElasticSearch, and attempts to send emails via SMTP. It includes retry mechanisms for failed deliveries and integrates with other microservices to send notifications based on entity events.
It sends email when new team is created. How to create a team and other rest api details you can see in [repo](https://github.com/olenavit/spring-boot-rest)
## Technologies Used
- Java
- Spring Boot
- ElasticSearch
- Kafka (Message Broker)
- Docker
## Features
Asynchronously receives email message details via Message Broker from another spring rest service described in this [repo](https://github.com/olenavit/spring-boot-rest)
Stores messages in ElasticSearch.
Attempts to send emails using JavaMailSender.
Retries failed email deliveries automatically every 5 minutes.
## Setup Instructions
1. By default, app use SMTP protocol to send message, so that before run the app you should to generate password for app in your gmail account. You can read how to do it in [this topic](https://stackoverflow.com/questions/26594097/javamail-exception-javax-mail-authenticationfailedexception-534-5-7-9-applicatio/72592946#72592946)
2. Update .env file
Update an [environment](.env) file in the root directory with the following content, adjusting values as per your email server setup:

```plaintext
EMAIL_HOST=smtp.gmail.com
EMAIL_PORT=587
EMAIL_USERNAME={your_username}
EMAIL_PASSWORD={your_password}
EMAIL_PROTOCOL=smtp
EMAIL_SMTP_AUTH=true
EMAIL_SMTP_STARTTLS_ENABLE=true
EMAIL_DEBUG=true
ADMIN_EMAIL={admin_email}
```
where:
- your_username : your email username
- your_password: your email password
- admin_email: email where email messages will be sent

3. Run app

```bash
docker-compose up --build
```
This command will build the Docker images and start the containers. Make sure all services are up and running before proceeding.

# Integration Tests:

To run integration tests:

```bash
mvn clean test
```
This command will execute tests to validate email sending logic under various scenarios. Please wait when connection to kafka failed and then all tests will be executed


