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

# Supported Product Attributes

* ID
* Name
* Description
* Category
* SKU
* Price

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

```shell
curl -XPOST -d '{
    "name": "Smart LED Light Bulb",
    "description": "Dimmable smart light bulb compatible with Alexa and Google Assistant. 16 million colors and adjustable brightness.",
    "category": "Smart Home",
    "sku": "LB-54321-SM",
    "price": "19.99"
  }' https://v7swljnkwc.execute-api.eu-central-1.amazonaws.com/dev/api/v1/products

curl -XPOST -d '{
    "name": "Ergonomic Office Chair",
    "description": "High-back mesh chair with lumbar support, adjustable armrests, and 360-degree swivel for maximum comfort.",
    "category": "Furniture",
    "sku": "OC-67890-ER",
    "price": "129.99"
  }' https://v7swljnkwc.execute-api.eu-central-1.amazonaws.com/dev/api/v1/products

curl -XPOST -d '{
    "name": "4K Ultra HD Action Camera",
    "description": "Waterproof action camera with 170° wide-angle lens, 4K resolution, and built-in Wi-Fi for sharing videos and photos.",
    "category": "Cameras",
    "sku": "AC-11223-4K",
    "price": "99.99"
  }' https://v7swljnkwc.execute-api.eu-central-1.amazonaws.com/dev/api/v1/products

curl -XPOST -d '{
    "name": "Yoga Mat with Carrying Strap",
    "description": "Non-slip, eco-friendly yoga mat made from TPE material, ideal for all types of yoga and fitness workouts. Includes a carrying strap.",
    "category": "Sports & Outdoors",
    "sku": "YM-33445-TPE",
    "price": "29.99"
  }' https://v7swljnkwc.execute-api.eu-central-1.amazonaws.com/dev/api/v1/products

curl -XPOST -d '{
    "name": "Stainless Steel Chefs Knife",
    "description": "8-inch professional chefs knife made from high-carbon stainless steel for precision cutting and durability.",
    "category": "Kitchen & Dining",
    "sku": "CK-55667-SS",
    "price": "45.99"
  }' https://v7swljnkwc.execute-api.eu-central-1.amazonaws.com/dev/api/v1/products
```

## Add Product with predefined UUID

```shell
curl -XPUT -d '{
  "name": "Organic Cotton Bed Sheets",
  "description": "Ultra-soft, breathable, 100% organic cotton bed sheet set with deep pockets. Includes one fitted sheet, one flat sheet, and two pillowcases.",
  "category": "Bedding",
  "sku": "BS-99887-OC",
  "price": "59.99"
}' https://v7swljnkwc.execute-api.eu-central-1.amazonaws.com/dev/api/v1/products/d9ac78a3-cd3d-49a0-a8f1-8c8dbbe526c3
```

## List All Products

```shell
curl -XGET https://v7swljnkwc.execute-api.eu-central-1.amazonaws.com/dev/api/v1/products
```

## Retrieve Product by ID

```shell
curl -XGET https://v7swljnkwc.execute-api.eu-central-1.amazonaws.com/dev/api/v1/products/d9ac78a3-cd3d-49a0-a8f1-8c8dbbe526c3
```

## Update Product by ID

```shell
curl -XPUT -d '{
  "name": "Portable Wireless Speaker",
  "description": "Compact Bluetooth speaker with deep bass, 12-hour battery life, and water-resistant design. Ideal for outdoor use.",
  "category": "Audio",
  "sku": "SP-77889-BT",
  "price": "39.99"
}' https://v7swljnkwc.execute-api.eu-central-1.amazonaws.com/dev/api/v1/products/d9ac78a3-cd3d-49a0-a8f1-8c8dbbe526c3
```

## Remove Product by ID

```shell
curl -XDELETE https://v7swljnkwc.execute-api.eu-central-1.amazonaws.com/dev/api/v1/products/d9ac78a3-cd3d-49a0-a8f1-8c8dbbe526c3
```

# Author

Dominik Cebula

* https://dominikcebula.com/
* https://blog.dominikcebula.com/
* https://www.udemy.com/user/dominik-cebula/
