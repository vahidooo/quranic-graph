package server.ext.quran;

import alg.root.TokensInWindowAlgorithm;
import base.GraphIndices;
import base.NodeLabels;
import base.NodeProperties;
import base.RelationshipTypes;
import model.api.block.VerseBlock;
import model.api.root.Root;
import model.api.root.RootManager;
import model.api.token.Token;
import model.api.word.Word;
import model.impl.base.ManagerFactory;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.TraversalDescription;
import server.repr.HasArabicPropertyRepresentation;
import server.repr.MapRepresentation;
import server.repr.TokenRepresentation;
import server.repr.VerseRepresentation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.charset.Charset;
import java.util.*;
import java.util.logging.Logger;

@Path("/root")
public class RootResource {

    private static final Logger logger = Logger.getLogger(RootResource.class.getName());
    private final GraphDatabaseService database;
    private RootManager rootManager;

    public RootResource(@Context GraphDatabaseService database) {
        this.database = database;
        this.rootManager = ManagerFactory.getFor(database).getRootIndex();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/tokens/{root}")
    public Response tokens(@PathParam("root") final String root) {

        logger.info(" /tokens/ (" + root + ")");

        Response response;
        try {
            try (Transaction tx = database.beginTx()) {
                List<Node> tokens = new ArrayList<>();


                Node rootNode = database.index().forNodes(GraphIndices.RootIndex).get(NodeProperties.GeneralText.arabic, root).getSingle();
                Iterable<Relationship> it = rootNode.getRelationships(Direction.INCOMING, RelationshipTypes.HAS_ROOT);
                for (Relationship r : it) {
                    Node token = r.getStartNode();
                    tokens.add(token);
                }


                response = Response.status(Response.Status.OK).entity(
                        (new TokenRepresentation().represent(tokens)).getBytes(Charset.forName("UTF-8"))).build();

                tx.success();
            }
        } catch (Throwable th) {
            response = Response.status(Response.Status.OK).entity(
                    (th.getMessage()).getBytes(Charset.forName("UTF-8"))).build();
        }

        return response;
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/lemmas/{root}")
    public Response lemmas(@PathParam("root") final String root) {

        logger.info(" /lemmas/ (" + root + ")");

        Response response;
        try {
            try (Transaction tx = database.beginTx()) {

                Node rootNode = database.index().forNodes(GraphIndices.RootIndex).get(NodeProperties.GeneralText.arabic, root).getSingle();

                TraversalDescription td = database.traversalDescription()
                        .depthFirst()
                        .relationships(RelationshipTypes.HAS_ROOT)
                        .relationships(RelationshipTypes.HAS_LEMMA)
                        .evaluator(new Evaluator() {
                                       @Override
                                       public Evaluation evaluate(org.neo4j.graphdb.Path path) {

                                           Node node = path.endNode();

                                           if (path.length() > 2) {
                                               return Evaluation.EXCLUDE_AND_PRUNE;
                                           }
                                           if (node.hasLabel(NodeLabels.LEMMA)) {
                                               return Evaluation.INCLUDE_AND_PRUNE;
                                           }
                                           return Evaluation.EXCLUDE_AND_CONTINUE;
                                       }
                                   }

                        );
                ResourceIterable<Node> lemmas = td.traverse(rootNode).nodes();


                response = Response.status(Response.Status.OK)
                        .entity((new HasArabicPropertyRepresentation().represent(lemmas)).
                                getBytes(Charset.forName("UTF-8"))).build();

                tx.success();
            }
        } catch (Throwable th) {
            response = Response.status(Response.Status.OK).entity(
                    (th.getMessage()).getBytes(Charset.forName("UTF-8"))).build();
        }

        return response;
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/verses/{roots}")
    public Response verses(@PathParam("roots") final String roots) {
        String[] parts = roots.split("-");
        return verses(roots, parts.length, parts.length);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/verses/{roots}/{min}")
    public Response verses(@PathParam("roots") final String roots, @PathParam("min") final int min) {
        String[] parts = roots.split("-");
        return verses(roots, min, parts.length);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/verses/{roots}/{min}/{max}")
    public Response verses(@PathParam("roots") final String roots, @PathParam("min") final int min, @PathParam("max") final int max) {

        logger.info(" /verses/ (" + roots + ")");

        Response response;
        try {
            try (Transaction tx = database.beginTx()) {

                List<Node> rootNodes = new ArrayList<>();
                for (String root : roots.split("-")) {
                    Node rootNode = database.index().forNodes(GraphIndices.RootIndex).get(NodeProperties.GeneralText.arabic, root).getSingle();
                    rootNodes.add(rootNode);
                }

                TraversalDescription td = database.traversalDescription()
                        .depthFirst()
                        .relationships(RelationshipTypes.HAS_ROOT, Direction.INCOMING)
                        .relationships(RelationshipTypes.CONTAINS_TOKEN, Direction.INCOMING)
                        .relationships(RelationshipTypes.CONTAINS_WORD, Direction.INCOMING)
                        .evaluator(new Evaluator() {
                                       @Override
                                       public Evaluation evaluate(org.neo4j.graphdb.Path path) {
                                           Node node = path.endNode();

                                           if (node.hasLabel(NodeLabels.VERSE)) {
                                               return Evaluation.INCLUDE_AND_PRUNE;
                                           }
                                           return Evaluation.EXCLUDE_AND_CONTINUE;
                                       }
                                   }
                        );


                Map<Node, Set<Node>> map = new HashMap<>();

                for (Node rootNode : rootNodes) {
                    ResourceIterable<Node> it = td.traverse(rootNode).nodes();
                    Set<Node> set = new HashSet<>();
                    for (Node node : it) {
                        set.add(node);
                    }
                    map.put(rootNode, set);
                }


                Map<Node, Integer> scores = new HashMap<>();
                for (Map.Entry<Node, Set<Node>> entry : map.entrySet()) {
                    Node rootNode = entry.getKey();
                    Set<Node> set = entry.getValue();

                    for (Node node : set) {
                        if (!scores.containsKey(node)) {
                            scores.put(node, 0);
                        }

                        Integer score = scores.get(node);
                        scores.put(node, score + 1);
                    }

                }


                List<Node> verses = new ArrayList<>();
                for (Map.Entry<Node, Integer> entry : scores.entrySet()) {
                    Node verse = entry.getKey();
                    Integer score = entry.getValue();

                    if (score <= max && score >= min) {
                        verses.add(verse);
                    }

                }

                response = Response.status(Response.Status.OK)
                        .entity((new VerseRepresentation().represent(verses)).
                                getBytes(Charset.forName("UTF-8"))).build();

                tx.success();
            }
        } catch (Throwable th) {
            response = Response.status(Response.Status.OK).entity(
                    (th.getMessage()).getBytes(Charset.forName("UTF-8"))).build();
        }

        return response;
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/verses/seq/{roots}")
    public Response sequence(@PathParam("roots") final String roots) {

        List<Node> verses = new ArrayList<>();
        Response response;
        try {
            try (Transaction tx = database.beginTx()) {

                String[] parts = roots.split("-");
                Root first = rootManager.getRootByArabic(parts[0]);

                for (Token token : first.getTokens()) {

                    boolean flag = true;
                    Token currentToken = token;

                    for (int i = 0; i < parts.length; i++) {
                        if (!flag)
                            break;

                        Root expectedRoot = rootManager.getRootByArabic(parts[i]);

                        if ((!currentToken.hasRoot()) || (!currentToken.getRoot().equals(expectedRoot))) {
                            flag = false;
                        }

                        while (currentToken.getWord().getSuccessor() != null && !currentToken.getWord().getSuccessor().getStem().hasRoot()) {
                            currentToken = currentToken.getWord().getSuccessor().getStem();
                        }

                        Word successorWord = currentToken.getWord().getSuccessor();
                        if (successorWord == null) {
                            if ((i != parts.length - 1)) {
                                flag = false;
                            }
                        } else {
                            currentToken = successorWord.getStem();
                        }


                    }

                    if (flag) {
                        verses.add(token.getVerse().getNode());
                    }


                }

                response = Response.status(Response.Status.OK)
                        .entity((new VerseRepresentation().represent(verses)).
                                getBytes(Charset.forName("UTF-8"))).build();

                tx.success();
            }
        } catch (Throwable th) {
            th.printStackTrace();
            response = Response.status(Response.Status.OK).entity(
                    (th.getMessage()).getBytes(Charset.forName("UTF-8"))).build();
        }

        return response;

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/dis/{mode}/{window}/{roots}")
    public Response distance(@PathParam("mode") final String mode , @PathParam("window") final int window ,@PathParam("roots") String roots) {

        List<Node> verses = new ArrayList<>();
        Response response;
        try {
            try (Transaction tx = database.beginTx()) {

                String[] parts = roots.split("-");

                List<Root> rootList = new ArrayList<>();
                for (String part : parts) {
                    Root root = rootManager.getRootByArabic(part);
                    rootList.add(root);
                }

                TokensInWindowAlgorithm alg = new TokensInWindowAlgorithm();
                Map<VerseBlock,Integer> res=  alg.solve(rootList,window);

                response = Response.status(Response.Status.OK)
                        .entity((new MapRepresentation().represent(res)).
                                getBytes(Charset.forName("UTF-8"))).build();

                tx.success();
            }
        } catch (Throwable th) {
            th.printStackTrace();
            response = Response.status(Response.Status.OK).entity(
                    (th.getMessage()).getBytes(Charset.forName("UTF-8"))).build();
        }

        return response;

    }

}