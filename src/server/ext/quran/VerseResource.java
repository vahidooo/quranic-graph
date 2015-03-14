package server.ext.quran;

import base.GraphIndices;
import base.NodeProperties;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import server.repr.VerseRepresentation;
import util.NodeUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Path("/verse")
public class VerseResource {
    private final GraphDatabaseService database;

    public VerseResource(@Context GraphDatabaseService database) {
        this.database = database;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/go/{chapterNo}/{verseNo }")
    public Response go(@PathParam("chapterNo") int chapterNo, @PathParam("verseNo") int verseNo) {

        Response response;
        try (Transaction tx = database.beginTx()) {

            String address = NodeUtils.getNodeAddress(chapterNo, verseNo);
            Node node = database.index().forNodes(GraphIndices.VerseIndex).get(NodeProperties.General.address, address).getSingle();


            try {
                response = Response.status(Response.Status.OK).entity(
                        (new VerseRepresentation().represent(node)).getBytes(Charset.forName("UTF-8"))).build();
            } catch (Throwable th) {
                response = Response.status(Response.Status.OK).entity(
                        (th.getMessage()).getBytes(Charset.forName("UTF-8"))).build();
            }
            tx.success();
        }

        return response;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/go/{addresses}")
    public Response go(@PathParam("addresses") String addresses) {

        Response response;
        try (Transaction tx = database.beginTx()) {

            List<Node> verses = new ArrayList<>();
            String[] parts = addresses.split(",");
            for (String part : parts) {
                Node node = database.index().forNodes(GraphIndices.VerseIndex).get(NodeProperties.General.address, part).getSingle();
                if ( node!=null)
                    verses.add(node);
            }

            try {
                response = Response.status(Response.Status.OK).entity(
                        (new VerseRepresentation().represent(verses)).getBytes(Charset.forName("UTF-8"))).build();
            } catch (Throwable th) {
                response = Response.status(Response.Status.OK).entity(
                        (th.getMessage()).getBytes(Charset.forName("UTF-8"))).build();
            }
            tx.success();
        }

        return response;
    }

}