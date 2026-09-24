![logo](src/main/resources/static/asets/img/msvc-ecommerce.png)

# 🛒 E-Commerce Microservices

Proyecto de práctica y aprendizaje orientado a una arquitectura de **microservicios para un sistema e-commerce**, desarrollado con **Java, Spring Boot, Spring Cloud, OpenFeign, Eureka, Apache Kafka, Docker, Kubernetes, Prometheus y Grafana**.

El objetivo es construir progresivamente una arquitectura distribuida, aplicando comunicación síncrona y asíncrona, separación de responsabilidades, persistencia independiente y arquitectura orientada a eventos.

---

## 🏗️ Arquitectura

```text
                              ┌─────────────────────┐
                              │     API GATEWAY     │
                              │        :8080        │
                              └──────────┬──────────┘
                                         │
                                         ▼
                              ┌─────────────────────┐
                              │    EUREKA SERVER    │
                              │        :8761        │
                              └──────────┬──────────┘
                                         │
              ┌──────────────────────────┼──────────────────────────┐
              │                          │                          │
              ▼                          ▼                          ▼
       ┌──────────────┐          ┌──────────────┐          ┌──────────────┐
       │ USER SERVICE │          │   PRODUCT    │          │ ITEM SERVICE │
       │    :8081     │          │   SERVICE    │          │              │
       └──────────────┘          └──────────────┘          └──────────────┘
                                         │
                                         │
                                         ▼
                               ┌──────────────────┐
                               │   ORDER SERVICE  │
                               │      :8084       │
                               └────────┬─────────┘
                                        │
                                        │ OrderCreatedEvent
                                        ▼
                               ┌──────────────────┐
                               │      KAFKA       │
                               │  order-created   │
                               └────────┬─────────┘
                                        │
                         ┌──────────────┴──────────────┐
                         │                             │
                         ▼                             ▼
                ┌──────────────────┐         ┌─────────────────────┐
                │ INVENTORY SERVICE│         │ NOTIFICATION SERVICE│
                └──────────────────┘         └─────────────────────┘
```

---

# 📦 Microservicios

| Servicio               | Responsabilidad                             |
| ---------------------- | ------------------------------------------- |
| `API-GATEWAY`          | Punto de entrada de las peticiones externas |
| `EUREKA-SERVER`        | Service Discovery                           |
| `USER-SERVICE`         | Gestión de usuarios y roles                 |
| `PRODUCT-SERVICE`      | Gestión del catálogo de productos           |
| `ITEM-SERVICE`         | Gestión de items                            |
| `ORDER-SERVICE`        | Creación y gestión de órdenes               |
| `INVENTORY-SERVICE`    | Gestión del stock                           |
| `NOTIFICATION-SERVICE` | Procesamiento de notificaciones             |

---

# 🛠️ Tecnologías

* Java
* Spring Boot
* Spring Web
* Spring Data JPA
* Spring Cloud
* Spring Cloud Netflix Eureka
* Spring Cloud Gateway
* OpenFeign
* Apache Kafka
* Maven
* Docker
* Kubernetes
* Prometheus
* Grafana
* MySQL
* PostgreSQL

---

# 📁 Estructura del proyecto

```text
E-COMMERCE-MICROSERVICES
│
├── API-GATEWAY
│
├── EUREKA-SERVER
│
├── INVENTORY-SERVICE
│
├── ITEM-SERVICE
│
├── NOTIFICATION-SERVICE
│
├── ORDER-SERVICE
│
├── PRODUCT-SERVICE
│
├── USER-SERVICE
│
├── prometheus
│   └── prometheus.yml
│
├── pom.xml
├── mvnw
└── mvnw.cmd
```

---

# 🌐 API Gateway

El `API-GATEWAY` funciona como punto de entrada para los clientes.

En lugar de que el cliente tenga que conocer cada microservicio, realiza las peticiones contra el Gateway.

```text
Client
  │
  ▼
API Gateway
  │
  ├── /api/users/**     → USER-SERVICE
  │
  ├── /api/products/**  → PRODUCT-SERVICE
  │
  ├── /api/items/**     → ITEM-SERVICE
  │
  └── /api/orders/**    → ORDER-SERVICE
```

