package org.jboss.arquillian.mockserver.wiser.container;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.jboss.arquillian.container.test.spi.command.CommandService;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.mockserver.common.ReflectionHelper;
import org.jboss.arquillian.mockserver.common.api.DeploymentInfo;
import org.jboss.arquillian.mockserver.wiser.WiserHostPortCommand;
import org.jboss.arquillian.mockserver.wiser.api.WiserResource;
import org.jboss.arquillian.test.spi.TestEnricher;

public class WiserResourceTestEnricherByCommand implements TestEnricher {

    @Inject
    private Instance<ServiceLoader> serviceLoader;

    @Override
    public void enrich(Object testCase) {

        List<Field> mockFields = ReflectionHelper.getFieldsWithAnnotation(testCase.getClass(), WiserResource.class);

        if (mockFields.size() > 0) {

            DeploymentInfo wiserHostPort = getWiserHostPort();

            for (Field mockField : mockFields) {
                try {
                    Object mocked = wiserHostPort;
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

    }

    private DeploymentInfo getWiserHostPort() {
        WiserHostPortCommand wiserHostPortCommand = new WiserHostPortCommand();
        return getCommandService().execute(wiserHostPortCommand);
    }

    private CommandService getCommandService() {

        ServiceLoader loader = serviceLoader.get();

        if (loader == null) {
            throw new IllegalStateException("No " + ServiceLoader.class.getName() + " found in context");
        }

        CommandService service = loader.onlyOne(CommandService.class);

        if (service == null) {
            throw new IllegalStateException("No " + CommandService.class.getName() + " found in context");
        }
        return service;
    }

    @Override
    public Object[] resolve(Method method) {
        return new Object[method.getParameterTypes().length];
    }

}
