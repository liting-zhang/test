package test;

import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.powermock.reflect.Whitebox;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import test.ApplicationConfig;
import test.TestService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@ContextConfiguration(classes = {ApplicationConfig.class})
@RunWith(PowerMockRunner.class)
@WebAppConfiguration
@SpringBootApplication
@PrepareForTest({PreBuiltTransportClient.class})
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@PowerMockIgnore({"javax.management.*", "javax.net.ssl.*"})
public class ExampleTest {

    PreBuiltTransportClient clientMock = PowerMockito.mock(PreBuiltTransportClient.class);
    TestService testService = PowerMockito.spy(new TestService());

    MockMvc mockMvc;


    @Before
    public void setUp() throws Exception {
        Whitebox.setInternalState(testService, "client", clientMock);
        mockMvc = standaloneSetup(testService).build();


    }

    @Test
    public void test() throws Exception {
        this.mockMvc.perform(get("/test"))
                .andExpect(status().isOk());

    }




}
