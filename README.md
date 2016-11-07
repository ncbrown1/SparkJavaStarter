# SparkJavaStarter

After beginning to work with [SparkJava](http://sparkjava.com/), a Sinatra-like
light-weight web framework for Java, I noticed that it was quite hard to be an
efficient developer given the stock utilities and settings. This project is my
attempt at creating a great starting point for future SparkJava projects to
expedite the setup process.

## Getting Started

### Set up your Github OAuth Credentials

Before trying to start this web server, you need to create `GITHUB_CLIENT_ID` and `GITHUB_SECRET` credentials.  You will need these later when you create your `env.sh` file.  

Note that the `env.sh` file is NOT saved to github, since it contains secret information. Alternatively, I have included `env.sh.example`, which you should copy into `env.sh` and fill in properly. To do that, take these steps:

1. Open this page in a new tab or window (keep this page of instructions open): https://github.com/settings/developers
1. Click "register an application".
1. Fill it in similar to the picture below
1. The callback URL should be http://localhost:4567/auth/callback

  ![OAuth App Create Example](https://github.com/ncbrown1/SparkJavaStarter/raw/master/oauth_app_create_example.png "Example OAuth Configurations")

1. You should be redirected to the following page. Take note of the client ID and
   the client secret. You will need that in the next step.

   ![OAuth App Result Example](https://github.com/ncbrown1/SparkJavaStarter/raw/master/oauth_app_result_example.png "Example OAuth Configurations")

### Creating and Running `env.sh`

Copy the `env.sh.example` into `env.sh`, fill in the proper values for your OAuth
from above, make it executable, and run it.

```bash
cp env.sh.example env.sh
(vim/emacs/atom/nano/etc) env.sh # edit file to add your OAuth settings
chmod +x env.sh
source ./env.sh
```

### Set Up the Application and Database

1. Ensure you have `postgresql` [installed](https://wiki.postgresql.org/wiki/Detailed_installation_guides) and running on localhost
1. Connect to your postgres instance (usually as user `postgres`) and run the following
```sql
CREATE DATABASE testjava;
CREATE USER testuser WITH PASSWORD 'testpassword';
GRANT ALL PRIVILEGES ON DATABASE "testjava" TO testuser;
```
1. Ensure you have `maven` and `java` installed.
1. Build the application via `mvn package`
1. To see all command line options, run
```
java -jar target/sparkjavastarter-1.0-jar-with-dependencies.jar --help
```
1. If you are running this for the first time, create the database tables:
```
java -jar target/sparkjavastarter-1.0-jar-with-dependencies.jar init-db
```
1. To run the web server, run
```
java -jar target/sparkjavastarter-1.0-jar-with-dependencies.jar serve
```

## Notable Files

* `src/main/resources/application.conf` - This is where the application
  configurations live.
* `src/main/java/me/nickbrown/sparkjavastarter/RunApplication.java` - This is
  the main class.

## Notable Additional Dependencies

You can check out the `pom.xml` file for the entire list, but here is a list of
some of the java libraries that I included and my reasons for doing so:

* [Typesafe Config](https://github.com/typesafehub/config)
  * Allows for dynamic configuration via `src/main/resources/application.conf`
  * We can specify static config values or pull them in from the environment
    * For example, to get `$PORT` from the environment, we simply write `${PORT}`
  * Also, we can override configs and provide fallback values.
    * If we wanted the environment port to be optional, we could specify a
      default and then follow up with an alternate syntax for environment vars:
      * `port = 4000`
      * `port = ${?PORT}`
* [Spark Handlebars Template Engine](https://github.com/perwendel/spark-template-engines/tree/master/spark-template-handlebars)
  * In order to provide dynamic HTML, we use Handlebars.
  * Our Controller class actually extends from the HandlebarsTemplateEngine so
    that we can call helper methods from the controller class inside the templates.
    * They can even accept arguments!
* [JDBI](http://jdbi.org/)
  * JDBI is essentially an extension of JDBC that allows us to interface easily
    with SQL-based databases.
  * Our concept of a Model is based on its SQL Object API (see
    `src/main/java/me/nickbrown/sparkjavastarter/models/user/UserDao.java`)
* [Spark-PAC4J](https://github.com/pac4j/spark-pac4j)
  * Allows for flexible authentication using a variety of sources. For now, I'm
    using GitHub OAuth, bit you could use whatever you like.

## License

See the [LICENSE](LICENSE.md) file for license rights and limitations (MIT).
