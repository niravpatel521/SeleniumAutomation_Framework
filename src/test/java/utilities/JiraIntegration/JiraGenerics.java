package utilities.JiraIntegration;

import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;
import utilities.PropertyManager;

/**
 * @author shwetankvashishtha
 */

public class JiraGenerics {

    PropertyManager propertyManager = new PropertyManager();
    JiraClient jira;
    Issue issue;

    public void setupJira() {
        BasicCredentials creds = new BasicCredentials(propertyManager.getResourceBundle.getProperty("jira.user.name"), propertyManager.getResourceBundle.getProperty("jira.user.password"));
        jira = new JiraClient(propertyManager.getResourceBundle.getProperty("jira.server.url"), creds);
    }

    public void getIssue(String issueId) {
        try {
            issue = jira.getIssue(issueId);
        } catch (JiraException jiraException) {
            System.err.println(jiraException.getMessage());

            if (jiraException.getCause() != null)
                System.err.println(jiraException.getCause().getMessage());
        }
    }
}
