package server.ext.quran;

import model.api.verse.Verse;
import model.impl.base.ManagersSet;
import org.neo4j.graphdb.GraphDatabaseService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/verse")
public class VerseWS extends BaseWS {

    public VerseWS(@Context GraphDatabaseService database, @Context ManagersSet managersSet) {
        super(database, managersSet);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/go/{chapterNo}/{verseNo}")
    public Response go(@PathParam("chapterNo") int chapterNo, @PathParam("verseNo") int verseNo) {
        Verse verse = managersSet.getVerseManager().get(chapterNo, verseNo);
        String json = getJson("verse", verse);
        return getOkResponse(json);
    }


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/go/{addresses}")
    public Response go(@PathParam("addresses") String addresses) {

        List<Verse> verses = new ArrayList<>();
        String[] parts = addresses.split(",");
        for (String address : parts) {
            Verse verse = managersSet.getVerseManager().get(address);
            verses.add(verse);
        }

        String json = getJson("verses", verses);
        return getOkResponse(json);
    }
}