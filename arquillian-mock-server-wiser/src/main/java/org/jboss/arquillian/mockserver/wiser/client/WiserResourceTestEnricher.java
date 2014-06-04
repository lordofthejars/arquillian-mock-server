package org.jboss.arquillian.mockserver.wiser.client;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.mockserver.common.ReflectionHelper;
import org.jboss.arquillian.mockserver.common.api.DeploymentInfo;
import org.jboss.arquillian.mockserver.wiser.api.WiserResource;
import org.jboss.arquillian.test.spi.TestEnricher;

public class WiserResourceTestEnricher implements TestEnricher {

    @Inject
    private Instance<DeploymentInfo> wiserHostPortInstance;

    @Override
    public void enrich(Object testCase) {

        List<Field> mockFields = ReflectionHelper.getFieldsWithAnnotation(testCase.getClass(), WiserResource.class);

        for (Field mockField : mockFields) {
            try {
                Object mocked = wiserHostPortInstance.get();
                if (DeploymentInfo.class.isAssignableFrom(mockField.getType())) {
                    if (!mockField.isAccessible()) {
                        mockField.setAccessible(true);
                    }
                    mockField.set(testCase, mocked);
                }
            } catch (Exception e) {
                throw new RuntimeException("Could not inject mocked object on field " + mockField, e);
            }
        }

    }

    @Override
    public Object[] resolve(Method method) {
        return new Object[method.getParameterTypes().length];
    }

}
