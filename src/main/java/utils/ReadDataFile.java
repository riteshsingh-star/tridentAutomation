package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import com.github.javafaker.Faker;

import java.io.InputStream;
import java.time.Instant;
import java.util.Iterator;
import java.util.UUID;

public class ReadDataFile {
    private static final String BASE_CONFIG_FOLDER = "testData/";
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private static final ThreadLocal<Faker> FAKER = ThreadLocal.withInitial(Faker::new);

    public static <T> T readJson(String resourcePath, Class<T> clazz) {
        try (InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("testData/" + resourcePath)) {
            if (is == null) {
                throw new RuntimeException("File not found: " + resourcePath);
            }
            return JSON_MAPPER.readValue(is, clazz);

        } catch (Exception e) {
            throw new RuntimeException("Failed to read JSON: " + resourcePath, e);
        }
    }

    public static <T> T loadDataFile(Class<T> clazz) throws Exception {
        String clientPath = System.getProperty("clientConfig");
        // support simplified tenant property -> testData/clientSpecific/{tenant}.json
        if ((clientPath == null || clientPath.isBlank()) && System.getProperty("tenant") != null) {
            String tenant = System.getProperty("tenant");
            clientPath = BASE_CONFIG_FOLDER + "clientSpecific/" + tenant + ".json";
        }
        String flow = clazz.getSimpleName();
        String baseFileName = Character.toLowerCase(flow.charAt(0)) + flow.substring(1) + ".json";
        String flowName = baseFileName.replace(".json", "").replace(".yaml", "").replace(".yml", "");
        System.out.println("Client Path: " + clientPath);
        String baseResourcePath = BASE_CONFIG_FOLDER + baseFileName;
        InputStream baseStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(baseResourcePath);
        if (baseStream == null) {
            throw new RuntimeException("Base config not found: " + baseResourcePath);
        }
        ObjectMapper baseMapper = getMapper(baseFileName);
        ObjectNode baseNode = (ObjectNode) baseMapper.readTree(baseStream);
        if (clientPath != null && !clientPath.isBlank()) {
            InputStream clientStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(clientPath);
            if (clientStream == null) {
                throw new RuntimeException("Client config not found: " + clientPath);
            }
            ObjectMapper clientMapper = getMapper(clientPath);
            ObjectNode clientRoot = (ObjectNode) clientMapper.readTree(clientStream);
            ObjectNode overrideNode;
            if (clientRoot.has(flowName) && clientRoot.get(flowName).isObject()) {
                overrideNode = (ObjectNode) clientRoot.get(flowName);
            } else {
                overrideNode = clientRoot;
            }
            merge(baseNode, overrideNode);
            System.out.println("Client Config Loaded:");
            System.out.println(JSON_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(clientRoot));
        }
        // Resolve placeholders (safe tree-walking) to generate unique/tenant-aware
        // values
        resolvePlaceholders(baseNode);

        System.out.println("Final Merged JSON:");
        System.out.println(JSON_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(baseNode));
        return JSON_MAPPER.treeToValue(baseNode, clazz);
    }

    private static ObjectMapper getMapper(String path) {
        if (path.endsWith(".yaml") || path.endsWith(".yml")) {
            return new ObjectMapper(new YAMLFactory());
        }
        return new ObjectMapper();
    }

    private static void merge(ObjectNode base, ObjectNode override) {
        override.fieldNames().forEachRemaining(field -> {
            JsonNode baseValue = base.get(field);
            JsonNode overrideValue = override.get(field);
            if (baseValue != null && baseValue.isObject() && overrideValue.isObject()) {
                merge((ObjectNode) baseValue, (ObjectNode) overrideValue);
            } else {
                base.set(field, overrideValue);
            }
        });
    }

    private static void resolvePlaceholders(ObjectNode node) {
        Faker faker = FAKER.get();
        Iterator<String> fields = node.fieldNames();
        while (fields.hasNext()) {
            String field = fields.next();
            JsonNode value = node.get(field);
            if (value.isObject()) {
                resolvePlaceholders((ObjectNode) value);
            } else if (value.isTextual()) {
                String text = value.asText();
                String resolved = resolveTextPlaceholders(text, faker);
                if (!resolved.equals(text)) {
                    node.put(field, resolved);
                }
            }
        }
    }

    private static String resolveTextPlaceholders(String text, Faker faker) {
        String result = text;
        if (result.contains("{{random}}")) {
            result = result.replace("{{random}}", faker.number().digits(5));
        }
        if (result.contains("{{uuid}}")) {
            result = result.replace("{{uuid}}", UUID.randomUUID().toString());
        }
        if (result.contains("{{now}}")) {
            result = result.replace("{{now}}", Instant.now().toString());
        }
        if (result.contains("{{tenant}}")) {
            result = result.replace("{{tenant}}", System.getProperty("tenant", "dev"));
        }
        if (result.contains("{{env}}")) {
            result = result.replace("{{env}}", System.getProperty("env", "sit"));
        }
        if (result.contains("{{plantName}}")) {
            result = result.replace("{{plantName}}", faker.company().name().replaceAll("\"", ""));
        }
        return result;
    }

}
