# AniWash <br/><img src="aniwash/src/main/resources/aniwash/images/what.png"  width="150" height="150">

## Table of contents
* [Features](#Features)
* [Installation](#Installation)
* [Future plans](#Future-plans)
* [Class overview](#Class-overview)
* * [Class diagram](#Class-diagram)
* [Use case overview](#Use-case) 
* * [Use case diagram](#Use-case-diagram)
* [Package diagram](#Package-diagram)
* [Activity diagram](#Activity-diagram)
* [Sequence diagram](#sequence-diagram)
* [](#)

## Features

Aniwash is a software for customer management and pet care scheduling. 

In the current version, the software has two employee types: the employer and the employee.
The employee has the ability to create customers, create pets and assign them to customers, create new products and services, and create new appointments.

The employer has the same abilities as the employee, but also has the ability to create new employees and edit the information of the employees.

There is a login page where the user can enter their username and password. If the user is not registered, the employer can create a new employee.

The software has a database where all the information is stored. The database is created using MySQL and the connection is made using the MySQL Connector.

The software is written in Java and uses the JavaFX library for the GUI.
## Installation
To run the software, you need to have Java 8 or higher installed on your computer. You also need to have MySQL installed on your computer. 
TODO: Add instructions for rest of installation.
### MySQL
To install MySQL, you can follow the instructions on the [MySQL website](https://dev.mysql.com/doc/mysql-installation-excerpt/5.7/en/).
### Java
To install Java, you can follow the instructions on the [Java website](https://www.java.com/en/download/help/download_options.xml).
## Future plans
* Add a page where the employer can see the employees.
* Add a customer page where the customer can see their pets and appointments.
* Add a pet page where the customer can see their pet's information and appointments.
* Add a page where the customer can see their invoices.
## Use case diagram:
<img src="readme_resources/usecase.jpg" width="800" height="600">

## Class diagram:
<img src="readme_resources/Class_diagram.png" width="800" height="600">

## Package diagram:
<img src="readme_resources/pkg_diagram.jpg" width="800" height="600">

## Activity diagram:
<img src="readme_resources/ActivityDiagram.jpg" width="600" height="600">

### Actors

Employee:
The employee actor is the main user of the application and is most in contact with customers. Employee can create new customers, pets, products and appointments. Employees are authenticated by login username and password.

Employer:
The employer actor has the same actions as an employee, but also can add or remove employees.

Database:
The database actor is responsible for accessing the database for CRUD operations. Database actor is responsible for backend access. Other actors use the actions to retrieve, store or edit information from the database. 

### Actions

Employee actions:
* Customers
* Pets
* Products
* Appointments
* *	All the above actions access all CRUD operations performed by Database actor.
* Login
* *	Only has access to read for login purposes.

Database CRUD actions:
* Create
* *	Inserts new data into the database.
* Read
* *	Retrieves data from the database.
* Update
* *	Updates data in the database.
* Delete
* *	Deletes data from the database.

Employer actions:
* Employees
* *	All CRUD operations

## Sequence diagram:
<img src="readme_resources/SequenceDiagram.jpg" width="1000" height="500">
