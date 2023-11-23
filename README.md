#  Emissions Calculator API


## How to set up locally?

1. Install `Java 17`
2. Use any IDE, recommendations are IntelliJ or STS
3. Configure Maven in IDE
4. Run the root project pom xml `emissions-calculator-api/pom.xml`
5. Tada... if no errors observed, then the module has been installed in your local maven repository and ready to use.
6. Start the server by running `src/main/java/com/xyzcorp/api/emissionscalculator/EmissionsCalculatorApiApplication.java`
7. Server runs on port **8090**
8. Try to access the open api doc (swagger) to read and test all the APIs available : `http://localhost:8090/api-ui.html`
9. Note: application uses H2 database
10. All the '**private**' APIs requires authorisation. Follow below steps to test private APIs
    1. Register user using '`/api/public/v1/user/register`'. Please use role as '_ROLE_ADMIN_' which is only role supported at the moment.
    2. For example: **POST** '`/api/public/v1/user/register`' with `{
"name": "My name",
"emailAddress": "myname@email.com",
"password": "myPassword12",
"role": "ROLE_ADMIN"
}`
    2. Login same user using '`/api/public/v1/user/authenticate`' or use default user credentials to login, refer '`src/main/resources/admin.json`'
    3. Authenticate API returns authorisation token, copy that token and add it in the Authorize option of OpenAPI
    4. OpenAPI is configured to remember the token even after page refresh, so the same authorisation token works for all private APIs until it expires