package com.doodle.Search.model;

import java.util.ArrayList;
import java.util.List;

public class StoresData {
    private static List<String> stores ;
    static {
        stores =  new ArrayList<String>();
        stores.add("Amazon");
        stores.add("Sears");
        stores.add("Ebay Home");
        stores.add("Macys Home");
        stores.add("JCpenney Kids");
        stores.add("Ebay Electronics");
        stores.add("Amazon Appliance");
        stores.add("Ebay Mobiles");
        stores.add("Ebay Kids");
        stores.add("Amazon Fashion");
        stores.add("Ebay Travel");
        stores.add("JCpenney Home");
        stores.add("JCpenney Luggage");
        stores.add("JCpenney Appliance");
        stores.add("JCpenney Fashion");
        stores.add("Amazon Luggage");
        stores.add("Macys Jewellery");
        stores.add("JCpenney Jewellery");
        stores.add("Amazon Jewellery");
    }

    public static List<String> getStores(){
        return stores;
    }

    public static List<String> filterData(String searchString){
        List<String> searchResults =  new ArrayList<String>();
        if(searchString != null){
            searchString = searchString.toLowerCase();

            for(String rec :  stores){
                if(rec.toLowerCase().contains(searchString)){
                    searchResults.add(rec);
                }
            }
        }
        return searchResults;
    }
}