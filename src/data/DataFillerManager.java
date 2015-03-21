package data;

import data.leeds.LeedsCorpusDataFiller;
import data.leeds.more.LeedsCorpusNextDataFiller;
import data.leeds.more.NextVerseInQuranDataFiller;
import data.leeds.more.WordIndexInQuranDataFiller;
import graph.quran.tanzil.TanzilDataFiller;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by vahidoo on 3/20/15.
 */
public class DataFillerManager {
    private static final Logger logger = Logger.getLogger(DataFillerManager.class.getName());

    public static Map<Class<? extends DataFiller>, Set<Class<? extends DataFiller>>> dependencyGraph;

    static {
        dependencyGraph = new HashMap<>();

        addDataFiller(TanzilDataFiller.class, null);
        addDataFiller(LeedsCorpusDataFiller.class, TanzilDataFiller.class);
        addDataFiller(LeedsCorpusNextDataFiller.class, LeedsCorpusDataFiller.class);
        addDataFiller(WordIndexInQuranDataFiller.class, LeedsCorpusNextDataFiller.class);
        addDataFiller(NextVerseInQuranDataFiller.class, LeedsCorpusNextDataFiller.class);



//        fillers.add(TanzilDataFiller.class);
//        fillers.add(LeedsCorpusDataFiller.class);
//        fillers.add(WordIndexInQuranDataFiller.class);
//        fillers.add(LeedsCorpusNextDataFiller.class);

    }

    private static void addDataFiller(Class<? extends DataFiller> clazz, Class<? extends DataFiller>... dependencies) {

        Set<Class<? extends DataFiller>> dependenciesSet = new HashSet<>();
        if (dependencies != null) {
            dependenciesSet.addAll(Arrays.asList(dependencies));
        }

        dependencyGraph.put(clazz, dependenciesSet);

    }


    public static List<Class<? extends DataFiller>> getTopologicalSorted() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        logger.info("start sorting");
        List<Class<? extends DataFiller>> topologicalSorted = new ArrayList<>();

        if (dependencyGraph.isEmpty())
            return topologicalSorted;

        while (dependencyGraph.size() != topologicalSorted.size()) {
            int added = 0;
            for (Class<? extends DataFiller> filler : dependencyGraph.keySet()) {

                boolean flag = true;
                Set<Class<? extends DataFiller>> dependencies = dependencyGraph.get(filler);
                for (Class<? extends DataFiller> dependency : dependencies) {
                    if (!topologicalSorted.contains(dependency)) {
                        flag = false;
                        break;
                    }
                }

                if (flag && !topologicalSorted.contains(filler)) {
                    topologicalSorted.add(filler);
                    added++;
                }

            }
            if (added == 0) {
                throw new RuntimeException("Dependency Graph isn't DAG");
            }
        }

        logger.info("sorting was finished");
        return topologicalSorted;
    }

    public static Set<Class<? extends DataFiller>> getDependencies(Class<? extends DataFiller> clazz) {
        return dependencyGraph.get(clazz);
    }

    public static Set<Class<? extends DataFiller>> getFillers() {
        logger.info("dependencyGraph.size : " + dependencyGraph.size());
        return dependencyGraph.keySet();
    }
}
