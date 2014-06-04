package org.jboss.arquillian.mockserver.wiser.client;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.mockserver.common.ReflectionHelper;
import org.jboss.arquillian.mockserver.common.api.DeploymentInfo;
import org.jboss.arquillian.mockserver.wiser.api.WiserConnection;
import org.jboss.arquillian.mockserver.wiser.api.WiserMessages;
import org.jboss.arquillian.test.spi.TestEnricher;

public class WiserMessagesTestEnricher implements TestEnricher {

    @Inject
    private Instance<DeploymentInfo> wiserHostPortInstance;
    
    @Override
    public void enrich(Object testCase) {
        List<Field> mockFields = ReflectionHelper.getFieldsWithAnnotation(testCase.getClass(), WiserMessages.class);

        for (Field mockField : mockFields) {
            try {
                Object value = new WiserConnection(getWiserUrl());
                if (WiserConnection.class.isAssignableFrom(mockField.getType())) {
                    if (!mockField.isAccessible()) {
                        mockField.setAccessible(true);
                    }
                    mockField.set(testCase, value);
                }
            } catch (Exception e) {
                throw new RuntimeException("Could not inject mocked object on field " + mockField, e);
            }
        }

    }

    
    
    private URL getWiserUrl() throws MalformedURLException {
        DeploymentInfo deploymentInfo = wiserHostPortInstance.get();
        String servletUrl = String.format("http://%s:%s/%s/wiser", deploymentInfo.getHost(), deploymentInfo.getContainerPort(), deploymentInfo.getContext());
        return new URL(servletUrl);
    }
    
    @Override
    public Object[] resolve(Method method) {
        return new Object[method.getParameterTypes().length];
    }

}
