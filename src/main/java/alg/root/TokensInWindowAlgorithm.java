package alg.root;

import alg.base.ResultItem;
import alg.base.ResultList;
import model.api.block.VerseBlock;
import model.api.root.Root;
import model.api.token.Token;
import model.impl.block.VerseBlockImpl;

import java.util.*;

/**
 * Created by vahidoo on 3/17/15.
 */
public class TokensInWindowAlgorithm {

    private static final int IN_WINDOW = 100;
    private static final int INFINITY = -1;

    private static final int MAX_SCORE = 100;
    private static final int MIN_SCORE = 0;

    private static final int THRESHOLD = 50;


    public ResultList<VerseBlock, Integer> solve(List<Root> roots, int window) {

        Map<VerseBlock, Integer> result = new HashMap<>(roots.size() * roots.size());
        Set<Root> set = new HashSet<>(roots);

        List<Token> tokens = findTokenAndSort(roots);

        for (int i = 0; i < tokens.size(); i++) {
            for (int j = i; j < tokens.size(); j++) {

                int score;

                Token first = tokens.get(i);
                Token second = tokens.get(j);
                int dis = distance(first, second, window);

                score = computeScore(set, tokens, i, j, dis);
                VerseBlock vb = new VerseBlockImpl(first.getVerse(), second.getVerse());
                if (score >= THRESHOLD && (!result.containsKey(vb) || result.get(vb) < score)) {
                    result.put(vb, score);
                }
            }
        }


        ResultList<VerseBlock, Integer> scores = new ResultList<>();
        for (Map.Entry<VerseBlock, Integer> entry : result.entrySet()) {
            VerseBlock block = entry.getKey();
            Integer score = entry.getValue();
            scores.add(new ResultItem<VerseBlock, Integer>(block, score));
        }
        scores.descendSort();

        return scores;
    }

    private int computeScore(Collection<Root> roots, List<Token> tokens, int start, int end, int dis) {

        if (dis == INFINITY) {
            return MIN_SCORE;
        }

        Set<Root> temp = new HashSet<>();
        for (int i = start; i <= end; i++) {
            Token token = tokens.get(i);
            Root root = token.getRoot();
            temp.add(root);
        }


        if (temp.size() == roots.size() && dis == IN_WINDOW) {
            return MAX_SCORE;
        }
        Double f = Double.valueOf((double) temp.size() / (double) roots.size());
        f = Math.pow(f, 2);
//        f = f * MAX_SCORE;
//        int weight =  / roots.size();
//        return (temp.size() * weight) * dis;
        return (int) (f * dis);


    }

    private int distance(Token first, Token second, int window) {

        if (first.getChapterIndex() != second.getChapterIndex()) {
            return INFINITY;
        } else {
            int dis = second.getWord().getIndexInQuran() - first.getWord().getIndexInQuran();
            if (dis <= window) {
                return IN_WINDOW;
            }
            if (dis > (window * 1.25)) {
                return INFINITY;
            }

            return (int) (IN_WINDOW - Math.pow(dis - window, 2) * 16);

        }


    }

    private List<Token> findTokenAndSort(List<Root> roots) {

        List<Token> tokens = new ArrayList<>();
        for (Root root : roots) {
            tokens.addAll(root.getTokens());
        }

        Collections.sort(tokens, new Comparator<Token>() {
            @Override
            public int compare(Token first, Token second) {

                String[] f = first.getAddress().split(":");
                String[] s = second.getAddress().split(":");

                for (int i = 0; i < 3; i++) {
                    Integer a = Integer.parseInt(f[i]);
                    Integer b = Integer.parseInt(s[i]);
                    if (!a.equals(b))
                        return a.compareTo(b);
                }

                return 0;
            }

        });


        return tokens;
    }

}

