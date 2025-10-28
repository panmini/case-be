package com.thy.constant;

import java.util.Set;

public class UrlConstants {
    private UrlConstants() {
    }

    public static final String API_BASE = "/api/";

    public static final String WORKPLACE_PATH = "workplace";
    public static final String WORKPLACE_BASE = API_BASE + WORKPLACE_PATH;
    public static final String WORKSPACE_PATH = "workspace";
    public static final String WORKSPACE_BASE = API_BASE + WORKSPACE_PATH;
    public static final String TEAM_PATH = "team";
    public static final String TEAM_BASE = API_BASE + TEAM_PATH;

    public static final String LANGFLOW_PATH = "flow";
    public static final String LANGFLOW_BASE = API_BASE + LANGFLOW_PATH;

    public static final String DEDICATED_SERVICE_PATH = "dedicated";
    public static final String DEDICATED_SERVICE_BASE = API_BASE + DEDICATED_SERVICE_PATH;

    public static final String PLAYGROUND_PATH = "playground";
    public static final String PLAYGROUND_PATH_BASE = API_BASE + PLAYGROUND_PATH;

    public static final String FINE_TUNING_PATH = "fine-tuning";
    public static final String FINE_TUNING_PATH_BASE = API_BASE + FINE_TUNING_PATH;

    public static final String LLAMA_FACTORY_PATH = "factory";
    public static final String LLAMA_FACTORY_PATH_BASE = API_BASE + LLAMA_FACTORY_PATH;

    public static final String PAYMENTS_PATH = "payments";
    public static final String PAYMENTS_PATH_BASE = API_BASE + PAYMENTS_PATH;

    public static final String PAYTR_CALLBACK_PATH = "paytr";
    public static final String PAYTR_CALLBACK_PATH_BASE = API_BASE + PAYTR_CALLBACK_PATH;

    public static final String DOCUMENTS_PATH = "documents";
    public static final String DOCUMENTS_PATH_BASE = API_BASE + DOCUMENTS_PATH;

    public static Set<String> getKnownSuffixes() {
        return Set.of(
                LANGFLOW_PATH,
                DEDICATED_SERVICE_PATH,
                PLAYGROUND_PATH,
                LLAMA_FACTORY_PATH,
                FINE_TUNING_PATH,
                PAYMENTS_PATH,
                DOCUMENTS_PATH);
    }

    public static Set<String> getKnownDomains() {
        return Set.of(WORKPLACE_PATH, WORKSPACE_PATH, TEAM_PATH);
    }
}
