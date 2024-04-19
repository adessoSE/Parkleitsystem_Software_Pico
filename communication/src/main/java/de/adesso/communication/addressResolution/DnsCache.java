package de.adesso.communication.addressResolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DnsCache {

    private final Map<String, String> dnsCacheMap;

    private final Map<String, LocalDateTime> timesToLive;

    public DnsCache() {
        this.dnsCacheMap = new ConcurrentHashMap<>();
        this.timesToLive = new ConcurrentHashMap<>();
    }

    protected DnsCache(Map<String, String> dnsCacheMap, Map<String, LocalDateTime> timesToLive){
        this.dnsCacheMap = dnsCacheMap;
        this.timesToLive = timesToLive;
    }

    public boolean hasEntryFor(String service){
        if(dnsCacheMap.containsKey(service)){
            return timesToLive.get(service).plusMinutes(1).isAfter(LocalDateTime.now());
        }
        return false;
    }

    public void clearCache(){
        dnsCacheMap.clear();
    }

    public String getAddress(String service){
        if(hasEntryFor(service)){
            return dnsCacheMap.get(service);
        }
        return null;
    }

    public void addEntry(String service, String address){
        dnsCacheMap.put(service, address);
        timesToLive.put(service, LocalDateTime.now());
    }

    @Scheduled(fixedRate = 900000L)
    protected void checkTimesToLive(){
        for(String s : dnsCacheMap.keySet()){
            if(timesToLive.get(s).plusMinutes(1).isBefore(LocalDateTime.now())){
                dnsCacheMap.remove(s);
                timesToLive.remove(s);
            }
        }
    }
}
