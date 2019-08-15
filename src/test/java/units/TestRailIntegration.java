package units;

import org.json.simple.JSONArray;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import utilities.TestRailIntegration.APIClient;

/**
 * @author shwetankvashishtha
 */

public class TestRailIntegration {

    @Mock
    JSONArray jsonArray;

    @Test
    public void testListProjects() throws Exception {
        APIClient apiClient = Mockito.mock(APIClient.class);
        Mockito.when(apiClient.sendGet("get_projects")).thenReturn(jsonArray);
        Assert.assertEquals(jsonArray, apiClient.sendGet("get_projects"));
    }
}