Esto permite centralizar posteriormente aspectos como:

* Routing
* Seguridad
* Autenticación
* Autorización
* Rate limiting
* Logging
* Observabilidad

---

# 🔎 Eureka Server

`EUREKA-SERVER` proporciona **Service Discovery**.

Los microservicios se registran en Eureka y pueden localizar otros servicios mediante su nombre lógico.

```text
                  EUREKA
                    │
        ┌───────────┼────────────┐
        │           │            │
        ▼           ▼            ▼
      USER       PRODUCT       ORDER
```

Por ejemplo:

```text
http://USER-SERVICE
```

en lugar de depender directamente de una IP fija.

---

# 🔗 OpenFeign

Para comunicación síncrona entre microservicios se utiliza **OpenFeign**.

Ejemplo conceptual:

```text
ORDER-SERVICE
      │
      │ HTTP / OpenFeign
      ▼
USER-SERVICE
      │
      │ response
      ▼
ORDER-SERVICE
```

El servicio solicitante espera la respuesta.

Este mecanismo es apropiado cuando necesitamos una respuesta inmediata.

---

# ⚡ Comunicación síncrona vs asíncrona

El proyecto utiliza ambos modelos.

## Comunicación síncrona

```text
SERVICE A
    │
    │ HTTP
    ▼
SERVICE B
    │
    │ response
    ▼
SERVICE A
```

Tecnologías:

```text
REST
OpenFeign
```

---

## Comunicación asíncrona

```text
SERVICE A
    │
    │ Event
    ▼
  KAFKA
    │
    ├────────────► SERVICE B
    │
    └────────────► SERVICE C
```

Tecnología:

```text
Apache Kafka
```

La comunicación asíncrona permite desacoplar procesos que no necesitan una respuesta inmediata.

---

# 📨 Apache Kafka

Kafka se utiliza como sistema de mensajería y distribución de eventos.

Uno de los eventos principales del proyecto es:

```text
order-created
```

El evento se genera desde:

```text
ORDER-SERVICE
```

y puede ser consumido por:

```text
INVENTORY-SERVICE
NOTIFICATION-SERVICE
```

Flujo:

```text
ORDER-SERVICE
       │
       │ OrderCreatedEvent
       ▼
     KAFKA
       │
       ├──────────────► INVENTORY-SERVICE
       │
       └──────────────► NOTIFICATION-SERVICE
```

---

# 🛒 Product Service

`PRODUCT-SERVICE` administra el catálogo.

Actualmente el modelo `Product` contiene:

```java
public class Product {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private String sku;
}
```

Ejemplo:

```text
Product
--------------------------------
id          = 10
name        = Laptop Lenovo
description = Laptop empresarial
price       = 2500.00
sku         = LENOVO-001
```

El `Product Service` representa la información del producto, no necesariamente su stock.

---

# 📦 Item Service

Actualmente `Item` contiene:

```java
public class Item {

    private Long id;
    private Long productId;
    private Integer quantity;
    private String serialNumber;
    private Double unitPrice;
    private Double totalPrice;
}
```

Ejemplo:

```text
Item
--------------------------------
productId   = 10
quantity    = 2
unitPrice   = 2500.00
totalPrice  = 5000.00
```

## ⚠️ Consideración de diseño

La responsabilidad exacta de `Item` debe mantenerse claramente diferenciada de `OrderItem`.

Si `Item` representa una unidad física identificada mediante `serialNumber`, normalmente:

```text
quantity = 1
```

Si `Item` representa una agrupación de productos, entonces:

```text
quantity > 1
```

puede tener sentido.

Esta distinción es importante para evitar duplicar responsabilidades entre `Item`, `OrderItem` e `Inventory`.

---

# 📋 Order Service

`ORDER-SERVICE` administra las órdenes.

Una orden puede contener múltiples `OrderItem`.

