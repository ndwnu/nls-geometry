package nu.ndw.nls.geometry.stream.collectors;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.operation.linemerge.LineMerger;
import org.springframework.util.CollectionUtils;

/**
 * Stateful collector that makes use of {@link LineMerger} to merge multiple {@link LineString}s. If all line strings
 * can be combined into a single LineString then the result will be a list with a single result. If there are gaps in
 * the line string and the result cannot map to a single line string, the {@link Optional#empty()} is returned.
 */
@Slf4j
public class ToSingleLineStringMergerCollector implements Collector<LineString, LineMerger, Optional<LineString>> {

    @Override
    public Supplier<LineMerger> supplier() {
        return LineMerger::new;
    }

    @Override
    public BiConsumer<LineMerger, LineString> accumulator() {
        return LineMerger::add;
    }

    @Override
    public BinaryOperator<LineMerger> combiner() {
        return (lineMerger, lineMerger2) -> {
            supplier().get().add(lineMerger.getMergedLineStrings());
            supplier().get().add(lineMerger2.getMergedLineStrings());
            return lineMerger;
        };
    }

    @Override
    public Function<LineMerger, Optional<LineString>> finisher() {
        return lineMerger -> {
            @SuppressWarnings("unchecked")
            List<LineString> mergedLineStrings = (List<LineString>)lineMerger.getMergedLineStrings();
            if (CollectionUtils.isEmpty(mergedLineStrings)) {
                return Optional.empty();
            } else if (mergedLineStrings.size() > 1) {
                log.debug("Result is more than one LineString");
                return Optional.empty();
            }

            return Optional.of(mergedLineStrings.iterator().next());
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.emptySet();
    }
}
