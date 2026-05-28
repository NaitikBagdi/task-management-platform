-> Task Management Platform (TMP)

This repository contains a production-realistic, highly scalable backend system for managing projects and tasks, built with Spring Boot 3.x and secured behind an independent, role-based JWT authentication framework.

-> Technical Stack & Framework Specifications
The platform has been strictly built using industry-standard modern specifications to ensure optimized containerization and LTS support:
* Java Version: Java 21
* Framework: Spring Boot 3.5.14 (Fully Stateless & Microservices Compliant)
* Build Tool: Apache Maven 3.x
* Database Engine: PostgreSQL 16.14

-> System Architecture & Component Layout
The platform utilizes a decoupled, database-isolated microservices architecture consisting of two distinct standalone applications that coordinate through standard inter-process HTTP communications:

* auth-service (Port `8081`): Owns identity contexts, handles user lifecycle management (Registration/Login), and constructs digitally signed compact JSON Web Tokens (HS256).
* task-service (Port `8082`): Manages the lifecycle of project workspaces and granular tasks. It validates incoming Bearer JWTs locally via a cryptographically matched shared secret key.


-> Database Storage Architecture (Schema Isolation)
To strictly satisfy resource separation requirements without deploying separate physical database servers, both microservices share a single PostgreSQL database instance (`tmp_database`) but operate within entirely isolated database schemas enforced at the application property level:
* Identity context states persist within the `authdb` schema.
* Project and Task business entities persist within the `taskdb` schema.

-> Local Schema Setup Script
Before launching the microservices, connect to your local PostgreSQL server via `psql` or pgAdmin and execute the following initialization script:

```sql
-- 1. Create the shared central database instance
CREATE DATABASE tmp_database;

-- 2. Connect to the newly created database instance
\c tmp_database;

-- 3. Provision distinct logical schemas for database isolation
CREATE SCHEMA authdb;
CREATE SCHEMA taskdb;
