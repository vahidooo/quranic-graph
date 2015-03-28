package filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

/**
 * An HttpRequestWrapper to allow adding parameters to request parameter map manually.
 * WHEREVER POSSIBLE USE REQUEST ATTRIBUTES INSTEAD!
 */
public class MutableRequestParameterWrapper extends HttpServletRequestWrapper {

    private Map<String, String[]> manualRequestParams = new Hashtable<String, String[]>();
    private List<String> updatedRequestParams = new ArrayList();

    public MutableRequestParameterWrapper(final HttpServletRequest decorated) {
        super(decorated);
    }

    /**
     * adds key-value pair to request parameters but WHEREVER POSSIBLE USE REQUEST ATTRIBUTES INSTEAD!
     * @param key
     * @param value
     */
    public void addRequestParameter(String key, String value) {
        manualRequestParams.put(key, new String[] {value});
    }

    /**
     * update key-value pair to request parameters but WHEREVER POSSIBLE USE REQUEST ATTRIBUTES INSTEAD!
     * @param key
     * @param value
     */
    public void updateRequestParameter(String key, String value) {
        manualRequestParams.put(key, new String[] { value });
        updatedRequestParams.add(key);
    }

    @Override
    public String[] getParameterValues(String name) {

        String[] valArr = null;
        if (updatedRequestParams.indexOf(name) == -1) {
            valArr = super.getParameterValues(name);
        }
        if (valArr == null) {
            valArr = new String[0];
        }

        String[] inManual = manualRequestParams.get(name);

        if (inManual != null) {
            String[] allValues = new String[ valArr.length + inManual.length ];
            for (int i = 0; i < valArr.length; i++) {
                allValues[i] = valArr[i];
            }
            for (int i = 0; i < inManual.length; i++) {
                allValues[i + valArr.length] = inManual[i];
            }
            return allValues;
        }
        else {
            return valArr.length > 0 ? valArr : null;
        }

//    	String[] valArr = updatedRequestParams.indexOf(name) == -1 ? super.getParameterValues(name) : null;
//    	List<String> values = valArr == null ? null : Arrays.asList(valArr);
//
//    	String[] inManual = manualRequestParams.get(name);
//    	if (inManual != null) {
//    		values = new ArrayList<String>(values);
//    		values.addAll(Arrays.asList(inManual));
//    	}
//
//    	return values == null ? null : values.toArray(new String[0]);
    }

    @Override
    public String getParameter(String name) {
        String[] inManual = manualRequestParams.get(name);
        if (inManual != null && inManual.length > 0)
            return inManual[0];

        return super.getParameter(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map getParameterMap() {
        Map<String, String[]> m = super.getParameterMap();

        if (manualRequestParams.size() > 0) {
            m = new Hashtable(m);
            m.putAll(manualRequestParams);
        }

        return m;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enumeration getParameterNames() {
        return new Enumeration() {
            private Enumeration superEnum = getSuperParameterNames();
            private Iterator manualIter = manualRequestParams.keySet().iterator();
            private boolean inManual = true;

            public boolean hasMoreElements() {
                return manualIter.hasNext() ? manualIter.hasNext() : superEnum.hasMoreElements();
            }

            public Object nextElement() {
                inManual = manualIter.hasNext();

                if (inManual) {
                    return manualIter.next();
                }
                else {
                    return superEnum.nextElement();
                }
            }

        };
    }

    @SuppressWarnings("unchecked")
    private Enumeration getSuperParameterNames() {
        return super.getParameterNames();
    }

}
