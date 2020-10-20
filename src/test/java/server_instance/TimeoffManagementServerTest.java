package server_instance;

import crawler.Crawler;
import crawler.Crawljax;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.Config;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class TimeoffManagementServerTest {
    private Crawler crawler;
    private Config config;
    private ServerInstanceManagement serverInstanceManagement;
    @Before
    public void setUp() throws Exception {
        this.config = new Config("./src/test/configuration/crawlerTestConfiguration.json");
        this.serverInstanceManagement = new TimeOffManagementServer(config.AUT_NAME, config.AUT_PORT);
        this.serverInstanceManagement.createServerInstance();
        this.crawler = new Crawljax(serverInstanceManagement);
    }

    @After
    public void tearDown() throws Exception {
        this.serverInstanceManagement.closeServerInstance();
    }

    @Test
    public void test_get_code_coverage(){
        int totalCoverage = this.serverInstanceManagement.getBranchCoverageVector().length;
        int branchCoverageAmount = Arrays.stream(this.serverInstanceManagement.getBranchCoverageVector()).filter(i -> i!=0).collect(Collectors.toList()).size();
        double coverage = 100*(1.0*branchCoverageAmount/totalCoverage);
        assertEquals(totalCoverage,1036);
        assertEquals(branchCoverageAmount,33);
        assertEquals(String.format("%.2f",coverage), "3.19");
    }
}
