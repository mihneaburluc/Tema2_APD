package com.apd.tema2.factory;

import com.apd.tema2.entities.Intersection;
import com.apd.tema2.intersections.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Prototype Factory: va puteti crea cate o instanta din fiecare tip de implementare de Intersection.
 */
public class IntersectionFactory {
    private static Map<String, Intersection> cache = new HashMap<>();

    public static Intersection newInstersection (String handler_type, int n, int t, int x) {
        // se creaza cate o intersectie speciala pentru fiecare task in parte
        return switch (handler_type) {
            case "simple_n_roundabout" -> cache.put(handler_type, new Task2(n, t));
            case "simple_strict_1_car_roundabout" -> cache.put(handler_type, new Task3(n, t));
            case "simple_strict_x_car_roundabout" -> cache.put(handler_type, new Task4(n, t, x));
            case "simple_max_x_car_roundabout" -> cache.put(handler_type, new Task5(n, t, x));
            case "priority_intersection" -> cache.put(handler_type, new Task6());
            case "simple_maintenance" -> cache.put(handler_type, new Task8(x));
            case "complex_maintenance" -> cache.put(handler_type, new Task9(n, t, x));
            case "railroad" -> cache.put(handler_type, new Task10());
            default -> null;
        };
    }

    public static Intersection getIntersection(String handlerType) {
        return cache.get(handlerType);
    }

}
