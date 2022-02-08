package net.frozenorb.foxtrot.util;

import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class MetaUtil {

    public static String getMetaDataFromList(List<MetadataValue> metadataValueList) {
        return metadataValueList.get(0).asString();
    }

}
