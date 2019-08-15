package utilities.TestRailIntegration;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.ITestResult;
import utilities.PropertyManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author shwetankvashishtha
 */

public class TestRailGenerics {

    PropertyManager propertyManager = new PropertyManager();
    String username = propertyManager.getResourceBundle.getProperty("testrail.user.name");
    String password = propertyManager.getResourceBundle.getProperty("testrail.user.password");
    APIClient client = new APIClient(propertyManager.getResourceBundle.getProperty("testrail.server.url"));

    public void loginTestRail() {
        client.setUser(username);
        client.setPassword(password);
    }

    public Object createProject(String projectName, String projectDescription, boolean projectAnnouncement,
                                Integer suiteMode) throws Exception {

        JSONObject obj = new JSONObject();

        obj.put("name", projectName);
        obj.put("announcement", projectDescription);
        obj.put("show_announcement", projectAnnouncement);
        obj.put("suite_mode", suiteMode);

        JSONObject jsonObj = (JSONObject) client.sendPost("add_project", obj);
        Object projectId = jsonObj.get("id");
        return projectId;
    }

    public JSONArray listProjects() throws Exception {
        return (JSONArray) client.sendGet("get_projects");
    }

    public void createAndUpdateProjectStatus(boolean isCompleted, Integer projectId, String projectName, String projectDescription, boolean projectAnnouncement, Integer suiteMode) throws Exception {
        JSONObject obj = new JSONObject();

        obj.put("is_completed", isCompleted);
        client.sendPost(
                "update_project/" + createProject(projectName, projectDescription, projectAnnouncement, suiteMode),
                obj);
    }

    public void deleteProject(Integer projectId) throws Exception {
        JSONObject obj = new JSONObject();
        client.sendPost("delete_project/" + projectId, obj);
    }

    public JSONObject getTestId(Integer testCaseId) throws Exception {
        loginTestRail();
        JSONObject obj = (JSONObject) client.sendGet("get_case/" + testCaseId);
        return obj;
    }

    public void createTestRun(Integer projectId, String testRunName, String testRunDescription, Integer milestoneId,
                              Integer assignToId, boolean IncludeAll, ArrayList<Integer> testCaseIds) throws Exception {
        loginTestRail();
        JSONObject obj = new JSONObject();

        obj.put("name", testRunName);
        obj.put("description", testRunDescription);
        obj.put("milestone_id", milestoneId);
        obj.put("assignedto_id", assignToId);
        obj.put("include_all", IncludeAll);
        obj.put("case_ids", testCaseIds);

        client.sendPost("add_run/" + projectId, obj);
    }

    public int getExpectedTestRunId(Integer projectId) throws Exception {
        ArrayList<Integer> list = new ArrayList<>();
        int a = 0;
        loginTestRail();
        JSONArray jsonArr = (JSONArray) client.sendGet("get_runs/" + projectId);
        String arr[] = jsonArr.get(0).toString().split("id");
        String arrNew[] = arr[3].split(",");
        StringBuilder sb = new StringBuilder(arrNew[0]);
        sb.deleteCharAt(0).deleteCharAt(0);
        String[] numbers = sb.toString().split(" ");
        for (String number : numbers) {
            list.add(Integer.valueOf(number));
            a = list.get(0);
        }
        return a;
    }

    public void getResultField() throws Exception {
        JSONArray jsonObj = (JSONArray) client.sendGet("get_result_fields/");
        System.out.println(jsonObj);
    }

    public void addTestResult(ITestResult result, Integer testRunId, Integer testCaseId, String comment, String version)
            throws Exception {
        try {
            if (result.getStatus() == ITestResult.SUCCESS) {

                JSONObject obj = new JSONObject();

                obj.put("status_id", 1);
                obj.put("comment", comment);
                obj.put("version", version);

                client.sendPost("add_result_for_case/" + testRunId + "/" + testCaseId, obj);
            } else if (result.getStatus() == ITestResult.FAILURE) {

                JSONObject obj = new JSONObject();

                obj.put("status_id", 5);
                obj.put("comment", comment);
                obj.put("version", version);

                client.sendPost("add_result_for_case/" + testRunId + "/" + testCaseId, obj);
            }
        } catch (Exception e) {
            System.out.println("TestRail API error");
            System.err.println(e);
        }
    }

    public ArrayList<Integer> getMilestones(Integer projectId) throws Exception {
        JSONArray milestones = (JSONArray) client.sendGet("get_milestones/" + projectId);
        System.out.println(milestones);
        return milestones;
    }

    public ArrayList<Integer> iterateOverXml() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document dom = db.parse(propertyManager.getResourceBundle.getProperty("xml"));
        Element docEle = dom.getDocumentElement();
        NodeList nl = docEle.getChildNodes();
        ArrayList<Integer> list = new ArrayList<Integer>();
        if (nl != null) {
            int length = nl.getLength();
            for (int i = 0; i < length; i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) nl.item(i);
                    if (el.getNodeName().contains("test")) {
                        NodeList classes = (NodeList) el.getElementsByTagName("class");
                        for (int j = 0; j < classes.getLength(); j++) {
                            Element line = (Element) classes.item(j);
                            String[] l = line.getAttribute("name").split("C");
                            int num = Integer.parseInt(l[1]);
                            list.add(num);
                        }
                    }
                }
            }
        }
        return list;
    }
}