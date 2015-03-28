package server.ext.quran.ws;

import alg.base.MappedLCS;
import alg.base.ResultItem;
import alg.base.ResultList;
import alg.root.TokensInWindowAlgorithm;
import alg.root.align.RootCompoundSet;
import alg.root.align.RootSubstitutionMatrix;
import data.schema.GraphIndices;
import data.schema.NodeLabels;
import data.schema.NodeProperties;
import data.schema.RelationshipTypes;
import model.api.block.VerseBlock;
import model.api.root.Root;
import model.api.token.Token;
import model.api.verse.Verse;
import model.api.word.Word;
import model.impl.base.ManagersSet;
import org.biojava.nbio.alignment.SimpleGapPenalty;
import org.biojava.nbio.alignment.SmithWaterman;
import org.biojava.nbio.alignment.template.GapPenalty;
import org.biojava.nbio.core.sequence.storage.ArrayListSequenceReader;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.TraversalDescription;
import server.repr.HasArabicPropertyRepresentation;
import util.VerseUtils;

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
public class RootWS extends BaseWS {

    private static final Logger logger = Logger.getLogger(RootWS.class.getName());

    public RootWS(@Context GraphDatabaseService database, @Context ManagersSet managerFactory) {
        super(database, managerFactory);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/tokens/{root}")
    public Response tokens(@PathParam("root") final String root) {

        List<Node> tokens = new ArrayList<>();

        Node rootNode = database.index().forNodes(GraphIndices.RootIndex).get(NodeProperties.GeneralText.arabic, root).getSingle();
        Iterable<Relationship> it = rootNode.getRelationships(Direction.INCOMING, RelationshipTypes.HAS_ROOT);
        for (Relationship r : it) {
            Node token = r.getStartNode();
            tokens.add(token);
        }


        Iterable<Token> t = managersSet.getSession().get(Token.class, tokens);
        String json = getJson("tokens", t);
        return getOkResponse(json);
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/lemmas/{root}")
    public Response lemmas(@PathParam("root") final String root) {
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


        //TODO : use new framework
        return Response.status(Response.Status.OK)
                .entity((new HasArabicPropertyRepresentation().represent(lemmas)).
                        getBytes(Charset.forName("UTF-8"))).build();
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

        Iterable<Verse> v = managersSet.getSession().get(Verse.class, verses);
        String json = getJson("verses", v);
        return getOkResponse(json);

    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/verses/seq/{roots}")
    public Response sequence(@PathParam("roots") final String roots) {

        List<Verse> verses = new ArrayList<>();

        String[] parts = roots.split("-");
        Root first = managersSet.getRootManager().getRootByArabic(parts[0]);

        for (Token token : first.getTokens()) {

            boolean flag = true;
            Token currentToken = token;

            for (int i = 0; i < parts.length; i++) {
                if (!flag)
                    break;

                Root expectedRoot = managersSet.getRootManager().getRootByArabic(parts[i]);

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
                verses.add(token.getVerse());
            }


        }

        String json = getJson("verses", verses);
        return getOkResponse(json);

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/dis/{mode}/{window}/{roots}")
    public Response distance(@PathParam("mode") final String mode, @PathParam("window") final int window, @PathParam("roots") String roots) {

        String[] parts = roots.split("-");

        List<Root> rootList = new ArrayList<>();
        for (String part : parts) {
            Root root = managersSet.getRootManager().getRootByArabic(part);
            rootList.add(root);
        }

        TokensInWindowAlgorithm alg = new TokensInWindowAlgorithm();
        ResultList<VerseBlock, Integer> blocks = alg.solve(rootList, window);

        String json = getJson("blocks", blocks);
        return getOkResponse(json);

    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/verse/lcs/{chapterNo}/{verseNo}/{threshold}")
    public Response lcs(@PathParam("chapterNo") int chapterNo, @PathParam("verseNo") int verseNo, @PathParam("threshold") int threshold) {

        ResultList<Verse, Integer> result = new ResultList<>();

        Verse verse = managersSet.getVerseManager().get(chapterNo, verseNo);

        List<Root> mainRootList = VerseUtils.getRootList(verse);
        Verse nextVerse = verse.getSuccessor();

        while (nextVerse != null) {
            List<Root> secondRootList = VerseUtils.getRootList(nextVerse);

            MappedLCS<Root> lcs = new MappedLCS<Root>(mainRootList, secondRootList);

            int score = lcs.run();
            if (score >= threshold) {
                result.add(new ResultItem<Verse, Integer>(nextVerse, score));
            }

            nextVerse = nextVerse.getNextInQuran();
        }


        String json = getJson("verses", result);
        return getOkResponse(json);

    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/verse/algin/sw/{chapterNo}/{verseNo}/{threshold}")
    public Response smithWaterman(@PathParam("chapterNo") int chapterNo, @PathParam("verseNo") int verseNo, @PathParam("threshold") int threshold) {


        int MATCH_SCORE = 3;
        ResultList<Verse, Double> scores = new ResultList<>();

        Verse verse = managersSet.getVerseManager().get(chapterNo, verseNo);

        List<Root> mainRootList = VerseUtils.getRootList(verse);
        Verse nextVerse = verse.getSuccessor();

        while (nextVerse != null) {
            List<Root> secondRootList = VerseUtils.getRootList(nextVerse);

            RootCompoundSet cs = new RootCompoundSet(mainRootList, secondRootList);

            SmithWaterman sw = new SmithWaterman();
            sw.setQuery(new ArrayListSequenceReader(cs.getCompounds(0), cs));
            sw.setTarget(new ArrayListSequenceReader(cs.getCompounds(1), cs));

            GapPenalty penalty = new SimpleGapPenalty(2, 1);
            sw.setGapPenalty(penalty);

            RootSubstitutionMatrix matrix = new RootSubstitutionMatrix(cs, (short) MATCH_SCORE, (short) -4);
            sw.setSubstitutionMatrix(matrix);

            Double score = sw.getScore();
            if (score >= (MATCH_SCORE * threshold)) {
                scores.add(new ResultItem<Verse, Double>(nextVerse, score));
//                System.out.println(nextVerse.getAddress() + "***" + score);
            }

            nextVerse = nextVerse.getNextInQuran();
        }

        scores.descendSort();
        String json = getJson("verses", scores);
        return getOkResponse(json);
    }

}