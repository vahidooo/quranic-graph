package alg.base;

import java.util.List;

/**
 * Created by vahidoo on 3/21/15.
 */
public class IntegerLCS {

    private List<Integer> first;
    private List<Integer> second;
    private int[][] score;


    public IntegerLCS(List<Integer> first, List<Integer> second) {
        this.first = first;
        this.second = second;
        score = new int[first.size() + 1][second.size() + 1];
    }

    public int run() {
        int result = 0;

        initTable();
        fillTable();

        return score[first.size()][second.size()];
    }

    private void fillTable() {

        for (int i = 0; i < first.size(); i++) {
            for (int j = 0; j < second.size(); j++) {
                int a = first.get(i);
                int b = second.get(j);

                int sc;
                if (a == b) {
                    sc = score[i][j] + 1;
                } else {
                    sc = Math.max(score[i + 1][j], score[i][j + 1]);
                }

                score[i + 1][j + 1] = sc;
            }
        }

    }

    private void initTable() {

        for (int i = 0; i <= first.size(); i++) {
            score[i][0] = 0;
        }

        for (int j = 0; j <= second.size(); j++) {
            score[0][j] = 0;
        }
    }

}
