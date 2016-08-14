/*
 *  WebDriver Wrapper
 *  Copyright (C) 2016  Nikolay Platov
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package nikoladasm.webdriver.wrapper.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver.TargetLocator;

import nikoladasm.webdriver.wrapper.listeners.WebDriverEventListener;

import static nikoladasm.webdriver.wrapper.internal.WebElementWrapperFactory.wrapWebElement;
import static nikoladasm.webdriver.wrapper.internal.AlertWrapperFactory.wrapAlert;

public final class TargetLocatorWrapperFactory {

	private static class InvocationHandlerImpl implements InvocationHandler {

		private final TargetLocator targetLocator;
		private final WebDriver driver;
		private final Listeners listeners;
		private final WebDriver wrappedDriver;
		
		public InvocationHandlerImpl(
				TargetLocator targetLocator,
				WebDriver driver,
				Listeners listeners,
				WebDriver wrappedDriver) {
			this.targetLocator = targetLocator;
			this.driver = driver;
			this.listeners = listeners;
			this.wrappedDriver = wrappedDriver;
		}
		
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			String methodName = method.getName();
			if ("getWrappedTargetLocator".equals(methodName))
				return targetLocator;
			Class<?>[] parameterTypes = method.getParameterTypes();
			WebDriverEventListener<TargetLocator> listener;
			if ((listener = listeners.getBeforeListener(methodName)) != null && listener.isApplicable(proxy, parameterTypes))
				listener.invoke(driver, targetLocator, proxy, method, args, null);
			Object result;
			Object wrappedResult;
			try {
				result = method.invoke(targetLocator, args);
				wrappedResult = wrapResult(result, methodName, parameterTypes);
			} catch (InvocationTargetException e) {
				Throwable t = e.getTargetException();
				if (listeners.getExeptionListener() != null)
					listeners.getExeptionListener().invoke(driver, targetLocator, proxy, method, args, t);
				throw t;
			}
			if ((listener = listeners.getAfterListener(methodName)) != null && listener.isApplicable(proxy, parameterTypes))
				listener.invoke(driver, targetLocator, proxy, method, args, result);
			return wrappedResult;
		}

		private Object wrapResult(Object result, String methodName, Class<?>[] parameterTypes) {
			if ("activeElement".equals(methodName) && Arrays.equals(parameterTypes, new Class<?>[]{}) &&
				!(result instanceof WebElementListenableWrapper))
				return wrapWebElement((WebElement) result, driver, listeners);
			if ("alert".equals(methodName) && Arrays.equals(parameterTypes, new Class<?>[]{}))
				return wrapAlert((Alert) result, driver, listeners);
			return wrappedDriver;
		}
	}
	
	public static TargetLocator wrapTargetLocator(
			TargetLocator targetLocator,
			WebDriver driver,
			Listeners listeners,
			WebDriver wrappedDriver) {
		return (TargetLocator) Proxy.newProxyInstance(
			TargetLocatorWrapperFactory.class.getClassLoader(),
			new Class<?>[]{TargetLocator.class, WrapsTargetLocator.class},
			new InvocationHandlerImpl(targetLocator, driver, listeners, wrappedDriver));
	}
	
	private TargetLocatorWrapperFactory() {}
}
