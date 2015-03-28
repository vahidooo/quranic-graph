package model.api.root;


import model.api.base.Textual;
import model.api.token.Token;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.Set;

/**
 * Created by vahidoo on 3/11/15.
 */
public interface Root extends Textual {

    @JsonIgnore
    Set<Token> getTokens();


}
