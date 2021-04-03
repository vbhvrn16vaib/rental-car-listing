package com.core.vehicle_listing.filter;

import com.core.vehicle_listing.model.VehicleDetails;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import org.springframework.stereotype.Service;

@Service
public class FilterService {

    private final static Map<String, Function<Map<String,String>,Predicate<VehicleDetails>>> FILTER_MAPS = new HashMap<>();

    {
        FILTER_MAPS.put("make", this::filterMake);
        FILTER_MAPS.put("color", this::filterColor);
        FILTER_MAPS.put("year", this::filterYearWithEquality);
        FILTER_MAPS.put("model", this::filterModel);
    }

    public Predicate<VehicleDetails> getFilters(Map<String, String> allParams) {
        final Predicate<VehicleDetails> predicate =
            allParams.containsKey("to") && allParams.containsKey("from") ?
                filterYear(allParams) :
                null;

        return FILTER_MAPS.entrySet()
            .stream()
            .filter(x -> allParams.containsKey(x.getKey()))
            .map(x -> x.getValue().apply(allParams))
            .reduce(predicate != null ? predicate : t -> true, Predicate::and);
    }

    Predicate<VehicleDetails> filterMake(Map<String,String> v){
        String val = v.get("make");
      return vd -> vd.getMake().equalsIgnoreCase(val);
    }

    Predicate<VehicleDetails> filterModel(Map<String,String> v){
        String val = v.get("model");
        return vd -> vd.getModel().equalsIgnoreCase(val);
    }

    Predicate<VehicleDetails> filterColor(Map<String,String> v){
        String val = v.get("color");
        return vd -> vd.getColor().equalsIgnoreCase(val);
    }

    Predicate<VehicleDetails> filterYear(Map<String,String> v){
        Long to = Long.parseLong(v.get("to"));
        Long from = Long.parseLong(v.get("from"));
        return vd -> vd.getYear() >= from && vd.getYear() <= to;
    }

    Predicate<VehicleDetails> filterYearWithEquality(Map<String,String> v){
        Long val = Long.valueOf(v.get("year"));
        return vd -> vd.getYear().equals(val);
    }
}
