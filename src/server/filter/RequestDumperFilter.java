package server.filter;

/**
 * Created by vahidoo on 3/20/15.
 */

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Logger;

/**
 * Copied from RequestDumperFilter of apache tomcat
 */
public class RequestDumperFilter implements Filter {

    private enum Level {
        none,
        header,
        full,
        full_response
    }

    private Level level = Level.header;

    private static final ThreadLocal<Timestamp> timestamp =
            new ThreadLocal<Timestamp>() {
                @Override
                protected Timestamp initialValue() {
                    return new Timestamp();
                }
            };

    private static final Logger logger = Logger.getLogger(RequestDumperFilter.class.getName());

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {


        logger.info( "FFFFFFFFFFFFFFFIIIIIIIIIIIIIIIIIIIIIIIIILLLLLLLLLLLLLLLLLLLLLLLTTTTTTTTTERRRRRRRRRRRRR");

        HttpServletRequest hRequest = (HttpServletRequest) request;
        HttpServletResponse hResponse = (HttpServletResponse) response;

        if (level.compareTo(Level.header) >= 0) {

            synchronized (this) {
                // Log pre-service information
                doLog("START TIME        ", getTimestamp());

                doLog("        requestURI", hRequest.getRequestURI());
                doLog("          authType", hRequest.getAuthType());
                doLog(" characterEncoding", request.getCharacterEncoding());
                doLog("     contentLength", Integer.valueOf(request.getContentLength()).toString());
                doLog("       contentType", request.getContentType());
                doLog("       contextPath", hRequest.getContextPath());

                Cookie cookies[] = hRequest.getCookies();
                if (cookies != null) {
                    for (Cookie cooky : cookies) {
                        doLog("            cookie", cooky.getName() + "=" + cooky.getValue());
                    }
                }
                Enumeration<String> hnames = hRequest.getHeaderNames();
                while (hnames.hasMoreElements()) {
                    String hname = hnames.nextElement();
                    Enumeration<String> hvalues = hRequest.getHeaders(hname);
                    while (hvalues.hasMoreElements()) {
                        String hvalue = hvalues.nextElement();
                        doLog("            header", hname + "=" + hvalue);
                    }
                }

                doLog("            locale", request.getLocale().toString());
                doLog("            method", hRequest.getMethod());

                Enumeration<String> pnames = request.getParameterNames();
                while (pnames.hasMoreElements()) {
                    String pname = pnames.nextElement();
                    String pvalues[] = request.getParameterValues(pname);
                    StringBuilder result = new StringBuilder(pname);
                    result.append('=');
                    for (int i = 0; i < pvalues.length; i++) {
                        if (i > 0)
                            result.append(", ");
                        result.append(pvalues[i]);
                    }
                    doLog("         parameter", result.toString());
                }

                doLog("          pathInfo", hRequest.getPathInfo());
                doLog("          protocol", request.getProtocol());
                doLog("       queryString", hRequest.getQueryString());
                doLog("        remoteAddr", request.getRemoteAddr());
                doLog("        remoteHost", request.getRemoteHost());
                doLog("        remoteUser", hRequest.getRemoteUser());
                doLog("requestedSessionId", hRequest.getRequestedSessionId());
                doLog("            scheme", request.getScheme());
                doLog("        serverName", request.getServerName());
                doLog("        serverPort", Integer.valueOf(request.getServerPort()).toString());
                doLog("       servletPath", hRequest.getServletPath());
                doLog("          isSecure", Boolean.valueOf(request.isSecure()).toString());

                doLog("------------------", "--------------------------------------------");
            }
        }

        HttpServletResponse responseToUse = hResponse;

        // Perform the request
        chain.doFilter(hRequest, responseToUse);

        if (level.compareTo(Level.header) >= 0) {
            synchronized (this) {
                // Log post-service information
                doLog("------------------", "--------------------------------------------");

                doLog("          authType", hRequest.getAuthType());
                doLog("       contentType", response.getContentType());

                Iterable<String> rhnames = hResponse.getHeaderNames();
                for (String rhname : rhnames) {
                    Iterable<String> rhvalues = hResponse.getHeaders(rhname);
                    for (String rhvalue : rhvalues)
                        doLog("            header", rhname + "=" + rhvalue);
                }

                doLog("        remoteUser", hRequest.getRemoteUser());
                doLog("            status", Integer.valueOf(hResponse.getStatus()).toString());



                doLog("END TIME          ", getTimestamp());
                doLog("==================", "============================================");
            }
        }
    }

    private void doLog(String attribute, String value) {
        StringBuilder sb = new StringBuilder(80);
        sb.append(Thread.currentThread().getName());
        sb.append(' ');
        sb.append(attribute);
        sb.append(" = ");
        sb.append(value);
        logger.info(sb.toString());
    }

    private String getTimestamp() {
        Timestamp ts = timestamp.get();
        long currentTime = System.currentTimeMillis();

        if ((ts.date.getTime() + 999) < currentTime) {
            ts.date.setTime(currentTime - (currentTime % 1000));
            ts.update();
        }
        return ts.dateString;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String levelStr = filterConfig.getInitParameter("level");
        if (levelStr != null) {
            level = Level.valueOf(levelStr);
        }
    }

    @Override
    public void destroy() {
    }

    private static final class Timestamp {
        private final Date date = new Date(0);
        private final SimpleDateFormat format =
                new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        private String dateString = format.format(date);

        private void update() {
            dateString = format.format(date);
        }
    }

//    private static class ByteArrayServletStream extends ServletOutputStream {
//
//        private ByteArrayOutputStream baos;
//        private WriteListener writerListener;
//
//        ByteArrayServletStream(ByteArrayOutputStream baos) {
//            this.baos = baos;
//        }
//
//        public void write(int param) throws IOException {
//            baos.write(param);
//        }
//
//        @Override
//        public boolean isReady() {
//            return true;
//        }
//
//        @Override
//        public void setWriteListener(WriteListener writeListener) {
//            throw new RuntimeException("WriteListener for RequestDumperFilter is not supported!");
////            this.writerListener = writeListener;
//        }
//    }

//    private static class ByteArrayPrintWriter {
//
//        private ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//        private PrintWriter pw = new PrintWriter(baos);
//
//        private ServletOutputStream sos = new ByteArrayServletStream(baos);
//
//        public PrintWriter getWriter() {
//            return pw;
//        }
//
//        public ServletOutputStream getStream() {
//            return sos;
//        }
//
//        byte[] toByteArray() {
//            return baos.toByteArray();
//        }
//    }


}
