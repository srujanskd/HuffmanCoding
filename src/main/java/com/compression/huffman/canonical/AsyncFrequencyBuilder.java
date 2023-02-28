package com.compression.huffman.wordhuffman.compress;

import com.compression.huffman.utils.FrequencyMap;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AsyncFrequencyBuilder {
    public Map<String, Integer> asyncFrequencyBuild(String path) throws Exception {
        try {
            List<CompletableFuture<Map<String, Integer>>> futures = new ArrayList<>();

            List<String> items = new ArrayList<>();
            Files.lines(Paths.get(path))
                    .forEach(line -> {
                        items.add(line);
                        if (items.size() % 10000 == 0) {
                            //add completable task for each of 10000 rows
                            futures.add(CompletableFuture.supplyAsync(yearCountSupplier(new ArrayList<>(items), new HashMap<>())));
                            items.clear();
                        }
                    });
            if (items.size() > 0) {
                //add completable task for remaining rows
                futures.add(CompletableFuture.supplyAsync(yearCountSupplier(items, new HashMap<>())));
            }
            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]))
                    .thenApply($ -> {
                        //join all task to collect result after all tasks completed
                        return futures.stream().map(ftr -> ftr.join()).collect(Collectors.toList());
                    })
                    .thenApply(maps -> {
                        Map<String, Integer> yearCountMap = new HashMap<>();
                        maps.forEach(map -> {
                            //merge the result of all the tasks
                            map.forEach((key, val) -> {
                                if (yearCountMap.containsKey(key)) {
                                    yearCountMap.put(key, yearCountMap.get(key) + val);
                                } else
                                    yearCountMap.put(key, val);
                            });
                        });
                        return yearCountMap;
                    })
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    //Supplier method to count the year in given rows
    public Supplier<Map<String, Integer>> yearCountSupplier(List<String> items, Map<String, Integer> map) {
        FrequencyMap fm = new FrequencyMap();
        return () -> {
            items.forEach((line) -> {
                yearCount(line, map, fm);
            });
            return map;
        };
    }

    public void yearCount(String line, Map<String, Integer> countMap, FrequencyMap fm) {
        fm.buildFrequencyTable(new ByteArrayInputStream(line.getBytes()));
//        countMap = fm.getKeyValues().stream()
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        for(Map.Entry<String, Integer> entry : fm.getKeyValues())
        {
            countMap.put(entry.getKey(), countMap.getOrDefault(entry.getKey(), 0) + entry.getValue());
        }
    }
}