```text
Order
 │
 ├── OrderItem
 │      ├── productId
 │      ├── quantity
 │      ├── unitPrice
 │      └── totalPrice
 │
 ├── OrderItem
 │      ├── productId
 │      ├── quantity
 │      ├── unitPrice
 │      └── totalPrice
 │
 └── ...
```

Ejemplo:

```text
ORDER #1001

Laptop Lenovo
quantity: 2
unitPrice: 2500
total: 5000

Mouse Logitech
quantity: 1
unitPrice: 80
total: 80

-------------------------
TOTAL: 5080
```

---

# 📦 Inventory Service

`INVENTORY-SERVICE` es responsable de la disponibilidad de stock.

Conceptualmente:

```java
public class Inventory {

    private Long id;
    private Long productId;
    private Integer quantity;
}
```

Ejemplo:

```text
productId = 10
quantity  = 25
```

Significa:

```text
Producto 10
Stock disponible: 25 unidades
```

El principio importante es:

> El servicio que posee el stock debe ser el propietario de la información de stock.

Por lo tanto, otros microservicios no deberían modificar directamente la base de datos de Inventory.

---

# 🧠 Data Ownership

Cada microservicio debe ser responsable de sus propios datos.

```text
USER-SERVICE
    │
    └── users database

PRODUCT-SERVICE
    │
    └── products database

ITEM-SERVICE
    │
    └── items database

ORDER-SERVICE
    │
    └── orders database

INVENTORY-SERVICE
    │
    └── inventory database

NOTIFICATION-SERVICE
    │
    └── notifications database
```

No se recomienda:

```text
ORDER-SERVICE
      │
      ▼
Inventory Database
```

ni:

```text
ITEM-SERVICE
      │
      ▼
Product Database
```

Los servicios deben comunicarse mediante APIs o eventos.

---

# 🔄 Flujo de creación de una orden

Un posible flujo es:

```text
                    CLIENT
                       │
                       ▼
                 API GATEWAY
                       │
                       ▼
                 ORDER SERVICE
                       │
          ┌────────────┼────────────┐
          │            │            │
          ▼            ▼            ▼
        USER        INVENTORY     PRODUCT
       SERVICE       SERVICE      SERVICE
          │            │            │
          └────────────┼────────────┘
                       │
                       ▼
                 CREATE ORDER
                       │
                       ▼
              OrderCreatedEvent
                       │
                       ▼
                     KAFKA
                    /     \
                   /       \
                  ▼         ▼
            INVENTORY   NOTIFICATION
```

---

# 📊 Stock y órdenes

Existe una diferencia importante entre:

```text
Validar stock
```

y:

```text
Descontar stock
```

Por ejemplo:

```text
Cliente solicita:

2 laptops
```

Inventory podría tener:

```text
25 laptops disponibles
```

El sistema debe evitar que dos órdenes simultáneas consuman el mismo stock de forma incorrecta.

Por eso, una evolución natural de la arquitectura es implementar:

```text
Reserve Stock
      │
      ▼
Create Order
      │
      ▼
Confirm Stock
```

o:

```text
Reserve Stock
      │
      ├── SUCCESS
      │
      └── FAILURE
```

Posteriormente puede incorporarse el patrón **Saga** para coordinar operaciones distribuidas.

---

# ⚠️ Importante con Kafka

Si se utiliza una llamada síncrona para reservar stock:

```text
ORDER
  │
  │ reserve
  ▼
INVENTORY
```

y posteriormente se publica:

```text
OrderCreatedEvent
```

el consumidor de Inventory no debe volver a descontar exactamente el mismo stock.

De lo contrario:

```text
Reserva: -2
Kafka:   -2
----------------
Total:   -4 ❌
```

Una arquitectura correcta debe definir claramente qué operación realiza cada mensaje.

Por ejemplo:

```text
OrderCreated
```

puede utilizarse para notificación, auditoría u otros procesos.

Mientras que:

```text
StockReserved
StockConfirmed
StockReleased
```

pueden representar explícitamente el ciclo de vida del inventario.

---

# 🔔 Notification Service

`NOTIFICATION-SERVICE` consume eventos de Kafka.

Por ejemplo:

