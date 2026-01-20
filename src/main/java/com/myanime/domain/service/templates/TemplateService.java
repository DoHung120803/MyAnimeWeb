package com.myanime.domain.service.templates;

import com.myanime.common.utils.FileUtil;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class TemplateService {

    public String loadTemplate(String path, Map<String, String> variables) {
        String template = FileUtil.readFromFile(path);
        if (template == null) {
            return safeValue(variables != null ? variables.get("content") : "");
        }
        return renderTemplate(template, variables);
    }

    public String renderTemplate(String template, Map<String, String> variables) {
        if (template == null || variables == null) return template;
        String rendered = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String key = "{{" + entry.getKey() + "}}";
            rendered = rendered.replace(key, safeValue(entry.getValue()));
        }
        return rendered;
    }

    private String safeValue(String value) {
        return (value == null || value.isBlank()) ? "" : value;
    }
}
