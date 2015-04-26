package model.api.lemma;

import model.api.base.Textual;
import model.api.root.Root;
import model.api.token.Token;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.Set;

/**
 * Created by vahidoo on 3/29/15.
 */
public interface Lemma extends Textual {

    @JsonIgnore
    Set<Token> getTokens();

    boolean hasRoot();
    Root getRoot();

}
