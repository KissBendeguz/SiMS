# How is it gonna work?
idk

>## Main structure
>#### The project has 3 main parts.
> * Web server(nginx) 
> * Backend server(springboot)
> * Database server(postgreSQL)

>## NGINX Web server
> Provides the frontend client to customers(clients).

>## Springboot Backend API
> Provides data for the client by communicating with the database.

>## PostgeSQL database
> Database accessable by the API

# App features

* Register
* Login
* Add business(es)
  * Employee management
    * Add & remove employees
    * Configure their permissions
* Virtual inventory
  * Item management
* Database manager
  * Own inventory API(Dynamic endpoints)
* QR code generation
* Item registration via QR code scanning(iOS & Android)