//package server.ext.quran;
//
//import base.NodeLabels;
//import base.NodeProperties;
//import org.codehaus.jackson.JsonEncoding;
//import org.codehaus.jackson.JsonGenerator;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.neo4j.graphdb.GraphDatabaseService;
//import org.neo4j.graphdb.Node;
//import org.neo4j.graphdb.ResourceIterable;
//import org.neo4j.graphdb.Transaction;
//import org.neo4j.server.rest.transactional.Neo4jJsonCodec;
//import org.neo4j.tooling.GlobalGraphOperations;
//
//import javax.ws.rs.*;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.StreamingOutput;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.nio.charset.Charset;
//import java.util.ArrayList;
//import java.util.List;
//
//@Path("/helloworld")
//public class HelloWorldResource {
//    private final GraphDatabaseService database;
////    private final ExecutionEngine cypher;
//
//    public HelloWorldResource(@Context GraphDatabaseService database) {
//        this.database = database;
////        this.cypher = cypher;
//    }
//
//    @GET
//    @Produces(MediaType.TEXT_PLAIN)
//    @Path("/id/{nodeId}")
//    public Response hello(@PathParam("nodeId") long nodeId) {
//        // Do stuff with the database
//        return Response.status(Response.Status.OK).entity(
//                ("Hello World, nodeId=" + nodeId).getBytes(Charset.forName("UTF-8"))).build();
//    }
//
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path("/list")
//    public Response list() {
//
//
//        StreamingOutput stream = new StreamingOutput() {
//            @Override
//            public void write(OutputStream os) throws IOException, WebApplicationException {
//                ObjectMapper objectMapper = new ObjectMapper();
//                JsonGenerator jg = objectMapper.getJsonFactory().createJsonGenerator(os, JsonEncoding.UTF8);
//
//                Neo4jJsonCodec codec = new Neo4jJsonCodec();
//
//                try (Transaction tx = database.beginTx()) {
//                    ResourceIterable<Node> nodes = GlobalGraphOperations.at(database).getAllNodesWithLabel(NodeLabels.ROOT);
//                    List<String> roots = new ArrayList<>();
//                    for (Node node : nodes) {
//                        roots.add((String) node.getProperty(NodeProperties.GeneralText.arabic));
//                    }
//                    codec.writeValue(jg, roots);
//                    tx.success();
//                }
//            }
//        };
//
//        return Response.ok().entity(stream).type(MediaType.APPLICATION_JSON).build();
//    }
//
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path("/node/list")
//    public Response nodeList() {
//
//        StreamingOutput stream = new StreamingOutput() {
//            @Override
//            public void write(OutputStream os) throws IOException, WebApplicationException {
//                ObjectMapper objectMapper = new ObjectMapper();
//                JsonGenerator jg = objectMapper.getJsonFactory().createJsonGenerator(os, JsonEncoding.UTF8);
//
//                Neo4jJsonCodec codec = new Neo4jJsonCodec();
//
//                try (Transaction tx = database.beginTx()) {
//                    ResourceIterable<Node> nodes = GlobalGraphOperations.at(database).getAllNodesWithLabel(NodeLabels.ROOT);
//                    codec.writeValue(jg, nodes);
//                    tx.success();
//                }
//            }
//        };
//
//        return Response.ok().entity(stream).type(MediaType.APPLICATION_JSON).build();
//    }
//
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path("/node/{nodeId}")
//    public Response node(@PathParam("nodeId") long nodeId) {
//
//
//        StreamingOutput stream = new StreamingOutput() {
//            @Override
//            public void write(OutputStream os) throws IOException, WebApplicationException {
//                ObjectMapper objectMapper = new ObjectMapper();
//                JsonGenerator jg = objectMapper.getJsonFactory().createJsonGenerator(os, JsonEncoding.UTF8);
//
//                Neo4jJsonCodec codec = new Neo4jJsonCodec();
//
//                try (Transaction tx = database.beginTx()) {
//                    Node node = database.getNodeById(1L);
//                    codec.writeValue(jg, node);
//                    tx.success();
//                }
//            }
//        };
//
//        return Response.ok().entity(stream).type(MediaType.APPLICATION_JSON).build();
//    }
//
//}