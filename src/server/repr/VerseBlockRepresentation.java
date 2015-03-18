package server.repr;


import model.api.block.VerseBlock;

/**
 * Created by vahidoo on 3/9/15.
 */
public class VerseBlockRepresentation extends Representation<VerseBlock> {


    @Override
    public String represent(VerseBlock vb) {
        StringBuilder sb = new StringBuilder();
        sb.append( "( " );
        sb.append( vb.getStartVerse().getAddress() );
        sb.append( " , " );
        sb.append( vb.getEndVerse().getAddress() );
        sb.append( " )" );
        return sb.toString();

    }
}
