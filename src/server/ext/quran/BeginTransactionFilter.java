package server.ext.quran;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by vahidoo on 3/26/15.
 */

public class BeginTransactionFilter implements Filter {
    private static Log log = LogFactory.getLog(BeginTransactionFilter.class);

    private GraphDatabaseService database;

    public BeginTransactionFilter(GraphDatabaseService database) {
        this.database = database;
    }

    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        try (Transaction tx = database.beginTx()) {
            log.debug("Starting a database transaction");

            // Call the next filter (continue request processing)
            chain.doFilter(request, response);

            // Commit and cleanup
            log.debug("Committing the database transaction");
            tx.success();

        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {
    }

}
