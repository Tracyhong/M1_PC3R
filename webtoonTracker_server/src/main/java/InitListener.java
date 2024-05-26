import org.pc3r.webtoontracker_server.persistence.Database;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;

@WebListener
public class InitListener implements ServletContextListener, HttpSessionListener, HttpSessionAttributeListener {

    public InitListener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        /* This method is called when the servlet context is initialized(when the Web application is deployed). */

        //DB Connection
        try {
            // Initialize the database connection
            Database.getInstance();
            System.out.println("Database connection initialized successfully.");
        } catch (Exception e) {
            System.err.println("Error initializing database connection: " + e.getMessage());
            // Consider logging the exception using a logging framework like Log4j
            throw new RuntimeException("Database initialization error", e);
        }
//        try {
//            System.out.println("InitListener.contextInitialized");
//            Class.forName("org.pc3r.webtoontracker_server.persistence.Database");
//            System.out.println("Database class loaded");
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        Database db = Database.getInstance();
//        ....
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        /* This method is called when the servlet Context is undeployed or Application Server shuts down. */
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        /* Session is created. */
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        /* Session is destroyed. */
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is added to a session. */
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is removed from a session. */
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is replaced in a session. */
    }
}
 
