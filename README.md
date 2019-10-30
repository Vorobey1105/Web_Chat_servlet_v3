# Web_Chat_servlet_v3
Webchat for exchanging messages by several users.

# Functions and working rules
The chat supports registration, login, and exchanging messages. 

On the registration page, users type the main information about themselves such as NICK and FULL NAME. It also possible to upload an image. The uploading of an image is advisable, but users can leave the download field empty anyway. Additionally, on the registration page, it is necessary to type a password two times for verification purposes. It also supports client-server validation. By the usage of regular expressions, the page helps users to type valid information. It shows and highlights the "wrong" fields and does not send the form until the information is valid.

After the registration, users are redirected to the chat page automatically. This page allows them to send messages and see the main information about online users on mouseover. It is advisable for users to logout, but their sessions will be destroyed within 20 minutes anyway if they decide just to close the chat page without logout.

The login page contains only two fields: Nick and Password. The application validates the user's data and redirects to the chat page in case of valid information or shows an error if a nick or a password is wrong.


## Getting Started (Deployment and test data)

The application works with the TomCat server. It is advisable to use the TomCat 9 version or later.

1. The application gets the SQL connection via JNDI. it is necessary to set the connection settings in the ${catalina.home}/conf/context.xml.
The example of connection settings:
```
<Resource name="jdbc/XE" 
	auth="Container" 
	type="javax.sql.DataSource"
	driverClassName="oracle.jdbc.OracleDriver"
	url="jdbc:oracle:thin:@localhost:1521:XE"
	username="{your userName}" 
	password="{your Password}" 
	maxActive="20"
    maxIdle="10"
    ma xWait="10000"
	/>
```
2. It also necessary to put the ojdbc6 file to the ${catalina.home}/lib/ folder. 
The file to copy can be found in the "lib" folder of the project -  ${basedir}/lib.

3. It also advisable to "turn on" the filter which is located in ${catalina.home}/conf/web.xml.
```
  <filter>
        <filter-name>httpHeaderSecurity</filter-name>
        <filter-class>org.apache.catalina.filters.HttpHeaderSecurityFilter</filter-class>
        <async-supported>true</async-supported>
    </filter>

	<filter-mapping>
   	 	<filter-name>httpHeaderSecurity</filter-name>
   	 	<url-pattern>/*</url-pattern>
	</filter-mapping>
```
4. Next, configure the ${catalina.home}/conf/tomcat-users.xml file. It is crucial to add the roles, name and a password of a user. IT is possible to write any userName and Password. A simple example of this file:
```
<?xml version="1.0" encoding="UTF-8"?>

<tomcat-users>
	<role rolename="manager-gui"/>
	<role rolename="manager-script"/>
	<role rolename="manager-jax"/>
	<role rolename="manager-status"/>
	<role rolename="admin"/>
	<user username="admin" password="admin" roles="admin,manager-gui,manager-script"/>
</tomcat-users>
```
5. On the next stage, we need to create initial tables and add some test data to the SQL database. There are two files containing the initial scrips for the tables' creation and adding the test data: "create_db.sql"  and "test_data.sql". They can be found in the resource folder inside the project ${basedir}/src/main/resources. The test data fills the initial information for the application working process.

```
"create_db.sql" - the creation the initial tables
"test_data.sql" - the initial test data

```
6. The application supports the creation of the WAR file with the help of Maven. This is possible via "mvn package" command and then the WAR can be found in the target folder of the project.

7. Finally, the WAR file can be deployed on The Tomcat server. Before the deployment, we need to start the TomCat server. It is possible to choose any methods to start it. However, the simplest (quick start) way to do this is to go to $CATALINA_HOME\bin\ folder from where it is necessary to start the Command Prompt ("cmd"). Just write the following line to start the server:
```
catalina.bat run
```
After the server has started, it is possible to upload the WAR file to the Tomcat Server. This can be done from the TomCat manager. The manager dashboard can be accessed by visiting: http://localhost:{your port}.
Find the "manager app" button. Next, it is necessary to type the UserName and Password (It should be done on the 4th step). After that, go to the "WAR file to deploy" section, push the "browse file" button and choose the war file of the application. Finally, click to the deploy button.

Alternatively, it can be deployed by copying the archive. All that should be done is just putting the WAR file into the $CATALINA_HOME\webapps\ directory of any Tomcat instance. If the instance is running, the deployment will start instantly as Tomcat unpacks the archive and configures its context path. If the instance is not running, then the server will deploy the project the next time it is started.

8. Now it is possible to start the chat
```
http://localhost:{your port}/WebChatServletV3-1.0/
example:
http://localhost:8080/WebChatServletV3-1.0/
```

## Technology used

javax.servlet-api - 3.1.0

log4j - 1.2.17

ojdbc6 - 11.2.0.4.0

gson - 2.8.5

commons-fileupload - 1.4

guava - 18.0

HTML5

JavaScript

jquery-3.4.1 + AJAX

Oracle XE (eXpress Edition)

apache-tomcat-9.0.26

## Versioning

Version 1.0

## Authors

* **Sergey Vorobyev** - *hopefully, the future Java Developer*
