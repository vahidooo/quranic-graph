package alg.base;

import java.util.*;

/**
 * Created by vahidoo on 3/21/15.
 */
public class MappedLCS<T> {

    private List<T> first;
    private List<T> second;

    private Map<T, Integer> alphabet = new HashMap<>();

    public MappedLCS(List<T> first, List<T> second) {
        this.first = first;
        this.second = second;
    }

    public int run() {

        List<Integer> f = doMap(first);
        List<Integer> s = doMap(second);

        return new IntegerLCS(f, s).run();
    }

    private List<Integer> doMap(List<T> list) {

        List<Integer> result = new ArrayList<>(list.size());
        for (T t : list) {
            if (!alphabet.containsKey(t)) {
                int code = alphabet.size() + 1;
                alphabet.put(t, code);
            }
            int code = alphabet.get(t);
            result.add(code);
        }
        return result;
    }


    public static void main(String[] args) {

        String str1="XMJYAUZ" , str2 ="MZJAWXU";
        List<Character> a = new ArrayList<Character>();
        for(char c : str1.toCharArray()) {
            a.add(c);
        }


        List<Character> b = new ArrayList<Character>();
        for(char c : str2.toCharArray()) {
            b.add(c);
        }


        MappedLCS<Character> alg = new MappedLCS<Character>(a, b);
        System.out.println(alg.run());

    }

    public Map<T,Integer> getAlphabet() {
        return alphabet;
    }
}
