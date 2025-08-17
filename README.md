# Customer Relationship Management System

A Spring Boot application for managing customers, sales, and interactions. It includes features like user authentication, role-based access, reporting, and logging.

---

## Table of Contents
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [API Endpoints](#api-endpoints)

---

## Features
- User authentication and authorization
- Role-based access control (Admin, Sales Representative, Sales Manager, Data Analyst)
- Customer management (Create, Read, Update, Delete)
- Sales tracking and revenue reports
- Customer interaction logs
- Logging and error handling
- RESTful APIs for integration

---

## Technologies Used
- Java 21
- Spring Boot 3.x
- Spring Data JPA
- Spring Security
- MySQL
- Maven
- Lombok

---

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/Mahesh-Hattatodi/CRMSystemBackend.git
2. Navigate into the project directory:
   ```bash
   cd crm-system
3. Build the project using Maven:
    ```bash
   mvn spring-boot:run
4. Run the application:
    ```bash
   mvn spring-boot:run
5. Access the application at:
   http://localhost:8080

## API Endpoints
### Security
| Endpoint       | Method | Description                                                                                                              |
| -------------- | ------ | ------------------------------------------------------------------------------------------------------------------------ |
| /auth/register | POST   | Register a new user (Admin can create Admin, Sales Manager, Data Analyst; Sales Manager can create Sales Representative) |
| /auth/login    | POST   | Login with username and password, returns JWT tokens                                                                     |
| /auth/refresh  | POST   | Refresh access token using a valid refresh token                                                                         |


![Security endpoints](crm%20backend/security/admin%20login.png)
![Security endpoints](crm%20backend/security/sales%20manager%20or%20data%20analyst%20register.png)
![Security endpoints](crm%20backend/security/refresh%20access%20token.png)


### Customer
| Endpoint                      | Method | Description                                       |
| ----------------------------- | ------ | ------------------------------------------------- |
| /customers                    | POST   | Create a new customer                             |
| /customers/{customerPublicId} | GET    | Get details of a customer by public ID            |
| /customers                    | GET    | List all customers                                |
| /customers/{customerPublicId} | PUT    | Update customer details by public ID              |
| /customers/{customerPublicId} | DELETE | Delete a customer by public ID                    |
| /customers/reassign           | PUT    | Reassign a customer from one sales rep to another |


![Security endpoints](crm%20backend/customers/create%20customers.png)
![Security endpoints](crm%20backend/customers/get%20all%20customers.png)
![Security endpoints](crm%20backend/customers/get%20cus%20by%20id.png)

### Sales
| Endpoint              | Method | Description                        |
| --------------------- | ------ | ---------------------------------- |
| /sales                | POST   | Create a new sale record           |
| /sales/{salePublicId} | GET    | Get details of a sale by public ID |
| /sales                | GET    | List all sales                     |
| /sales/{salePublicId} | PUT    | Update a sale record by public ID  |
| /sales/{salePublicId} | DELETE | Delete a sale record by public ID  |


![Security endpoints](crm%20backend/sales/create%20sale%20record.png)
![Security endpoints](crm%20backend/sales/get%20sale%20by%20id.png)

### Customer Interactions
| Endpoint                            | Method | Description                                |
| ----------------------------------- | ------ | ------------------------------------------ |
| /interactions                       | POST   | Create a new customer interaction          |
| /interactions/{interactionPublicId} | GET    | Get details of an interaction by public ID |
| /interactions                       | GET    | List all customer interactions             |
| /interactions/{interactionPublicId} | PUT    | Update a customer interaction by public ID |
| /interactions/{interactionPublicId} | DELETE | Delete a customer interaction by public ID |

### Reporting
| Endpoint                       | Method | Description                                                                  |
| ------------------------------ | ------ | ---------------------------------------------------------------------------- |
| /reports/customer-revenue      | GET    | Get revenue report for all customers between start and end dates             |
| /reports/salesrep-revenue      | GET    | Get revenue report for all sales representatives between start and end dates |
| /reports/customer-interactions | GET    | Get customer interaction report between start and end dates                  |

