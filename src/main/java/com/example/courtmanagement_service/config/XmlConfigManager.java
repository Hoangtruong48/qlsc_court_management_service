package com.example.courtmanagement_service.config;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class XmlConfigManager {
    // Singleton instance
    private static volatile XmlConfigManager instance;
    private final Map<String, String> configMap;
    private final String configFilePath;

    // Private constructor
    private XmlConfigManager(String configFilePath) {
        this.configFilePath = configFilePath;
        this.configMap = new ConcurrentHashMap<>();
        loadConfig();
    }

    // Public static method to get instance
    public static XmlConfigManager getInstance(String configFilePath) {
        if (instance == null) {
            synchronized (XmlConfigManager.class) {
                if (instance == null) {
                    instance = new XmlConfigManager(configFilePath);
                }
            }
        }
        return instance;
    }

    // Load config từ file XML
    private void loadConfig() {
        try {
            File xmlFile = new File(configFilePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            // Bảo mật: Ngăn chặn XXE Attack
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("property");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String key = element.getAttribute("name");
                    String value = element.getTextContent().trim();
                    configMap.put(key, value);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load XML configuration", e);
        }
    }

    // Lấy giá trị config
    public String get(String key) {
        return configMap.get(key);
    }

    // Lấy giá trị config với giá trị mặc định
    public String get(String key, String defaultValue) {
        return configMap.getOrDefault(key, defaultValue);
    }

    // Lấy tất cả config
    public Map<String, String> getAll() {
        return new HashMap<>(configMap);
    }

    // Kiểm tra key có tồn tại
    public boolean containsKey(String key) {
        return configMap.containsKey(key);
    }

    // Reload config (nếu cần)
    public void reload() {
        configMap.clear();
        loadConfig();
    }
}
