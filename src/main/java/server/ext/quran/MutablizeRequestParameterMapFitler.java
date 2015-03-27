package server.ext.quran;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter to wrap request with a request allowing to add parameters to parameter map
 * WHEREVER POSSIBLE USE REQUEST ATTRIBUTES INSTEAD!
 */
public class MutablizeRequestParameterMapFitler extends OncePerRequestFilter {

    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!(request instanceof MutableRequestParameterWrapper)) {
            request = new MutableRequestParameterWrapper(request);
        }
        chain.doFilter(request, response);
    }
}
