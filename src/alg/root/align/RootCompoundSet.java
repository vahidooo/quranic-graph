package alg.root.align;

import model.api.root.Root;
import org.biojava.nbio.core.sequence.template.CompoundSet;
import org.biojava.nbio.core.sequence.template.Sequence;

import java.util.*;

public class RootCompoundSet implements CompoundSet<RootCompound> {

    private final Map<String, RootCompound> rootCache = new HashMap<String, RootCompound>();
    private final List<List<RootCompound>> compounds = new ArrayList<>();

    public RootCompoundSet(List<Root>... collections) {
        Set<Root> roots = new HashSet<>();
        for (Collection<Root> collection : collections) {
            roots.addAll(collection);
        }
        for (Root root : roots) {
            rootCache.put(root.getBuckwalter(), new RootCompound(this, root));
        }

        for (int i = 0; i < collections.length; i++) {
            List<Root> collection = collections[i];
            List<RootCompound> set = new ArrayList<>();
            compounds.add(set);
            for (int j = 0; j < collection.size(); j++) {
                Root root = collection.get(j);
                set.add(rootCache.get(root.getBuckwalter()));
            }
        }


    }

    @Override
    public String getStringForCompound(RootCompound compound) {
        return compound.toString();
    }

    @Override
    public RootCompound getCompoundForString(String string) {
        return this.rootCache.get(string);
    }

    @Override
    public int getMaxSingleCompoundStringLength() {
        return 4;
    }


    @Override
    public boolean isCompoundStringLengthEqual() {
        return false;
    }

    @Override
    public boolean compoundsEquivalent(RootCompound compoundOne, RootCompound compoundTwo) {
        Set<RootCompound> equivalents = getEquivalentCompounds(compoundOne);
        return (equivalents != null) && equivalents.contains(compoundTwo);
    }

    @Override
    public Set<RootCompound> getEquivalentCompounds(RootCompound compound) {
        HashSet<RootCompound> set = new HashSet<RootCompound>();
        set.add(rootCache.get(compound.getBase()));
        return set;

    }

    @Override
    public boolean hasCompound(RootCompound compound) {
        return rootCache.containsValue(compound);
    }

    @Override
    public boolean isValidSequence(Sequence<RootCompound> sequence) {
        for (RootCompound compound : sequence) {
            if (!hasCompound(compound)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<RootCompound> getAllCompounds() {
        return new ArrayList<RootCompound>(rootCache.values());
    }


    @Override
    public boolean isComplementable() {
        return false;
    }

    public List<RootCompound> getCompounds(int i) {
        return compounds.get(i);
    }
}
