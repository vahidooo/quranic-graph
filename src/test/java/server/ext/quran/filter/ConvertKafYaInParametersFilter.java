package server.ext.quran.filter;

import org.springframework.web.filter.OncePerRequestFilter;
import server.util.LocaleUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * The filter has a defaultEnable init parameter. Conversion can be enabled/disabled for each request by setting
 * the request parameters ckye/ckyd (Convert Kaf Ya Enable/Disable)
 */
public class ConvertKafYaInParametersFilter extends OncePerRequestFilter {

    private Boolean defaultEnable = null;

    public boolean isDefaultEnable() {
        if (defaultEnable == null) {
            String de = getFilterConfig().getInitParameter("defaultEnable");
            defaultEnable = de != null ? Boolean.valueOf(de): false;
        }
        return defaultEnable;
    }

    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        boolean enable = isDefaultEnable();
        if (request.getParameter("ckye") != null) {
            enable = true;
        }
        else if (request.getParameter("ckyd") != null) {
            enable = false;
        }

    	if (enable && request instanceof MutableRequestParameterWrapper) {
    		MutableRequestParameterWrapper mr = (MutableRequestParameterWrapper)request;
    		Enumeration names = mr.getParameterNames();
    		while (names.hasMoreElements()) {
    			String n = (String) names.nextElement();
    			String v = mr.getParameter(n);
    			String res = convertIfNeeded(v);
    			if (res != null) {
    				mr.updateRequestParameter(n, res);
    			}
    		}
    	}

//        RequestUtils.setSession(request.getSession());  //TODO:ABOLFAZL:IMPORTANT session should be set somewhere more meaningful

        chain.doFilter(request, response);
    }

	private String convertIfNeeded(String s) {
        String cs = LocaleUtils.correctKF(s);
		return cs != s ? cs : null;
	}

}
