package org.jboss.arquillian.mockserver.fakeftp.client;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class FakeFtpMockServerExtensionTest {

    @Deployment(testable = false)
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class, "myapp.war").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void should_upload_file_to_ftp_server() throws IOException {

        FTPClient client = new FTPClient();

        String server = "localhost";
        String user = "user";
        String password = "password";

        client.connect(server, 10001);
        boolean login = client.login(user, password);
        
        System.out.println(login);
        

    }

}
