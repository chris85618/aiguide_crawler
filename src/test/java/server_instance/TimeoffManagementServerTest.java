package server_instance;

import crawler.Crawler;
import crawler.Crawljax;
import directive_tree.CrawlerDirective;
import usecase.learningPool.formInputValueList.FormInputValueList;
import usecase.learningPool.learningTask.LearningTask;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.Action;
import util.Config;
import util.HighLevelAction;

import java.util.*;
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
    public void test_get_coverage_vector_with_server_created(){
        Integer[] branchCoverage = this.serverInstanceManagement.getBranchCoverageVector();
        Integer[] statementCoverage = this.serverInstanceManagement.getStatementCoverageVector();

        int branchCoveredAmount = Arrays.stream(branchCoverage).filter(i -> i!=0).collect(Collectors.toList()).size();
        int statementCoveredAmount = Arrays.stream(statementCoverage).filter(i -> i!=0).collect(Collectors.toList()).size();

        assertEquals(1036, branchCoverage.length);
        assertEquals(2698,  statementCoverage.length);

        assertEquals(33, branchCoveredAmount);
        assertEquals(476, statementCoveredAmount);

    }

    @Test
    public void test_get_coverage_vector_with_request_forgot_password_page(){
        final LearningTask firstLearningTask = this.preCrawl().get(0);
        final String firstStateID = firstLearningTask.getStateID();
        final String firstDom = firstLearningTask.getDom();
        this.crawlWithXpath("/html[1]/body[1]/div[1]/form[1]/div[4]/div[2]/p[1]/a[1]".toUpperCase(), "", firstStateID, firstDom);


        Integer[] branchCoverage = this.serverInstanceManagement.getBranchCoverageVector();
        Integer[] statementCoverage = this.serverInstanceManagement.getStatementCoverageVector();

        int branchCoveredAmount = Arrays.stream(branchCoverage).filter(i -> i!=0).collect(Collectors.toList()).size();
        int statementCoveredAmount = Arrays.stream(statementCoverage).filter(i -> i!=0).collect(Collectors.toList()).size();

        assertEquals(1036, branchCoverage.length);
        assertEquals(2698,  statementCoverage.length);

        assertEquals(33, branchCoveredAmount);
        assertEquals(477, statementCoveredAmount);
    }

    @Test
    public void test_recordCoverage(){
        // request register
        final LearningTask registerLearningTask = this.preCrawl().get(0);
        final String registerStateID = registerLearningTask.getStateID();
        final String registerDom = registerLearningTask.getDom();
        this.crawlWithXpath("/html[1]/body[1]/div[1]/form[1]/div[4]/div[2]/p[1]/a[2]".toUpperCase(), "", registerStateID, registerDom);

        Integer[] branchCoverage = this.serverInstanceManagement.getBranchCoverageVector();
        Integer[] statementCoverage = this.serverInstanceManagement.getStatementCoverageVector();

        int branchCoveredAmount = Arrays.stream(branchCoverage).filter(i -> i!=0).collect(Collectors.toList()).size();
        int statementCoveredAmount = Arrays.stream(statementCoverage).filter(i -> i!=0).collect(Collectors.toList()).size();

        assertEquals(1036, branchCoverage.length);
        assertEquals(2698,  statementCoverage.length);

        assertEquals(36, branchCoveredAmount);
        assertEquals(480, statementCoveredAmount);


        this.serverInstanceManagement.recordCoverage();
        this.serverInstanceManagement.restartServerInstance();

        //request forgot password
        final LearningTask forgetPasswordLearningTask = this.preCrawl().get(0);
        final String forgetPasswordStateID = forgetPasswordLearningTask.getStateID();
        final String forgetPasswordDom = forgetPasswordLearningTask.getDom();
        this.crawlWithXpath("/html[1]/body[1]/div[1]/form[1]/div[4]/div[2]/p[1]/a[1]".toUpperCase(), "", forgetPasswordStateID, forgetPasswordDom);

        branchCoverage = this.serverInstanceManagement.getBranchCoverageVector();
        statementCoverage = this.serverInstanceManagement.getStatementCoverageVector();

        branchCoveredAmount = Arrays.stream(branchCoverage).filter(i -> i!=0).collect(Collectors.toList()).size();
        statementCoveredAmount = Arrays.stream(statementCoverage).filter(i -> i!=0).collect(Collectors.toList()).size();
        assertEquals(33, branchCoveredAmount);
        assertEquals(477, statementCoveredAmount);

        // assert record code coverage
        this.serverInstanceManagement.recordCoverage();

        Integer[] totalBranchCoverage = this.serverInstanceManagement.getTotalBranchCoverageVector();
        Integer[] totalStatementCoverage = this.serverInstanceManagement.getTotalStatementCoverageVector();

        int totalBranchCoveredAmount = Arrays.stream(totalBranchCoverage).filter(i -> i!=0).collect(Collectors.toList()).size();
        int totalStatementCoveredAmount = Arrays.stream(totalStatementCoverage).filter(i -> i!=0).collect(Collectors.toList()).size();

        assertEquals(36, totalBranchCoveredAmount);
        assertEquals(481, totalStatementCoveredAmount);
    }

    private List<LearningTask> preCrawl(){
        return crawler.crawlingWithDirectives(config, new ArrayList<>());
    }

    private List<LearningTask> crawlWithXpath(String actionXpath, String actionValue, String stateID, String dom){
        Action action = new Action(actionXpath, actionValue);
        List<Action> actionSet = new LinkedList<>();
        actionSet.add(action);
        HighLevelAction highLevelAction = new HighLevelAction(actionSet);

        CrawlerDirective crawlerDirective = new CrawlerDirective(
            stateID,
            dom,
            Collections.singletonList(highLevelAction),
            new FormInputValueList()
        );

        List<CrawlerDirective> directives = new ArrayList<>();
        directives.add(crawlerDirective);

        return crawler.crawlingWithDirectives(config, directives);
    }
}
