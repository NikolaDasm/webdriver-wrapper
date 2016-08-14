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

import static java.util.stream.Collectors.toList;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.internal.WrapsElement;

import nikoladasm.webdriver.wrapper.listeners.WebDriverEventListener;

public final class WebElementWrapperFactory {

	private static class InvocationHandlerImpl implements InvocationHandler {

		private final WebElement element;
		private final WebDriver driver;
		private final Listeners listeners;
		
		public InvocationHandlerImpl(
				WebElement element,
				WebDriver driver,
				Listeners listeners) {
			this.element = element;
			this.driver = driver;
			this.listeners = listeners;
		}
		
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			String methodName = method.getName();
			if ("getWrappedElement".equals(methodName))
				return element;
			if ("getWrappedDriver".equals(methodName))
				return driver;
			Class<?>[] parameterTypes = method.getParameterTypes();
			WebDriverEventListener<WebElement> listener;
			if ((listener = listeners.getBeforeListener(methodName)) != null && listener.isApplicable(proxy, parameterTypes))
				listener.invoke(driver, element, proxy, method, args, null);
			Object result;
			Object wrappedResult;
			try {
				result = method.invoke(element, args);
				wrappedResult = wrapResult(result, methodName, parameterTypes);
			} catch (InvocationTargetException e) {
				Throwable t = e.getTargetException();
				if (listeners.getExeptionListener() != null)
					listeners.getExeptionListener().invoke(driver, element, proxy, method, args, t);
				throw t;
			}
			if ((listener = listeners.getAfterListener(methodName)) != null && listener.isApplicable(proxy, parameterTypes))
				listener.invoke(driver, element, proxy, method, args, result);
			return wrappedResult;
		}
		
		@SuppressWarnings("unchecked")
		private Object wrapResult(Object result, String methodName, Class<?>[] parameterTypes) {
			if ("findElement".equals(methodName) && Arrays.equals(parameterTypes, new Class<?>[]{By.class}))
				return wrapWebElement((WebElement) result, driver, listeners);
			if ("findElements".equals(methodName) && Arrays.equals(parameterTypes, new Class<?>[]{By.class}))
				return ((List<WebElement>) result).stream().map(element -> wrapWebElement(element, driver, listeners)).collect(toList());
			return result;
		}
	}
	
	public static WebElement wrapWebElement(
			WebElement element,
			WebDriver driver,
			Listeners listeners) {
		return (WebElement) Proxy.newProxyInstance(
			WebElementWrapperFactory.class.getClassLoader(),
			extractInterfaces(element),
			new InvocationHandlerImpl(element, driver, listeners));
	}
	
	private static Class<?>[] extractInterfaces(Object object) {
		Set<Class<?>> allInterfaces = new HashSet<>();
		allInterfaces.add(WrapsElement.class);
		allInterfaces.add(WrapsDriver.class);
		allInterfaces.add(WebElementListenableWrapper.class);
		extractInterfaces(allInterfaces, object.getClass());
		return allInterfaces.toArray(new Class<?>[allInterfaces.size()]);
	}

	private static void extractInterfaces(Set<Class<?>> addTo, Class<?> clazz) {
		if (Object.class.equals(clazz))
			return;
		Class<?>[] classes = clazz.getInterfaces();
		addTo.addAll(Arrays.asList(classes));
		extractInterfaces(addTo, clazz.getSuperclass());
	}
	
	private WebElementWrapperFactory() {}
}
