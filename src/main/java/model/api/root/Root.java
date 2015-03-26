package model.api.root;


import model.api.base.Textual;
import model.api.token.Token;

import java.util.Set;

/**
 * Created by vahidoo on 3/11/15.
 */
public interface Root extends Textual {

    Set<Token> getTokens();


}
