[![CI Pipeline](https://github.com/dominikcebula/aws-lambda-java-spring-sample/actions/workflows/maven.yml/badge.svg)](https://github.com/dominikcebula/aws-lambda-java-spring-sample/actions/workflows/maven.yml)

# Introduction

This repository contains sample AWS Lambda code that implements Product Catalog Management API exposed using API
Gateway.

# High Level Design Diagram

TBD

# Supported Use Cases

All CRUD operations regarding Products Catalog Management are supported:

* Product Creation
* Product Update
* Products Listing
* Product Retrieval based on ID
* Product Deletion

# API

TBD

# Deployment

Code is deployed using AWS SAM.

First, build the project:

```shell
mvn clean install
```

Then use AWS SAM to deploy it:

```shell
sam deploy
```

Once deployment is done, last line will show API URL:

```text
---------------------------------------------------------------------------------

CloudFormation outputs from deployed stack
---------------------------------------------------------------------------------
Outputs
---------------------------------------------------------------------------------
Key                 ApiEndpoint
Description         API Gateway endpoint URL for Products API
Value               https://v7swljnkwc.execute-api.eu-central-1.amazonaws.com/
---------------------------------------------------------------------------------
```

API will be available under API URL with StageName, in the above example it will, using get all products example:

`GET https://v7swljnkwc.execute-api.eu-central-1.amazonaws.com/dev/api/v1/products`

# Sample operations using API

## Add Products

TBD

## List All Products

TBD

## Retrieve Product by ID

TBD

## Update Product by ID

TBD

## Remove Product by ID

TBD

# Author

Dominik Cebula

* https://dominikcebula.com/
* https://blog.dominikcebula.com/
* https://www.udemy.com/user/dominik-cebula/
