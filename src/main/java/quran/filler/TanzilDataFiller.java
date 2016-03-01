package quran.filler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import quran.entity.Chapter;
import quran.entity.ChapterType;
import quran.entity.Verse;
import quran.repository.ChapterRepository;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by vahidoo on 3/5/15.
 */

public class TanzilDataFiller implements DataFiller {

    private static final String SURAS_TAG = "suras";
    private static final String SURA_TAG = "sura";
    private static final String AYA_TAG = "aya";

    private static final String INDEX_ATTR = "index";
    private static final String ORDER_ATTR = "order";
    private static final String NAME_ATTR = "name";
    private static final String TYPE_ATTR = "type";
    private static final String TEXT_ATTR = "text";


    private InputStream metadataFile;
    private InputStream uthmaniFile;

    @Autowired
    private Neo4jTemplate template;

    @Autowired
    private ChapterRepository chapterRepository;

    public void setMetadataFile(InputStream metadataFile) {
        this.metadataFile = metadataFile;
    }

    public void setUthmaniFile(InputStream uthmaniFile) {
        this.uthmaniFile = uthmaniFile;
    }

    @Override
    @Transactional
    public void fill() throws ParserConfigurationException, IOException, SAXException {
        fillChapters();
        fillVerses();
    }

    @Transactional
    public void fillChapters() throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(metadataFile);

        NodeList suras = ((Element) doc.getDocumentElement().getElementsByTagName(SURAS_TAG).item(0)).getElementsByTagName(SURA_TAG);

        for (int i = 0; i < suras.getLength(); i++) {
            System.out.println("add chapter #" + (i + 1));
            Node sura = suras.item(i);

            Chapter chapter = new Chapter();

            String name = sura.getAttributes().getNamedItem(NAME_ATTR).getNodeValue();
            String type = sura.getAttributes().getNamedItem(TYPE_ATTR).getNodeValue();
            Integer index = Integer.parseInt(sura.getAttributes().getNamedItem(INDEX_ATTR).getNodeValue());
            Integer order = Integer.parseInt(sura.getAttributes().getNamedItem(ORDER_ATTR).getNodeValue());

            chapter.setName(name);
            chapter.setType(ChapterType.valueOf(type));
            chapter.setOrder(order);
            chapter.setIndex(index);

            template.save(chapter);
        }
    }

    @Transactional
    public void fillVerses() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(uthmaniFile);

        NodeList suras = ((Element) doc.getDocumentElement()).getElementsByTagName(SURA_TAG);

        for (int i = 0; i < suras.getLength(); i++) {
            System.out.println("update chapter #" + (i + 1));
            Node sura = suras.item(i);

            int chapterIndex = Integer.parseInt(sura.getAttributes().getNamedItem(INDEX_ATTR).getNodeValue());
            Chapter chaper = chapterRepository.findByIndex(chapterIndex);

            NodeList ayas = ((Element) sura).getElementsByTagName(AYA_TAG);
            for (int j = 0; j < ayas.getLength(); j++) {
                Node aya = ayas.item(j);

                int verseIndex = Integer.parseInt(aya.getAttributes().getNamedItem(INDEX_ATTR).getNodeValue());
                String text = aya.getAttributes().getNamedItem(TEXT_ATTR).getNodeValue();

                Verse verse = new Verse();
                verse.setChapter(chaper);
                verse.setIndex(verseIndex);
                verse.setText(text);

                template.save(verse);
            }
        }
    }
}
