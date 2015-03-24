package alg.root.align;

import org.biojava.nbio.alignment.template.SubstitutionMatrix;

import java.util.*;

public class RootSubstitutionMatrix implements SubstitutionMatrix<RootCompound> {

    private static final String comment = "#";

    private RootCompoundSet compoundSet;
    private String description, name;
    private short[][] matrix;
    private short max, min;
    private List<RootCompound> rows, cols;

    public RootSubstitutionMatrix(RootCompoundSet compoundSet, short match, short replace) {
        this.compoundSet = compoundSet;
        description = "Identity matrix. All replaces and all matches are treated equally.";
        name = "IDENTITY_" + match + "_" + replace;
        max = (match > replace) ? match : replace;
        min = (match < replace) ? match : replace;
        rows = cols = compoundSet.getAllCompounds();
        matrix = new short[rows.size()][cols.size()];
        for (int r = 0; r < rows.size(); r++) {
            for (int c = 0; c < cols.size(); c++) {
                try {
                    matrix[r][c] = (compoundSet.compoundsEquivalent(rows.get(r), cols.get(c))) ? match : replace;
                } catch (UnsupportedOperationException e) {
                    matrix[r][c] = (r == c) ? match : replace;
                }
            }
        }
    }

    @Override
    public RootCompoundSet getCompoundSet() {
        return compoundSet;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public short[][] getMatrix() {
        short[][] copy = new short[matrix.length][matrix[0].length];
        for (int i = 0; i < copy.length; i++) {
            copy[i] = Arrays.copyOf(matrix[i], matrix[i].length);
        }
        return copy;
    }

    @Override
    public String getMatrixAsString() {
        StringBuilder s = new StringBuilder();
        int lengthCompound = compoundSet.getMaxSingleCompoundStringLength(), lengthRest =
                Math.max(Math.max(Short.toString(min).length(), Short.toString(max).length()), lengthCompound) + 1;
        String padCompound = "%" + Integer.toString(lengthCompound) + "s",
                padRest = "%" + Integer.toString(lengthRest);
        for (int i = 0; i < lengthCompound; i++) {
            s.append(" ");
        }
        for (RootCompound col : cols) {
            s.append(String.format(padRest + "s", compoundSet.getStringForCompound(col)));
        }
        s.append(String.format("%n"));
        for (RootCompound row : rows) {
            s.append(String.format(padCompound, compoundSet.getStringForCompound(row)));
            for (RootCompound col : cols) {
                s.append(String.format(padRest + "d", getValue(row, col)));
            }
            s.append(String.format("%n"));
        }
        return s.toString();
    }

    @Override
    public short getMaxValue() {
        return max;
    }

    @Override
    public short getMinValue() {
        return min;
    }

    @Override
    public String getName() {
        return name;
    }
    /**
     * Returns the index of the first occurrence of the specified element in the list.
     * If the list does not contain the given compound, the index of the first occurrence
     * of the element according to case-insensitive equality.
     * If no such elements exist, -1 is returned.
     * @param list list of compounds to search
     * @param compound compound to search for
     * @return Returns the index of the first match to the specified element in this list, or -1 if there is no such index.
     */
    private static int getIndexOfCompound(List<RootCompound> list, RootCompound compound) {
        int index = list.indexOf(compound);
        if (index == -1) {
            for (int i = 0; i < list.size(); i++) {
                if (compound.equalsIgnoreCase(list.get(i))) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }
    @Override
    public short getValue(RootCompound from, RootCompound to) {
        int row = getIndexOfCompound(rows, from), col = getIndexOfCompound(cols, to);
        if (row == -1 || col == -1) {
            row = getIndexOfCompound(cols, from);
            col = getIndexOfCompound(rows, to);
            if (row == -1 || col == -1) {
                return min;
            }
        }
        return matrix[row][col];
    }

    @Override
    public SubstitutionMatrix<RootCompound> normalizeMatrix(short scale) {
        // TODO SubstitutionMatrix<C> normalizeMatrix(short)
        return null;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns in a format similar to the standard NCBI files.
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        StringTokenizer st = new StringTokenizer(description, "\n\r");
        while (st.hasMoreTokens()) {
            String line = st.nextToken();
            if (!line.startsWith(comment)) {
                s.append(comment);
            }
            s.append(String.format("%s%n", line));
        }
        s.append(getMatrixAsString());
        return s.toString();
    }

    @Override
    public Map<RootCompound, Short> getRow(RootCompound row) {
        int rowIndex = rows.indexOf(row);
        Map<RootCompound, Short> map = new HashMap<RootCompound, Short>();
        for (int colIndex = 0; colIndex < matrix[rowIndex].length; colIndex++) {
            map.put(cols.get(colIndex), matrix[rowIndex][colIndex]);
        }
        return map;
    }

    @Override
    public Map<RootCompound, Short> getColumn(RootCompound column) {
        int colIndex = cols.indexOf(column);
        Map<RootCompound, Short> map = new HashMap<RootCompound, Short>();
        for (int i = 0; i < matrix.length; i++) {
            map.put(rows.get(i), matrix[i][colIndex]);
        }
        return map;
    }

}
