package com.automatium.config;

import com.automatium.filter.*;
import com.automatium.logging.TestLogger;
import com.automatium.target.TestTarget;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Gurusharan on 20-11-2016.
 *
 * A set of test configuration properties that are obtained from
 * the test configuration file. The configuration can also be overridden
 * by specifying an environment variable or system property of the same
 * name. A system priority takes higher priority.
 *
 * The path of the configuration file can be given as either an
 * environment variable or a system property named 'configFile'
 *
 */
public class TestConfiguration extends OverridableProperties {

    private List<TestFilter> testFilters = new LinkedList<>();
    private boolean usingDefaultConfig = false;

    private static TestConfiguration singletonInstance;

    public static final String CONFIG_FILE_ENV_VARIABLE = "configFile";

    private static final String PRODUCT_FILTER_PROPERTY = "product";
    private static final String MODULE_FILTER_PROPERTY = "module";
    private static final String NAME_FILTER_PROPERTY = "testname";
    private static final String ID_FILTER_PROPERTY = "testid";
    private static final String PRIORITY_FILTER_PROPERTY = "priority";
    private static final String GROUP_FILTER_PROPERTY = "group";

    private static final String TARGET_PROPERTY = "target";
    private static final String START_URL_PROPERTY = "url";

    private TestConfiguration(String configFileName) throws IOException {
        super(configFileName);
        loadTestFilters();
    }

    public String getStartUrl() {
        return get(START_URL_PROPERTY);
    }

    public TestTarget getTestTarget() {
        return TestTarget.testTargetFromString(get(TARGET_PROPERTY));
    }

    private void loadTestFilters() {
        ProductFilter productFilters = loadProductFilters();
        if (productFilters != null) {
            testFilters.add(productFilters);
        }

        ModuleFilter moduleFilters = loadModuleFilters();
        if (moduleFilters != null) {
            testFilters.add(moduleFilters);
        }

        NameFilter nameFilters = loadNameFilters();
        if (nameFilters != null) {
            testFilters.add(nameFilters);
        }

        IdFilter idFilters = loadIdFilters();
        if (idFilters != null) {
            testFilters.add(idFilters);
        }

        PriorityFilter priorityFilters = loadPriorityFilters();
        if (priorityFilters != null) {
            testFilters.add(priorityFilters);
        }

        GroupFilter groupFilters = loadGroupFilters();
        if (groupFilters != null) {
            testFilters.add(groupFilters);
        }
    }

    private ProductFilter loadProductFilters() {
        String products = get(PRODUCT_FILTER_PROPERTY);
        if (products == null || products.equals("")) {
            return null;
        }
        ProductFilter productFilter = null;

        for (String product : products.split(",")) {
            if (productFilter == null) {
                productFilter = new ProductFilter(product);
            }
            else {
                productFilter.or(new ProductFilter(product));
            }
        }

        return productFilter;
    }

    private ModuleFilter loadModuleFilters() {
        String modules = get(MODULE_FILTER_PROPERTY);
        if (modules == null || modules.equals("")) {
            return null;
        }
        ModuleFilter moduleFilter = null;

        for (String module : modules.split(",")) {
            if (moduleFilter == null) {
                moduleFilter = new ModuleFilter(module);
            }
            else {
                moduleFilter.or(new ModuleFilter(module));
            }
        }

        return moduleFilter;
    }

    private NameFilter loadNameFilters() {
        String names = get(NAME_FILTER_PROPERTY);
        if (names == null || names.equals("")) {
            return null;
        }
        NameFilter nameFilter = null;

        for (String name : names.split(",")) {
            if (nameFilter == null) {
                nameFilter = new NameFilter(name);
            }
            else {
                nameFilter.or(new NameFilter(name));
            }
        }

        return nameFilter;
    }

    private IdFilter loadIdFilters() {
        String ids = get(ID_FILTER_PROPERTY);
        if (ids == null || ids.equals("")) {
            return null;
        }
        IdFilter idFilter = null;

        for (String id : ids.split(",")) {
            if (idFilter == null) {
                idFilter = new IdFilter(id);
            }
            else {
                idFilter.or(new IdFilter(id));
            }
        }

        return idFilter;
    }

    private GroupFilter loadGroupFilters() {
        String groups = get(GROUP_FILTER_PROPERTY);
        if (groups == null || groups.equals("")) {
            return null;
        }
        GroupFilter groupFilter = null;

        for (String group : groups.split(",")) {
            if (groupFilter == null) {
                groupFilter = new GroupFilter(group);
            }
            else {
                groupFilter.or(new IdFilter(group));
            }
        }

        return groupFilter;
    }

    private PriorityFilter loadPriorityFilters() {
        String priorities = get(PRIORITY_FILTER_PROPERTY);
        if (priorities == null || priorities.equals("")) {
            return null;
        }
        PriorityFilter priorityFilter = null;

        for (String priority : priorities.split(",")) {
            if (priorityFilter == null) {
                priorityFilter = new PriorityFilter(priority);
            }
            else {
                priorityFilter.or(new PriorityFilter(priority));
            }
        }

        return priorityFilter;
    }

    public List<TestFilter> getTestFilters() {
        return testFilters;
    }

    public static TestConfiguration getSingletonInstance() {
        if (singletonInstance == null) {
            try {
                String configFileName = System.getProperty(CONFIG_FILE_ENV_VARIABLE, System.getenv(CONFIG_FILE_ENV_VARIABLE));
                boolean usingDefaultConfiguration = false;
                if (configFileName == null) {
                    // This is probably an IDE run - let's use the default configuration
                    configFileName = new File(TestConfiguration.class.getClassLoader().getResource("defaults/testConfig.properties").toURI()).getAbsolutePath();
                    usingDefaultConfiguration = true;
                }
                singletonInstance = new TestConfiguration(configFileName);
                singletonInstance.usingDefaultConfig = usingDefaultConfiguration;
            }
            catch (IOException | URISyntaxException e) {
                e.printStackTrace();
                TestLogger.printErrorAndExit(String.format("Define system property or environment variable '%s'.", CONFIG_FILE_ENV_VARIABLE));
            }
        }
        return singletonInstance;
    }

    public boolean isUsingDefaultConfig() {
        return usingDefaultConfig;
    }
}