```text
OrderCreatedEvent
        │
        ▼
      Kafka
        │
        ▼
Notification Service
        │
        ▼
Enviar / registrar notificación
```

La ventaja es que `ORDER-SERVICE` no necesita esperar a que la notificación termine.

---

# 🔄 Event Driven Architecture

El proyecto utiliza un enfoque orientado a eventos.

Ejemplo:

```text
ORDER CREATED
      │
      ▼
OrderCreatedEvent
      │
      ▼
Kafka
      │
      ├─────────────► Notification
      │
      └─────────────► Inventory
```

Cada consumidor puede reaccionar al mismo evento de forma independiente.

---

# 🧩 Serialización y deserialización

Kafka necesita convertir los objetos Java en datos que puedan transportarse.

### Serialización

```text
Java Object
     │
     ▼
JSON / bytes
```

Por ejemplo:

```java
OrderCreatedEvent
```

se convierte en un mensaje.

### Deserialización

```text
JSON / bytes
     │
     ▼
Java Object
```

El consumidor reconstruye:

```java
OrderCreatedEvent
```

Este proceso es fundamental para la comunicación Kafka.

---

# 🗄️ Persistencia

Cada servicio debe administrar su propia persistencia.

Ejemplo:

```text
USER-SERVICE
       │
       ▼
     MySQL

PRODUCT-SERVICE
       │
       ▼
   PostgreSQL

ORDER-SERVICE
       │
       ▼
     MySQL
```

La tecnología concreta puede variar según el servicio.

El objetivo es evitar un modelo de base de datos monolítico compartido.

---

# 📈 Observabilidad

El proyecto incorpora:

```text
Spring Boot Actuator
        │
        ▼
   Prometheus
        │
        ▼
     Grafana
```

Los microservicios pueden exponer métricas mediante:

```text
/actuator/prometheus
```

Prometheus recopila las métricas y Grafana permite construir dashboards.

---

# 🐳 Docker

La arquitectura está preparada para ejecutarse mediante contenedores.

Conceptualmente:

```text
Docker
│
├── API Gateway
├── Eureka Server
├── User Service
├── Product Service
├── Item Service
├── Order Service
├── Inventory Service
├── Notification Service
├── Kafka
├── Databases
├── Prometheus
└── Grafana
```

Esto permite reproducir el entorno de desarrollo de manera más consistente.

---

# ☸️ Kubernetes

Como evolución del proyecto, los servicios pueden desplegarse en Kubernetes.

Conceptualmente:

```text
                    Kubernetes Cluster
                           │
                           ▼
                    ┌─────────────┐
                    │ API Gateway │
                    └──────┬──────┘
                           │
        ┌──────────────────┼──────────────────┐
        ▼                  ▼                  ▼
      USER              PRODUCT             ORDER
                                               │
                                               ▼
                                             KAFKA
                                            /     \
                                           ▼       ▼
                                      INVENTORY  NOTIFICATION
```

Objetivos:

* Deployments
* Services
* ConfigMaps
* Secrets
* Health Checks
* Scaling
* Rolling Updates
* Configuración externa
* Observabilidad

---

# ▶️ Ejecución local

Cada microservicio puede ejecutarse desde IntelliJ IDEA o mediante Maven.

### Linux / Git Bash

```bash
./mvnw spring-boot:run
```

### Windows

```cmd
mvnw.cmd spring-boot:run
```

Orden sugerido:

```text
1. EUREKA-SERVER
2. API-GATEWAY
3. USER-SERVICE
4. PRODUCT-SERVICE
5. ITEM-SERVICE
6. INVENTORY-SERVICE
7. ORDER-SERVICE
8. NOTIFICATION-SERVICE
9. KAFKA
10. PROMETHEUS
11. GRAFANA
```

El orden puede variar dependiendo de la configuración y del entorno de ejecución.

---

# 🔌 Puertos

Ejemplo de distribución actual:

```text
EUREKA-SERVER       :8761
API-GATEWAY         :8080
USER-SERVICE        :8081
ORDER-SERVICE       :8084
```

