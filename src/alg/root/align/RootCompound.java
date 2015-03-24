package alg.root.align;

import model.api.root.Root;
import org.biojava.nbio.core.sequence.template.AbstractCompound;
import org.biojava.nbio.core.sequence.template.Compound;
import org.biojava.nbio.core.sequence.template.CompoundSet;

/**
 * Used to describe an Amino Acid.
 *
 * @author Richard Holland
 * @author Scooter Willis
 * @author Andy Yates
 */
public class RootCompound extends AbstractCompound {

    private final RootCompoundSet compoundSet;
    private final Root root;

    public RootCompound(RootCompoundSet compoundSet, Root root) {
        super(root.getBuckwalter());
        this.root = root;
        this.compoundSet = compoundSet;
    }

    // TODO need to allow for modified name; that's not equality though is it?
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof RootCompound)) {
            return false;
        }
        RootCompound them = (RootCompound) obj;
        if (toString().equals(them.toString())) {
            return true;
        }
        return getBase().equals(them.getBase());

    }

    @Override
    public int hashCode() {
        return root.hashCode();
    }

    @Override
    public boolean equalsIgnoreCase(Compound compound) {
        if (compound == null) {
            return false;
        }
        if (!(compound instanceof RootCompound)) {
            return false;
        }
        RootCompound them = (RootCompound) compound;
        if (toString().equalsIgnoreCase(them.toString())) {
            return true;
        }
        return getLongName().equalsIgnoreCase(them.getLongName());
    }

    public CompoundSet<RootCompound> getCompoundSet() {
        return compoundSet;
    }

    @Override
    public String toString() {
        return root.toString();
    }
}
