# Logiweb
Application simulates system of a certain company that carries out the transportation of different goods. 
## How to use 
It will be useful for both company employees and drivers. Drivers and employees can access their profiles through logging menu using username and password.
### For employee
Employee can add, edit, delete drivers, trucks, cargos using UI. 
Also employee can place new orders, then pick suitable truck, drivers and dates for newly created order.
### For driver
Driver, as mentioned above, can see details of an order he was picked for, current truck and shiftdriver. Also driver can change order status by checking order
waypoints. Driver status can be changed too, for example, from 'driving' to 'shifting'.
## Tech stack
Web app was build on mvc architecture pattern and written in Java, using Spring Framework, Thymeleaf and Tomcat for deploying. PostgreSQL is used as database.
Below you can see application database scheme.
![alt text](https://github.com/ChpokHead/DeutscheTelekom/blob/dev/db-scheme.png "database scheme")