Los demás servicios pueden utilizar los puertos configurados en sus respectivos `application.properties` o `application.yml`.

---

# 🧪 Objetivos de aprendizaje

Este proyecto permite practicar:

### Java

* Java moderno
* POO
* Collections
* Exceptions
* DTOs
* Interfaces
* Dependency Injection

### Spring Boot

* Controllers
* Services
* Repositories
* JPA
* Hibernate
* Configuration
* Actuator

### Spring Cloud

* Eureka
* API Gateway
* OpenFeign
* Service Discovery

### Kafka

* Producer
* Consumer
* Topics
* Partitions
* Consumer Groups
* Events
* Serialization
* Deserialization
* Retry
* Error Handling

### Arquitectura

* Microservices
* Event-driven architecture
* Data ownership
* Synchronous communication
* Asynchronous communication
* Eventual consistency
* Saga
* Idempotency

### DevOps

* Docker
* Kubernetes
* Prometheus
* Grafana
* Linux

---

# 🚀 Próximos pasos

```text
[ ] Definir definitivamente la responsabilidad de Item
[ ] Separar correctamente Item y OrderItem
[ ] Implementar reserva de stock
[ ] Implementar confirmación de stock
[ ] Implementar liberación de stock
[ ] Mejorar OrderCreatedEvent
[ ] Configurar Kafka JSON Serializer
[ ] Configurar Kafka JSON Deserializer
[ ] Implementar manejo de errores Kafka
[ ] Implementar Retry
[ ] Implementar Dead Letter Topic
[ ] Implementar idempotencia
[ ] Implementar Correlation ID
[ ] Agregar Distributed Tracing
[ ] Crear Docker Compose
[ ] Desplegar en Kubernetes
[ ] Agregar Health Checks
[ ] Implementar OAuth2 / JWT
[ ] Mejorar dashboards de Grafana
```

---

# 🎯 Objetivo final

El objetivo es construir una plataforma e-commerce distribuida utilizando:

```text
                     ┌───────────────────┐
                     │    E-COMMERCE     │
                     └─────────┬─────────┘
                               │
        ┌──────────────────────┼──────────────────────┐
        │                      │                      │
        ▼                      ▼                      ▼
   Spring Boot            Spring Cloud             Kafka
        │                      │                      │
        │              ┌───────┴───────┐              │
        │              │               │              │
        ▼              ▼               ▼              ▼
      REST           Eureka          Gateway       Events
        │              │               │              │
        └──────────────┴───────────────┴──────────────┘
                               │
                               ▼
                           Docker
                               │
                               ▼
                         Kubernetes
                               │
                               ▼
                    Prometheus + Grafana
```

El proyecto se desarrolla de forma incremental con el objetivo de comprender no solamente **cómo implementar microservicios**, sino también **cómo definir correctamente sus responsabilidades, cómo se comunican y cómo mantener la consistencia de los datos en un sistema distribuido**.

---

## 👨‍💻 Proyecto de aprendizaje

```text
Java
Spring Boot
Spring Cloud
Kafka
Microservices
Docker
Kubernetes
Prometheus
Grafana
```

**Construido para aprender, experimentar y evolucionar una arquitectura de e-commerce hacia un entorno cloud-native.**


## 📄 Licencia

Este proyecto puede ser distribuido bajo licencia **MIT** (si corresponde). Agrega un archivo `LICENSE` en la raíz si deseas publicarlo.

---

## 📬 Contacto

Para dudas, sugerencias o contribuciones:

📧 [**casseli.layza@gmail.com**](mailto:casseli.layza@gmail.com)

🔗 [LinkedIn](https://www.linkedin.com/in/casseli-layza/) 🔗 [GitHub](https://github.com/CasseliLayza)

💡 **Desarrollado por Casseli Layza como parte de un proyecto con SpringCloud / SpringBoot. Arquitectura Distribuida.**

**_💚 ¡Gracias por revisar este proyecto!... Powered by Casse 🌟📚🚀...!!_**

## Derechos Reservados

```markdown
© 2026 Casse. Todos los derechos reservados.
```