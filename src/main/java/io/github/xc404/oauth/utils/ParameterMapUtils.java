package io.github.xc404.oauth.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author chaox
 * @Date 12/24/2023 9:23 AM
 */
public final class ParameterMapUtils
{
    private ParameterMapUtils() {
    }

    public static Map<String, List<String>> toParameterMap(Map<String, Object> map) {
        Map<String, List<String>> parameterMap = new HashMap<>();
        map.entrySet().forEach(entry -> {
            Object value = entry.getValue();
            if( value != null ) {
                parameterMap.put(entry.getKey(), toList(value));
            }
        });
        return parameterMap;
    }

    public static List<String> toList(Object value) {
        return Optional.ofNullable(value).map(instance -> {
            if( instance.getClass().isArray() ) {
                return Arrays.asList(ArrayUtils.toObjectArray(instance));
            } else if( instance instanceof List ) {
                return (List) instance;
            } else if( instance instanceof Collection ) { //TODO can we support other collection which cannot guarantee the order
                return new ArrayList<>((Collection<?>) instance);
            }
            return Collections.singleton(instance);
        }).map(list -> {
            List<String> collect = (List<String>) list.stream().filter(v -> v != null)
                    .map(Object::toString).collect(Collectors.toList());
            return collect;
        }).orElse(null);
    }
}
